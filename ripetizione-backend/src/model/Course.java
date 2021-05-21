package model;

import java.util.ArrayList;

public class Course {
    int id;
    String name;
    ArrayList<Teacher> teachers=new ArrayList<>();

    public Course(int id, String name, ArrayList<Teacher> teachers) {
        this.id = id;
        this.name = name;
        this.teachers = teachers;
    }
}
