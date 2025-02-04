package com.example.swiftCodesApp.util;

import com.example.swiftCodesApp.dto.ResponseMessageDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseMessageUtilTest {

    @Test
    void createMessage() {
        // Given
        ResponseMessageUtil.ResponseMessageType type = ResponseMessageUtil.ResponseMessageType.CREATED_MESSAGE;
        String swiftCode = "test-swift-code";
        ResponseMessageDto expected = new ResponseMessageDto(
                type.getMessage().formatted(swiftCode)
        );

        // When
        // Then
        assertThat(ResponseMessageUtil.createMessage(type, swiftCode)).isEqualTo(expected);
    }
}