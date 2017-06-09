package com.borovyksv.sql;

import com.borovyksv.sql.vehicle.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {
    @Query(value = "SELECT id, model_make, group_concat(DISTINCT model_name ORDER BY model_name ASC separator ', ') AS model_name FROM techdoc.vehicle GROUP BY model_make",
            nativeQuery=true
    )
    public List<Vehicle> findAllAndGroup();

}
