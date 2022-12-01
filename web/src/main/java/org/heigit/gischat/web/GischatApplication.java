package org.heigit.gischat.web;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.web.servlet.error.*;

@SpringBootApplication
public class GischatApplication implements ErrorController {

	public static void main(String[] args) {
		SpringApplication.run(GischatApplication.class, args);
	}
}
