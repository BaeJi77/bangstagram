package com.bangstagram.timeline.domain.repository;

import com.bangstagram.timeline.domain.model.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineRepository extends JpaRepository <Timeline, Long> {
}
