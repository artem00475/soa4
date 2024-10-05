package itmo.tuchin.nikitin.first_service.repository;

import itmo.tuchin.nikitin.first_service.entity.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer> {
}
