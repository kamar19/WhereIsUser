package ru.firstset.whereisuser.data.tracker;

public class TrackSummary {

    private int idTrack;

    public int getIdTrack() {
        return idTrack;
    }

    public void setIdTrack(int idTrack) {
        this.idTrack = idTrack;
    }

    public int getCountPoints() {
        return countPoints;
    }

    public void setCountPoints(int countPoints) {
        this.countPoints = countPoints;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int countPoints;
    private String date;

    public TrackSummary(int idTrack, int countPoints, String date) {
        this.idTrack = idTrack;
        this.countPoints = countPoints;
        this.date = date;
    }
}
