package org.heigit.gischat.web;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebApplicationConfiguration implements WebMvcConfigurer {

	/**
	 * Forward all allowed frontend paths to frontend homepage
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
		registry.addViewController("").setViewName("forward:/index.html");
		registry.addViewController("/chat/**").setViewName("forward:/index.html");
	}

	/**
	 * Allow CORS access from all places.
	 * Necessary if frontend runs on different domain.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
				.allowedOrigins("*")
				.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE");
	}

}