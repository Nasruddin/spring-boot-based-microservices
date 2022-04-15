package io.javatab.microservices.api.composite.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

public interface CourseCompositeService {

    /**
     * Sample usage: "curl $HOST:$PORT/course-composite/1".
     *
     * @param courseId of the course
     * @return the composite course info, if found, else null
     */
    @Operation(
            summary = "${open-api.course-composite.get-composite-course.description}",
            description = "${open-api.course-composite.get-composite-course.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${open-api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${open-api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${open-api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${open-api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping("/course-composite/{courseId}")
    Mono<CourseAggregate> getCourse(@PathVariable("courseId") int courseId);
}
