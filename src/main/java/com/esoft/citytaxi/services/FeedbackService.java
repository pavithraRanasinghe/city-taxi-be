package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.FeedbackRequest;
import com.esoft.citytaxi.models.Feedback;
import com.esoft.citytaxi.models.Trip;
import com.esoft.citytaxi.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TripService tripService;

    public Feedback saveFeedback(final FeedbackRequest feedbackRequest){
        Trip trip = tripService.findById(feedbackRequest.getTripId());
        Feedback feedback = Feedback.builder()
                .comment(feedbackRequest.getComment())
                .rate(feedbackRequest.getRate())
                .trip(trip)
                .build();

        return feedbackRepository.save(feedback);
    }
}
