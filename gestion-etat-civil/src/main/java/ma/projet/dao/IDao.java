package ma.projet.dao;

import java.util.List;

public interface IDao<T> {

    void create(T objet);

    void update(T objet);

    void delete(T objet);

    T findById(Long id);

    List<T> findAll();
}
