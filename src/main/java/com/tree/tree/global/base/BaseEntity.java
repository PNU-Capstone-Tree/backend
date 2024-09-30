package com.tree.tree.global.base;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity {

    @CreatedDate
    @Column("create_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column("update_at")
    private LocalDateTime updateAt;

    protected BaseEntity() {}
}
