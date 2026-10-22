package ma.projet.util;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY =
            construireSessionFactory();

    private static SessionFactory construireSessionFactory() {
        try {
            // Charger la configuration depuis src/main/resources
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

            // Configurer Hibernate et enregistrer les entités JPA
            Configuration configuration = new Configuration();
            configuration.setProperties(properties);

            configuration.addAnnotatedClass(Employe.class);
            configuration.addAnnotatedClass(Projet.class);
            configuration.addAnnotatedClass(Tache.class);
            configuration.addAnnotatedClass(EmployeTache.class);

            return configuration.buildSessionFactory();

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // SessionFactory implémente EntityManagerFactory :
    // nous pouvons donc utiliser l'API JPA EntityManager.
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void fermer() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}
