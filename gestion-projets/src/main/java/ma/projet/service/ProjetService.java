package ma.projet.service;

import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProjetService extends AbstractDao<Projet> {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProjetService() {
        super(Projet.class);
    }

    // Tâches prévues pour un projet
    public List<Tache> afficherTachesPlanifiees(Long projetId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT t FROM Tache t WHERE t.projet.id = :id "
                    + "ORDER BY t.dateDebutPrevue",
                    Tache.class)
                    .setParameter("id", projetId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Tâches ayant une date de début et une date de fin réelles
    public List<Tache> afficherTachesRealisees(Long projetId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT t FROM Tache t "
                    + "WHERE t.projet.id = :id "
                    + "AND t.dateDebutReelle IS NOT NULL "
                    + "AND t.dateFinReelle IS NOT NULL "
                    + "ORDER BY t.dateDebutReelle",
                    Tache.class)
                    .setParameter("id", projetId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Affichage similaire à l'exemple de l'énoncé
    public void afficherRapportProjet(Long projetId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            Projet projet = em.find(Projet.class, projetId);

            if (projet == null) {
                System.out.println("Projet introuvable : " + projetId);
                return;
            }

            System.out.println("Projet : " + projet.getId()
                    + "    Nom : " + projet.getNom()
                    + "    Date début : " + projet.getDateDebut());

            System.out.println("Liste des tâches réalisées :");
            System.out.printf("%-5s %-20s %-20s %-20s%n",
                    "Num", "Nom", "Date Début Réelle",
                    "Date Fin Réelle");

            for (Tache t : afficherTachesRealisees(projetId)) {
                String debut = t.getDateDebutReelle() == null
                        ? "-" : t.getDateDebutReelle().format(FORMAT);
                String fin = t.getDateFinReelle() == null
                        ? "-" : t.getDateFinReelle().format(FORMAT);

                System.out.printf("%-5s %-20s %-20s %-20s%n",
                        t.getId(), t.getNom(), debut, fin);
            }
        } finally {
            em.close();
        }
    }
}
