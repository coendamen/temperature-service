package com.coenos.temperature.rest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestClientException extends Exception {

  private final int httpStatusCode;
  private final String message;

}
