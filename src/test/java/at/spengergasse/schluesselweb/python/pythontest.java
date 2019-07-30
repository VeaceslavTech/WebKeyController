package at.spengergasse.schluesselweb.python;

import at.spengergasse.schluesselweb.domain.Schluessel;
import at.spengergasse.schluesselweb.domain.Verfuegbarkeit;
import at.spengergasse.schluesselweb.foundation.MotorController;
import at.spengergasse.schluesselweb.persistence.Schluesselrepository;
import org.junit.runner.RunWith;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static at.spengergasse.schluesselweb.foundation.FaecherMatrix.intToString;
import static org.assertj.core.api.Assertions.assertThat;
@RunWith(SpringRunner.class)
@DataJpaTest
public class pythontest
{

    @Autowired
    private Schluesselrepository schluesselrepository;

    @Test
   public void test()
   { PythonInterpreter python = new PythonInterpreter();
    int number1 = 10;
    int number2 = 32;
    python.set("number1", new PyInteger(number1));
    python.set("number2", new PyInteger(number2));
    python.exec("number3 = number1+number2");
    PyObject number3 = python.get("number3");
    System.out.println("val : "+number3.toString());
       assertThat(number3).isNotNull();
   }

   @Test
   public void erstelleSchluesselundsetPos() {
       int zeilen = 4;
       int spalten = 4;
       int y_def = 0;
       int x_def = 125;
       int snr = 1;
       for (int z = 0; z < zeilen; z++) {
           if (z != 0)
               y_def += 1000;
           x_def = 125;
           for (int s = 0; s < spalten; s++) {
               Schluessel schluessel =Schluessel.builder().verfuegbarkeit(Verfuegbarkeit.VERFUEGBAR).schluessel_nr(snr).build();
               if (s != 0) {
                   x_def += 250;
               }
               schluessel.setPos_x(intToString(x_def));
               schluessel.setPos_y(intToString(y_def));
               snr++;
               if (z == 3) {
                   schluessel.setZimmerbezeichnung("TestZimmer");
               }
               schluesselrepository.save(schluessel);
           }
       }
   }
}
