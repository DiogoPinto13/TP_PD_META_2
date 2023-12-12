package isec.pd.meta2.Shared;

import java.io.Serializable;

public class Event implements Serializable {
    private String designation;
    private String place;
    private Time timeBegin, timeEnd;

    public Event(String designation, String place, Time timeBegin, Time timeEnd) {
        this.designation = designation;
        this.place = place;
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
    }



    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }

    public Time getTimeBegin() {
        return timeBegin;
    }
    public Time getTimeEnd() {
        return timeEnd;
    }
    public void setTimeBegin(Time timeBegin) {
        this.timeBegin = timeBegin;
    }
    public void setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
    }

}
