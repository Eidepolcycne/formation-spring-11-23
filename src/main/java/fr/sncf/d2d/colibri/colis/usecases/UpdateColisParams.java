package fr.sncf.d2d.colibri.colis.usecases;

import java.util.UUID;

import fr.sncf.d2d.colibri.colis.models.ColisStatus;
import fr.sncf.d2d.colibri.common.models.SetField;

public class UpdateColisParams {

    private UUID id;
    
    private SetField<String> email = SetField.noop();

    private SetField<String> details = SetField.noop();
    
    private SetField<UUID> deliveryPersonId = SetField.noop();

    private SetField<String> address = SetField.noop();

    private SetField<ColisStatus> status = SetField.noop();

    

    public SetField<String> getEmail() {
        return email;
    }

    public void setEmail(SetField<String> email) {
        this.email = email;
    }

    public SetField<String> getDetails() {
        return details;
    }

    public void setDetails(SetField<String> details) {
        this.details = details;
    }

    public SetField<UUID> getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(SetField<UUID> deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public SetField<String> getAddress() {
        return address;
    }

    public void setAddress(SetField<String> address) {
        this.address = address;
    }

    public SetField<ColisStatus> getStatus() {
        return status;
    }

    public void setStatus(SetField<ColisStatus> status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    

}
