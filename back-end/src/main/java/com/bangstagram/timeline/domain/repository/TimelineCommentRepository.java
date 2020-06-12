package com.bangstagram.timeline.domain.repository;

import com.bangstagram.timeline.domain.model.TimelineComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * author: Ji-Hoon Bae
 * Date: 2020.05.21
 */

@Repository
public interface TimelineCommentRepository extends JpaRepository<TimelineComment, Long> {
}
