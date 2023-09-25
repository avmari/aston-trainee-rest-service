package repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <K, E> {

    Optional<E> findById(K id);

    boolean deleteById(K id);

    List<E> findAll();

    E save(E e);

    void update(E e);
}
