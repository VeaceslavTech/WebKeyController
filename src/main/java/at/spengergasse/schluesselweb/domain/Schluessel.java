package at.spengergasse.schluesselweb.domain;

import at.spengergasse.schluesselweb.foundation.MotorController;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Table(name = "Schluessel")
public class Schluessel extends AbstractBaseDomain<Long>
{

    @OneToOne(mappedBy = "schluessel")
    private Fach fach;
    @Getter
    @Setter
    @Column(name = "zimmerbezeichnung")
    private String zimmerbezeichnung;

    @OneToMany(
            mappedBy = "schluessel",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reservierung> reservierungList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    @Column(name = "verfuegbar")
    private Verfuegbarkeit verfuegbarkeit;
    @Column(name = "abgeholt_am")
    @Getter
    @Setter
    private Date abgeholt_datum;
    @Getter
    @Setter
    private LocalTime abgeholt_zeit;

    @Column(name = "retourniert_am")
    @Getter
    @Setter
    private Date retourniert_datum;
    @Getter
    @Setter
    private LocalTime retourniert_zeit;

    @OneToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private User user_key;
    @Getter
    @Setter
    @Column(name = "pos_y")
    private String pos_y;
    @Getter
    @Setter
    @Column(name = "pos_x")
    private String pos_x;
    @Getter
    @Setter
    @Column(name ="schluessel_nr")
    private int schluessel_nr;



    public Schluessel setReservierung(Reservierung reservierung)
    {
        if(this.verfuegbarkeit == Verfuegbarkeit.VERFUEGBAR)
        {
                reservierungList.add(reservierung);
                setVerfuegbarkeit(Verfuegbarkeit.RESERVIERT);
        }
        return this;
    }
    @Builder
    public Schluessel(Date retourniert_datum,Date abgeholt_datum,LocalTime retourniert_zeit, LocalTime abgeholt_zeit,String zimmerbezeichnung,
                      Verfuegbarkeit verfuegbarkeit,List<Reservierung> reservierungList,Fach fach,int schluessel_nr,String pos_x,String pos_y)
    {

        this.retourniert_datum = retourniert_datum;
        this.abgeholt_datum = abgeholt_datum;
        this.retourniert_zeit = retourniert_zeit;
        this.abgeholt_zeit = abgeholt_zeit;
        this.zimmerbezeichnung = zimmerbezeichnung;
        this.verfuegbarkeit = verfuegbarkeit;
        this.reservierungList = reservierungList;
        this.fach = fach;
        this.schluessel_nr = schluessel_nr;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    public String schluesselabholenprivat()
    {
        String msg_suc = "Schlüssel erfolgreich abgeholt";
        String msg_fail = "Schlüssel hat die Verfügbarkeit "+this.getVerfuegbarkeit()+" daher kann dieser nicht abgeholt werden";
        if(this.getVerfuegbarkeit()==Verfuegbarkeit.VERFUEGBAR)
        {
            this.setAbgeholt_datum(Date.valueOf(LocalDate.now()));
            this.setAbgeholt_zeit(LocalTime.now());
            this.setVerfuegbarkeit(Verfuegbarkeit.ENTNOMMEN);
            return msg_suc;
        }
        else {
            return msg_fail;
        }
    }

    public String schluesselretournierenprivat()
    {
        String msg_suc = "Schlüssel erfolgreich retourniert";
        String msg_fail = "Schlüssel hat die Verfügbarkeit "+this.getVerfuegbarkeit()+" daher kann dieser nicht retourniert werden";
        if(this.getVerfuegbarkeit()==Verfuegbarkeit.ENTNOMMEN)
        {
            this.setRetourniert_datum(Date.valueOf(LocalDate.now()));
            this.setRetourniert_zeit(LocalTime.now());
            this.setVerfuegbarkeit(Verfuegbarkeit.VERFUEGBAR);
            return msg_suc;
        }
        else {
            return msg_fail;
        }
    }

    public String statusString()
    {
        StringBuilder sb = new StringBuilder();
        if(verfuegbarkeit==Verfuegbarkeit.ENTNOMMEN)
        return sb.append(verfuegbarkeit.getName()).append(" am ").append(abgeholt_datum).append(" um ").append(abgeholt_zeit.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))).toString();
        else
            return sb.append(verfuegbarkeit.getName()).toString();
    }

}
