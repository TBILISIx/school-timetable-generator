package com.solvd.school.timetable.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TimetableEntry {

    private long   id;
    private Timeslot  slot;
    private Subject   subject;
    private Teacher   teacher;
    private Classroom classroom;
}
