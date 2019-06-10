package at.spengergasse.schluesselweb.service;

import at.spengergasse.schluesselweb.domain.Reservierung;
import at.spengergasse.schluesselweb.persistence.ReservierungRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class Reservierungservice
{
    private final ReservierungRepository reservierungRepository;
    @Transactional(readOnly = false)
    public Reservierung createReservierung(@NotNull @Valid Reservierung reservierung) {

        if(reservierung.getBeginnt_datum().before(reservierung.getAbgeschlossen_datum())&&(reservierung.getAbgeschlossen_datum().after(reservierung.getBeginnt_datum()))) {
            return reservierungRepository.save(reservierung);
        }
        else
        {
            System.out.println("Ungültige Zeit");
            return reservierung;
        }

    }
    public List<Reservierung> ueberpruefeUeberlappung(@NotNull @Valid Reservierung reservierung)
    {
        List<Reservierung> reservierungList = reservierungList();
        List<Reservierung> result = new ArrayList<>();
        Iterator itr = reservierungList.iterator();
        while (itr.hasNext()) {
            Reservierung r = (Reservierung) itr.next();
            if (r.getBeginnt_datum() == reservierung.getBeginnt_datum()) {
                if (r.getBeginn_zeit().isBefore(reservierung.getBeginn_zeit()) && (r.getAbgeschlossen_zeit().isAfter(reservierung.getBeginn_zeit()))) {
                    result.add(r);
                }
            }
        }
        return result;
    }



    public List<Reservierung> reservierungList() { return reservierungRepository.findAll();}
    public Reservierung findbyid(Long id){ return reservierungRepository.getOne(id);}


    @Transactional(readOnly = false)
    public void deleteReservierung(Reservierung reservierung)
    {
        reservierungRepository.delete(reservierung);
    }
}
