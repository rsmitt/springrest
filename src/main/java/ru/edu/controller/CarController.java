package ru.edu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.edu.entity.Car;
import ru.edu.service.CarService;

import java.util.List;

// http://localhost:8080/swagger-ui/index.html#/
@RestController
@RequestMapping(value = "/api/v1/cars", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Car", description = "Car API")
public class CarController {

    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    private final CarService service;

    @GetMapping
    @Operation(summary = "Get all cars")
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = service.findAll();
        logger.info("getting car list: {}", cars);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    /**
     *
     * @param carId
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get car details")
    public Car getCarById(@PathVariable("id") long carId) {
        return service.findById(carId);
    }

    @PostMapping
    @Operation(summary = "Create a new car")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        service.save(car);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/cars/" + car.getId());
        return new ResponseEntity<>(null, headers, HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update a car")
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        service.update(car);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{carId}")
    @Operation(summary = "Delete a car by id")
    public ResponseEntity<Car> deleteCarById(@PathVariable long carId) {
        service.deleteById(carId);
        return ResponseEntity.ok().build();
    }

}
