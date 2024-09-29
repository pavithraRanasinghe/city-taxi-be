package com.esoft.citytaxi.services;

import com.esoft.citytaxi.dto.request.FeedbackRequest;
import com.esoft.citytaxi.dto.response.FeedbackResponse;
import com.esoft.citytaxi.models.Feedback;
import com.esoft.citytaxi.models.Trip;
import com.esoft.citytaxi.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

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
                .date(LocalDate.now())
                .time(LocalTime.now())
                .build();

        return feedbackRepository.save(feedback);
    }

    public List<FeedbackResponse> filterFeedback(final Long driverId, final Long passengerId){
        List<Feedback> feedbackList = feedbackRepository.findByDriverIdOrPassengerId(driverId, passengerId);
        return mapToResponse(feedbackList);
    }

    public List<FeedbackResponse> getTop10RecentFeedbacks() {
        List<Feedback> recentFeedbacks = feedbackRepository.findRecentFeedbacks(PageRequest.of(0, 10));
        return mapToResponse(recentFeedbacks);
    }

    private List<FeedbackResponse> mapToResponse(List<Feedback> feedbackList){
        return feedbackList.stream().map(feedback -> FeedbackResponse.builder()
                .driver(feedback.getTrip().getDriver())
                .passenger(feedback.getTrip().getPassenger())
                .rate(feedback.getRate())
                .comment(feedback.getComment())
                .date(feedback.getDate())
                .time(feedback.getTime()).build()).toList();
    }

    public double getAverageRatingByDriverId(Long driverId) {
        Double rate = feedbackRepository.calculateAverageRatingByDriverId(driverId);
        return Objects.nonNull(rate) ? rate: 0;
    }
}
