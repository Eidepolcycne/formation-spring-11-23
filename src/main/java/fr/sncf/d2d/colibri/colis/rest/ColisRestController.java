package fr.sncf.d2d.colibri.colis.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.sncf.d2d.colibri.colis.exceptions.ColisNotFoundException;
import fr.sncf.d2d.colibri.colis.models.Colis;
import fr.sncf.d2d.colibri.colis.usecases.CreateColisParams;
import fr.sncf.d2d.colibri.colis.usecases.CreateColisUseCase;
import fr.sncf.d2d.colibri.colis.usecases.PaginateColisUsecase;
import fr.sncf.d2d.colibri.colis.usecases.UpdateColisParams;
import fr.sncf.d2d.colibri.colis.usecases.UpdateColisUseCase;
import fr.sncf.d2d.colibri.common.models.Page;
import fr.sncf.d2d.colibri.users.exceptions.UserNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/colis")
public class ColisRestController {

    private final CreateColisUseCase createColisUseCase;
    private final PaginateColisUsecase paginateColisUsecase;
    private final UpdateColisUseCase updateColisUseCase;

    public ColisRestController(
        CreateColisUseCase createColisUseCase, 
        PaginateColisUsecase paginateColisUsecase,
        UpdateColisUseCase updateColisUseCase
    ){
        this.createColisUseCase = createColisUseCase;
        this.paginateColisUsecase = paginateColisUsecase;
        this.updateColisUseCase = updateColisUseCase;
    }
    
    @PostMapping
    public Colis createColis(@Valid @RequestBody CreateColisBody body) throws UserNotFoundException {
        final var params = new CreateColisParams();
        params.setAddress(body.getAddress());
        params.setDetails(body.getDetails());
        params.setDeliveryPersonId(body.getDeliveryPersonId());
        params.setEmail(body.getEmail());
        return this.createColisUseCase.create(params);
    }

    @GetMapping
    public Page<Colis> paginateColis(@Valid PaginateColisQuery query){
        return this.paginateColisUsecase.paginate(query.getPage(), query.getItemsPerPage());
    }

    @PatchMapping("/{colisId}")
    public Colis updateColis(@PathVariable UUID colisId, @Valid @RequestBody UpdateColisBody body) throws Exception {
        final var params = new UpdateColisParams();
        params.setId(colisId);
        params.setAddress(body.getAddress());
        params.setDeliveryPersonId(body.getDeliveryPersonId());
        params.setDetails(body.getDetails());
        params.setEmail(body.getEmail());
        params.setStatus(body.getStatus());
        return this.updateColisUseCase.update(params);
    }
}
