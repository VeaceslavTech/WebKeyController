package at.spengergasse.schluesselweb.service;

import at.spengergasse.schluesselweb.domain.*;
import at.spengergasse.schluesselweb.persistence.PasswordResetTokenRepository;
import at.spengergasse.schluesselweb.persistence.Schluesselrepository;
import at.spengergasse.schluesselweb.persistence.UserRepository;
import at.spengergasse.schluesselweb.persistence.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = false)
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;
    @Autowired
    private Schluesselrepository schluesselrepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    public static String QR_PREFIX = "https://chart.googleapis.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=";
    public static String APP_NAME = "SpringRegistration";

    public List<User> userList() {
        return userRepository.findAll();
    }

    public User findbyid(Long id) {
        return userRepository.getOne(id);
    }

    @Transactional(readOnly = false)
    public void saveUser(@NotNull @Valid User user) {
        Schluessel schluessel = new Schluessel();
        schluessel.setZimmerbezeichnung("Eigener_Schlüssel");
        schluessel.setVerfuegbarkeit(Verfuegbarkeit.VERFUEGBAR);
        schluesselrepository.save(schluessel);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setPrivaterSchluessel(schluessel);
        Role userRole = roleRepository.findByRole("USER");
                user.addRole(userRole);
                userRepository.save(user);
        schluessel.setUser_key(user);
    }
    public List<Reservierung> findListbyEmail(String email)
    {
        return userRepository.findByEmail(email).getReservierungList();
    }
    public List<Reservierung> findListbyId(Long id)
    {
        return userRepository.findById(id).get().getReservierungList();
    }

    @Transactional(readOnly = false)
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByResetToken(String reset_token)
    {
        return userRepository.findByResetToken(reset_token);
    }


    public String errorMsg()
    {
        return "Beim erstellen des Users ist ein fehler Entstanden,bitte die Attribute nochmal überprüfen";
    }
    @Transactional(readOnly = false)
    public void createPasswordResetTokenForUser() {
        final PasswordResetToken myToken = new PasswordResetToken();
        passwordTokenRepository.save(myToken);
    }

    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    public User getUserByPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token)
                .getUser();
    }

    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = false)
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }





    }

