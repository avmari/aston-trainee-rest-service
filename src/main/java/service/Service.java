package service;

import entity.Entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Service <E extends Entity>{

    E save(E entity);

    Optional<E> findById(UUID uuid);

    List<E> findAll();

    boolean deleteById(UUID id);
}
