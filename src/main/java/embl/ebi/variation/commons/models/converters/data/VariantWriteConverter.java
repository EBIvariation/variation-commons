package embl.ebi.variation.commons.models.converters.data;

import org.springframework.core.convert.converter.Converter;
import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.Variant;

/**
 * Created by tom on 21/09/16.
 */
public class VariantWriteConverter implements Converter<Variant, DBObject> {

    @Override
    public DBObject convert(Variant variant) {
        return null;
    }

}
