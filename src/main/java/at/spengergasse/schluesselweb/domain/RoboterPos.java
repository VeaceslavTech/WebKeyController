package at.spengergasse.schluesselweb.domain;


import at.spengergasse.schluesselweb.persistence.FaecherRepository;
import at.spengergasse.schluesselweb.service.FaecherService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Data
public class RoboterPos {
    /*private Fach[][] schluesselbox;
    private int zeilen;
    private int spalten;
    @Autowired
    private FaecherService faecherService;
    public RoboterPos()
    {
        initFaecherundsetPos();
    }

    @Transactional(readOnly = false)
    public void initFaecherundsetPos()
    {
        int zeilen=4;
        int spalten=4;
        int y_def=0;
        int x_def=125;
        Fach faecher[][]= new Fach[zeilen][spalten];
        for(int z=0;z<zeilen;z++)
        {
            if(z!=0)
                y_def+=1000;
            x_def=125;
            for(int s=0;s<spalten;s++)
            {
                if(s!=0)
                    x_def+=250;
                faecher[z][s] = new Fach(intToString(x_def),intToString(y_def),z+"/"+s, true);
                Fach fachzumspeicher = faecher[z][s];
                if(fachzumspeicher!=null)
                    System.out.println("Ich bin nicht null");
                else
                {
                    System.out.println("Ich bin null");
                }
                faecherService.createFach(fachzumspeicher);
            }
        }
    }
    public Fach getnaechstenFreienFach()
    {
        Fach f = new Fach("leer","leer",9+"/"+9, false);
        boolean reingekommen = false;
        for (int zeile = 0; zeile < zeilen; zeile++)
        {
            for (int spalte = 0; spalte < spalten ; spalte++) {
                if(schluesselbox[zeile][spalte].isVerfuegbar())
                {
                    if(!reingekommen) {
                        f=schluesselbox[zeile][spalte];
                    }
                    reingekommen=true;
                }
            }
        }
        {
            return f;
        }

    }


    public Fach getFachausArray(int zeile,int spalte)
    {
        return schluesselbox[zeile-1][spalte-1];
    }
    */
}
