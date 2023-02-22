package com.increff.pos.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.increff.pos.model.SignupForm;
import com.increff.pos.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.AdminService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/session")
public class LoginController {

	@Autowired
	private AdminService service;
	@Autowired
	private InfoData info;
	
	@ApiOperation(value = "Logs in a user")
	@RequestMapping(path = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView login(HttpServletRequest req, LoginForm loginForm){
		UserPojo userPojo = service.get(loginForm.getEmail());
		if(userPojo == null){
			info.setMessage("Invalid email!");
			return new ModelAndView("redirect:/site/login");
		}
		if(!Objects.equals(userPojo.getPassword(), loginForm.getPassword())){
			info.setMessage("Wrong password!");
			return new ModelAndView("redirect:/site/login");
		}

		// Create authentication object
		Authentication authentication = convert(userPojo);
		// Create new session
		HttpSession session = req.getSession(true);
		// Attach Spring SecurityContext to this new session
		SecurityUtil.createContext(session);
		// Attach Authentication object to the Security Context
		SecurityUtil.setAuthentication(authentication);
		info.setMessage("");
		return new ModelAndView("redirect:/ui/home");

	}

	@ApiOperation(value = "SignUp a user")
	@RequestMapping(path = "/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ModelAndView signup(HttpServletRequest req, SignupForm signupForm) throws ApiException {
		info.setMessage(Validate.validateSignupForm(signupForm));
		if(info.getMessage()!=""){
			return new ModelAndView("redirect:/site/signup");
		}
		UserPojo userPojo = Helper.convertSignupFormToPojo(signupForm);
		//if exists in properties file give supervisor role else operator.
		Properties property = new Properties();
		try {
			property.load(new FileInputStream("pos.properties"));
		} catch (IOException e) {
			System.out.println("Failed to load properties file.");
			e.printStackTrace();
		}
		if(property.containsKey(userPojo.getEmail())){
			userPojo.setRole("supervisor");
		}
		else userPojo.setRole("operator");

		Normalize.normalizeEmail(userPojo);
		UserPojo existing = service.get(userPojo.getEmail());
		if (existing != null) {
			info.setMessage("User with given email already exists!");
			return new ModelAndView("redirect:/site/signup");
		}
		service.add(userPojo);
		info.setMessage("");
		return new ModelAndView("redirect:/site/login");
	}


	@RequestMapping(path = "/session/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().invalidate();
		return new ModelAndView("redirect:/site/logout");
	}

	private static Authentication convert(UserPojo userPojo) {
		// Create principal
		UserPrincipal principal = new UserPrincipal();
		principal.setEmail(userPojo.getEmail());
		principal.setId(userPojo.getId());
		principal.setRole(userPojo.getRole());

		// Create Authorities
		ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(userPojo.getRole()));

		// Create Authentication
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, null,
				authorities);
		return token;
	}

}
