package com.fintech.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(name = "phone_nr", length = 20)
    @Pattern(regexp = "^\\+[1-9]\\d{6,14}$")
    private String phoneNr;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "modified_time", nullable = false)
    private LocalDateTime modifiedTime;

    @Column(name = "deleted_time")
    private LocalDateTime deletedTime;


    public void softDelete(){
        this.setIsActive(null);
        this.setDeletedTime(LocalDateTime.now());
    }

    public void updateName(String newName) {
        if (StringUtils.hasText(newName) && isNameChanged(newName)) {
            this.name = newName;
        }
    }

    public void updatePhoneNumber(String newPhoneNr) {
        if (StringUtils.hasText(newPhoneNr) && isPhoneNumberChanged(newPhoneNr)) {
            this.phoneNr = newPhoneNr;
        }
    }

    public boolean isPhoneNumberChanged(String newPhoneNr) {
        return !Objects.equals(this.phoneNr, newPhoneNr);
    }

    public boolean isNameChanged(String newName) {
        return !Objects.equals(this.name, newName);
    }
}