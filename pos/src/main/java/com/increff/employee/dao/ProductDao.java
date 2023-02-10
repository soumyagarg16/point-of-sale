package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.employee.pojo.ProductPojo;
import org.springframework.stereotype.Repository;


@Repository
public class ProductDao extends AbstractDao {

    private static String DELETE_ID = "delete from ProductPojo productPojo where id=:id";
    private static String SELECT_ID = "select productPojo from ProductPojo productPojo where id=:id";
    private static String SELECT_ALL = "select productPojo from ProductPojo productPojo";
    private static String SELECT_ID_BY_BARCODE = "select productPojo from ProductPojo productPojo where barcode=:barcode";

    private static String SELECT_ALL_BY_BRAND_CATEGORY_ID = "select productPojo from ProductPojo productPojo where brand_category=:brand_category";


    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(ProductPojo productPojo) {
        em.persist(productPojo);
    }

    public void delete(Integer id) {
        Query query = em.createQuery(DELETE_ID);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public ProductPojo select(Integer id) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public ProductPojo select(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID_BY_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }
    public List<ProductPojo> selectAll(Integer id) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL_BY_BRAND_CATEGORY_ID, ProductPojo.class);
        query.setParameter("brand_category",id);
        return query.getResultList();
    }



}
