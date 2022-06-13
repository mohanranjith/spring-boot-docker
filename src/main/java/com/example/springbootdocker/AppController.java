package com.example.springbootdocker;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

	@GetMapping("/detail")
	public String hello(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal, Model model) {
		Object uidObj = principal.getFirstAttribute("urn:oid:0.9.2342.19200300.100.1.1");		//uid
		model.addAttribute("uid", String.valueOf(uidObj));
		return "detail";
	}

}
