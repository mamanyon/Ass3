package bgu.spl.net.impl.BGRS;

import java.util.Vector;

public class Course {
    private int courseNum;
    private String courseName;
    private Vector<Integer> KdamCoursesList;
    private int numOfMaxStudents;
    private int numRegistered;
    private Vector<User> listOfStudents;

    public Course(int courseNum, String courseName, Vector<Integer> kdamCoursesList, int numOfMaxStudents) {
        this.courseNum = courseNum;
        this.courseName = courseName;
        KdamCoursesList = kdamCoursesList;
        this.numOfMaxStudents = numOfMaxStudents;
        listOfStudents=new Vector<>();
        this.numRegistered = 0;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public int getCourseNum() {
        return courseNum;
    }



    public Vector<Integer> getKdamCoursesList() {
        return KdamCoursesList;
    }

    public int getNumRegistered() {
        return numRegistered;
    }

    public void setNumRegistered(int studentsRegistered) {
        this.numRegistered = studentsRegistered;
    }

    public Vector<User> getListOfStudents() {
        return listOfStudents;
    }

    public void addUser(User user) {
        listOfStudents.add(user);
    }

    public String getCourseName() {
        return courseName;
    }
}
