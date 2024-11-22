package info.stepanoff.trsis.samples.service;
import java.util.Optional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.stepanoff.trsis.samples.model.Car;
import info.stepanoff.trsis.samples.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CarConsumer {

    private final CarRepository carRepository;
    private final CarService carService;
    private final String instanceId;

    @Autowired

    public CarConsumer(CarRepository carRepository, CarService carService, @Value("${app.instance-id}") String instanceId) {
        this.carRepository = carRepository;
        this.carService = carService;
        this.instanceId = instanceId;
        System.out.println("CarConsumer instance initialized. Instance ID: " + instanceId);
    }


    @KafkaListener(topics = "car-topic", groupId = "car-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
        System.out.println("Instance ID: " + instanceId);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(message);
            String sourceApp = jsonNode.get("sourceApp").asText();

            System.out.println("Received message with sourceApp: " + sourceApp);
            System.out.println("Current instanceId: " + instanceId);
            System.out.println("Message received for processing: " + message);

            if (sourceApp.equals(instanceId)) {
                System.out.println("Ignored message from the same instance: " + instanceId);
                return;
            }

            String action = jsonNode.get("action").asText();

            if ("save".equals(action)) {
                Car car = new Car(
                        jsonNode.get("make").asText(),
                        jsonNode.get("model").asText(),
                        jsonNode.get("mileage").asInt(),
                        jsonNode.get("price").asInt()
                );
                car.setId(jsonNode.get("id").asLong());
                carRepository.save(car);

            } if ("delete".equals(action)) {
                Car car = new Car(
                        jsonNode.get("make").asText(),
                        jsonNode.get("model").asText(),
                        jsonNode.get("mileage").asInt(),
                        jsonNode.get("price").asInt()
                );
                System.out.println("Attempting to delete car: " + car);
                carService.deleteCar(car);  // Используем новый метод
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
