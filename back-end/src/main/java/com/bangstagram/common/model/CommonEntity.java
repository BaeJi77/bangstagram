package com.bangstagram.common.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.04.30
 */

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class CommonEntity {
    @CreatedDate
    private LocalDateTime createdAt;
}
