package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mariages")
public class Mariage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "homme_id", nullable = false)
    private Homme homme;

    @ManyToOne(optional = false)
    @JoinColumn(name = "femme_id", nullable = false)
    private Femme femme;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "nombre_enfants", nullable = false)
    private int nombreEnfants;

    public Mariage() {
    }

    public Mariage(Homme homme, Femme femme,
                   LocalDate dateDebut, LocalDate dateFin,
                   int nombreEnfants) {
        this.homme = homme;
        this.femme = femme;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreEnfants = nombreEnfants;
    }

    public Long getId() {
        return id;
    }

    public Homme getHomme() {
        return homme;
    }

    public void setHomme(Homme homme) {
        this.homme = homme;
    }

    public Femme getFemme() {
        return femme;
    }

    public void setFemme(Femme femme) {
        this.femme = femme;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getNombreEnfants() {
        return nombreEnfants;
    }

    public void setNombreEnfants(int nombreEnfants) {
        this.nombreEnfants = nombreEnfants;
    }

    // Sans date de fin : mariage considéré en cours dans l'application
    public boolean estEnCours() {
        return dateFin == null;
    }

    @Override
    public String toString() {
        return "Mariage{id=" + id
                + ", homme=" + homme.getNom() + " " + homme.getPrenom()
                + ", femme=" + femme.getNom() + " " + femme.getPrenom()
                + ", dateDebut=" + dateDebut
                + ", dateFin=" + dateFin
                + ", nombreEnfants=" + nombreEnfants + '}';
    }
}
