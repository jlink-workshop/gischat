package org.heigit.gischat.web.frontend;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/js/")
public class ConfigJsController {

	@Value("${gischat.pollingInterval}")
	private int pollingInterval;

	@Value("${gischat.pychatterUrl}")
	private String pychatterUrl;

	@GetMapping(path = "config.js", produces = "text/javascript")
	public ResponseEntity<String> getChat() {
		// TODO: Extract builder for javascript module file
		return ResponseEntity.ok(
			"""
  			export default {
  			  pollingInterval: %s,
  			  pychatterUrl: "%s"
  			};
  			""".formatted(pollingInterval, pychatterUrl)
		);
	}

}
