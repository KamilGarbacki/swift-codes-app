package com.example.swiftCodesApp.model;

import com.example.swiftCodesApp.listener.SwiftCodeDetailsListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(SwiftCodeDetailsListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SwiftCodeDetails {
    @Id
    @SequenceGenerator(
            name = "swift_code_details_id_seq",
            sequenceName = "swift_code_details_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "swift_code_details_id_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String countryISO2;

    @Column(nullable = false)
    private String countryName;

    @Column(unique = true, nullable = false, length = 11)
    private String swiftCode;

    public boolean isHeadquarters() {
        return swiftCode.endsWith("XXX");
    }
}
