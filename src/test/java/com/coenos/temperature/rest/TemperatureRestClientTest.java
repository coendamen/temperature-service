package com.coenos.temperature.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.coenos.temperature.model.City;
import com.coenos.temperature.repository.CityRepository;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.InlineResponse200;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.threeten.bp.OffsetDateTime;

@ExtendWith(MockitoExtension.class)
class TemperatureRestClientTest {

  @Mock private DefaultApi defaultApi;
  @Mock private CityRepository cityRepository;

  @Test
  void getTemp() throws ApiException {

    TemperatureRestClient temperatureRestClient =
        new TemperatureRestClient(this.defaultApi, this.cityRepository);

    City city = City.builder().name("Veldhoven").build();

    InlineResponse200 inlineResponse200 = new InlineResponse200();
    inlineResponse200.temperature(BigDecimal.valueOf(10.1d));
    inlineResponse200.setTime(OffsetDateTime.now());

    Mockito.when(defaultApi.getTempAsync(any(String.class), any(ApiCallback.class)))
        .thenAnswer(
            invocation -> {
              ((ApiCallback<InlineResponse200>) invocation.getArguments()[1])
                  .onSuccess(inlineResponse200, 200, null);

              Mockito.verifyNoInteractions(cityRepository);
              return null;
            });

    temperatureRestClient.getTemperature(city);
  }

  @Test
  void getTempNotFound() throws ApiException {

    TemperatureRestClient temperatureRestClient =
        new TemperatureRestClient(this.defaultApi, this.cityRepository);

    City city = City.builder().name("Veldhoven").build();

    Mockito.when(defaultApi.getTempAsync(any(String.class), any(ApiCallback.class)))
        .thenAnswer(
            invocation -> {
              ((ApiCallback<ApiException>) invocation.getArguments()[1])
                  .onFailure(new ApiException(), 404, null);

              verify(cityRepository).delete(city);
              return null;
            });

    temperatureRestClient.getTemperature(city);
  }
}
