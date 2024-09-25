package com.fpt.fis.license_management.model;

import com.fpt.fis.license_management.repository.entity.License;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class LicenseModel {
    private String id;

    private String companyCode;

    private String companyName;

    private Long licensedUserCount;

    private String currentPackage;

    private Boolean active;

    private LocalDate deliveryDate;

    private LocalDate expirationDate;

    private LocalDateTime createdAt;

    public static LicenseModel fromEntity(License license) {
        LicenseModel licenseResponse = new LicenseModel();
        licenseResponse.setId(license.getId());
        licenseResponse.setCompanyCode(license.getCompanyCode());
        licenseResponse.setCompanyName(license.getCompanyName());
        licenseResponse.setLicensedUserCount(license.getLicensedUserCount());
        licenseResponse.setCurrentPackage(license.getCurrentPackage());
        licenseResponse.setActive(license.getActive());
        licenseResponse.setCreatedAt(license.getCreatedAt());
        return licenseResponse;
    }

    public License toEntity() {
        License license = new License();
        license.setId(this.getId());
        license.setCompanyCode(this.getCompanyCode());
        license.setCompanyName(this.getCompanyName());
        license.setLicensedUserCount(this.getLicensedUserCount());
        license.setCurrentPackage(this.getCurrentPackage());
        license.setActive(this.getActive());
        license.setCreatedAt(this.getCreatedAt());
        return license;
    }
}
