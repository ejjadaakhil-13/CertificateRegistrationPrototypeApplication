package com.certificate_registration.certificate_registration.service;


import com.certificate_registration.certificate_registration.model.Certificate;
import com.certificate_registration.certificate_registration.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    public void registerCertificate(Certificate certificate, MultipartFile file) throws IOException {
        certificate.setCertificateFile(file.getBytes());
        certificate.setCertificateFileType(file.getContentType());
        certificateRepository.save(certificate);
    }

    public List<Certificate> getCertificates(String search) {
        if (search != null && !search.isEmpty()) {
            return certificateRepository.search(search);
        }
        return certificateRepository.findAll();
    }

    public ResponseEntity<byte[]> viewCertificate(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, certificate.getCertificateFileType())
                .body(certificate.getCertificateFile());
    }

    public Certificate getCertificateById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
    }

    public void updateCertificate(Long id, Certificate certificate, MultipartFile file) throws IOException {
        Certificate existing = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        existing.setCertificateId(certificate.getCertificateId());
        existing.setFullName(certificate.getFullName());
        existing.setEmail(certificate.getEmail());
        if (file != null && !file.isEmpty()) {
            existing.setCertificateFile(file.getBytes());
            existing.setCertificateFileType(file.getContentType());
        }
        certificateRepository.save(existing);
    }

    public void deleteCertificate(Long id) {
        if (!certificateRepository.existsById(id)) {
            throw new RuntimeException("Certificate not found");
        }
        certificateRepository.deleteById(id);
    }
}
