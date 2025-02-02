package fr.sncf.d2d.colibri.colis.exceptions;

import java.util.UUID;

public class ColisNotFoundException extends Exception {
    private ColisNotFoundException(String message){
        super(message);
    }

    public static ColisNotFoundException id(UUID id){
        return new ColisNotFoundException(String.format("le colis avec id %s n'existe pas", id));
    }
}
