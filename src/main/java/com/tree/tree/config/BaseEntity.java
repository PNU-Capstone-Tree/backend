package com.tree.tree.config;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Table
public abstract class BaseEntity {

    @CreatedDate
    @Column("create_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column("update_at")
    private LocalDateTime updateAt;

}
