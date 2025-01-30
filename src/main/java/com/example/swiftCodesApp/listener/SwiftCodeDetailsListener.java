package com.example.swiftCodesApp.listener;

import com.example.swiftCodesApp.model.SwiftCodeDetails;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class SwiftCodeDetailsListener {

    @PrePersist
    @PreUpdate
    private void beforeUpdate(SwiftCodeDetails details) {
        details.setAddress(details.getAddress().toUpperCase());
        details.setBankName(details.getBankName().toUpperCase());
        details.setCountryISO2(details.getCountryISO2().toUpperCase());
        details.setCountryName(details.getCountryName().toUpperCase());
        details.setSwiftCode(details.getSwiftCode().toUpperCase());
    }

}
