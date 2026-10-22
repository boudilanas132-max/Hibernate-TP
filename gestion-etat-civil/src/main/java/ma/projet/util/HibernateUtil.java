package ma.projet.util;

import ma.projet.beans.Homme;
import ma.projet.beans.Femme;
import ma.projet.beans.Mariage;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY =
            construireSessionFactory();

    private static SessionFactory construireSessionFactory() {
        try {
            Properties properties = new Properties();

            // Charger application.properties depuis les ressources Maven
            try (InputStream input =
                    HibernateUtil.class.getClassLoader()
                            .getResourceAsStream("application.properties")) {

                if (input == null) {
                    throw new IllegalStateException(
                            "application.properties introuvable");
                }

                properties.load(input);
            }

            Configuration configuration = new Configuration();
            configuration.setProperties(properties);

            // Déclarer les entités JPA
            configuration.addAnnotatedClass(Homme.class);
            configuration.addAnnotatedClass(Femme.class);
            configuration.addAnnotatedClass(Mariage.class);

            return configuration.buildSessionFactory();

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void fermer() {
        if (SESSION_FACTORY != null && !SESSION_FACTORY.isClosed()) {
            SESSION_FACTORY.close();
        }
    }
}
