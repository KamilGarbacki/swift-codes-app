package com.example.swiftCodesApp.util;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceUtilTest {

    @Test
    void readFile() throws IOException {
        // Given
        String path = "/data/testFile.txt";

        // When
        // Then
        assertThat(ResourceUtil.readFile(path)).isEqualTo("test");
    }

    @Test
    void willThrowWhenFileNotFound() {
        // When
        // Then
        assertThatThrownBy(() -> ResourceUtil.readFile("non-existent-file.txt"))
                .isInstanceOf(NullPointerException.class);
    }
}