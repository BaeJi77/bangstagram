package com.bangstagram.Auditing;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public abstract class TimeAuditorEntity {
    @CreatedDate
    private LocalDateTime createdAt;
}
