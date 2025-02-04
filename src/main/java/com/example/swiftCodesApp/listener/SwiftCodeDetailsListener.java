package com.example.swiftCodesApp.listener;

import com.example.swiftCodesApp.exception.InternalServerException;
import com.example.swiftCodesApp.model.SwiftCodeDetails;
import com.example.swiftCodesApp.util.TransformUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwiftCodeDetailsListener {

    @PrePersist
    @PreUpdate
    private void beforeUpdate(SwiftCodeDetails details) {
        try {
            TransformUtil.allObjectFieldsToUpperCase(details);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new InternalServerException("Encountered an exception during entity transformation");
        }

    }

}
