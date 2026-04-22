package com.ecommerce.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class AuditableEntity extends BaseEntity{
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "created_by")
    private Long createBy;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @PrePersist
    public void onCreate(){
        createAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        updateAt = LocalDateTime.now();
    }

}
