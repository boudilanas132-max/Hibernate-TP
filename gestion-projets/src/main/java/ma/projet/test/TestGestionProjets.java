package ma.projet.test;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class TestGestionProjets {

    public static void main(String[] args) {

        EmployeService employeService = new EmployeService();
        ProjetService projetService = new ProjetService();
        TacheService tacheService = new TacheService();
        EmployeTacheService employeTacheService =
                new EmployeTacheService();

        try {
            // 1. Créer un employé responsable du projet
            Employe employe = new Employe(
                    "Alami", "Youssef", 150.0);
            employeService.create(employe);

            // 2. Créer un projet
            Projet projet = new Projet(
                    "Gestion de stock",
                    LocalDate.of(2013, 1, 14),
                    employe);
            projetService.create(projet);

            // 3. Créer les tâches prévues pour ce projet
            Tache analyse = new Tache(
                    "Analyse", 800.0,
                    LocalDate.of(2013, 2, 1),
                    LocalDate.of(2013, 2, 28),
                    projet);

            Tache conception = new Tache(
                    "Conception", 1200.0,
                    LocalDate.of(2013, 3, 1),
                    LocalDate.of(2013, 3, 31),
                    projet);

            Tache developpement = new Tache(
                    "Développement", 2500.0,
                    LocalDate.of(2013, 4, 1),
                    LocalDate.of(2013, 4, 30),
                    projet);

            // Enregistrer les tâches
            tacheService.create(analyse);
            tacheService.create(conception);
            tacheService.create(developpement);

            // 4. Enregistrer les dates réelles de réalisation
            analyse.setDateDebutReelle(
                    LocalDate.of(2013, 2, 10));
            analyse.setDateFinReelle(
                    LocalDate.of(2013, 2, 20));

            conception.setDateDebutReelle(
                    LocalDate.of(2013, 3, 10));
            conception.setDateFinReelle(
                    LocalDate.of(2013, 3, 15));

            developpement.setDateDebutReelle(
                    LocalDate.of(2013, 4, 10));
            developpement.setDateFinReelle(
                    LocalDate.of(2013, 4, 25));

            tacheService.update(analyse);
            tacheService.update(conception);
            tacheService.update(developpement);

            // 5. Enregistrer les heures travaillées
            employeTacheService.create(
                    new EmployeTache(employe, analyse, 20));
            employeTacheService.create(
                    new EmployeTache(employe, conception, 35));
            employeTacheService.create(
                    new EmployeTache(employe, developpement, 80));

            // 6. Afficher le rapport du projet
            System.out.println("\n=== RAPPORT DU PROJET ===");
            projetService.afficherRapportProjet(projet.getId());

            // 7. Afficher les tâches planifiées
            System.out.println("\n=== TACHES PLANIFIEES ===");
            List<Tache> planifiees =
                    projetService.afficherTachesPlanifiees(
                            projet.getId());

            for (Tache t : planifiees) {
                System.out.println(t);
            }

            // 8. Afficher les tâches dont le prix dépasse 1000 DH
            System.out.println(
                    "\n=== TACHES DE PRIX SUPERIEUR A 1000 DH ===");

            for (Tache t :
                    tacheService.rechercherPrixSuperieurA1000()) {
                System.out.println(t);
            }

            // 9. Afficher les tâches réalisées entre deux dates
            System.out.println(
                    "\n=== TACHES REALISEES ENTRE DEUX DATES ===");

            List<Tache> entreDates =
                    tacheService.rechercherEntreDeuxDates(
                            LocalDate.of(2013, 3, 1),
                            LocalDate.of(2013, 4, 30));

            for (Tache t : entreDates) {
                System.out.println(t);
            }

            // 10. Afficher les tâches réalisées par l'employé
            System.out.println(
                    "\n=== PARTICIPATIONS DE L'EMPLOYE ===");

            for (EmployeTache et :
                    employeService.afficherTachesRealisees(
                            employe.getId())) {
                System.out.println(et);
            }

            // 11. Afficher les projets gérés par l'employé
            System.out.println(
                    "\n=== PROJETS GERES PAR L'EMPLOYE ===");

            for (Projet p :
                    employeService.afficherProjetsGeres(
                            employe.getId())) {
                System.out.println(p);
            }

            // 12. Calculer le coût total du projet
            double cout =
                    employeTacheService.calculerCoutTotalParProjet(
                            projet.getId());

            System.out.println(
                    "\nCoût total de la main-d'œuvre : "
                    + cout + " DH");

        } catch (Exception e) {
            System.err.println(
                    "Une erreur est survenue pendant le test :");
            e.printStackTrace();

        } finally {
            // Fermer Hibernate à la fin du programme
            HibernateUtil.fermer();
        }
    }
}
