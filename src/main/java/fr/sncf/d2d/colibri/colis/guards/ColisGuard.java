package fr.sncf.d2d.colibri.colis.guards;

import org.springframework.stereotype.Component;

import fr.sncf.d2d.colibri.colis.models.Colis;
import fr.sncf.d2d.colibri.users.models.Role;
import fr.sncf.d2d.colibri.users.persistence.ApplicationUserDetails;

@Component("colisGuard")
public class ColisGuard {
    
    public boolean canCreate(Colis colis, Object principal){

        final var userdetails = (ApplicationUserDetails)principal;

        if (userdetails != null){

            final var user = userdetails.getUser();

            if (user.getRole().equals(Role.ADMIN)){
                return true;
            }

            if (user.getRole().equals(Role.DELIVERY_PERSON)){
                return colis.getDeliveryPersonId() == null 
                    || colis.getDeliveryPersonId().equals(user.getId());
            }
        }    

        return false;
    }
}
