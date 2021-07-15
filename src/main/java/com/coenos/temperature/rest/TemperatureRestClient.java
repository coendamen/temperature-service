package com.coenos.temperature.rest;

import com.coenos.temperature.model.City;
import com.coenos.temperature.model.TemperatureData;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.InlineResponse200;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.threeten.bp.format.DateTimeFormatter;

@Slf4j
@Component
public class TemperatureRestClient {

  private final DefaultApi defaultApi;

  public TemperatureRestClient(DefaultApi defaultApi) {
    this.defaultApi = defaultApi;
  }

  public TemperatureData getTemperature(final City city) throws RestClientException {

    try {
      InlineResponse200 response200 = this.defaultApi.getTemp(city.getName());

      return TemperatureData.builder()
          .city(city)
          .temperature(response200.getTemperature().doubleValue())
          .requestTime(OffsetDateTime.now())
          .temperatureTime(
              OffsetDateTime.parse(
                  response200.getTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
          .build();

    } catch (ApiException e) {
      throw RestClientException.builder()
          .httpStatusCode(e.getCode())
          .message(e.getMessage())
          .build();
    } catch (Exception e) {
      throw RestClientException.builder()
          .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .message(e.getMessage())
          .build();
    }
  }
}
