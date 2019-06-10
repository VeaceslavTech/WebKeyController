package at.spengergasse.schluesselweb.service;

import at.spengergasse.schluesselweb.domain.Fach;
import at.spengergasse.schluesselweb.persistence.FaecherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FaecherService
{
    private FaecherRepository faecherRepository;

    public List<Fach> getFachList()
    {
        return faecherRepository.findAll();
    }
    @Transactional(readOnly = false)
    public Fach createFach(@NotNull @Valid Fach fach)
    {
        return faecherRepository.save(fach);
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
}
