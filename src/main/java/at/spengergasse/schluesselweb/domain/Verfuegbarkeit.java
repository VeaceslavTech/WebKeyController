package at.spengergasse.schluesselweb.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public enum Verfuegbarkeit
{
    VERFUEGBAR("Verfügbar"),
    RESERVIERT("Reserviert"),
    ENTNOMMEN("Entnommen");

    @Getter

    private final String name;
    Verfuegbarkeit(String name) { this.name = name; }

    public static Verfuegbarkeit forCode(String name) {
        Objects.requireNonNull(name);
        return Arrays.stream(values())
                .filter(s -> name.equalsIgnoreCase(s.name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unbekannter Status code: " + name));
    }
}
