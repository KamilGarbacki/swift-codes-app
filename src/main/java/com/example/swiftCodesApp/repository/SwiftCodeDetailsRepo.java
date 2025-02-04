package com.example.swiftCodesApp.repository;

import com.example.swiftCodesApp.model.SwiftCodeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SwiftCodeDetailsRepo extends JpaRepository<SwiftCodeDetails, Long> {
    Optional<SwiftCodeDetails> findBySwiftCode(String swiftCode);
    List<SwiftCodeDetails> findByCountryISO2(String countryISO2);
    boolean existsBySwiftCode(String swiftCode);

    @Query("""
            select s from SwiftCodeDetails s
            where s.swiftCode like concat(substr(?1, 1, 8), '%') and s.swiftCode not like concat('%', 'XXX')""")
    List<SwiftCodeDetails> findBranches(String swiftCode);
}
