package com.solvd.schooltimetablegenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TimeSlot {

    private Long id;
    private String dayOfWeek;
    private Integer periodNumber;
}