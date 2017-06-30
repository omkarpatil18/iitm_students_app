package in.ac.iitm.students.objects;

import java.util.Date;

/**
 * Created by SAM10795 on 28-06-2017.
 */

public class Calendar_Event {

    private int date;
    private int month;
    private String day;
    private String details;
    private boolean holiday;
    private boolean remind;

    public boolean isHoliday() {
        return holiday;
    }

    public boolean isRemind() {
        return remind;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getDetails() {
        return details;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

}
