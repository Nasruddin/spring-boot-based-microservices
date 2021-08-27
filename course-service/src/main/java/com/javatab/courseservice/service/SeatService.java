package com.javatab.courseservice.service;

import com.javatab.courseservice.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SeatService {

    private SeatRepository seatRepository;

}
