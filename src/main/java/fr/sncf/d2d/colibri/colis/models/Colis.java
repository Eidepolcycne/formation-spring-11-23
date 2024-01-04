package fr.sncf.d2d.colibri.colis.models;

import java.util.UUID;

// TODO: créer des objets colis de manière scalable et élégante
public class Colis {

    private UUID id;
    
    private final String address;

    private final String deliveryPersonId;

    private final String details;

    private final String email;

    private Colis(UUID id, String address, String deliveryPersonId, String details, String email){
        this.id = id;
        this.address = address;
        this.deliveryPersonId = deliveryPersonId;
        this.details = details;
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDeliveryPersonId() {
        return this.deliveryPersonId;
    }

    public String getDetails() {
        return this.details;
    }

    public String getEmail() {
        return this.email;
    }

    public UUID getId(){
        return this.id;
    }
    
    public void setId(UUID id){
        this.id = id;
    }

    public static ColisBuilder builder(){
        return new ColisBuilder();
    }

    public static class ColisBuilder {

        private String address;

        private String deliveryPersonId;

        private String details;

        private String email;

        private ColisBuilder(){}

        public ColisBuilder address(String address){
            this.address = address;
            return this;
        }

        public ColisBuilder deliveryPersonId(String deliveryPersonId){
            this.deliveryPersonId = deliveryPersonId;
            return this;
        }

        public ColisBuilder details(String details){
            this.details = details;
            return this;
        }

        public ColisBuilder email(String email){
            this.email = email;
            return this;
        }

        public Colis build(){
            return new Colis(null, this.address, this.deliveryPersonId, this.details, this.email);
        }
    }
}
