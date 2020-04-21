package serverModule.repository.database_repository;

import commonModule.domain.BaseEntity;
import serverModule.repository.Repository;
import serverModule.repository.database_repository.sort_pack.Sort;

import java.io.Serializable;

public interface SortingRepository<ID extends Serializable,
        T extends BaseEntity<ID>> extends Repository<ID,T> {

    Iterable<T> findAll(Sort sort);
}
