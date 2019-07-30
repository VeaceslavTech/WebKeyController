package at.spengergasse.schluesselweb.service;

import at.spengergasse.schluesselweb.domain.Fach;
import at.spengergasse.schluesselweb.domain.Schluessel;
import at.spengergasse.schluesselweb.foundation.FaecherMatrix;
import at.spengergasse.schluesselweb.foundation.MotorController;
import at.spengergasse.schluesselweb.persistence.Schluesselrepository;
import lombok.Getter;
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
    private FaecherMatrix faecherMatrix = new FaecherMatrix();

    public Schluessel createSchluessel(@NotNull @Valid Schluessel schluessel)
    {
        return schluesselrepository.save(schluessel);

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
}
