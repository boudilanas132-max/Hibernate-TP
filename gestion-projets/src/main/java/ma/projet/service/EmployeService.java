package ma.projet.service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.AbstractDao;
import ma.projet.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class EmployeService extends AbstractDao<Employe> {

    public EmployeService() {
        super(Employe.class);
    }

    // Afficher les participations aux tâches d'un employé
    public List<EmployeTache> afficherTachesRealisees(Long employeId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT et FROM EmployeTache et "
                    + "JOIN FETCH et.tache "
                    + "WHERE et.employe.id = :id "
                    + "ORDER BY et.tache.nom",
                    EmployeTache.class)
                    .setParameter("id", employeId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Afficher les projets gérés par un employé
    public List<Projet> afficherProjetsGeres(Long employeId) {
        EntityManager em = HibernateUtil.getSessionFactory()
                .createEntityManager();

        try {
            return em.createQuery(
                    "SELECT p FROM Projet p "
                    + "WHERE p.responsable.id = :id "
                    + "ORDER BY p.nom",
                    Projet.class)
                    .setParameter("id", employeId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
