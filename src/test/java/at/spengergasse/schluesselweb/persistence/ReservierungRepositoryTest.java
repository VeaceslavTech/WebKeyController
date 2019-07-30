package at.spengergasse.schluesselweb.persistence;

import at.spengergasse.schluesselweb.domain.Fach;
import at.spengergasse.schluesselweb.domain.Reservierung;
import at.spengergasse.schluesselweb.domain.Schluessel;
import at.spengergasse.schluesselweb.domain.Verfuegbarkeit;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@DataJpaTest
class ReservierungRepositoryTest
{
    @Autowired
    private ReservierungRepository reservierungRepository;

    @Test
    void ensureSavingReservierungWorksProperly()
    {
        //given
        Fach f1 = Fach.builder().build();
        Schluessel s1 = Schluessel.builder().verfuegbarkeit(Verfuegbarkeit.VERFUEGBAR).zimmerbezeichnung("Testzimmer").build();
        Reservierung r1 = Reservierung.builder()
                .beginn_datum(Date.valueOf(LocalDate.of(2019,Month.APRIL,17))).schluessel(s1)
                .build();

        //when
        reservierungRepository.save(r1);


        // then

        assertThat(r1.getSchluessel()).isNotNull();
        assertThat(s1.getVerfuegbarkeit()).isEqualTo(Verfuegbarkeit.VERFUEGBAR);
    }

}