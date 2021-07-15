package com.coenos.temperature.rest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.coenos.temperature.model.City;
import com.coenos.temperature.model.TemperatureData;
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

  @Test
  void getTemp() throws RestClientException, ApiException {

    TemperatureRestClient temperatureRestClient = new TemperatureRestClient(this.defaultApi);

    City city = City.builder().name("Veldhoven").build();
    InlineResponse200 response200 = new InlineResponse200();
    response200.setTemperature(BigDecimal.valueOf(10.1));
    response200.setTime(OffsetDateTime.now());

    Mockito.when(this.defaultApi.getTemp(city.getName())).thenReturn(response200);

    TemperatureData temperatureData = temperatureRestClient.getTemperature(city);

    assertEquals("", "50.2", temperatureData.getTemperatureInFahrenheit());
  }
}
