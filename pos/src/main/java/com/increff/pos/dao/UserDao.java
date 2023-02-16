package com.increff.pos.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.UserPojo;

@Repository
public class UserDao extends AbstractDao {

	private static String DELETE_ID = "delete from UserPojo userPojo where id=:id";
	private static String SELECT_ID = "select userPojo from UserPojo userPojo where id=:id";
	private static String SELECT_EMAIL = "select userPojo from UserPojo userPojo where email=:email";
	private static String SELECT_ALL = "select userPojo from UserPojo userPojo";

	
	@Transactional
	public void insert(UserPojo userPojo) {
		em().persist(userPojo);
	}

	public Integer delete(Integer id) {
		Query query = em().createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public UserPojo select(Integer id) {
		TypedQuery<UserPojo> query = getQuery(SELECT_ID, UserPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(SELECT_EMAIL, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	public List<UserPojo> selectAll() {
		TypedQuery<UserPojo> query = getQuery(SELECT_ALL, UserPojo.class);
		return query.getResultList();
	}


}
