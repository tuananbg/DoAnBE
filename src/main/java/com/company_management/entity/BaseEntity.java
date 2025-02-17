package com.company_management.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private Long createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY")
    private Long updatedBy;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedDate;

    @Column(name = "IS_ACTIVE")
    private Integer isActive = 1;

    @Override
    public String toString() {
        return "[id:" + this.getId() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof BaseEntity) {
            if (this.getId() != null) {
                return this.getId().equals(((BaseEntity) obj).getId());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, createdBy, updatedDate, updatedBy, isActive);
    }
}
