package at.spengergasse.schluesselweb.config;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;


import at.spengergasse.schluesselweb.domain.*;
import at.spengergasse.schluesselweb.persistence.*;
import at.spengergasse.schluesselweb.service.FaecherService;
import at.spengergasse.schluesselweb.service.Schluesselservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FaecherRepository faecherRepository;
    @Autowired
    private FaecherService faecherService;
    @Autowired
    private Schluesselrepository schluesselrepository;
    @Autowired
    private Schluesselservice schluesselservice;
    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<Privilege>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Role adminRole = createRoleIfNotFound("ADMIN", adminPrivileges);
        final Role userRole = createRoleIfNotFound("USER", userPrivileges);
        // create boxes and keys
        createFacherIfNotFound("0/0");
        createZimmerSchluesselIfNotFound("B.10");
        // == create initial user
        createUserIfNotFound("test@test.com", "Test", "Test", "admin", new ArrayList<Role>(Arrays.asList(adminRole)));
        createUserIfNotFound("iva16578@spengergasse.at", "Veaceslav", "Ivanov", "Boxer232", new ArrayList<Role>(Arrays.asList(userRole)));



        alreadySetup = true;
    }

    @Transactional
    protected Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    protected Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByRole(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }
    @Transactional
    protected void createFacherIfNotFound(String fachnr) {
        Optional<Fach> fach = faecherRepository.findByfachnr(fachnr);
        if (!fach.isPresent()) {
            faecherService.initFaecherundsetPos();
        }
    }

    @Transactional
    protected void createZimmerSchluesselIfNotFound(String zimmerbezeichung) {
            Optional<Schluessel> schluessel = schluesselrepository.findByZimmerbezeichnung(zimmerbezeichung);
            if(!schluessel.isPresent())
            {
                schluesselservice.initSchluesselundsetFach();
            }
        }

    @Transactional
    protected  User createUserIfNotFound(final String email, final String firstName, final String lastName, final String password, final Collection<Role> roles) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setEmail(email);
            user.setActive(1);
            Schluessel schluessel = new Schluessel();
            schluessel.setZimmerbezeichnung("EigenerSchluessel");
            schluessel.setUser_key(user);
            schluessel.setVerfuegbarkeit(Verfuegbarkeit.VERFUEGBAR);
            Fach freiesfach = faecherService.getnaechstenFreienFach();
            schluessel.setFach(freiesfach);
            freiesfach.setSchluessel(schluessel);
            freiesfach.setVerfuegbar(false);
            schluessel = schluesselrepository.save(schluessel);
            user.setPrivaterSchluessel(schluessel);
        }
        user.setRoles((List<Role>) roles);
        user = userRepository.save(user);
        return user;
    }



}