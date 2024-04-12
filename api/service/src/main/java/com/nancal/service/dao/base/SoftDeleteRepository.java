package com.nancal.service.dao.base;

import com.nancal.model.entity.BusinessObjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface SoftDeleteRepository<T extends BusinessObjectEntity, ID extends String> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.uid=?1 AND e.delFlag = false")
    public Optional<T> findById(ID id);

    @Override
    @Transactional(readOnly = true)
    default List<T> findAllById(Iterable<ID> ids) {
        List<T> results = new ArrayList<>();
        for (ID id : ids) {
            findById(id).ifPresent(results::add);
        }

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.delFlag = false")
    public List<T> findAll();

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.delFlag = true")
    public List<T> findAllDeleted();

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.delFlag = false")
    public List<T> findAll(Sort sort);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.delFlag = true")
    public List<T> findAllDeleted(Sort sort);

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.delFlag = false")
    public Page<T> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.delFlag = true")
    public Page<T> findAllDeleted(Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.delFlag = false")
    public long count();

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.delFlag = true")
    public long countDeleted();

    @Override
    @Query("UPDATE #{#entityName} e SET e.delFlag=true WHERE e.uid = ?1")
    @Transactional
    @Modifying
    public void deleteById(ID id);

    @Query("UPDATE #{#entityName} e SET e.delFlag=false WHERE e.uid = ?1")
    @Transactional
    @Modifying
    public void undeleteById(ID id);

    @Override
    @Transactional
    default void delete(T entity) {
        deleteById((ID) entity.getUid());
    }

    @Override
    @Query("UPDATE #{#entityName} e SET e.delFlag=true")
    @Transactional
    @Modifying
    public void deleteAll();

    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(e -> deleteById((ID) e.getUid()));
    }
}
