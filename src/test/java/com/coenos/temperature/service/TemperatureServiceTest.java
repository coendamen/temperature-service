package com.coenos.temperature.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.coenos.temperature.model.City;
import com.coenos.temperature.model.Period;
import com.coenos.temperature.repository.CityRepository;
import com.coenos.temperature.rest.TemperatureRestClient;
import java.util.Collections;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemperatureServiceTest {

  @Mock private Validator validator;
  @Mock private CityRepository cityRepository;
  @Mock private TemperatureRestClient temperatureRestClient;
  @Captor private ArgumentCaptor<City> restClientCityCaptor;

  private TemperatureService temperatureService;

  @BeforeEach
  void beforeEach() {
    temperatureService =
        new TemperatureService(
            this.validator, this.cityRepository, this.temperatureRestClient, 1000);
  }

  @Test
  void initialize() {
    when(validator.validate(any(Period.class))).thenReturn(Collections.emptySet());

    try {
      this.temperatureService.initialize();
    } catch (IllegalStateException e) {
      fail();
    }
  }

  @Test
  void happyFlow() {

    City testCity = City.builder().name("Veldhoven").build();

    when(cityRepository.findAll())
        .thenReturn(Collections.singletonList(City.builder().name("Veldhoven").build()));

    temperatureService.scheduleGetTemperature();

    verify(this.temperatureRestClient).getTemperature(restClientCityCaptor.capture());
    assertTrue("", restClientCityCaptor.getValue().equals(testCity));
  }
}
