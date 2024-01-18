package fr.sncf.d2d.colibri.colis.persistence;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Collections;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.sncf.d2d.colibri.colis.models.Colis;
import fr.sncf.d2d.colibri.common.models.Page;

@Repository
public class ColisRepository {
    
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final RowMapper<Colis> ROW_MAPPER = (resultSet, rowNum) -> Colis.builder()
        .id(UUID.fromString(resultSet.getString("id")))
        .address(resultSet.getString("address"))
        .email(resultSet.getString("email"))
        .deliveryPersonId(resultSet.getString("delivery_person_id"))
        .details(resultSet.getString("details"))
        .build(); 

    public ColisRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void persist(Colis colis){

        colis.setId(UUID.randomUUID());

        final var sql = "INSERT INTO colis (id, address, details, delivery_person_id, email)" + 
            "VALUES (:id, :address, :details, :deliveryPersonId, :email)";

        this.jdbcTemplate.update(
            sql, 
            new HashMap<>() {{
                put("id", colis.getId());
                put("address", colis.getAddress());
                put("details", colis.getDetails());
                put("deliveryPersonId", colis.getDeliveryPersonId());
                put("email", colis.getEmail());
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
