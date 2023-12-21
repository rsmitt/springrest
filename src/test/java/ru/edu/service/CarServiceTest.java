package ru.edu.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edu.entity.Car;
import ru.edu.exception.ItemNotFoundException;
import ru.edu.repository.CarRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository repository;

    private CarService service;

    @BeforeEach
    void setUp() {
        service = new CarService(repository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        List<Car> cars = new ArrayList<>(Arrays.asList(
                new Car(1L, "LADA", "AA121212SD", 1990),
                new Car(2L, "BMW", "XC323232BV", 2000)
        ));

        when(repository.findAll()).thenReturn(cars);

        List<Car> allCars = service.findAll();
        assertThat(allCars.size()).isGreaterThan(0);
    }

    @Test
    void findById() {
        Car car = new Car(1L, "LADA", "AA121212SD", 1990);

        when(repository.findById(anyLong())).thenReturn(Optional.of(car));

        Car singleCar = service.findById(1L);
        assertThat(singleCar).isNotNull().isEqualTo(car);
    }

    @Test
    void save() {
        Car car = new Car(1L, "LADA", "AA121212SD", 1990);

        when(repository.save(car)).thenReturn(car);
        Car savedCar = service.save(car);
        assertThat(savedCar.getName()).isNotNull();
    }

    @Test
    void update() {
        Car car = new Car(1L, "LADA", "AA121212SD", 1990);

        when(repository.save(car)).thenReturn(car);

        Car savedCar = service.save(car);
        assertThat(savedCar.getName()).isNotNull();
    }

    @Test
    void deleteById() {
        Car car = new Car(1L, "LADA", "AA121212SD", 1990);

        when(repository.findById(anyLong())).thenReturn(Optional.of(car));
        doNothing().when(repository).deleteById(anyLong());

        service.deleteById(1L);
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void itemNotFoundException() {
        //CarService serviceMock = mock(CarService.class);
        //doThrow(new ItemNotFoundException("Car not found, id = 1")).when(serviceMock).deleteById(1L);
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> service.deleteById(1L));
        assertEquals("Car not found, id = 1", exception.getMessage());
        //verify(repository, times(0)).deleteById(1L);
        //assertThrows(ItemNotFoundException.class, () -> service.deleteById(1L));
    }
}