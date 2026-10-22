package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projets")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private LocalDate dateDebut;

    // Plusieurs projets peuvent être gérés par le même employé
    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Employe responsable;

    // Liste des tâches associées au projet
    @OneToMany(mappedBy = "projet")
    private List<Tache> taches = new ArrayList<>();

    public Projet() {
    }

    public Projet(String nom, LocalDate dateDebut, Employe responsable) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.responsable = responsable;
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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Employe getResponsable() {
        return responsable;
    }

    public void setResponsable(Employe responsable) {
        this.responsable = responsable;
    }

    public List<Tache> getTaches() {
        return taches;
    }

    @Override
    public String toString() {
        return "Projet{id=" + id
                + ", nom='" + nom + '\''
                + ", dateDebut=" + dateDebut + '}';
    }
}
