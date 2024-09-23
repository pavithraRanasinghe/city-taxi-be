package com.esoft.citytaxi.controller;

import com.esoft.citytaxi.dto.request.FeedbackRequest;
import com.esoft.citytaxi.models.Feedback;
import com.esoft.citytaxi.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/feedback")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> saveFeedback(@RequestBody FeedbackRequest feedbackRequest){
        Feedback feedback = feedbackService.saveFeedback(feedbackRequest);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }
}
