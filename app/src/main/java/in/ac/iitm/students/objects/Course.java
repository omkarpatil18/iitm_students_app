package in.ac.iitm.students.objects;

/**
 * Created by SAM10795 on 14-06-2017.
 */

public class Course {
    private char slot;
    private String course_id;
    private int days;


    public char getSlot() {
        return slot;
    }

    public int getDays() {
        return days;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setSlot(char slot) {
        this.slot = slot;
    }
}
