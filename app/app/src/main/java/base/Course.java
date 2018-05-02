package base;

/**
 * Created by 成刚 on 2016/4/9.
 */
import java.io.Serializable;

/**
 * 这个是课程的jsonBean。
 */
public class Course implements Serializable {
    private int id;//题号
    private int hard;//难度系数1最低，5最高
    private String title;//题目
    private String A;//选项
    private String B;
    private String C;
    private String D;
    private String answer;//答案
    private String analysis;//答案分析

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHard() {
        return hard;
    }

    public void setHard(int hard) {
        this.hard = hard;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
