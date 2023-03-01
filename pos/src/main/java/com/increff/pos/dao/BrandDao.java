package com.increff.pos.dao;

import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.pos.pojo.BrandPojo;
import org.springframework.stereotype.Repository;


@Repository
public class BrandDao extends AbstractDao {

    private static final String SELECT_ID = "select brandPojo from BrandPojo brandPojo where id=:id";
    private static final String SELECT_ALL = "select brandPojo from BrandPojo brandPojo";
    private static final String SELECT_BY_BRAND_CATEGORY  = "select brandPojo from BrandPojo brandPojo where brand=:brand and category=:category";
    private static String MY_QUERY = "select brandPojo from BrandPojo brandPojo where ";



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
    //TODO create more static final queries and add logic in service layer
    public List<BrandPojo> selectAllByBrandCategory(String brand, String category) {
        if(Objects.equals(brand, "")){
            MY_QUERY+="category=:category";
            TypedQuery<BrandPojo> query = getQuery(MY_QUERY, BrandPojo.class);
            query.setParameter("category",category);
            return query.getResultList();
        }
        else if(Objects.equals(category, "")){
            MY_QUERY+="brand=:brand";
            TypedQuery<BrandPojo> query = getQuery(MY_QUERY, BrandPojo.class);
            query.setParameter("brand",brand);
            return query.getResultList();
        }
        else{
            MY_QUERY = SELECT_BY_BRAND_CATEGORY;
            TypedQuery<BrandPojo> query = getQuery(MY_QUERY, BrandPojo.class);
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
