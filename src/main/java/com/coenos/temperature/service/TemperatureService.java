package com.coenos.temperature.service;

import com.coenos.temperature.model.City;
import com.coenos.temperature.model.Period;
import com.coenos.temperature.repository.CityRepository;
import com.coenos.temperature.rest.RestClientException;
import com.coenos.temperature.rest.TemperatureRestClient;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TemperatureService {

  private final Validator validator;
  private final CityRepository cityRepository;
  private final TemperatureRestClient temperatureRestClient;
  private final int fetchTimeInterval;

  public TemperatureService(
      final Validator validator,
      final CityRepository cityRepository,
      final TemperatureRestClient temperatureRestClient,
      @Value("${temperature.fetch.time.interval}") final int fetchTimeInterval) {
    this.validator = validator;
    this.cityRepository = cityRepository;
    this.temperatureRestClient = temperatureRestClient;
    this.fetchTimeInterval = fetchTimeInterval;
  }

  @PostConstruct
  public void initialize() {

    var period = Period.builder().interval(this.fetchTimeInterval).build();
    Set<ConstraintViolation<Period>> violations = this.validator.validate(period);

    if (!violations.isEmpty()) {
      // TODO can be improved to show more details on which validation that failed
      throw new IllegalStateException("Period is invalid");
    }
  }

  @Scheduled(fixedRateString = "${temperature.fetch.time.interval}")
  public void scheduleGetTemperature() {
    log.info("Running FetchTemperaturesTask");
    cityRepository.findAll().stream()
        .limit(5)
        .forEach(
            city -> {
              try {
                temperatureRestClient.getTemperature(city);
              } catch (RestClientException e) {
                this.handleException(city, e);
              }
            });
  }

  private void handleException(City city, RestClientException e) {
    if (e.getHttpStatusCode() == 404) {
      log.error("City {} Not Found, will be deleted from db", city);
      this.cityRepository.delete(city);
    } else {
      log.error(
          "RestClientException occurred with code {} for city {}. ErrorMessage : {}",
          e.getHttpStatusCode(),
          city,
          e.getMessage());
    }
  }
}
