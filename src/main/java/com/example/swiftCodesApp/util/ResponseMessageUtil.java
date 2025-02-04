package com.example.swiftCodesApp.util;

import com.example.swiftCodesApp.dto.ResponseMessageDto;
import lombok.Getter;

public class ResponseMessageUtil {
    public static ResponseMessageDto createMessage(ResponseMessageType type, String swiftCode) {
        return new ResponseMessageDto(type.getMessage().formatted(swiftCode));
    }

    @Getter
    public enum ResponseMessageType {
        CREATED_MESSAGE("Created data for swift code: %s"),
        DELETED_MESSAGE("Deleted data for swift code: %s");

        private final String message;

        ResponseMessageType(String message) {
            this.message = message;
        }

    }
}
