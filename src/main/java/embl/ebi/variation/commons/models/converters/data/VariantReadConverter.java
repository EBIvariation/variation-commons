package embl.ebi.variation.commons.models.converters.data;

import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.opencga.storage.core.variant.VariantStorageManager;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToSamplesConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantSourceEntryConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantStatsConverter;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tom on 21/09/16.
 */
public class VariantReadConverter implements Converter<DBObject, Variant> {

    @Override
    public Variant convert(DBObject dbObject) {
        DBObjectToVariantConverter converter =
                new DBObjectToVariantConverter(
                        new DBObjectToVariantSourceEntryConverter(VariantStorageManager.IncludeSrc.FULL),
                        new DBObjectToVariantStatsConverter()
                );
        return converter.convertToDataModelType(dbObject);
    }

}
