package com.example.swiftCodesApp.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResourceUtil {

    public static String readFile(String filePath) throws IOException {
        return new String(
                Objects.requireNonNull(ResourceUtil.class.getResourceAsStream(filePath)).readAllBytes(),
                StandardCharsets.UTF_8
        );
    }
}
