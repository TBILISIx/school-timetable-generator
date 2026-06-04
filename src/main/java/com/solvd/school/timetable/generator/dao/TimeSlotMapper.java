package com.solvd.school.timetable.generator.dao;

import com.solvd.school.timetable.generator.model.TimeSlot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TimeSlotMapper {

    List<TimeSlot> findAll();

    TimeSlot findByDayAndPeriod(@Param("dayOfWeek") int dayOfWeek,
                                @Param("periodNumber") int periodNumber);
}
