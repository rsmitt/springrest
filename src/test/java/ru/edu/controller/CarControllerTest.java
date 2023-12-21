package ru.edu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.edu.entity.Car;
import ru.edu.service.CarService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({CarController.class})
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService service;

    @InjectMocks
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("check receiving all cars")
    void getAllCars() throws Exception {
        List<Car> cars = new ArrayList<>(Arrays.asList(
                new Car(1L, "LADA", "AA121212SD", 1990),
                new Car(2L, "BMW", "XC323232BV", 2000)
        ));

        when(service.findAll()).thenReturn(cars);

        mockMvc.perform(get("/api/v1/cars"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
        //.andExpect(content().json("[{}, {}]"));
    }

    @Test
    @DisplayName("check receiving single car details")
    void getCarById() throws Exception {
        Car car = new Car(1L, "LADA", "AA121212SD", 1990);

        when(service.findById(anyLong())).thenReturn(car);

        mockMvc.perform(get("/api/v1/cars/1"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.name").value("LADA"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("check adding a new car")
    void createCar() throws Exception {
        Car car = new Car(3L, "KIA", "SA767676CX", 2010);

        when(service.save(any(Car.class))).thenReturn(car);

        mockMvc.perform(post("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(car))
                )
                .andDo(print())
                .andExpect(header().string("Location", "/api/v1/cars/" + car.getId()))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("check updating a car")
    void updateCar() throws Exception {
        Car car = new Car(3L, "KIA", "SA767676CX", 2011);

        when(service.update(any(Car.class))).thenReturn(car);

        mockMvc.perform(put("/api/v1/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(car))
                )
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("check deleting a car by id")
    void deleteCarById() throws Exception {

        doNothing().when(service).deleteById(anyLong());

        mockMvc.perform(delete("/api/v1/cars/1"))
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(status().isOk());
    }


/*    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(
                        containsString("Welcome to...")));
    }
*/

}