package at.spengergasse.schluesselweb.service;

import at.spengergasse.schluesselweb.domain.Fach;
import at.spengergasse.schluesselweb.persistence.FaecherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FaecherService
{
    private  final FaecherRepository faecherRepository;
    private Fach[][] faecher;


    public List<Fach> getFachList()
    {
        return faecherRepository.findAll();
    }
    @Transactional(readOnly = false)
    public Fach createFach(@NotNull @Valid Fach fach)
    {
        return faecherRepository.save(fach);
    }

    public Optional<Fach> findByFachNr(String fachnr) {
        return faecherRepository.findByfachnr(fachnr);
    }
    public Fach findByid(Long id)
    {
        return faecherRepository.getOne(id);
    }
    @Transactional(readOnly = false)
    public void deleteFach(Fach fach)
    {
        faecherRepository.delete(fach);
    }

    @Transactional(readOnly = false)
    public void initFaecherundsetPos()
    {
        int zeilen=4;
        int spalten=4;
        int y_def=0;
        int x_def=125;
        faecher= new Fach[zeilen][spalten];
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
                createFach(fachzumspeicher);
            }
        }
    }
    public Fach getnaechstenFreienFach() {
        int zeilen=4;
        int spalten=4;
        Fach f = new Fach("leer", "leer", 9 + "/" + 9, false);
        boolean reingekommen = false;
        for (int zeile = 0; zeile < zeilen; zeile++) {
            for (int spalte = 0; spalte < spalten; spalte++) {
                if (faecher[zeile][spalte].isVerfuegbar()) {
                    if (!reingekommen) {
                        f = faecher[zeile][spalte];
                    }
                    reingekommen = true;
                }
            }
        }
        {
            return f;
        }
    }
    public static String intToString(int input)
    {
        return Integer.toString(input);
    }

}
