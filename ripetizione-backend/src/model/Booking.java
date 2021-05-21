package model;

public class Booking {
    int id;
    String course;
    String teacher;
    String user;
    int status;
    String date;
    String start;

    public Booking(int id, String course, String teacher, String user, int status, String date, String start) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.user = user;
        this.status = status;
        this.date = date;
        this.start = start;
    }
}
