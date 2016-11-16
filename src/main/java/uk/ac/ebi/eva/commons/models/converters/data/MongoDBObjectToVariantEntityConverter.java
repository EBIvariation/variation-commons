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
import uk.ac.ebi.eva.commons.models.metadata.VariantEntity;

/**
 * Created by tom on 21/09/16.
 */
public class MongoDBObjectToVariantEntityConverter implements Converter<DBObject, VariantEntity> {

    protected static Logger logger = LoggerFactory.getLogger(MongoDBObjectToVariantEntityConverter.class);

    @Override
    public VariantEntity convert(DBObject dbObject) {

        logger.warn("INSIDE MongoDBObjectToVariantEntityConverter.convert");

        DBObjectToVariantConverter converter =
                new DBObjectToVariantConverter(
                        new DBObjectToVariantSourceEntryConverter(VariantStorageManager.IncludeSrc.NO),
                        new DBObjectToVariantStatsConverter()
                );
        return new VariantEntity(converter.convertToDataModelType(dbObject));
    }

}
