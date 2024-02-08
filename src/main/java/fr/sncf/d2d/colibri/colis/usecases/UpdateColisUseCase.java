package fr.sncf.d2d.colibri.colis.usecases;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import fr.sncf.d2d.colibri.colis.exceptions.ColisNotFoundException;
import fr.sncf.d2d.colibri.colis.exceptions.ColisStatusException;
import fr.sncf.d2d.colibri.colis.models.Colis;
import fr.sncf.d2d.colibri.colis.models.ColisStatus;
import fr.sncf.d2d.colibri.colis.persistence.ColisRepository;
import fr.sncf.d2d.colibri.users.exceptions.UserNotFoundException;
import fr.sncf.d2d.colibri.users.models.Role;
import fr.sncf.d2d.colibri.users.persistence.UsersRepository;

@Service
public class UpdateColisUseCase {
    
    private final ColisRepository colisRepository;
    private final UsersRepository usersRepository;
    private final JavaMailSender javaMailSender;

    public UpdateColisUseCase(ColisRepository colisRepository, UsersRepository usersRepository, JavaMailSender sender){
        this.colisRepository = colisRepository;
        this.usersRepository = usersRepository;
        this.javaMailSender = sender;
    }

    public Colis update(UpdateColisParams params) throws Exception {
        final var colis = this.colisRepository.findById(params.getId())
            .orElseThrow(() -> ColisNotFoundException.id(params.getId()));
        params.getAddress().ifMustUpdate(colis::setAddress);
        params.getEmail().ifMustUpdate(colis::setEmail);
        params.getDeliveryPersonId().ifMustUpdate(deliveryPersonId -> {
            if (deliveryPersonId != null){
                this.usersRepository.getUsers()
                    .stream()
                    .filter(user -> user.getRole().equals(Role.DELIVERY_PERSON) && user.getId().equals(deliveryPersonId))
                    .findFirst()
                    .orElseThrow(() -> UserNotFoundException.id(deliveryPersonId));
            }
            colis.setDeliveryPersonId(deliveryPersonId);
        });
        params.getDetails().ifMustUpdate(colis::setDetails);


        final var oldStatus = colis.getStatus();

        params.getStatus().ifMustUpdate(newStatus -> {
            switch (oldStatus){
                case PENDING:
                    break;
                case IN_TRANSIT:
                    if (Stream.of(ColisStatus.RETURNED, ColisStatus.DELIVERED).noneMatch(acceptable -> newStatus.equals(acceptable))){
                        throw new ColisStatusException();
                    }
                    break;
                case DELIVERED:
                    if (ColisStatus.RETURNED != newStatus){
                        throw new ColisStatusException();
                    }
                    break;
                default:
                    throw new ColisStatusException();
            }
            
            colis.setStatus(newStatus);

            final var message = new SimpleMailMessage(); 
            message.setFrom("noreply@colibri.com");
            message.setTo(colis.getEmail()); 
            message.setSubject("Notification concernant votre colis"); 
            message.setText(this.getEmailContent(colis));
            this.javaMailSender.send(message);

        });

        this.colisRepository.update(colis);

        return colis;
        
    }

    private String getEmailContent(Colis colis){
        switch (colis.getStatus()){
            case DELIVERED: return String.format("votre colis a été livré à l'addresse \"%s\"", colis.getAddress());
            case IN_TRANSIT: return String.format("votre colis est en cours de livraison et sera remis à l'addresse complète: \"%s\"", colis.getAddress());
            case RETURNED: return String.format("votre colis a été retourné à l'expéditeur");
            default: throw new IllegalStateException();
        }
    }
}
