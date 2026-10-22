package ma.projet.dao;

import java.util.List;

public interface IDao<T> {

    // Ajouter un objet dans la base
    void create(T objet);

    // Modifier un objet existant
    T update(T objet);

    // Supprimer un objet
    void delete(T objet);

    // Rechercher un objet par son identifiant
    T findById(Long id);

    // Récupérer tous les objets
    List<T> findAll();
}