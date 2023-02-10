package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.stereotype.Repository;


@Repository
public class OrderItemDao extends AbstractDao {

    private static String DELETE_ID = "delete from OrderItemPojo where id=:id";
    private static String SELECT_ID = "select orderItemPojo from OrderItemPojo orderItemPojo where id=:id";
    private static String SELECT_ALL = "select orderItemPojo from OrderItemPojo orderItemPojo";
    private static String SELECT_ALL_BY_ORDER_ID = "select orderItemPojo from OrderItemPojo orderItemPojo where orderId=:orderId";

    //private static String update = "update BrandPojo p set ";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        em.persist(orderItemPojo);
    }

//    public void delete(Integer id) {
//        Query query = em.createQuery(delete_id);
//        query.setParameter("id", id);
//        query.executeUpdate();
//    }

    public OrderItemPojo select(Integer id) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ID, OrderItemPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<OrderItemPojo> selectAll(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL_BY_ORDER_ID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }


}
