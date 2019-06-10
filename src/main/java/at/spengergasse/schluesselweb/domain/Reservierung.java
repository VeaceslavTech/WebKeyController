package at.spengergasse.schluesselweb.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "Reservierung")
public class Reservierung extends AbstractBaseDomain<Long> {
    @Enumerated(EnumType.STRING)
    @Setter
    @Getter
    @Column(name = "reservierung_status")
    private ReservierungStatus status_enum;
    @NotNull
    @Column(name = "Beginnt_am")
    @Getter
    @Setter
    private java.sql.Date beginnt_datum;
    @Getter
    @Setter
    private LocalTime beginn_zeit;
    @NotNull
    @Column(name = "Abgeschlossen_am")
    @Getter
    @Setter
    private java.sql.Date abgeschlossen_datum;
    @Getter
    @Setter
    private LocalTime abgeschlossen_zeit;

    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinColumn(name="user_id")
    @Getter
    @Setter
    private User user;

    public void removeUser(Reservierung reservierung) {
        if (reservierung != null) {
            if (reservierung.getUser() != null) {
                reservierung.setUser(null);
            }
        }
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schluessel_id")
    @Getter
    @Setter
    private Schluessel schluessel;

    public void reserviereSchluessel(Schluessel schluessel)
    {
        schluessel.setVerfuegbarkeit(Verfuegbarkeit.RESERVIERT);
        schluessel.setReservierung(this);
    }

    public String schluesselabholen()
    {
        String msg_suc = "Schlüssel erfolgreich abgeholt";
        String msg_fail = "Schlüssel hat die Verfügbarkeit "+schluessel.getVerfuegbarkeit()+" daher kann dieser nicht abgeholt werden";
        if(schluessel.getVerfuegbarkeit()==Verfuegbarkeit.RESERVIERT)
        {
            this.setStatus_enum(ReservierungStatus.SCHLÜSSEL_ABGEHOLT);
            schluessel.setAbgeholt_datum(Date.valueOf(LocalDate.now()));
            schluessel.setAbgeholt_zeit(LocalTime.now());
            schluessel.setVerfuegbarkeit(Verfuegbarkeit.ENTNOMMEN);
                        return msg_suc;
        }
        else {
            return msg_fail;
        }
    }

    public void schluesselretournieren()
    {
        if(schluessel.getVerfuegbarkeit()==Verfuegbarkeit.ENTNOMMEN)
        {
            this.setStatus_enum(ReservierungStatus.ABGESCHLOSSEN);
            schluessel.setRetourniert_datum(Date.valueOf(LocalDate.now()));
            schluessel.setRetourniert_zeit(LocalTime.now());
            schluessel.setVerfuegbarkeit(Verfuegbarkeit.VERFUEGBAR);
        }
    }
    @Builder
    public Reservierung( ReservierungStatus reservierungStatus, LocalTime beginn_zeit, LocalTime ende_zeit, Date beginn_datum,
                        Date ende_datum,
                        User reservierung_user,Schluessel schluessel )
    {
        this.user = reservierung_user;
        this.status_enum = reservierungStatus;
        this.beginn_zeit = beginn_zeit;
        this.abgeschlossen_zeit = ende_zeit;
        this.beginnt_datum = beginn_datum;
        this.abgeschlossen_datum = ende_datum;
        this.schluessel = schluessel;
    }

}

