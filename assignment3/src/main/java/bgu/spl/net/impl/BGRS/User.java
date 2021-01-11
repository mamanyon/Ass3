package bgu.spl.net.impl.BGRS;

import java.util.Vector;

public class User {
    final private String username;
    final private String password;
    private boolean isAdmin;
    private boolean isLoggedIn;
    private Vector<Integer> KdamCoursesList; //list of courses registered

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.KdamCoursesList=new Vector<>();
        this.isLoggedIn = false;
    }

    public void LogIn(){
        isLoggedIn=true;
    }
    public void LogOut(){
        isLoggedIn=false;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public Vector<Integer> getKdamCoursesList() {
        return KdamCoursesList;
    }

    public String getUsername() {
        return username;
    }
}
