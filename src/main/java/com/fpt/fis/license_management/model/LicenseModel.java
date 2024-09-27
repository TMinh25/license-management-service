package com.fpt.fis.license_management.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.fis.license_management.repository.entity.License;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseModel {
    @JsonIgnore
    private String id;

    @JsonProperty("business_id")
    private String companyCode;

    @JsonProperty("business")
    private String companyName;

    @JsonProperty("user_count")
    private Long licensedUserCount;

    @JsonProperty("package")
    private String currentPackage;

    @JsonIgnore
    private Boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate deliveryDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate expirationDate;

    @JsonIgnore
    private LocalDateTime createdAt;

    public static LicenseModel fromEntity(License license) {
        LicenseModel licenseResponse = new LicenseModel();
        licenseResponse.setId(license.getId());
        licenseResponse.setCompanyCode(license.getCompanyCode());
        licenseResponse.setCompanyName(license.getCompanyName());
        licenseResponse.setLicensedUserCount(license.getLicensedUserCount());
        licenseResponse.setCurrentPackage(license.getCurrentPackage());
        licenseResponse.setActive(license.getActive());
        licenseResponse.setDeliveryDate(license.getDeliveryDate());
        licenseResponse.setExpirationDate(license.getExpirationDate());
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
        license.setDeliveryDate(this.getDeliveryDate());
        license.setExpirationDate(this.getExpirationDate());
        license.setCreatedAt(this.getCreatedAt());
        return license;
    }
}
