package itmo.tuchin.nikitin.first_service.repository;

import itmo.tuchin.nikitin.first_service.entity.Person;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {
    Optional<Person> findPersonById(int id);
    Integer countTotalByHeight(int height);
    @Query("SELECT AVG(p.height) from Person p")
    Double getAverageHeight();

    @NotNull Page<Person> findAll(Specification<Person> filter, @NotNull Pageable pageable);
}
