package com.rajorshi.samay.model.dao.impl;

import lombok.AccessLevel;
import lombok.Getter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class AbstractDao <E , P> {

    @Getter
    @PersistenceContext
    private EntityManager entityManager;
    @Getter(AccessLevel.PROTECTED)
    private Class<E> entityClass;

    public AbstractDao(EntityManager entityManager, Class<E> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    public E findById(P id) {
        return entityManager.find(getEntityClass(), id);
    }

    protected E insert(E entity)
    {
        entityManager.persist(entity);
        return entity;
    }

    protected List<E> insert(List<E> entities)
    {
        if (entities != null) {
            List<E> result = new ArrayList<E>();
            for (E entity : entities) {
                result.add(insert(entity));
            }
            return result;
        }

        return Collections.emptyList();
    }

    protected E update(E entity)
    {
        entityManager.merge(entity);
        return entity;
    }

    protected List<E> update(List<E> entities)
    {
        if (entities != null) {
            List<E> result = new ArrayList<E>();
            for (E entity : entities) {
                result.add(update(entity));
            }
            return result;
        }

        return Collections.emptyList();
    }

    protected CriteriaQuery<E> getCriteriaQuery()
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        return builder.createQuery(entityClass);
    }


}
