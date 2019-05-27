package com.assignment.courseservice.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "seats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat implements Serializable {

    @Id
    @Column(name = "seat_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "available", nullable = false)
    private Boolean isSeatAvailable;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType type;

    @ManyToOne
    @JoinColumn(name="course_id", nullable=false)
    private Course course;
}
