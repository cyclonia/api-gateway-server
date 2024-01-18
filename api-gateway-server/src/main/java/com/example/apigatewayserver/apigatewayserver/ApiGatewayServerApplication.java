package com.example.apigatewayserver.apigatewayserver;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayServerApplication.class, args);
	}

	@Bean
	public RouteLocator myRouterLocator(RouteLocatorBuilder builder) {

		Function<PredicateSpec, Buildable<Route>> routeFunction 
			= p -> p.path("/get")
				.filters(f -> f.addRequestHeader("Myheader", "MyValue"))
				.uri("http://httpbin.org:80");

		Function<PredicateSpec, Buildable<Route>> exchangeRoute 
			= p -> p.path("/currency-exchange/**")
				.uri("lb://currency-exchange-service");

		Function<PredicateSpec, Buildable<Route>> conversionRoute 
		= p -> p.path("/currency-conversion/**")
				.uri("lb://currency-conversion-service");
		
		return builder.routes()
				.route(routeFunction)
				.route(exchangeRoute)
				.route(conversionRoute)
				.build();

	}

}
