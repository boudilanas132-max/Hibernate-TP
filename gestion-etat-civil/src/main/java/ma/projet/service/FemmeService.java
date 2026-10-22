package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;

public class FemmeService extends AbstractDao<Femme> {

    public FemmeService() {
        super(Femme.class);
    }

    // 1. Requête nommée JPQL : femmes mariées au moins deux fois
    public List<Femme> rechercherMarieesAuMoinsDeuxFois() {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createNamedQuery(
                    "Femme.marieesAuMoinsDeuxFois", Femme.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // 2. Requête SQL native nommée : nombre d'enfants entre deux dates
    public Number nombreEnfantsEntreDates(
            Long femmeId, LocalDate debut, LocalDate fin) {

        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException("Période invalide.");
        }

        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            Object resultat = em.createNamedQuery(
                    "Femme.nombreEnfantsEntreDates")
                    .setParameter("femmeId", femmeId)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .getSingleResult();

            return (Number) resultat;
        } finally {
            em.close();
        }
    }

    // 3. Compter les hommes mariés à exactement quatre femmes
    // différentes entre deux dates, grâce à l'API Criteria.
    public long compterHommesMariesAQuatreFemmes(
            LocalDate debut, LocalDate fin) {

        if (debut == null || fin == null || debut.isAfter(fin)) {
            throw new IllegalArgumentException("Période invalide.");
        }

        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();

            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Mariage> mariage = cq.from(Mariage.class);

            // Sous-requête : pour chaque homme, compter les femmes distinctes
            javax.persistence.criteria.Subquery<Long> sousRequete =
                    cq.subquery(Long.class);

            Root<Mariage> m2 = sousRequete.from(Mariage.class);

            sousRequete.select(
                    cb.countDistinct(m2.get("femme").get("id")));

            sousRequete.where(
                    cb.equal(m2.get("homme").get("id"),
                             mariage.get("homme").get("id")),
                    cb.greaterThanOrEqualTo(
                            m2.<LocalDate>get("dateDebut"), debut),
                    cb.lessThanOrEqualTo(
                            m2.<LocalDate>get("dateDebut"), fin)
            );

            // Filtrer les mariages de la période, puis ne compter
            // chaque homme qu'une seule fois.
            cq.select(cb.countDistinct(mariage.get("homme").get("id")));

            cq.where(
                    cb.greaterThanOrEqualTo(
                            mariage.<LocalDate>get("dateDebut"), debut),
                    cb.lessThanOrEqualTo(
                            mariage.<LocalDate>get("dateDebut"), fin),
                    cb.equal(sousRequete, 4L)
            );

            return em.createQuery(cq).getSingleResult();

        } finally {
            em.close();
        }
    }

    // 4. Afficher toutes les femmes
    public void afficherToutesLesFemmes() {
        for (Femme femme : findAll()) {
            System.out.println(femme);
        }
    }

    // 5. Femme la plus âgée : date de naissance la plus ancienne
    public Femme rechercherFemmeLaPlusAgee() {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            List<Femme> femmes = em.createQuery(
                    "SELECT f FROM Femme f "
                    + "WHERE f.dateNaissance IS NOT NULL "
                    + "ORDER BY f.dateNaissance ASC",
                    Femme.class)
                    .setMaxResults(1)
                    .getResultList();

            return femmes.isEmpty() ? null : femmes.get(0);
        } finally {
            em.close();
        }
    }
}
