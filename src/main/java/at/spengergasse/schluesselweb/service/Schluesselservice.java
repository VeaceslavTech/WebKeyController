package at.spengergasse.schluesselweb.service;

import at.spengergasse.schluesselweb.domain.Fach;
import at.spengergasse.schluesselweb.domain.Schluessel;
import at.spengergasse.schluesselweb.domain.Verfuegbarkeit;
import at.spengergasse.schluesselweb.persistence.Schluesselrepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.parseInt;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = false)
public class Schluesselservice
{
    private final Schluesselrepository schluesselrepository;
    private final FaecherService faecherService;
    public Schluessel createSchluessel(@NotNull @Valid Schluessel schluessel)
    {
        return schluesselrepository.save(schluessel);

    }

    public Schluessel findeSchluesselnachFach(Fach fach)
    {
        return fach.getSchluessel();

    }

    public List<Schluessel> findAlleAusserEigenen()
    {
        List<Schluessel> schluessels = schluessellist();
        List<Schluessel> result = new ArrayList<>();
        Iterator itr = schluessels.iterator();
        while(itr.hasNext())
        {
            Schluessel s = (Schluessel)itr.next();
            if(s.getUser_key()==null)
            {
                result.add(s);
            }
        }
        return result;
    }
    public List<Schluessel> schluessellist()
    {
        return schluesselrepository.findAll();
    }

    @Transactional(readOnly = false)
    public void initSchluesselundsetFach()
    {
        for(int i=0; i<9; i++)
        {
            Schluessel schluessel = new Schluessel();
            Fach freiesfach = faecherService.getnaechstenFreienFach();
            schluessel.setFach(freiesfach);
            freiesfach.setSchluessel(schluessel);
            freiesfach.setVerfuegbar(false);
            if(i<5) {
                schluessel.setZimmerbezeichnung("B.1" + i);
            }
            else
            {
                schluessel.setZimmerbezeichnung("A.2"+i);
            }
            schluessel.setVerfuegbarkeit(Verfuegbarkeit.VERFUEGBAR);
            schluessel.setSchluessel_nr(i+1);
            createSchluessel(schluessel);
        }
    }
}
