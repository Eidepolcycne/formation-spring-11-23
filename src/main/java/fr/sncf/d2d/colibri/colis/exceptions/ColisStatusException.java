package fr.sncf.d2d.colibri.colis.exceptions;

public class ColisStatusException extends Exception {
    
    public ColisStatusException(){
        super("cette action n'est pas autorisée");
    }
}
