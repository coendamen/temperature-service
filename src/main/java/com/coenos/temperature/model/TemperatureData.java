package com.coenos.temperature.model;

import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TemperatureData {

  private City city;
  private OffsetDateTime requestTime;
  private double temperature;
  private OffsetDateTime temperatureTime;

  public String toFormattedString() {
    return String.format(
        "[city: %s, requestTime: %s, temperature: %s, temperatureTime: %s]",
        this.city.getName(),
        this.offsetDateTimeInUtc(this.requestTime),
        this.getTemperatureInFahrenheit(),
        this.offsetDateTimeInUtc(this.temperatureTime));
  }

  private String offsetDateTimeInUtc(OffsetDateTime offsetDateTime) {
    return offsetDateTime
        .atZoneSameInstant(ZoneId.of("UTC"))
        .withNano(0)
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public String getTemperatureInFahrenheit() {
    var df = new DecimalFormat("#.#");
    return df.format((9.0 / 5.0) * this.temperature + 32);
  }
}
