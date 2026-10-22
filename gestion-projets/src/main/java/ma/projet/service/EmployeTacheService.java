package ma.projet.service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class EmployeTacheService extends AbstractDao<EmployeTache> {

    public EmployeTacheService() {
        super(EmployeTache.class);
    }

    // Afficher les participations d'une tâche
    public List<EmployeTache> rechercherParTache(Long tacheId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT et FROM EmployeTache et "
                    + "JOIN FETCH et.employe "
                    + "WHERE et.tache.id = :id",
                    EmployeTache.class)
                    .setParameter("id", tacheId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Additionner les coûts horaires de toutes les participations
    public double calculerCoutTotalParProjet(Long projetId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            List<EmployeTache> participations = em.createQuery(
                    "SELECT et FROM EmployeTache et "
                    + "JOIN FETCH et.employe "
                    + "JOIN FETCH et.tache t "
                    + "WHERE t.projet.id = :id",
                    EmployeTache.class)
                    .setParameter("id", projetId)
                    .getResultList();

            double total = 0;

            for (EmployeTache et : participations) {
                total += et.calculerCout();
            }

            return total;
        } finally {
            em.close();
        }
    }
}
