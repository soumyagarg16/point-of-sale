package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.employee.util.Normalize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.UserDao;
import com.increff.employee.pojo.UserPojo;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public void add(UserPojo userPojo) throws ApiException {
		Normalize.normalizeEmail(userPojo);
		UserPojo existing = dao.select(userPojo.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists!");
		}
		dao.insert(userPojo);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional
	public void delete(Integer id) {
		dao.delete(id);
	}

}
