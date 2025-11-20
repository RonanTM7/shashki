package com.example.cursach;

import java.util.ArrayList;
import java.util.List;

public class UserProgress {
    private long timeSpentInApp;
    private long legendsReadCount;
    private List<String> readLegendIds;
    private boolean readRules;
    private List<String> friends;
    private List<String> friendRequestsSent;
    private List<String> friendRequestsReceived;

    public UserProgress() {
        // Default constructor required for calls to DataSnapshot.getValue(UserProgress.class)
        this.readLegendIds = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.friendRequestsSent = new ArrayList<>();
        this.friendRequestsReceived = new ArrayList<>();
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

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public void setFriendRequestsSent(List<String> friendRequestsSent) {
        this.friendRequestsSent = friendRequestsSent;
    }

    public List<String> getFriendRequestsReceived() {
        return friendRequestsReceived;
    }

    public void setFriendRequestsReceived(List<String> friendRequestsReceived) {
        this.friendRequestsReceived = friendRequestsReceived;
    }
}
