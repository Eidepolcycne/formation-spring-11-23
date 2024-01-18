package fr.sncf.d2d.colibri.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import fr.sncf.d2d.colibri.users.persistence.UsersRepository;

@Configuration
public class SecurityConfiguration {

    private final UsersRepository usersRepository;

    public SecurityConfiguration(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .httpBasic(Customizer.withDefaults())
            .userDetailsService(this.usersRepository)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
