package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRole(String role);
    @Override
    void delete(Role role);

}
