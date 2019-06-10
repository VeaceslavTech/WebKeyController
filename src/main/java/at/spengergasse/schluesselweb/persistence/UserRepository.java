package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>
{
    User findByEmail(String email);
    User findByResetToken(String resetToken);
}
