package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MariageService extends AbstractDao<Mariage> {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MariageService() {
        super(Mariage.class);
    }

    // Rechercher tous les mariages d'un homme
    public List<Mariage> rechercherParHomme(Long hommeId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT m FROM Mariage m "
                    + "JOIN FETCH m.femme "
                    + "WHERE m.homme.id = :id "
                    + "ORDER BY m.dateDebut",
                    Mariage.class)
                    .setParameter("id", hommeId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher les mariages en cours et les mariages terminés
    public void afficherDetailsHomme(Long hommeId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            Homme homme = em.find(Homme.class, hommeId);

            if (homme == null) {
                System.out.println("Homme introuvable : " + hommeId);
                return;
            }

            List<Mariage> mariages = rechercherParHomme(hommeId);

            System.out.println("Nom : "
                    + homme.getNom().toUpperCase() + " "
                    + homme.getPrenom().toUpperCase());

            System.out.println("\nMariages En Cours :");
            int numero = 1;

            for (Mariage m : mariages) {
                if (m.estEnCours()) {
                    afficherLigne(numero++, m, false);
                }
            }

            System.out.println("\nMariages échoués :");
            numero = 1;

            for (Mariage m : mariages) {
                if (!m.estEnCours()) {
                    afficherLigne(numero++, m, true);
                }
            }

        } finally {
            em.close();
        }
    }

    private void afficherLigne(int numero, Mariage m,
                               boolean afficherDateFin) {
        String debut = m.getDateDebut() == null
                ? "-" : m.getDateDebut().format(FORMAT);

        String fin = m.getDateFin() == null
                ? "-" : m.getDateFin().format(FORMAT);

        System.out.print(numero + ". Femme : "
                + m.getFemme().getNom().toUpperCase() + " "
                + m.getFemme().getPrenom().toUpperCase()
                + "    Date Début : " + debut);

        if (afficherDateFin) {
            System.out.print("    Date Fin : " + fin);
        }

        System.out.println("    Nbr Enfants : " + m.getNombreEnfants());
    }
}
