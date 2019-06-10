package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.Schluessel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Schluesselrepository extends JpaRepository<Schluessel,Long>
{
}
