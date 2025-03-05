package io.javatab.microservices.core.review.web;

import io.javatab.microservices.core.review.domain.Review;
import io.javatab.microservices.core.review.domain.ReviewService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/reviews")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    /*
    * http POST :9002/api/reviews courseId:=1 author="John Doe" content="Amazing book"  email="abc@xyz.com"
    * */
    @PostMapping
    public ResponseEntity<Review> addReview(@Valid @RequestBody ReviewDTO review) {
        logger.info("Received request to add review to course id {} by email: {} and ", review.getCourseId(), review.getEmail());
        Review addedReview = reviewService.addReview(review);
        return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        logger.info("Received request to fetch all reviews");
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        logger.info("Received request to fetch review with id: {}", id);
        return ResponseEntity.ok(reviewService.getReviewId(id));
    }

    @GetMapping(params = {"course"})
    public ResponseEntity<List<Review>> getReviewByCourseId(@RequestParam("course") Long courseId) {
        logger.info("Received request to fetch review with course id: {}", courseId);
        return ResponseEntity.ok(reviewService.getReviewsByCourseId(courseId));
    }

    /*
    * http :9002/api/reviews courseId==1 email==abc@xyz.com
    * or
    * http GET "http://localhost:9002/api/reviews?courseId=1&email=abc@xyz.com"
     * */
    @GetMapping(params = {"courseId", "email"})
    public ResponseEntity<List<Review>> getReviewByCourseIdAndEmail(@RequestParam("courseId") Long courseId, @RequestParam("email") String email) {
        logger.info("Received request to fetch review with course id: {} and email : {}", courseId, email);
        return ResponseEntity.ok(reviewService.getReviewsByCourseIdAndEmail(courseId, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        logger.info("Received request to delete review with id: {}", id);
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
