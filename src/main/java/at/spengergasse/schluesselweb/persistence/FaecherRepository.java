package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.Fach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaecherRepository extends JpaRepository<Fach,Long>
{
    Optional<Fach> findByfachnr(String fachnr);
}