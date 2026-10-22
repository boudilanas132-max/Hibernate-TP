package ma.projet.classes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employes")
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    // Coût d'une heure de travail de cet employé, en DH
    private double tauxHoraire;

    // Projets dont cet employé est responsable
    @OneToMany(mappedBy = "responsable")
    private List<Projet> projets = new ArrayList<>();

    // Tâches auxquelles cet employé participe
    @OneToMany(mappedBy = "employe")
    private List<EmployeTache> participations = new ArrayList<>();

    public Employe() {
    }

    public Employe(String nom, String prenom, double tauxHoraire) {
        this.nom = nom;
        this.prenom = prenom;
        this.tauxHoraire = tauxHoraire;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public double getTauxHoraire() {
        return tauxHoraire;
    }

    public void setTauxHoraire(double tauxHoraire) {
        this.tauxHoraire = tauxHoraire;
    }

    public List<Projet> getProjets() {
        return projets;
    }

    public List<EmployeTache> getParticipations() {
        return participations;
    }

    @Override
    public String toString() {
        return "Employe{id=" + id
                + ", nom='" + nom + '\''
                + ", prenom='" + prenom + '\''
                + ", tauxHoraire=" + tauxHoraire + " DH}";
    }
}
