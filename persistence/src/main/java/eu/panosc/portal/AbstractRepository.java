package eu.panosc.portal;

import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.util.List;

abstract class AbstractRepository<T> {

    private final Provider<EntityManager> entityManager;

    protected AbstractRepository(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    public void persist(final T object) {
        entityManager.get().persist(object);
    }

    public T merge(final T object) {
        T persistedObject = entityManager.get().merge(object);
        return persistedObject;
    }

    public void remove(final T object) {
        EntityManager em = entityManager.get();

        em.remove(em.contains(object) ? object : em.merge(object));
    }

    public abstract List<T> getAll();

    public EntityManager getEntityManager() {
        return entityManager.get();
    }

}
