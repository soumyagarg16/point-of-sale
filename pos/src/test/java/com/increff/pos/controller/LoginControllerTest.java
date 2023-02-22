package com.increff.pos.controller;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.AdminDao;
import com.increff.pos.model.InfoData;
import com.increff.pos.model.LoginForm;
import com.increff.pos.model.SignupForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class LoginControllerTest extends AbstractUnitTest{
    @Autowired
    LoginController loginController;
    @Autowired
    AdminDao adminDao;
    @Autowired
    InfoData info;

    @Test
    public void testLogin(){
        UserPojo userPojo = TestHelper.createUserPojo("a@a.c","a","supervisor");
        adminDao.insert(userPojo);

        LoginForm loginForm = new LoginForm();
        MockHttpServletRequest request = new MockHttpServletRequest();
        loginForm.setEmail("a@a.c");
        loginForm.setPassword("a");
        loginController.login(request,loginForm);
        assertEquals("",info.getMessage());
    }
    @Test
    public void testSignup() throws ApiException {

        SignupForm signupForm = new SignupForm();
        MockHttpServletRequest request = new MockHttpServletRequest();
        signupForm.setEmail("a@a.c");
        signupForm.setPassword("a");
        loginController.signup(request,signupForm);
        assertEquals("",info.getMessage());
    }
}
