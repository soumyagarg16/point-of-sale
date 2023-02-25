package com.increff.pos.controller;

import com.increff.pos.AbstractUnitTest;
import com.increff.pos.dao.AdminDao;
import com.increff.pos.model.BrandData;
import com.increff.pos.model.BrandForm;
import com.increff.pos.model.UserData;
import com.increff.pos.model.UserForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TestHelper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdminControllerTest extends AbstractUnitTest {
    @Autowired
    AdminApiController adminApiController;
    @Autowired
    AdminDao adminDao;

    @Test
    public void testAddUser() throws ApiException {
        UserForm userForm = TestHelper.createUserForm("a@b.co", "a","supervisor");
        adminApiController.addUser(userForm);
        UserPojo userPojo = adminDao.selectAll().get(0);
        assertEquals("a@b.co", userPojo.getEmail());
        assertEquals("a", userPojo.getPassword());
        assertEquals("supervisor",userPojo.getRole());
    }
    @Test
    public void testDeleteUser(){
        UserPojo userPojo = TestHelper.createUserPojo("a@b.co", "a","supervisor");
        adminDao.insert(userPojo);
        adminApiController.deleteUser(userPojo.getId());
        List<UserPojo> existing = adminDao.selectAll();
        assertEquals(0,existing.size());
    }

    @Test
    public void testGetAllUser() throws ApiException {
        List<UserPojo> userPojos = new ArrayList<>();
        for(int i=1; i<=3; i++){
            UserPojo userPojo = TestHelper.createUserPojo(i+"a@a.com","a","supervisor");
            adminDao.insert(userPojo);
        }
        List<UserData> userDataList = adminApiController.getAllUser();
        assertEquals(3,userDataList.size());
    }
}
