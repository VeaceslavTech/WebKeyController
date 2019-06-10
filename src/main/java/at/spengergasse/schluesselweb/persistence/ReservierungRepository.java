package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.Reservierung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservierungRepository extends JpaRepository<Reservierung,Long>
{
}
