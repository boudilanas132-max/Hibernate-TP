package ma.projet.util;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommande;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY =
            buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Charger les paramètres de application.properties
            Properties properties = new Properties();

            try (InputStream input =
                    HibernateUtil.class.getClassLoader()
                            .getResourceAsStream("application.properties")) {

                if (input == null) {
                    throw new IllegalStateException(
                            "Fichier application.properties introuvable");
                }

                properties.load(input);
            }

            // Configurer Hibernate
            Configuration configuration = new Configuration();
            configuration.setProperties(properties);

            // Déclarer les entités gérées par Hibernate
            configuration.addAnnotatedClass(Categorie.class);
            configuration.addAnnotatedClass(Produit.class);
            configuration.addAnnotatedClass(Commande.class);
            configuration.addAnnotatedClass(LigneCommande.class);

            return configuration.buildSessionFactory();

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void shutdown() {
        if (SESSION_FACTORY != null) {
            SESSION_FACTORY.close();
        }
    }
}