/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.core.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compression utilities to handle text compression.
 */
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

    public static String gunzip(byte[] value) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(value));
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            stringBuilder.append(Charset.forName("UTF-8").decode(ByteBuffer.wrap(buffer)), 0, length);
        }
        return stringBuilder.toString();
    }

    public static void uncompress(String inputCompressedFile, File outputFile) throws IOException {
        GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(inputCompressedFile));
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

        byte[] buffer = new byte[1024];
        final int offset = 0;
        int length;
        while ((length = gzipInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, offset, length);
        }

        gzipInputStream.close();
        fileOutputStream.close();
    }

}
