package com.solvd.school.timetable.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Teacher {

    private Long id;
    private String name;
    private List<Subject> subjects;

}
