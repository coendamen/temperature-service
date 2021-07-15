package com.coenos.temperature.configuration;

import io.swagger.client.ApiClient;
import io.swagger.client.api.DefaultApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class RestConfiguration {

  @Bean
  public DefaultApi defaultApi() {
    return new DefaultApi(apiClient());
  }

  @Bean
  public ApiClient apiClient() {
    return new ApiClient();
  }

}
