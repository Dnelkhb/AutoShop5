package info.stepanoff.trsis.samples.controller;

import info.stepanoff.trsis.samples.model.Car;
import info.stepanoff.trsis.samples.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String listCars(Model model) {
        model.addAttribute("cars", carService.findAllCars());
        return "cars";
    }

    @PostMapping("/add")
    public String addCar(@RequestParam String make,
                         @RequestParam String model,  // исправил имя параметра обратно на "model" для согласованности с HTML
                         @RequestParam int mileage,
                         @RequestParam int price) {
        Car car = new Car(make, model, mileage, price);
        carService.saveCar(car);
        return "redirect:/cars";
    }

    @PostMapping("/delete")
    public String deleteCar(@RequestParam String make,
                            @RequestParam String model,
                            @RequestParam int mileage,
                            @RequestParam int price) {
        carService.deleteCar(new Car(make, model, mileage, price));
        return "redirect:/cars";
    }




}
