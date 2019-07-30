package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);
    @Override
    void delete(Privilege privilege);

}