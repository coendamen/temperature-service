package com.coenos.temperature.rest;

import com.coenos.temperature.model.City;
import com.coenos.temperature.repository.CityRepository;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TemperatureRestClient {

  private final CityRepository cityRepository;
  private final DefaultApi defaultApi;

  public TemperatureRestClient(final DefaultApi defaultApi, final CityRepository cityRepository) {
    this.defaultApi = defaultApi;
    this.cityRepository = cityRepository;
  }

  public void getTemperature(final City city) throws RestClientException {

    try {
      this.defaultApi.getTempAsync(
          city.getName(), new TemperatureApiCallback(city, cityRepository));
    } catch (ApiException e) {
      throw RestClientException.builder()
          .httpStatusCode(e.getCode())
          .message(e.getMessage())
          .build();
    }
  }
}
