package com.example.springbootdocker;

import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
	
	@GetMapping("/index")
	public String index() {
		return "home";
	}

	@GetMapping("/index/secured/hello")
	public String hello(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal, Model model) {
		List<Object> uidList = principal.getAttribute("urn:oid:0.9.2342.19200300.100.1.1");		//uid
		String targetUid = String.valueOf(uidList.get(0));
		model.addAttribute("name", targetUid);
		return "hello";
	}

}
