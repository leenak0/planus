package com.planus.trip.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TripReqDTO {
    private String startDate;
    private int period;
    private int[] areaId;
    private int admin;

    public TripReqDTO(String startDate, int period, int[] areaId, int admin) {
        this.startDate = startDate;
        this.period = period;
        this.areaId = areaId;
        this.admin = admin;
    }
}
