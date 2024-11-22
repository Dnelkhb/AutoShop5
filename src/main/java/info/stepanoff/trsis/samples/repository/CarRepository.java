package info.stepanoff.trsis.samples.repository;

import info.stepanoff.trsis.samples.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // Метод для удаления машины по всем полям
    @Modifying
    @Transactional
    @Query("DELETE FROM Car c WHERE c.make = :make AND c.model = :model AND c.mileage = :mileage AND c.price = :price")
    int deleteByFields(@Param("make") String make,
                       @Param("model") String model,
                       @Param("mileage") int mileage,
                       @Param("price") int price);
    @Query("SELECT c FROM Car c WHERE c.make = :make AND c.model = :model AND c.mileage = :mileage AND c.price = :price")
    List<Car> findByFields(@Param("make") String make,
                           @Param("model") String model,
                           @Param("mileage") int mileage,
                           @Param("price") int price);

}
