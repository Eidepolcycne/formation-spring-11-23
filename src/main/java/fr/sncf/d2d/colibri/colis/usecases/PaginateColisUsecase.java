package fr.sncf.d2d.colibri.colis.usecases;

import org.springframework.stereotype.Service;

import fr.sncf.d2d.colibri.colis.models.Colis;
import fr.sncf.d2d.colibri.colis.persistence.ColisRepository;
import fr.sncf.d2d.colibri.common.models.Page;

@Service
public class PaginateColisUsecase {
    
    private final ColisRepository repository;

    public PaginateColisUsecase(ColisRepository repository){
        this.repository = repository;
    }

    public Page<Colis> paginate(long page, long itemsPerPage){
        return this.repository.paginate(page, itemsPerPage);
    }
}
