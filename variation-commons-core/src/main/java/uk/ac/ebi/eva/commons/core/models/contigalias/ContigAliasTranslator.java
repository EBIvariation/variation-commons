package uk.ac.ebi.eva.commons.core.models.contigalias;

import java.util.NoSuchElementException;

public class ContigAliasTranslator {

    public static String getTranslatedContig(ContigAliasResponse contigAliasResponse, ContigNamingConvention contigNamingConvention) {
        ContigAliasChromosome contigAliasChromosome = contigAliasResponse.getEmbedded().getContigAliasChromosomes().get(0);
        return getTranslatedContig(contigAliasChromosome, contigNamingConvention);
    }

    public static String getTranslatedContig(ContigAliasChromosome contigAliasChromosome, ContigNamingConvention contigNamingConvention) {
        String contig;
        switch (contigNamingConvention) {
            case GENBANK_SEQUENCE_NAME:
                contig = contigAliasChromosome.getGenbankSequenceName();
                break;
            case REFSEQ:
                contig = contigAliasChromosome.getRefseq();
                break;
            case UCSC:
                contig = contigAliasChromosome.getUcscName();
                break;
            case ENA_SEQUENCE_NAME:
                contig = contigAliasChromosome.getEnaSequenceName();
                break;
            case MD5_CHECKSUM:
                contig = contigAliasChromosome.getMd5checksum();
                break;
            case TRUNC512_CHECKSUM:
                contig = contigAliasChromosome.getTrunc512checksum();
                break;
            default:
                contig = contigAliasChromosome.getInsdc();
        }

        if (contig != null) {
            return contig;
        } else {
            throw new NoSuchElementException("Contig " + contigAliasChromosome.getInsdc() +
                    " could not be translated to " + contigNamingConvention);
        }
    }
}
