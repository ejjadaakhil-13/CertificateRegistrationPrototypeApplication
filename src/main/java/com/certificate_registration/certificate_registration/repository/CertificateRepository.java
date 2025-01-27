package com.certificate_registration.certificate_registration.repository;


import com.certificate_registration.certificate_registration.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    @Query("SELECT c FROM Certificate c WHERE " +
            "LOWER(c.certificateId) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Certificate> search(String search);
}