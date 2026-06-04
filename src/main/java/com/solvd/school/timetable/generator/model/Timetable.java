package com.solvd.school.timetable.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Timetable {

    private Long id;

    @Builder.Default
    private List<TimetableEntry> entries = new ArrayList<>();

    @Builder.Default
    private double fitnessScore = -1.0;
}
