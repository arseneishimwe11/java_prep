package rw.arsene.erp.v1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.arsene.erp.v1.entity.RoleEntity;
import rw.arsene.erp.v1.enums.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(Role name);
    boolean existsByName(Role name);
}
