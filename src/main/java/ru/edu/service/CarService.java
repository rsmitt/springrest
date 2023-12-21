package ru.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edu.entity.Car;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CarRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository repository;

    public List<Car> findAll() {
        return repository.findAll();
    }

    public Car findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ItemNotFoundException("Car not found, id = " + id));
    }

    public Car save(Car car) {
        return repository.save(car);
    }

    public Car update(Car car) {
        findById(car.getId());
        return repository.save(car);
    }

    public void deleteById(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
