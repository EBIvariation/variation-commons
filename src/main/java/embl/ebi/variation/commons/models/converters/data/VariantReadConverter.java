package embl.ebi.variation.commons.models.converters.data;

import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tom on 21/09/16.
 */
public class VariantReadConverter implements Converter<DBObject, Variant> {

    @Override
    public Variant convert(DBObject dbObject) {
        DBObjectToVariantConverter dbObjectToVariantConverter = new DBObjectToVariantConverter();
        return dbObjectToVariantConverter.convertToDataModelType(dbObject);
    }

}
