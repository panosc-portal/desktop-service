package eu.panosc.portal;

import com.google.inject.Inject;
import com.google.inject.Provider;
import eu.panosc.portal.core.domain.InstanceSession;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class InstanceSessionRepository extends AbstractRepository<InstanceSession> {

    @Inject
    InstanceSessionRepository(final Provider<EntityManager> entityManager) {
        super(entityManager);
    }

    public List<InstanceSession> getAll() {
        final TypedQuery<InstanceSession> query = getEntityManager()
            .createNamedQuery("instanceSession.getAll", InstanceSession.class);
        return query.getResultList();
    }

    public InstanceSession getByInstanceId(final Long instanceId) {
        try {
            final TypedQuery<InstanceSession> query = getEntityManager()
                .createNamedQuery("instanceSession.getByInstanceId", InstanceSession.class);
            query.setParameter("instanceId", instanceId);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public InstanceSession getById(final Long id) {
        try {
            final TypedQuery<InstanceSession> query = getEntityManager()
                .createNamedQuery("instanceSession.getById", InstanceSession.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            return null;
        }
    }

    public void delete(final InstanceSession instanceSession) {
        remove(instanceSession);
    }

    public void save(final InstanceSession instanceSession) {
        if (instanceSession.getId() == null) {
            persist(instanceSession);
        } else {
            merge(instanceSession);
        }
    }
}
