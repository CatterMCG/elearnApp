package base;

import java.io.Serializable;

/**
 * Created by 成刚 on 2016/4/18.
 */
public class UserTestBean implements Serializable {
    private String phone;
    private String scores;
    private String course;
    private String time;
    private int hard;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String username) {
        this.phone = phone;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    public String getScores() {
        return scores;
    }

    public void setScores(String scores) {
        this.scores = scores;
    }
}
