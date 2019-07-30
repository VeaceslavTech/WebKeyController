package at.spengergasse.schluesselweb.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor

/// NR1
@MappedSuperclass // jpa annotation
public abstract class Person extends  AbstractBaseDomain<Long>

{
    @Column(name="Vorname")
    private String firstName;
    @Column(name = "Nachname")
    private String lastName;
    @Column(name = "Geburtsdatum")
    private Date birthdate;
}