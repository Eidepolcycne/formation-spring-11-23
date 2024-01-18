package fr.sncf.d2d.colibri.colis.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.sncf.d2d.colibri.colis.models.Colis;
import fr.sncf.d2d.colibri.colis.usecases.CreateColisParams;
import fr.sncf.d2d.colibri.colis.usecases.CreateColisUseCase;
import fr.sncf.d2d.colibri.colis.usecases.PaginateColisUsecase;
import fr.sncf.d2d.colibri.common.models.Page;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/colis")
public class ColisRestController {

    private final CreateColisUseCase createColisUseCase;
    private final PaginateColisUsecase paginateColisUsecase;

    public ColisRestController(
        CreateColisUseCase createColisUseCase, 
        PaginateColisUsecase paginateColisUsecase
    ){
        this.createColisUseCase = createColisUseCase;
        this.paginateColisUsecase = paginateColisUsecase;
    }
    
    @PostMapping
    public Colis createColis(@Valid @RequestBody CreateColisBody body){

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
}
