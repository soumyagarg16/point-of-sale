package com.increff.pos.service;

import com.increff.pos.dao.AdminDao;
import com.increff.pos.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminDao dao;

    @Transactional
    public void add(UserPojo userPojo) throws ApiException {
        UserPojo existing = get(userPojo.getEmail());
        System.out.println("here!");
        if (existing == null) {
            dao.insert(userPojo);
            System.out.println("null existing");
        } else {
            System.out.println("thrwoing execpuob!");
            throw new ApiException("Email already registered!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        dao.delete(id);
    }

    public UserPojo get(String email) {
        return dao.select(email);
    }

    public List<UserPojo> getAll() {
        return dao.selectAll();
    }

}
