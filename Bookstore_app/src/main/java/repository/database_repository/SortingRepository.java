package repository.database_repository;

import domain.BaseEntity;
import repository.Repository;
import repository.database_repository.sort_pack.Sort;

import java.io.Serializable;

public interface SortingRepository<ID extends Serializable,
        T extends BaseEntity<ID>> extends Repository<ID,T> {

    Iterable<T> findAll(Sort sort);
}
