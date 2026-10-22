package ma.projet.service;

import ma.projet.classes.Tache;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class TacheService extends AbstractDao<Tache> {

    public TacheService() {
        super(Tache.class);
    }

    // Utilise la requête nommée déclarée dans Tache.java
    public List<Tache> rechercherPrixSuperieurA1000() {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createNamedQuery(
                    "Tache.prixSuperieurA1000", Tache.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Tâches réalisées pendant l'intervalle demandé
    public List<Tache> rechercherEntreDeuxDates(
            LocalDate debut, LocalDate fin) {

        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException(
                    "Les dates sont invalides.");
        }

        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT t FROM Tache t "
                    + "WHERE t.dateDebutReelle IS NOT NULL "
                    + "AND t.dateFinReelle IS NOT NULL "
                    + "AND t.dateDebutReelle >= :debut "
                    + "AND t.dateFinReelle <= :fin "
                    + "ORDER BY t.dateDebutReelle",
                    Tache.class)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
