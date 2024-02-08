package fr.sncf.d2d.colibri.colis.persistence;

import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.UUID;
import java.util.Collections;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import fr.sncf.d2d.colibri.colis.models.Colis;
import fr.sncf.d2d.colibri.colis.models.ColisStatus;
import fr.sncf.d2d.colibri.common.models.Page;

@Repository
public class ColisRepository {
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final RowMapper<Colis> ROW_MAPPER = (resultSet, rowNum) -> {
        final var colisBuilder = Colis.builder()
            .id(UUID.fromString(resultSet.getString("id")))
            .address(resultSet.getString("address"))
            .email(resultSet.getString("email"))
            .status(ColisStatus.valueOf(resultSet.getString("status")));

        Optional.ofNullable(resultSet.getString("delivery_person_id"))
            .map(UUID::fromString)
            .ifPresent(colisBuilder::deliveryPersonId);

        Optional.ofNullable(resultSet.getString("details"))
            .ifPresent(colisBuilder::details);

        return colisBuilder.build();
    }; 

    public ColisRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Colis> findById(UUID id){
        try {
            return Optional.of(this.jdbcTemplate.queryForObject(
                "SELECT * FROM colis WHERE id = :id", 
                Collections.singletonMap("id", id.toString()), 
                ROW_MAPPER
            ));
        } catch(EmptyResultDataAccessException empty){
            return Optional.empty();
        }
    }

    @PreAuthorize("@colisGuard.canCreate(#colis, principal)")
    public void insert(Colis colis){

        colis.setId(UUID.randomUUID());

        final var sql = "INSERT INTO colis (id, address, details, delivery_person_id, email, status)" + 
            "VALUES (:id, :address, :details, :deliveryPersonId, :email, :status)";

        this.jdbcTemplate.update(
            sql, 
            new HashMap<>() {{
                put("id", colis.getId());
                put("address", colis.getAddress());
                put("details", colis.getDetails());
                put("deliveryPersonId", colis.getDeliveryPersonId());
                put("email", colis.getEmail());
                put("status", colis.getStatus().name());
            }}
        );
    } 

    @PreAuthorize("@colisGuard.canUpdate(#colis, principal)")
    public void update(Colis colis){
        final var sql = "UPDATE colis SET address = :address, details = :details, delivery_person_id = :deliveryPersonId, email = :email, status = :status WHERE id = :id";

        this.jdbcTemplate.update(
            sql,
            new HashMap<>(){{
                put("id", colis.getId());
                put("address", colis.getAddress());
                put("details", colis.getDetails());
                put("deliveryPersonId", colis.getDeliveryPersonId());
                put("email", colis.getEmail());
                put("status", colis.getStatus().name());
            }}
        );
    }

    public Page<Colis> paginate(long page, long itemsPerPage){
        final var countSql = "SELECT COUNT(id) FROM colis";
        final var itemsSql = "SELECT * FROM colis ORDER BY id LIMIT :itemsPerPage OFFSET :offset";

        final var total = this.jdbcTemplate.queryForObject(countSql, Collections.emptyMap(), Long.class);
        final var items = this.jdbcTemplate.queryForStream(
            itemsSql, 
            Map.of(
                "itemsPerPage", itemsPerPage,
                "offset", page * itemsPerPage
            ), 
            ROW_MAPPER
        ).toList();
        return new Page<>(items, total);
    }
}
