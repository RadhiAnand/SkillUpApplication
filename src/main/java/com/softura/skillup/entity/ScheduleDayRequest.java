package com.softura.skillup.entity;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.time.DayOfWeek;

@Getter
@Setter
public class ScheduleDayRequest {
    DayOfWeek scheduleDay;
    String dayId;

    public ScheduleDayRequest(DayOfWeek scheduleDay, String dayIds) {
        this.scheduleDay = scheduleDay;
        this.dayId = dayIds;
    }

}
