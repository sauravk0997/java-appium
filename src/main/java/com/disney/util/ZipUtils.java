package com.disney.util;

import com.zebrunner.agent.core.registrar.Artifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Files.readAllBytes;

public class ZipUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void zipDirectory(String sourceDirectoryPath, String zipPath) throws IOException {
        Path zipFilePath = createFile(Paths.get(zipPath));

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(newOutputStream(zipFilePath))) {
            Path sourceDirPath = Paths.get(sourceDirectoryPath);

            try (Stream<Path> entries = Files.walk(sourceDirPath)) {
                entries.filter(path -> !path.toFile().isDirectory())
                        .forEach(path -> {
                            ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                            try {
                                zipOutputStream.putNextEntry(zipEntry);
                                zipOutputStream.write(readAllBytes(path));
                                zipOutputStream.closeEntry();
                            } catch (Exception e) {
                                LOGGER.info("Failed to zip file with exception: {} ",  e.toString());
                            }
                        });
            }
        }
    }

    public static void uploadZipFileToJenkinsAsArtifact(String baseFile, String pathToZip){
        try {
            ZipUtils.zipDirectory(baseFile, pathToZip);
        } catch (IOException e){
            LOGGER.error("Failed to zip file with exception: {}", e.toString());
        }
        Artifact.attachToTest(pathToZip, Path.of(pathToZip));
    }

    private ZipUtils(){

    }
}