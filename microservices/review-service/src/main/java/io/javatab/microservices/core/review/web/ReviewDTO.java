package io.javatab.microservices.core.review.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDTO {

    @NotNull(message = "The course id must be defined.")
    private int courseId;
    @NotBlank(message = "Author is required")
    @Size(min = 4, max = 40, message = "Author must be between 4 and 40 characters")
    private String author;
    @NotBlank(message = "Content is required")
    @Size(min = 5, max = 500, message = "Content must be between 50 and 500 characters")
    private String content;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

}
