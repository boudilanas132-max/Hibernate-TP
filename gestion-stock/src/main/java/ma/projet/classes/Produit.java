package ma.projet.classes;

import javax.persistence.*;

@Entity
@Table(name = "produits")
@NamedQuery(
    name = "Produit.prixSuperieurA100",
    query = "SELECT p FROM Produit p WHERE p.prix > 100 ORDER BY p.prix DESC"
)
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Column(nullable = false)
    private double prix;

    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "categorie_id", nullable = false)
    private Categorie categorie;

    public Produit() {
    }

    public Produit(String reference, double prix, int stock,
                   Categorie categorie) {
        this.reference = reference;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Produit{id=" + id
                + ", reference='" + reference + '\''
                + ", prix=" + prix
                + ", stock=" + stock + '}';
    }
}