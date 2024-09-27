package com.fpt.fis.license_management.repository.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "licenses")
public class License {
    @Id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @Indexed(unique = true)
    private String companyCode;

    @Indexed(unique = true)
    private String companyName;

    private Long licensedUserCount;

    private String currentPackage;

    private Boolean active;

    private LocalDate deliveryDate;

    private LocalDate expirationDate;

    @CreatedDate
    private LocalDateTime createdAt;
}
