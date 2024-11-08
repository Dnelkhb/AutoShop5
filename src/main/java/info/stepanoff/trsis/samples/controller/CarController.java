package info.stepanoff.trsis.samples.controller;

import info.stepanoff.trsis.samples.model.Car;
import info.stepanoff.trsis.samples.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarRepository carRepository;

    @GetMapping
    public String listCars(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "cars";
    }

    @PostMapping("/add")
    public String addCar(@RequestParam String make,
                         @RequestParam String model,
                         @RequestParam int mileage,
                         @RequestParam int price) {
        Car car = new Car(make, model, mileage, price);
        carRepository.save(car);
        return "redirect:/cars";
    }

    @PostMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
        carRepository.deleteById(id);
        return "redirect:/cars";
    }
}
