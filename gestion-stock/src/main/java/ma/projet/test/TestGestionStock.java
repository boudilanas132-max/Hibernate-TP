package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;

import ma.projet.service.CategorieService;
import ma.projet.service.ProduitService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class TestGestionStock {

    public static void main(String[] args) {

        CategorieService categorieService = new CategorieService();
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        LigneCommandeService ligneService = new LigneCommandeService();

        try {
            // =========================================
            // 1. CREATION DES DONNEES DE TEST
            // =========================================

            // Suffixe différent à chaque exécution
            String suffixe = String.valueOf(System.currentTimeMillis());

            Categorie categorie =
                    new Categorie("Informatique-" + suffixe);

            categorieService.create(categorie);

            Produit p1 = new Produit(
                    "ES12-" + suffixe, 120.0, 50, categorie);

            Produit p2 = new Produit(
                    "ZR85-" + suffixe, 100.0, 40, categorie);

            Produit p3 = new Produit(
                    "EE85-" + suffixe, 200.0, 25, categorie);

            produitService.create(p1);
            produitService.create(p2);
            produitService.create(p3);

            // Créer une commande datée d'aujourd'hui
            Commande commande = new Commande(LocalDate.now());

            commande.ajouterLigne(new LigneCommande(p1, 7));
            commande.ajouterLigne(new LigneCommande(p2, 14));
            commande.ajouterLigne(new LigneCommande(p3, 5));

            // CascadeType.ALL enregistre aussi les lignes
            commandeService.create(commande);

            // =========================================
            // 2. TEST : PRODUITS PAR CATEGORIE
            // =========================================

            System.out.println("\n=== PRODUITS PAR CATEGORIE ===");

            List<Produit> produitsCategorie =
                    produitService.findByCategorie(categorie);

            for (Produit p : produitsCategorie) {
                System.out.println(p);
            }

            // =========================================
            // 3. TEST : PRODUITS COMMANDES ENTRE 2 DATES
            // =========================================

            System.out.println("\n=== PRODUITS COMMANDES ENTRE DEUX DATES ===");

            LocalDate debut = LocalDate.now().minusDays(7);
            LocalDate fin = LocalDate.now();

            List<Produit> produitsPeriode =
                    produitService.findCommandesEntreDates(debut, fin);

            for (Produit p : produitsPeriode) {
                System.out.println(p);
            }

            // =========================================
            // 4. TEST : PRODUITS D'UNE COMMANDE
            // =========================================

            System.out.println("\n=== DETAIL DE LA COMMANDE ===");

            Long commandeId = commande.getId();

            produitService.afficherProduitsCommande(commandeId);

            // =========================================
            // 5. TEST : PRIX SUPERIEUR A 100 DH
            // =========================================

            System.out.println("\n=== PRODUITS DE PRIX > 100 DH ===");

            List<Produit> produitsChers =
                    produitService.findPrixSuperieurA100();

            for (Produit p : produitsChers) {
                System.out.println(p);
            }

            // =========================================
            // 6. TEST : AUTRES OPERATIONS CRUD
            // =========================================

            System.out.println("\n=== RECHERCHE D'UN PRODUIT PAR ID ===");

            Produit produitTrouve = produitService.findById(p1.getId());
            System.out.println(produitTrouve);

            System.out.println("\n=== LISTE DES COMMANDES ===");

            for (Commande c : commandeService.findAll()) {
                System.out.println(c);
            }

            // Le service des lignes est également testé
            System.out.println("\n=== NOMBRE DE LIGNES DE COMMANDE ===");

            System.out.println(
                    "Lignes enregistrées : "
                    + ligneService.findAll().size());

        } catch (Exception e) {
            System.err.println("Erreur pendant le test : " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Fermer proprement Hibernate
            HibernateUtil.shutdown();
        }
    }
}