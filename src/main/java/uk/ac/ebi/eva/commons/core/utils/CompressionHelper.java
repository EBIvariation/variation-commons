package uk.ac.ebi.eva.commons.core.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class CompressionHelper {

    public static byte[] gzip(String text) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                new GZIPOutputStream(byteArrayOutputStream));

        try {
            bufferedOutputStream.write(text.getBytes());
        } finally {
            bufferedOutputStream.close();
        }

        byte[] retval = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return retval;
    }

}
