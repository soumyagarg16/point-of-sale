package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;


@Repository
public class BrandDao extends AbstractDao {

    private static String SELECT_ID = "select brandPojo from BrandPojo brandPojo where id=:id";
    private static String SELECT_ALL = "select brandPojo from BrandPojo brandPojo";
    private static String SELECT_BY_BRAND_CATEGORY  = "select brandPojo from BrandPojo brandPojo where brand=:brand and category=:category";




    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(BrandPojo brandPojo) {
        em.persist(brandPojo);
    }


    public BrandPojo select(Integer id) {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }
    public BrandPojo select(String brand, String category){
        TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY,BrandPojo.class);
        query.setParameter("brand",brand);
        query.setParameter("category",category);
        return getSingle(query);
    }

    public List<BrandPojo> selectAllByBrandCategory(String brand, String category) {
        String myQuery = "select brandPojo from BrandPojo brandPojo where ";
        if(brand==""){
            myQuery+="category=:category";
            TypedQuery<BrandPojo> query = getQuery(myQuery, BrandPojo.class);
            query.setParameter("category",category);
            return query.getResultList();
        }
        else if(category==""){
            myQuery+="brand=:brand";
            TypedQuery<BrandPojo> query = getQuery(myQuery, BrandPojo.class);
            query.setParameter("brand",brand);
            return query.getResultList();
        }
        else{
           myQuery = SELECT_BY_BRAND_CATEGORY;
            TypedQuery<BrandPojo> query = getQuery(myQuery, BrandPojo.class);
            query.setParameter("brand",brand);
            query.setParameter("category",category);
            return query.getResultList();
        }

    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }




}
