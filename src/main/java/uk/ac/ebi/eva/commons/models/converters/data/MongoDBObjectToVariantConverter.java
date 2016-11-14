package uk.ac.ebi.eva.commons.models.converters.data;

import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.opencga.storage.core.variant.VariantStorageManager;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantSourceEntryConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantStatsConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by tom on 21/09/16.
 */
public class MongoDBObjectToVariantConverter implements Converter<DBObject, Variant> {

    protected static Logger logger = LoggerFactory.getLogger(MongoDBObjectToVariantConverter.class);

    @Override
    public Variant convert(DBObject dbObject) {

        logger.warn("INSIDE MongoDBObjectToVariantConverter.convert");

        DBObjectToVariantConverter converter =
                new DBObjectToVariantConverter(
                        new DBObjectToVariantSourceEntryConverter(VariantStorageManager.IncludeSrc.NO),
                        new DBObjectToVariantStatsConverter()
                );
        return converter.convertToDataModelType(dbObject);
    }

}
