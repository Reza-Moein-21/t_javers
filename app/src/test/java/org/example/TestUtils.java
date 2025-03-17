package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class TestUtils {

    public static String getFileContent(String path) {
        try (
                final var f = Files.lines(
                        Path.of(Objects.requireNonNull(
                                        TestUtils.class.getResource(path))
                                .toURI()))


        ) {

            return f.reduce((s1, s2) -> s1 + s2).orElseThrow();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
