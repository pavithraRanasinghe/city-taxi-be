package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.FeedbackRequest;
import com.esoft.citytaxi.dto.response.FeedbackResponse;
import com.esoft.citytaxi.models.Feedback;
import com.esoft.citytaxi.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/feedback")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> saveFeedback(@RequestBody FeedbackRequest feedbackRequest){
        log.info("test");
        Feedback feedback = feedbackService.saveFeedback(feedbackRequest);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    @GetMapping("/filter")
    public List<FeedbackResponse> getFeedback(@RequestParam(required = false) Long driverId,
                                      @RequestParam(required = false) Long passengerId) {
        return feedbackService.filterFeedback(driverId, passengerId);
    }

    @GetMapping("/recent-feedback")
    public List<FeedbackResponse> findRecentFeedbacks(){
        return feedbackService.getTop10RecentFeedbacks();
    }
}
