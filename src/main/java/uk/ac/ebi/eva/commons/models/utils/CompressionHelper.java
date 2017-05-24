package uk.ac.ebi.eva.commons.models.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressionHelper {

    public static byte[] gzip(String text) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedOutputStream bufos = new BufferedOutputStream(new GZIPOutputStream(bos));

        try {
            bufos.write(text.getBytes());
        } finally {
            bufos.close();
        }

        byte[] retval = bos.toByteArray();
        bos.close();
        return retval;
    }

}
