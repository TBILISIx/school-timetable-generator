package com.solvd.school.timetable.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
