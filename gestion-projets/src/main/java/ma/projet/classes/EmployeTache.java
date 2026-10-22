package ma.projet.classes;

import javax.persistence.*;

@Entity
@Table(name = "employe_taches")
public class EmployeTache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tache_id")
    private Tache tache;

    private double nombreHeures;

    public EmployeTache() {
    }

    public EmployeTache(Employe employe, Tache tache,
                        double nombreHeures) {
        this.employe = employe;
        this.tache = tache;
        this.nombreHeures = nombreHeures;
    }

    public Long getId() {
        return id;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Tache getTache() {
        return tache;
    }

    public void setTache(Tache tache) {
        this.tache = tache;
    }

    public double getNombreHeures() {
        return nombreHeures;
    }

    public void setNombreHeures(double nombreHeures) {
        this.nombreHeures = nombreHeures;
    }

    // Coût de cette participation en DH
    public double calculerCout() {
        return nombreHeures * employe.getTauxHoraire();
    }

    @Override
    public String toString() {
        return "EmployeTache{id=" + id
                + ", employe=" + employe.getNom()
                + ", tache=" + tache.getNom()
                + ", heures=" + nombreHeures
                + ", coût=" + calculerCout() + " DH}";
    }
}
