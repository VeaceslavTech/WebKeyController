package at.spengergasse.schluesselweb.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "Fach")
public class Fach extends AbstractBaseDomain<Long>
{
    @NotNull
    @Column(name = "Fach_Nr")
    private int fach_nr;
    @Column(name = "offen")
    private boolean offen;

    @OneToOne
    private Schluessel schluessel;

    @NotNull
    @ManyToOne(optional = false)
    private User user;

    @Column(name = "pos_x")
    private int pos_x;

    @Column(name = "pos_y")
    private int pos_y;

}
