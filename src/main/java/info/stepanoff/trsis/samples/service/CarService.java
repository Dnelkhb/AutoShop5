package info.stepanoff.trsis.samples.service;

import info.stepanoff.trsis.samples.model.Car;
import info.stepanoff.trsis.samples.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.instance-id}")
    private String instanceId; // Чтение идентификатора приложения из конфигурации

    @Autowired
    public CarService(CarRepository carRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.carRepository = carRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // Найти все машины
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }

    // Сохранить машину
    public Car saveCar(Car car) {
        // Сохранение в базу данных
        Car savedCar = carRepository.save(car);

        // Формируем сообщение для Kafka
        String message = String.format(
                "{" +
                        "\"action\": \"save\"," +
                        "\"id\": %d," +
                        "\"make\": \"%s\"," +
                        "\"model\": \"%s\"," +
                        "\"mileage\": %d," +
                        "\"price\": %d," +
                        "\"sourceApp\": \"%s\"" +
                        "}",
                savedCar.getId(),
                savedCar.getMake(),
                savedCar.getModel(),
                savedCar.getMileage(),
                savedCar.getPrice(),
                instanceId
        );

        // Отправка сообщения с ключом (ID машины)
        kafkaTemplate.send("car-topic", String.valueOf(savedCar.getId()), message);

        return savedCar;
    }

    // Удалить машину
    public void deleteCar(Car car) {
        System.out.println("Attempting to delete car: " + car);

        // Проверяем, существует ли машина в базе данных
        List<Car> cars = carRepository.findByFields(
                car.getMake(),
                car.getModel(),
                car.getMileage(),
                car.getPrice()
        );

        if (cars.isEmpty()) {
            System.out.println("No matching car found for deletion: " + car);
            return;
        }

        // Если машина найдена, удаляем её
        int rowsAffected = carRepository.deleteByFields(
                car.getMake(),
                car.getModel(),
                car.getMileage(),
                car.getPrice()
        );

        System.out.println("Deletion successful for car: " + car + ". Rows affected: " + rowsAffected);

        // Формируем сообщение для Kafka
        String key = car.getMake() + "_" + car.getModel() + "_" + car.getMileage() + "_" + car.getPrice();
        String message = String.format(
                "{" +
                        "\"action\": \"delete\"," +
                        "\"make\": \"%s\"," +
                        "\"model\": \"%s\"," +
                        "\"mileage\": %d," +
                        "\"price\": %d," +
                        "\"sourceApp\": \"%s\"" +
                        "}",
                car.getMake(),
                car.getModel(),
                car.getMileage(),
                car.getPrice(),
                instanceId
        );

        // Отправка сообщения с ключом
        kafkaTemplate.send("car-topic", key, message);
    }
}
