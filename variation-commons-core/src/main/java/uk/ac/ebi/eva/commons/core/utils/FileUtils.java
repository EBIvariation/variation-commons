/*
 * Copyright 2018 EMBL - European Bioinformatics Institute
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

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

public class FileUtils {

    private static final String GZIP_FILE_EXTENSION = ".gz";

    public static File getResourceFile(String resourcePath) {
        return new File(FileUtils.class.getResource(resourcePath).getFile());
    }

    public static Resource getResource(File file) throws IOException {
        Resource resource;
        if (isGzip(file)) {
            resource = new GzipLazyResource(file);
        } else {
            resource = new FileSystemResource(file);
        }
        return resource;
    }

    public static boolean isGzip(String file) throws IOException {
        return isGzip(new File(file));
    }

    public static boolean isGzip(File file) throws IOException {
        try {
            GZIPInputStream inputStream = new GZIPInputStream(new FileInputStream(file));
            inputStream.close();
        } catch (ZipException exception) {
            return false;
        } catch (FileNotFoundException exception) {
            return file.getName().endsWith(GZIP_FILE_EXTENSION);
        }
        return true;
    }

    public static Properties getPropertiesFile(InputStream propertiesInputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(propertiesInputStream);
        return properties;
    }

    public static long countNonCommentLines(InputStream in) throws IOException {
        BufferedReader file = new BufferedReader(new InputStreamReader(in));
        long lines = 0;
        String line;
        while ((line = file.readLine()) != null) {
            if (line.charAt(0) != '#') {
                lines++;
            }
        }
        file.close();
        return lines;
    }

}
