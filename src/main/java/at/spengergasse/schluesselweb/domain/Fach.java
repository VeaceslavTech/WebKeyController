package at.spengergasse.schluesselweb.domain;


import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity(name = "Fach")
public class Fach extends AbstractBaseDomain<Long>
{
    @Builder
    public Fach(String posx,String posy,String fachnr,boolean verfuegbar)
    {
        setPos_x(posx);
        setPos_y(posy);
        setFachnr(fachnr);
        setVerfuegbar(verfuegbar);
    }

    @Column(name = "Fach_Nr")
    private String fachnr;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schluessel_id")
    private Schluessel schluessel;

  /*  @ManyToOne(optional = false)
    private User user;
    */
    @Column(name = "pos_x")
    private String pos_x;

    @Column(name = "pos_y")
    private String pos_y;

    @Column(name="verfuegbar")
    private boolean verfuegbar;


}
