package com.solution.green.dto;

import lombok.*;
import org.springframework.lang.Nullable;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {
    @Nullable
    private Date firstDate;
    @Nullable
    private Date lastDate;
    @Nullable
    private Double homeRewardSum;
    @Nullable
    private Double consumptionRewardSum;
    @Nullable
    private Double transportRewardSum;
    @Nullable
    private Double foodRewardSum;
    @Nullable
    private int totalQuestCount;
    @Nullable
    private int doneQuestCount;
}
