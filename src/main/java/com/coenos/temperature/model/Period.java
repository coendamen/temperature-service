package com.coenos.temperature.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Period {

  @Min(value = 5000, message = "min is 5 seconds")
  @Max(value = 60000, message = "max is 60 seconds")
  private int interval = 10000;

}
