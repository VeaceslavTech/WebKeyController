package at.spengergasse.schluesselweb.foundation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface MutualListSupport {

    default <T,E extends T> List<T> toList(E... elements)
    {
        return  Arrays.stream(elements).collect(Collectors.toCollection(ArrayList::new));
    }
}
