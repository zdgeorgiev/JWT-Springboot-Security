package jwt.security.repository;

import jwt.security.entity.Role;
import jwt.security.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRoleType(RoleType roleType);
}
