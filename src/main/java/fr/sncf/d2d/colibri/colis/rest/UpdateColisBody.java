package fr.sncf.d2d.colibri.colis.rest;

import java.util.UUID;

import fr.sncf.d2d.colibri.colis.models.ColisStatus;
import fr.sncf.d2d.colibri.common.models.SetField;

public class UpdateColisBody {
    
    private SetField<String> email = SetField.noop();

    private SetField<String> details = SetField.noop();
    
    private SetField<UUID> deliveryPersonId = SetField.noop();

    private SetField<String> address = SetField.noop();

    private SetField<ColisStatus> status = SetField.noop();

    public SetField<ColisStatus> getStatus(){
        return status;
    }

    public SetField<String> getEmail() {
        return email;
    }

    public SetField<String> getDetails() {
        return details;
    }

    public SetField<UUID> getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public SetField<String> getAddress() {
        return address;
    }

    public void setEmail(String email){
        this.email = SetField.withValue(email);
    }

    public void setDetails(String details){
        this.details = SetField.withNullable(details);
    }

    public void deliveryPersonId(UUID deliveryPersonId){
        this.deliveryPersonId = SetField.withNullable(deliveryPersonId);
    }

    public void setAddress(String address){
        this.address = SetField.withValue(address);
    }

    public void setStatus(String status){
        this.status = SetField.withValue(ColisStatus.valueOf(status.toUpperCase()));
    }
}
