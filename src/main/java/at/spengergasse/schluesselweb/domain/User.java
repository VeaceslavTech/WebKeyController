package at.spengergasse.schluesselweb.domain;


import at.spengergasse.schluesselweb.foundation.MutualListSupport;
import lombok.*;
import org.jboss.aerogear.security.otp.api.Base32;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "user")
public class User extends Person implements MutualListSupport
{
    @Getter
    @Setter
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;

    @Getter
    @Setter
    @Column(name = "password")
    private String password;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="user_role", joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
    @Getter
    @Setter
    private List<Role> roles = new ArrayList<>();
    public User addRole(final Role role)
    {
        Objects.requireNonNull(role);
        this.roles.add(role);
        return this;
    }
    @Getter
    @Setter
    private int active;

   /* @OneToMany( mappedBy = "user", cascade=CascadeType.ALL)
    private List<Fach> fachverlauf = new ArrayList<>();

*/
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade=CascadeType.ALL)
    @Getter
    private List<Reservierung> reservierungList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY
    ,mappedBy = "user_key")
    @JoinColumn(name = "default_schluessel_id")
    @Getter
    @Setter
    private Schluessel privaterSchluessel;

    @Getter
    @Setter
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "secret")
    @Getter
    @Setter
    private String secret;

    private boolean isUsing2FA;
    @Builder
    public User(String firstName, String lastName, Date birthdate,
                List<Reservierung>reservierungList,List<Role>roles,Schluessel privaterSchluessel,String  resetToken) {
        super(firstName, lastName, birthdate);
        this.privaterSchluessel = privaterSchluessel;
        this.resetToken = resetToken;
        Optional.ofNullable(reservierungList).ifPresent(lfl -> lfl.stream().forEach(this::addReservierung));
        this.secret = Base32.random();
        Optional.ofNullable(roles).ifPresent(lfl -> lfl.stream().forEach(this::addRole));
    }
    public User addReservierung(final Reservierung reservierung)
    {
        Objects.requireNonNull(reservierung);
        reservierung.removeUser(reservierung);
        reservierung.setUser(this);
        this.reservierungList.add(reservierung);
        return this;
    }
    public User addReservierungen(final Reservierung... reservierungs)
    {
        Arrays.stream(reservierungs).forEach(this::addReservierung);
        return this;
    }
    public boolean isUsing2FA() {
        return isUsing2FA;
    }

    public void setUsing2FA(boolean isUsing2FA) {
        this.isUsing2FA = isUsing2FA;
    }


}
