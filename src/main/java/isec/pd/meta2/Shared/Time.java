package isec.pd.meta2.Shared;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time implements Serializable {
    private int Hour, Minute, Day, Month, Year;

    public Time(int year, int month, int day, int hour, int minute) {
        Hour = hour;
        Minute = minute;
        Day = day;
        Month = month;
        Year = year;
    }

    public Time(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s");
        Date date = dateFormat.parse(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH) + 1;
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        Hour = calendar.get(Calendar.HOUR_OF_DAY);
        Minute = calendar.get(Calendar.MINUTE);
    }

    public Time() {
        Calendar calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH) + 1;
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        Hour = calendar.get(Calendar.HOUR_OF_DAY);
        Minute = calendar.get(Calendar.MINUTE);
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getHour() {
        return Hour;
    }
    public void setHour(int hour) {
        Hour = hour;
    }
    public int getMinute() {
        return Minute;
    }
    public void setMinute(int minute) {
        Minute = minute;
    }
    @Override
    public String toString() {
        return getYear() + "-" + getMonth() + "-" + getDay() + " " + getHour() + ":" + getMinute() + ":00";
    }
    public String toStringHourMinute(){
        return getHour() + ":" + getMinute();
    }
    public String toStringDay(){
        return getYear() + "-" + getMonth() + "-" + getDay();
    }
}
