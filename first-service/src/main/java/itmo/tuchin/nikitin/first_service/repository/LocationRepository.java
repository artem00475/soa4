package itmo.tuchin.nikitin.first_service.repository;

import itmo.tuchin.nikitin.first_service.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
