package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "femmes")

// Requête JPQL : femmes mariées au moins deux fois
@NamedQuery(
    name = "Femme.marieesAuMoinsDeuxFois",
    query = "SELECT f FROM Femme f "
          + "WHERE (SELECT COUNT(m) FROM Mariage m "
          + "        WHERE m.femme = f) >= 2"
)

// Requête SQL native nommée : total des enfants d'une femme
// pour les mariages commencés pendant une période donnée
@NamedNativeQuery(
    name = "Femme.nombreEnfantsEntreDates",
    query = "SELECT COALESCE(SUM(m.nombre_enfants), 0) "
          + "FROM mariages m "
          + "WHERE m.femme_id = :femmeId "
          + "AND m.date_debut >= :debut "
          + "AND m.date_debut <= :fin"
)
public class Femme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private LocalDate dateNaissance;

    @OneToMany(mappedBy = "femme")
    private List<Mariage> mariages = new ArrayList<>();

    public Femme() {
    }

    public Femme(String nom, String prenom, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public List<Mariage> getMariages() {
        return mariages;
    }

    @Override
    public String toString() {
        return "Femme{id=" + id
                + ", nom='" + nom + '\''
                + ", prenom='" + prenom + '\''
                + ", dateNaissance=" + dateNaissance + '}';
    }
}
