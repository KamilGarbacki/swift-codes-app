package com.example.swiftCodesApp.unitTests.service;

import com.example.swiftCodesApp.controller.v1.requests.SwiftCodeDetailsReq;
import com.example.swiftCodesApp.dto.CountrySwiftCodesListDto;
import com.example.swiftCodesApp.dto.HeadquartersDetailsDto;
import com.example.swiftCodesApp.dto.SwiftCodeDetailsDto;
import com.example.swiftCodesApp.exception.ConflictException;
import com.example.swiftCodesApp.exception.NotFoundException;
import com.example.swiftCodesApp.mapper.SwiftCodeDetailsMapper;
import com.example.swiftCodesApp.model.SwiftCodeDetails;
import com.example.swiftCodesApp.repository.SwiftCodeDetailsRepo;
import com.example.swiftCodesApp.service.SwiftCodeDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SwiftCodeDetailsServiceImplTest {
    @Spy
    private SwiftCodeDetailsMapper swiftMapper = Mappers.getMapper(SwiftCodeDetailsMapper.class);
    @Mock
    private SwiftCodeDetailsRepo swiftCodeDetailsRepo;
    @InjectMocks
    private SwiftCodeDetailsServiceImpl underTest;

    @Test
    void addSwiftCodeDetails() {
        // Given
        SwiftCodeDetailsReq req = getSampleSwiftCodeDetailsReq();

        // When
        when(swiftCodeDetailsRepo.save(any(SwiftCodeDetails.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Then
        assertThat(underTest.addSwiftCodeDetails(req))
                .isEqualTo(swiftMapper.toSwiftCodeDetailsDto(swiftMapper.toSwiftCodeDetails(req)));
    }

    @Test
    void willThrowWhenSwiftCodeAlreadyExists() {
        // When
        when(swiftCodeDetailsRepo.existsBySwiftCode(anyString()))
                .thenReturn(true);

        //Then
        assertThatThrownBy(() -> underTest.addSwiftCodeDetails(getSampleSwiftCodeDetailsReq()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("This Swift code already exists");
    }

    @Test
    void deleteSwiftCodeDetails() {
        // Given
        SwiftCodeDetails details = getSampleSwiftCodeDetails(false);

        // When
        when(swiftCodeDetailsRepo.findBySwiftCode(details.getSwiftCode()))
                .thenReturn(Optional.of(details));
        underTest.deleteSwiftCodeDetails(details.getSwiftCode());

        // Then
        verify(swiftCodeDetailsRepo).delete(details);
    }

    @Test
    void willThrowWhenSwiftCodeToBeDeletedDoesNotExist() {
        // Given
        String sampleSwiftCode = "abcdefghijk";

        // When
        // Then
        assertThatThrownBy(() -> underTest.deleteSwiftCodeDetails(sampleSwiftCode))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Swift code not found");
    }

    @Test
    void getCountrySwiftCodes() {
        // Given
        SwiftCodeDetails details = getSampleSwiftCodeDetails(false);

        // When
        when(swiftCodeDetailsRepo.findByCountryISO2(details.getCountryISO2()))
                .thenReturn(List.of(details));

        CountrySwiftCodesListDto result = underTest.getCountrySwiftCodes(details.getCountryISO2());

        // Then
        assertThat(result.countryISO2()).isEqualTo(details.getCountryISO2());
        assertThat(result.countryName()).isEqualTo(details.getCountryName());
        assertThat(result.swiftCodes()).isEqualTo(
                List.of(
                        swiftMapper.toCountrySwiftCodeDto(details)
                )
        );
    }

    @Test
    void willThrowWhenSwiftCodesAreEmpty() {
        // Given
        String sampleISO2 = "pl";

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCountrySwiftCodes(sampleISO2))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Swift code data for countryISO2 code %s not found".formatted(sampleISO2));
    }


    @Test
    void getSwiftCodeDetailsForBranch() {
        // Given
        SwiftCodeDetails details = getSampleSwiftCodeDetails(false);

        // When
        when(swiftCodeDetailsRepo.findBySwiftCode(details.getSwiftCode()))
                .thenReturn(Optional.of(details));

        SwiftCodeDetailsDto result = underTest.getSwiftCodeDetails(details.getSwiftCode());

        // Then
        assertThat(result).isEqualTo(swiftMapper.toSwiftCodeDetailsDto(details));
    }

    @Test
    void getSwiftCodeDetailsForHeadquarters() {
        // Given
        SwiftCodeDetails headquarters = getSampleSwiftCodeDetails(true);
        SwiftCodeDetails branch = getSampleSwiftCodeDetails(false);
        HeadquartersDetailsDto expected = swiftMapper.toHeadquartersSwiftCodeDetailsDto(headquarters);
        expected.setBranches(
                List.of(
                        swiftMapper.toSwiftCodeDetailsDto(branch)
                )
        );

        // When
        when(swiftCodeDetailsRepo.findBySwiftCode(headquarters.getSwiftCode()))
                .thenReturn(Optional.of(headquarters));

        when(swiftCodeDetailsRepo.findBranches(headquarters.getSwiftCode()))
                .thenReturn(List.of(branch));

        SwiftCodeDetailsDto result = underTest.getSwiftCodeDetails(headquarters.getSwiftCode());

        // Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void willThrowWhenSwiftCodeDoesNotExist() {
        // Given
        String sampleSwiftCode = "abcdefghijk";

        // Then
        assertThatThrownBy(() -> underTest.getSwiftCodeDetails(sampleSwiftCode))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Swift code not found");
    }

    private SwiftCodeDetailsReq getSampleSwiftCodeDetailsReq() {
        return new SwiftCodeDetailsReq(
                "sample-address",
                "sample-bank-name",
                "pl",
                "poland",
                false,
                "abcdefghijk"
        );
    }

    private SwiftCodeDetails getSampleSwiftCodeDetails(boolean isHeadquarters) {
        String swiftCode = isHeadquarters ? "abcdefghXXX" : "abcdefghijk";

        return new SwiftCodeDetails(
                1L,
                "sample-address",
                "sample-bank-name",
                "pl",
                "poland",
                swiftCode
        );
    }
}