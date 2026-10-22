package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class HommeService extends AbstractDao<Homme> {

    public HommeService() {
        super(Homme.class);
    }

    // Afficher les épouses d'un homme entre deux dates
    public List<Mariage> afficherEpousesEntreDates(
            Long hommeId, LocalDate debut, LocalDate fin) {

        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException("Période invalide.");
        }

        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT m FROM Mariage m "
                    + "JOIN FETCH m.femme "
                    + "WHERE m.homme.id = :hommeId "
                    + "AND m.dateDebut >= :debut "
                    + "AND m.dateDebut <= :fin "
                    + "ORDER BY m.dateDebut",
                    Mariage.class)
                    .setParameter("hommeId", hommeId)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
