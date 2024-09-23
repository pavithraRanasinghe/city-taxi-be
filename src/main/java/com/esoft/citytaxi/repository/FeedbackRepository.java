package com.esoft.citytaxi.repository;

import com.esoft.citytaxi.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
