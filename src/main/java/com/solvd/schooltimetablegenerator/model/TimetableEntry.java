package com.solvd.schooltimetablegenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TimetableEntry {

    private Long id;
    private TimeSlot slot;
    private Subject subject;
    private Teacher teacher;
    private Classroom classroom;
    private Long timetableId;

}
