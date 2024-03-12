package ro.mpp2024.Repository;

import ro.mpp2024.Model.Entity;

import java.util.Collection;

public interface Repository<T extends Entity<Tid>, Tid>  {
    void add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
    Collection<T> getAll();

}