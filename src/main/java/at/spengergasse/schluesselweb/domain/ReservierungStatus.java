package at.spengergasse.schluesselweb.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

public enum ReservierungStatus {

    GEPLANT("Geplant"),
    SCHLÜSSEL_ABGEHOLT("Schlüssel abgeholt"),
    UEBERGEBEN("Übergeben"),
    ABGESCHLOSSEN("Abgeschlossen");
    @Getter
    private final String name;

    ReservierungStatus(String code) {
        this.name = code;
    }

    public static ReservierungStatus forCode(String name) {
        Objects.requireNonNull(name);
        return Arrays.stream(values())
                .filter(s -> name.equalsIgnoreCase(s.name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unbekannter Status code: " + name));
    }
}


