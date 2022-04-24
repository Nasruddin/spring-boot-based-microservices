package io.javatab.microservices.api.composite.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "security_auth")
@Tag(name = "CourseComposite", description = "REST API for composite course information.")
public interface CourseCompositeService {

// TODO:: Update API definitions
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
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/course-composite")
    Mono<String> createProduct(@RequestBody CourseAggregate body);


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
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/course-composite/{courseId}")
    Mono<String> deleteCourse(@PathVariable("courseId") int courseId);

}
