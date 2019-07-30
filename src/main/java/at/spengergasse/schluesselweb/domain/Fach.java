package at.spengergasse.schluesselweb.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor


@Entity(name = "Fach")
public class Fach extends AbstractBaseDomain<Long>
{
    @Builder
    public Fach(String posx,String posy,String fachnr,Verfuegbarkeit verfuegbarkeit)
    {
        setPos_x(posx);
        setPos_y(posy);
        setFachnr(fachnr);
        setVerfuegbarkeit(verfuegbarkeit);
    }

    @Column(name = "Fach_Nr")
    private String fachnr;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "schluessel_id", nullable = false)
    private Schluessel schluessel;

    @ManyToOne(optional = false)
    private User user;

    @Column(name = "pos_x")
    private String pos_x;

    @Column(name = "pos_y")
    private String pos_y;

    @Column(name="verfuegbarkeit")
    private Verfuegbarkeit verfuegbarkeit;

}
