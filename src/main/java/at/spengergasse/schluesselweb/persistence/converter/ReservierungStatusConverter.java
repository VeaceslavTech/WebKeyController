package at.spengergasse.schluesselweb.persistence.converter;



import at.spengergasse.schluesselweb.domain.ReservierungStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ReservierungStatusConverter implements AttributeConverter<ReservierungStatus,String> {
    @Override
    public String convertToDatabaseColumn(ReservierungStatus reservierungStatus) {
        return(reservierungStatus!=null)
                ?reservierungStatus.getName()
                :null;
    }

    @Override
    public ReservierungStatus convertToEntityAttribute(String code) {
        return (code!=null)
                ? ReservierungStatus.forCode(code)
                :null;
    }
}

