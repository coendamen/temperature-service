package com.coenos.temperature.rest;

import com.coenos.temperature.model.City;
import com.coenos.temperature.model.TemperatureData;
import com.coenos.temperature.repository.CityRepository;
import io.swagger.client.ApiCallback;
import io.swagger.client.ApiException;
import io.swagger.client.model.InlineResponse200;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.threeten.bp.format.DateTimeFormatter;

@Slf4j
public class TemperatureApiCallback implements ApiCallback<InlineResponse200> {

  private final City city;
  private final CityRepository cityRepository;

  public TemperatureApiCallback(final City city, final CityRepository cityRepository) {
    this.city = city;
    this.cityRepository = cityRepository;
  }

  @Override
  public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
    log.error(
        "Failed to get temperature for City {}. Code {}. {}",
        this.city.getName(),
        statusCode,
        e.getMessage());

    if (statusCode == 404) {
      log.info("City {} not found, will be deleted from DB.", city.getName());
      this.cityRepository.delete(this.city);
    }
  }

  @Override
  public void onSuccess(
      InlineResponse200 result, int statusCode, Map<String, List<String>> responseHeaders) {

    TemperatureData data =
        TemperatureData.builder()
            .city(this.city)
            .temperature(result.getTemperature().doubleValue())
            .requestTime(OffsetDateTime.now())
            .temperatureTime(
                OffsetDateTime.parse(
                    result.getTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
            .build();

    log.info(data.toFormattedString());
  }

  @Override
  public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
    //
  }

  @Override
  public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
    //
  }
}
