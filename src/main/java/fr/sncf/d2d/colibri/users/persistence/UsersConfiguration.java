package fr.sncf.d2d.colibri.users.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import fr.sncf.d2d.colibri.users.models.Role;
import fr.sncf.d2d.colibri.users.models.User;

@Configuration
@ConfigurationProperties(prefix = "colibri")
public class UsersConfiguration {
    
    private List<User> users;

    public List<User> getUsers(){
        return this.users;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }
}
