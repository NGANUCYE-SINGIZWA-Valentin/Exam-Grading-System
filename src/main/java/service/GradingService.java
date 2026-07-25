package service;

import domain.Mark;

public class GradingService {

    public String grade(int mark) {
        if (mark < 0 || mark > 100) {
            throw new IllegalArgumentException("Mark must be between 0 and 100");
        }

        if (mark >= 90) {
            return "A";
        }
        if (mark >= 80) {
            return "B";
        }
        if (mark >= 70) {
            return "C";
        }
        if (mark >= 60) {
            return "D";
        }
        return "F";
    }

    public String gradeFromMark(Mark mark) {
        return grade(mark.getValue());
    }

    public String[] gradeAll(int[] marks) {
        if (marks == null) {
            throw new IllegalArgumentException("Marks cannot be null");
        }

        String[] grades = new String[marks.length];
        for (int i = 0; i < marks.length; i++) {
            grades[i] = grade(marks[i]);
        }
        return grades;
    }

    public double classAverage(int[] marks) {
        if (marks == null || marks.length == 0) {
            throw new IllegalArgumentException("Marks cannot be empty");
        }

        int total = 0;
        for (int mark : marks) {
            validateMark(mark);
            total += mark;
        }
        return (double) total / marks.length;
    }

    private void validateMark(int mark) {
        if (mark < 0 || mark > 100) {
            throw new IllegalArgumentException("Mark must be between 0 and 100");
        }
    }
}
