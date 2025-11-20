package com.example.cursach;

import java.util.ArrayList;
import java.util.List;

public class UserProgress {
    private long timeSpentInApp;
    private long legendsReadCount;
    private List<String> readLegendIds;
    private boolean readRules;

    public UserProgress() {
        // Default constructor required for calls to DataSnapshot.getValue(UserProgress.class)
        this.readLegendIds = new ArrayList<>();
    }

    public long getTimeSpentInApp() {
        return timeSpentInApp;
    }

    public void setTimeSpentInApp(long timeSpentInApp) {
        this.timeSpentInApp = timeSpentInApp;
    }

    public long getLegendsReadCount() {
        return legendsReadCount;
    }

    public void setLegendsReadCount(long legendsReadCount) {
        this.legendsReadCount = legendsReadCount;
    }

    public List<String> getReadLegendIds() {
        return readLegendIds;
    }

    public void setReadLegendIds(List<String> readLegendIds) {
        this.readLegendIds = readLegendIds;
    }

    public boolean isReadRules() {
        return readRules;
    }

    public void setReadRules(boolean readRules) {
        this.readRules = readRules;
    }
}
