package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;
import ma.projet.classes.Produit;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class ProduitService extends AbstractDao<Produit> {

    public ProduitService() {
        super(Produit.class);
    }

    // 1. Produits appartenant à une catégorie
    public List<Produit> findByCategorie(Categorie categorie) {
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT p FROM Produit p " +
                    "WHERE p.categorie.id = :id",
                    Produit.class)
                    .setParameter("id", categorie.getId())
                    .list();
        }
    }

    // 2. Produits commandés entre deux dates
    public List<Produit> findCommandesEntreDates(
            LocalDate debut, LocalDate fin) {

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT DISTINCT lc.produit " +
                    "FROM LigneCommande lc " +
                    "WHERE lc.commande.date BETWEEN :debut AND :fin",
                    Produit.class)
                    .setParameter("debut", debut)
                    .setParameter("fin", fin)
                    .list();
        }
    }

    // 3. Lignes et quantités d'une commande donnée
    public List<LigneCommande> findLignesByCommande(Long commandeId) {
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                    "SELECT lc FROM LigneCommande lc " +
                    "JOIN FETCH lc.produit " +
                    "WHERE lc.commande.id = :id " +
                    "ORDER BY lc.id",
                    LigneCommande.class)
                    .setParameter("id", commandeId)
                    .list();
        }
    }

    // 4. Produits dont le prix est supérieur à 100 DH
    // Requête nommée définie dans Produit.java
    public List<Produit> findPrixSuperieurA100() {
        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            return session.createNamedQuery(
                    "Produit.prixSuperieurA100",
                    Produit.class)
                    .list();
        }
    }

    // Affichage des produits et quantités d'une commande
    public void afficherProduitsCommande(Long commandeId) {

        try (Session session =
                HibernateUtil.getSessionFactory().openSession()) {

            Commande commande = session.get(Commande.class, commandeId);

            if (commande == null) {
                System.out.println("Commande introuvable : " + commandeId);
                return;
            }

            List<LigneCommande> lignes = findLignesByCommande(commandeId);

            System.out.println("Commande : " + commande.getId()
                    + "     Date : " + commande.getDate());

            System.out.println("Liste des produits :");
            System.out.printf("%-15s %-15s %-10s%n",
                    "Référence", "Prix", "Quantité");

            for (LigneCommande ligne : lignes) {
                Produit produit = ligne.getProduit();

                System.out.printf("%-15s %-15s %-10d%n",
                        produit.getReference(),
                        produit.getPrix() + " DH",
                        ligne.getQuantite());
            }
        }
    }
}