package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestEtatCivil {

    public static void main(String[] args) {

        FemmeService femmeService = new FemmeService();
        HommeService hommeService = new HommeService();
        MariageService mariageService = new MariageService();

        try {
            // Pour éviter les doublons, exécuter ce test sur une base vide
            // ou commenter ce bloc après la première exécution.

            List<Femme> femmes = new ArrayList<>();
            List<Homme> hommes = new ArrayList<>();

            // 1. Créer 10 femmes
            femmes.add(new Femme("RAMI", "SALIMA",
                    LocalDate.of(1970, 5, 12)));
            femmes.add(new Femme("ALI", "AMAL",
                    LocalDate.of(1975, 7, 20)));
            femmes.add(new Femme("ALAOUI", "WAFA",
                    LocalDate.of(1980, 2, 15)));
            femmes.add(new Femme("ALAMI", "KARIMA",
                    LocalDate.of(1965, 1, 10)));
            femmes.add(new Femme("BENALI", "SARA",
                    LocalDate.of(1985, 3, 22)));
            femmes.add(new Femme("IDRISSI", "NADIA",
                    LocalDate.of(1990, 8, 5)));
            femmes.add(new Femme("MANSOURI", "LINA",
                    LocalDate.of(1978, 11, 30)));
            femmes.add(new Femme("TAZI", "HIND",
                    LocalDate.of(1982, 6, 18)));
            femmes.add(new Femme("FASSI", "MERYEM",
                    LocalDate.of(1992, 4, 2)));
            femmes.add(new Femme("CHERKAOUI", "ASMA",
                    LocalDate.of(1988, 9, 14)));

            for (Femme f : femmes) {
                femmeService.create(f);
            }

            // 2. Créer 5 hommes
            hommes.add(new Homme("SAFI", "SAID",
                    LocalDate.of(1960, 3, 5)));
            hommes.add(new Homme("BENJELLOUN", "OMAR",
                    LocalDate.of(1968, 4, 12)));
            hommes.add(new Homme("EL FASSI", "KARIM",
                    LocalDate.of(1972, 9, 17)));
            hommes.add(new Homme("TAHIRI", "MOHAMED",
                    LocalDate.of(1975, 12, 1)));
            hommes.add(new Homme("MANSOUR", "ALI",
                    LocalDate.of(1980, 2, 21)));

            for (Homme h : hommes) {
                hommeService.create(h);
            }

            // 3. Créer les mariages
            // Les mariages terminés ont une date de fin.
            // Les mariages en cours ont dateFin = null.

            // Said : quatre mariages successifs, dont trois terminés
            mariageService.create(new Mariage(
                    hommes.get(0), femmes.get(3),
                    LocalDate.of(1989, 9, 3),
                    LocalDate.of(1990, 9, 3), 0));

            mariageService.create(new Mariage(
                    hommes.get(0), femmes.get(0),
                    LocalDate.of(1990, 9, 3), null, 4));

            mariageService.create(new Mariage(
                    hommes.get(0), femmes.get(1),
                    LocalDate.of(1995, 9, 3), null, 2));

            mariageService.create(new Mariage(
                    hommes.get(0), femmes.get(2),
                    LocalDate.of(2000, 11, 4), null, 3));

            // Omar : marié à deux femmes à des périodes différentes
            mariageService.create(new Mariage(
                    hommes.get(1), femmes.get(4),
                    LocalDate.of(2001, 1, 15),
                    LocalDate.of(2005, 5, 20), 1));

            mariageService.create(new Mariage(
                    hommes.get(1), femmes.get(5),
                    LocalDate.of(2006, 2, 10), null, 2));

            // Karim : deux mariages
            mariageService.create(new Mariage(
                    hommes.get(2), femmes.get(6),
                    LocalDate.of(2002, 3, 12),
                    LocalDate.of(2008, 7, 18), 2));

            mariageService.create(new Mariage(
                    hommes.get(2), femmes.get(7),
                    LocalDate.of(2009, 6, 1), null, 1));

            // Mohamed : un mariage
            mariageService.create(new Mariage(
                    hommes.get(3), femmes.get(8),
                    LocalDate.of(2012, 4, 2), null, 1));

            // Ali : un mariage
            mariageService.create(new Mariage(
                    hommes.get(4), femmes.get(9),
                    LocalDate.of(2015, 10, 10), null, 2));

            // 4. Afficher toutes les femmes
            System.out.println("\n=== LISTE DES FEMMES ===");
            femmeService.afficherToutesLesFemmes();

            // 5. Afficher la femme la plus âgée
            System.out.println("\n=== FEMME LA PLUS AGEE ===");
            Femme agee = femmeService.rechercherFemmeLaPlusAgee();
            System.out.println(agee == null
                    ? "Aucune femme enregistrée."
                    : agee);

            // 6. Afficher les épouses de Said entre deux dates
            System.out.println("\n=== EPOUSES DE SAID ===");

            List<Mariage> epouses =
                    hommeService.afficherEpousesEntreDates(
                            hommes.get(0).getId(),
                            LocalDate.of(1989, 1, 1),
                            LocalDate.of(2001, 12, 31));

            for (Mariage m : epouses) {
                System.out.println(m.getFemme().getNom() + " "
                        + m.getFemme().getPrenom()
                        + " | Début : " + m.getDateDebut()
                        + " | Enfants : " + m.getNombreEnfants());
            }

            // 7. Nombre d'enfants de Salima entre deux dates
            System.out.println(
                    "\n=== ENFANTS DE SALIMA ENTRE DEUX DATES ===");

            Number enfants = femmeService.nombreEnfantsEntreDates(
                    femmes.get(0).getId(),
                    LocalDate.of(1990, 1, 1),
                    LocalDate.of(2000, 12, 31));

            System.out.println("Nombre d'enfants : " + enfants);

            // 8. Femmes mariées au moins deux fois
            System.out.println(
                    "\n=== FEMMES MARIEES AU MOINS DEUX FOIS ===");

            for (Femme f :
                    femmeService.rechercherMarieesAuMoinsDeuxFois()) {
                System.out.println(f);
            }

            // 9. Nombre d'hommes mariés à quatre femmes
            // différentes dans la période indiquée
            System.out.println(
                    "\n=== HOMMES MARIES A QUATRE FEMMES ===");

            long nombreHommes =
                    femmeService.compterHommesMariesAQuatreFemmes(
                            LocalDate.of(1989, 1, 1),
                            LocalDate.of(2020, 12, 31));

            System.out.println("Nombre d'hommes : " + nombreHommes);

            // 10. Afficher les détails de tous les mariages de Said
            System.out.println("\n=== DETAILS DES MARIAGES DE SAID ===");

            mariageService.afficherDetailsHomme(
                    hommes.get(0).getId());

        } catch (Exception e) {
            System.err.println("Erreur pendant le test :");
            e.printStackTrace();

        } finally {
            HibernateUtil.fermer();
        }
    }
}
