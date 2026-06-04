package com.solvd.school.timetable.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Timetable {

    private Long id;
    private List<TimetableEntry> entries = new ArrayList<>();
    private Integer fitnessScore = 0;
  
}
