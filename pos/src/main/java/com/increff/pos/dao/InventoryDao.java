package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.InventoryPojo;
import org.springframework.stereotype.Repository;


@Repository
public class InventoryDao extends AbstractDao {


    private static String SELECT_ID = "select inventoryPojo from InventoryPojo inventoryPojo where id=:id";
    private static String SELECT_ALL = "select inventoryPojo from InventoryPojo inventoryPojo";

    //private static String update = "update BrandPojo p set ";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(InventoryPojo inventoryPojo) {
        em.persist(inventoryPojo);
    }


    public InventoryPojo select(Integer id) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ID, InventoryPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }


    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
        return query.getResultList();
    }




}
