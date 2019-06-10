package at.spengergasse.schluesselweb.persistence.converter;

import at.spengergasse.schluesselweb.domain.Verfuegbarkeit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
@Converter(autoApply = true)
public class VerfuegbarkeitConverter implements AttributeConverter<Verfuegbarkeit,String>
{
    @Override
    public String convertToDatabaseColumn(Verfuegbarkeit verfuegbarkeit) {
        return(verfuegbarkeit!=null)
                ?verfuegbarkeit.getName()
                :null;
    }

    @Override
        public Verfuegbarkeit convertToEntityAttribute(String code) {
            return (code!=null)
                    ? Verfuegbarkeit.forCode(code)
                    :null;
        }
    }
