package com.assignment.courseservice.service;

import com.assignment.courseservice.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SeatService {

    private SeatRepository seatRepository;

}
