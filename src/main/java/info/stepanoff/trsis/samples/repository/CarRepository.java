package info.stepanoff.trsis.samples.repository;

import info.stepanoff.trsis.samples.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
