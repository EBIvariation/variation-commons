package uk.ac.ebi.eva.commons.models.converters.data;

import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.stats.VariantGlobalStats;
import org.springframework.core.convert.converter.Converter;

public class DbObjectToVariantGlobalStatsConverter implements Converter<DBObject, VariantGlobalStats> {

    public final static String NUMSAMPLES_FIELD = "nSamp";
    public final static String NUMVARIANTS_FIELD = "nVar";
    public final static String NUMSNPS_FIELD = "nSnp";
    public final static String NUMINDELS_FIELD = "nIndel";
    public final static String NUMSTRUCTURAL_FIELD = "nSv";
    public final static String NUMPASSFILTERS_FIELD = "nPass";
    public final static String NUMTRANSITIONS_FIELD = "nTi";
    public final static String NUMTRANSVERSIONS_FIELD = "nTv";
    public final static String MEANQUALITY_FIELD = "meanQ";

    @Override
    public VariantGlobalStats convert(DBObject statsObject) {
        return new VariantGlobalStats(
                (int) statsObject.get(NUMVARIANTS_FIELD),
                (int) statsObject.get(NUMSAMPLES_FIELD),
                (int) statsObject.get(NUMSNPS_FIELD),
                (int) statsObject.get(NUMINDELS_FIELD),
                0, // TODO Add structural variants to schema!
                (int) statsObject.get(NUMPASSFILTERS_FIELD),
                (int) statsObject.get(NUMTRANSITIONS_FIELD),
                (int) statsObject.get(NUMTRANSVERSIONS_FIELD),
                -1,
                ((Double) statsObject.get(MEANQUALITY_FIELD)).floatValue(),
                null
        );
    }
}
