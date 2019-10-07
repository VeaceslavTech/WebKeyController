package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.Schluessel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Schluesselrepository extends JpaRepository<Schluessel,Long>
{
    Optional<Schluessel> findByZimmerbezeichnung(String zimmerbezeichnung);
}
