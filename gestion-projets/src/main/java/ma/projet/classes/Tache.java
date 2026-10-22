package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "taches")
@NamedQuery(
    name = "Tache.prixSuperieurA1000",
    query = "SELECT t FROM Tache t WHERE t.prix > 1000"
)
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    // Prix de la tâche en DH
    private double prix;

    private LocalDate dateDebutPrevue;
    private LocalDate dateFinPrevue;

    private LocalDate dateDebutReelle;
    private LocalDate dateFinReelle;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @OneToMany(mappedBy = "tache")
    private List<EmployeTache> participations = new ArrayList<>();

    public Tache() {
    }

    public Tache(String nom, double prix,
                 LocalDate dateDebutPrevue,
                 LocalDate dateFinPrevue,
                 Projet projet) {
        this.nom = nom;
        this.prix = prix;
        this.dateDebutPrevue = dateDebutPrevue;
        this.dateFinPrevue = dateFinPrevue;
        this.projet = projet;
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

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDate getDateDebutPrevue() {
        return dateDebutPrevue;
    }

    public void setDateDebutPrevue(LocalDate dateDebutPrevue) {
        this.dateDebutPrevue = dateDebutPrevue;
    }

    public LocalDate getDateFinPrevue() {
        return dateFinPrevue;
    }

    public void setDateFinPrevue(LocalDate dateFinPrevue) {
        this.dateFinPrevue = dateFinPrevue;
    }

    public LocalDate getDateDebutReelle() {
        return dateDebutReelle;
    }

    public void setDateDebutReelle(LocalDate dateDebutReelle) {
        this.dateDebutReelle = dateDebutReelle;
    }

    public LocalDate getDateFinReelle() {
        return dateFinReelle;
    }

    public void setDateFinReelle(LocalDate dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<EmployeTache> getParticipations() {
        return participations;
    }

    @Override
    public String toString() {
        return "Tache{id=" + id
                + ", nom='" + nom + '\''
                + ", prix=" + prix + " DH"
                + ", debutReel=" + dateDebutReelle
                + ", finReelle=" + dateFinReelle + '}';
    }
}
