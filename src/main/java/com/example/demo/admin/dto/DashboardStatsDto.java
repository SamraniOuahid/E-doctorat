package com.example.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalUsers;
    private long totalCandidats;
    private long totalSujets;
    private long pendingCandidatures;
    private long enabledUsers;
    private long disabledUsers;
}
