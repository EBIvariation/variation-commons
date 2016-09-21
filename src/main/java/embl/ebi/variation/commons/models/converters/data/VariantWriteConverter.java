package embl.ebi.variation.commons.models.converters.data;

import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter;
import org.springframework.core.convert.converter.Converter;
import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.Variant;

/**
 * Created by tom on 21/09/16.
 */
public class VariantWriteConverter implements Converter<Variant, DBObject> {

    @Override
    public DBObject convert(Variant variant) {
        DBObjectToVariantConverter dbObjectToVariantConverter = new DBObjectToVariantConverter();
        return dbObjectToVariantConverter.convertToStorageType(variant);
    }

}
