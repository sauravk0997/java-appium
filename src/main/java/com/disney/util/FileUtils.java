package com.disney.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;

/**
 * Created by bogdan.zayats on 7/26/16.
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /* Create file if not present */
    public static void appendContentToFile(String filePath, String dataToAppend, boolean appendToExistingContent){
        try {
            File file = new File(filePath);
            LOGGER.info("File name: " + file.getName() + ", file path: " + file.getAbsolutePath());
            FileWriter fileWriter;

            if(appendToExistingContent) {
                fileWriter = new FileWriter(file, true);
            }else{
                fileWriter = new FileWriter(file);
            }

            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write(dataToAppend);
            bufferWriter.close();
            LOGGER.info("<<<< All data has been appended >>>>>");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getResourceFilePath(String resourceFileName) {
        return getClass().getClassLoader().getResource(resourceFileName).getFile();
    }

    public String getResourceFileAsString(String resourceFileName) throws IOException {
        return org.apache.commons.io.FileUtils.readFileToString(new File(getResourceFilePath(resourceFileName)), String.valueOf(Charset.forName("UTF-8")));
    }
}
