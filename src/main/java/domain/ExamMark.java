package domain;

public class ExamMark {

    private String studentName;
    private int mark;

    public ExamMark(String studentName, int mark) {
        this.studentName = studentName;
        this.mark = mark;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getMark() {
        return mark;
    }
}
