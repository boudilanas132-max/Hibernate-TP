package ma.projet.dao;

import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractDao<T> implements IDao<T> {

    private final Class<T> type;

    protected AbstractDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T objet) {
        executerTransaction(em -> em.persist(objet));
    }

    @Override
    public void update(T objet) {
        executerTransaction(em -> em.merge(objet));
    }

    @Override
    public void delete(T objet) {
        executerTransaction(em -> {
            T managed = em.contains(objet) ? objet : em.merge(objet);
            em.remove(managed);
        });
    }

    @Override
    public T findById(Long id) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();
        try {
            return em.find(type, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();
        try {
            String jpql = "SELECT e FROM " + type.getSimpleName() + " e";
            return em.createQuery(jpql, type).getResultList();
        } finally {
            em.close();
        }
    }

    // Exécuter une opération avec validation ou annulation
    protected void executerTransaction(Consumer<EntityManager> operation) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            operation.accept(em);
            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }
}
