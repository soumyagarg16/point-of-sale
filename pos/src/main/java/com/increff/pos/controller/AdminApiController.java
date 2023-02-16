package com.increff.pos.controller;

import java.util.ArrayList;
import java.util.List;

import com.increff.pos.util.Helper;
import com.increff.pos.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class AdminApiController {

	@Autowired
	private UserService service;

	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/api/admin/user", method = RequestMethod.POST)
	public void addUser(@RequestBody UserForm userForm) throws ApiException {
		Validate.validateUserForm(userForm);
		UserPojo userPojo = Helper.convertUserFormToPojo(userForm);
		service.add(userPojo);
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(path = "/api/admin/user/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/api/admin/user", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> userPojos = service.getAll();
		List<UserData> userDatas = new ArrayList<UserData>();
		for (UserPojo userPojo : userPojos) {
			userDatas.add(Helper.convertUserPojoToData(userPojo));
		}
		return userDatas;
	}




}