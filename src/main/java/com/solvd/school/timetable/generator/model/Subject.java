package com.solvd.school.timetable.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    private Long id;
    private String name;
    private boolean isPe;

}