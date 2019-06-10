package at.spengergasse.schluesselweb.python;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class pythontest {

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
}
