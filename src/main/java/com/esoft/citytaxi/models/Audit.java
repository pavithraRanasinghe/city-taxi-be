package com.esoft.citytaxi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@MappedSuperclass
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Audit {

    @JsonIgnore
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 25)
    private String createdBy;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @JsonIgnore
    @LastModifiedBy
    @Column(name = "modified_by", length = 25)
    private String modifiedBy;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Audit audit = (Audit) o;
        return createdBy.equals(audit.createdBy) && createdDate.equals(audit.createdDate) && modifiedBy.equals(audit.modifiedBy) && modifiedDate.equals(audit.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdBy, createdDate, modifiedBy, modifiedDate);
    }
}
