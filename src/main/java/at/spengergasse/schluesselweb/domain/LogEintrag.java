package at.spengergasse.schluesselweb.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LogEintrag {
    private Date datum;
    private Time zeit;
    private User user;
    private Schluessel schluessel;

    public LogEintrag(Date datum, Time zeit, User user, Schluessel schluessel) {
        this.datum = datum;
        this.zeit = zeit;
        this.user = user;
        this.schluessel = schluessel;
    }

    public LogEintrag createLogEintrag(User user)
    {
        Schluessel privaterschluessel = user.getPrivaterSchluessel();
        LogEintrag neuerLog = new LogEintrag();
        neuerLog.setUser(user);
        neuerLog.setSchluessel(privaterschluessel);
        return neuerLog;
    }
}
