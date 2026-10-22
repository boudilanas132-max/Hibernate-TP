package ma.projet.dao;

import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

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
            T managed = em.contains(objet)
                    ? objet : em.merge(objet);
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

    // Exécute une opération dans une transaction JPA
    protected void executerTransaction(
            java.util.function.Consumer<EntityManager> operation) {

        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            operation.accept(em);
            transaction.commit();

        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }
}
