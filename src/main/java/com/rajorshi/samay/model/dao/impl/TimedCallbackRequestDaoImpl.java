package com.rajorshi.samay.model.dao.impl;

import com.rajorshi.samay.model.dao.CallbackSearchFilter;
import com.rajorshi.samay.model.dao.TimedCallbackRequestDao;
import com.rajorshi.samay.model.repository.CallbackRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class TimedCallbackRequestDaoImpl extends  AbstractDao<CallbackRequest,Long> implements TimedCallbackRequestDao {

    @Inject
    public TimedCallbackRequestDaoImpl(EntityManager entityManager) {
        super(entityManager, CallbackRequest.class);
    }

    @Override
    public CallbackRequest createNewTimedCallback(CallbackRequest callback) {
        return this.insert(callback);
    }

    @Override
    public CallbackRequest save(CallbackRequest callback) {
        return update(callback);
    }

    @Override
    public List<CallbackRequest> save(List<CallbackRequest> callbacks) {
        return update(callbacks);
    }

    @Override
    public List<CallbackRequest> getPendingCallbacks(String namespace, Date date, long limit) {
        TypedQuery<CallbackRequest> query= getEntityManager()
                .createNamedQuery(CallbackRequest.QUERY_GET_PENDING_FOR_UPDATE, CallbackRequest.class)
                .setParameter(1, namespace)
                .setParameter(2, date)
                .setParameter(3, limit);
        return query.getResultList();
    }

    @Override
    public int deleteCallbacksByDate(Date date, long limit) {
        TypedQuery<CallbackRequest> query= getEntityManager()
                .createNamedQuery(CallbackRequest.QUERY_PURGE_BY_DATE, CallbackRequest.class)
                .setParameter(1, date)
                .setParameter(2, limit);
        return query.executeUpdate();

    }

    @Override
    public List<CallbackRequest> findCallbacks(CallbackSearchFilter filter, int offset, int limit) {

        CriteriaQuery<CallbackRequest> cq = getCriteriaQuery();
        Root<CallbackRequest> request = cq.from(getEntityClass());

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();
        if (filter.getExtRefId() != null) {
            predicates.add(cb.equal(request.get("ext_ref_id"), filter.getExtRefId()));
        } else {
            if (filter.getNamespace() != null) {
                predicates.add(cb.equal(request.get("namespace"), filter.getNamespace()));
            }
            if (filter.getSource() != null) {
                predicates.add(cb.equal(request.get("source"), filter.getSource()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(request.get("status"), filter.getStatus()));
            }
            Expression<Date> schedule = request.<Date>get("call_at").as(Date.class);
            if (filter.getBefore() != null && filter.getAfter() != null) {
                predicates.add(
                        cb.between(schedule, filter.getAfter(), filter.getBefore())
                );
            } else {
                predicates.add(
                        (filter.getBefore() != null)
                                ? cb.lessThanOrEqualTo(schedule, filter.getBefore())
                                : cb.greaterThanOrEqualTo(schedule, filter.getAfter())
                );
            }
        }

        cq = cq.select(request);
        cq = cq.where(predicates.toArray(new Predicate[predicates.size()]));

        TypedQuery<CallbackRequest> query = getEntityManager().createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return  query.getResultList();
    }
}
