package com.certificate_registration.certificate_registration.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateId;
    private String fullName;
    private String email;

    @Lob
    private byte[] certificateFile;
    private String certificateFileType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getCertificateFile() {
        return certificateFile;
    }

    public void setCertificateFile(byte[] certificateFile) {
        this.certificateFile = certificateFile;
    }

    public String getCertificateFileType() {
        return certificateFileType;
    }

    public void setCertificateFileType(String certificateFileType) {
        this.certificateFileType = certificateFileType;
    }
}
