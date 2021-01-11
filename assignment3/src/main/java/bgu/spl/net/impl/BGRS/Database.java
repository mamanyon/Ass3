package bgu.spl.net.impl.BGRS;

import bgu.spl.net.impl.BGRS.messages.Acknowledgement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.DoubleToIntFunction;

/**
     * Passive object representing the Database where all courses and users are stored.
     * <p>
     * This class must be implemented safely as a thread-safe singleton.
     * You must not alter any of the given public methods of this class.
     * <p>
     * You can add private fields and methods to this class as you see fit.
     */
    public class Database {

    private HashMap<Integer, Course> courseHashMap; //number course to course

    private ConcurrentHashMap<String, User> userConcurrentHashMap; // username to user

    //to prevent user from creating new Database
    private Database() {
        courseHashMap = new HashMap<>();
        userConcurrentHashMap = new ConcurrentHashMap<>();

        initialize("/home/spl211/IdeaProjects/assignment3/src/main/java/BGRSMain/Courses.txt");

    }


    private static class SingletonHolder {
        private static Database instance = new Database();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    boolean initialize(String coursesFilePath) {

        try {

            File courseFile = new File(coursesFilePath);
            Scanner reader = new Scanner(courseFile);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] splitLine = line.split("\\|");
                System.out.println(Arrays.toString(splitLine));
                //Initiate course number, name and the number of max student of the course
                int courseNumber = Integer.parseInt(splitLine[0]);
                String courseName = splitLine[1];
                int numOfMaxStudent = Integer.parseInt(splitLine[3]);

                //Initiate Kdam courses vector
                System.out.println(splitLine[2]);

                String[] stringKdamCourses = (splitLine[2].substring(1, splitLine[2].length()-1)).split(",");

                System.out.println("stringKdamCourses ---> " + Arrays.toString(stringKdamCourses));

                Vector<Integer> kdamCourses = new Vector<>();
                if(!stringKdamCourses[0].equals("")) {

                    for (String courseNum : stringKdamCourses)
                        kdamCourses.add(Integer.parseInt(courseNum));

                    System.out.println(kdamCourses.toString());
                }

                //Embed the course to the hash map with the key represented by the course number
                courseHashMap.put(courseNumber, new Course(courseNumber, courseName, kdamCourses, numOfMaxStudent));
                System.out.println("HERE");
            }
        } catch (FileNotFoundException e) {
            System.out.println("You got a problem with your file path");
            return false;
        }
        return true;
    }


    public boolean addUser(String user, String pass, boolean isAdmin) {
        if (userConcurrentHashMap.containsKey(user)) {
            //username already exists
            return false;
        } else {
            synchronized (this) {

                if (isAdmin) {
                    User admin = new User(user, pass, true);
                    userConcurrentHashMap.put(user, admin);
                    return true;
                } else //not admin
                {
                    User user1 = new User(user, pass, false);
                    userConcurrentHashMap.put(user, user1);
                    return true;
                }
            }
        }
    }

    public boolean Login(String user, String pass) {
        User login = userConcurrentHashMap.get(user);
        if (login == null || login.isLoggedIn() | !(login.getPassword().equals(pass))) {
            //mo such username || already logged in | password is not correct
            return false;
        } else {
            login.LogIn();
            return true;
        }
    }

    public boolean Logout(String user) {
        User logout = userConcurrentHashMap.get(user);
        if (logout == null || !(logout.isLoggedIn())) {
            //no such username || not logged in ..
            return false;
        } else {
            synchronized (this) {

                logout.LogOut();
                return true;
            }
        }
    }

    public boolean CourseRegister(String user, Integer courseNum) {
        User userReg = userConcurrentHashMap.get(user);
        Course toRegister = courseHashMap.get(courseNum);
        if (userReg == null || userReg.getIsAdmin()) return false;
        //user isn't exist or admin can't register to a course
        if (!userReg.isLoggedIn()) return false; //the user is not logged in
        if (toRegister == null) return false; //no such course is exist
        if (isFull(toRegister)) return false; //there's no place in the course.
        if (!hasFinishedKdam(userReg, toRegister)) return false; //the student does not have all the Kdam courses

        synchronized (this) {
            int numOfStudentRegistered = toRegister.getNumRegistered();
            numOfStudentRegistered++;
            toRegister.setNumRegistered(numOfStudentRegistered);
            toRegister.addUser(userReg);
            userReg.getKdamCoursesList().add(courseNum); //register to the course

            return true;
        }
    }

    private boolean hasFinishedKdam(User userReg, Course toRegister) {
        Vector<Integer> userKdamCourses = userReg.getKdamCoursesList();
        for (Integer i : toRegister.getKdamCoursesList()) { //go throw the kdam courses for the specific course
            if (!userKdamCourses.contains(i))
                return false;
        }
        return true;
    }

    private boolean isFull(Course toRegister) {
        return (toRegister.getNumOfMaxStudents() - toRegister.getNumRegistered() == 0);
    }

    //Return true if removed user registration from specific course
    public boolean unregisterToCourse(String username, int courseNumber) {
        User user = userConcurrentHashMap.get(username);
        if (courseHashMap.get(courseNumber) != null | !user.getIsAdmin()) {

            synchronized (this) {
                Integer cast = courseNumber;
                boolean removedCourse = user.getKdamCoursesList().remove(cast);
                Vector<User> listOfStudents = courseHashMap.get(courseNumber).getListOfStudents();
                return removedCourse & listOfStudents.remove(user);
            }
        }
        return false;
    }

    //Return user's course list
    public String CheckMyCurrentCourses(String username) {
        return userConcurrentHashMap.get(username).getKdamCoursesList().toString();
    }

    //Return Kdam course list of specific course number if exist, otherwise return null
    public String kdamCheck(int courseNumber) {
        Course course = courseHashMap.get(courseNumber);
        if(course != null)
            return course.getKdamCoursesList().toString();
        return null;
    }

    public String CourseStats(String userName, int courseNumber) { // message 7
        String output = null;
        User user = userConcurrentHashMap.get(userName);
        if (user.getIsAdmin()) { // only for admins
            Course course = courseHashMap.get(courseNumber);
            if (course != null) { //course is not exists
                output = "Course:" + "(" + course.getCourseNum() + ")" + course.getCourseName() + "\n";
                int numOfSeatsAvailable = course.getNumOfMaxStudents() - course.getNumRegistered();
                output += "Seats Available:" + numOfSeatsAvailable + "/" + course.getNumOfMaxStudents() + "\n";
                Vector<String> usersName = new Vector<>();
                for (User u : course.getListOfStudents())
                    usersName.add(u.getUsername());
                Collections.sort(usersName);
                output += "Students Registered: " + userName;
            }
        }
        return output;
    }

    public String isRegistered(String username, int courseNumber) { //message 9
        if (courseHashMap.get(courseNumber) != null | !userConcurrentHashMap.get(username).getIsAdmin()) {
            Vector<Integer> courseList = userConcurrentHashMap.get(username).getKdamCoursesList();
            boolean isRegistered = courseList.contains(courseNumber);
            if (isRegistered) {
                //courseList.remove(courseNumber);
                return "REGISTERED";
            } else {
                return "NOT REGISTERED";
            }
        } else {
            return "ERR";
        }
    }

    public String StudentsStats(String userName) { //message 8
        String output = null;
        User user = userConcurrentHashMap.get(userName);
        if (user != null && user.getIsAdmin()) { //  only for admins
            output= "Student:" + user.getUsername() +"\n";
            output+= "Courses:" + user.getKdamCoursesList().toString() ;
        }
        return output;
    }

}

