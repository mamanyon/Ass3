package bgu.spl.net.impl.BGRS;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRS.messages.*;
import bgu.spl.net.impl.BGRS.messages.Error;

public class MessagingProtocolImpl<T> implements MessagingProtocol<String> {
    private boolean shouldTerminate = false;
    //when given logout - shouldterminate = true;  - > - like "bye" ..
    private String username;

    @Override
    public String process(String msg) {
        System.out.println("MESSAGE RECIEVED ---->" + msg);
        String op = msg.substring(0,2);
        int opCode;
        if(op.charAt(1)==' ')
            opCode = Integer.parseInt(msg.substring(0,1));
        else
            opCode=Integer.parseInt(msg.substring(0,2));
        System.out.println("opcode:" + opCode);
        Error ERR = new Error(opCode);
        String[] usernameAndPassword;

        switch (opCode) {
            case 1:
                if(!isThereClientLoggedIn()) {
                    usernameAndPassword = getUserNameOrPassword(msg);
                    AdminRegister admin = new AdminRegister(usernameAndPassword[1], usernameAndPassword[2]);
                    return admin.execute();
                }
                return ERR.execute();
            case 2:
                if(!isThereClientLoggedIn()) {
                    usernameAndPassword = getUserNameOrPassword(msg);
                    StudentRegister student = new StudentRegister(usernameAndPassword[1], usernameAndPassword[2]);
                    return student.execute();
                }
                return ERR.execute();
            case 3:
                if(!isThereClientLoggedIn()) {
                    usernameAndPassword = getUserNameOrPassword(msg);
                    LoginRequest loginToSystem = new LoginRequest(usernameAndPassword[1], usernameAndPassword[2]);
                    String isLoggedIn = loginToSystem.execute();
                    this.username = isLoggedIn.equals("12 3 ") ? usernameAndPassword[1] : null;     //<---is there a better condition?
                    return isLoggedIn;
                }
                return ERR.execute();
            case 4:
                if(isThereClientLoggedIn()) {
                    String output = new LogoutRequest(username).execute();
                    if(output.equals("12 4 ")) {
                        username = null;
                        return output;
                    }
                }
                return ERR.execute();
            case 5:
                if(isThereClientLoggedIn())
                    return new RegisterToCourse(username, getCourseNumber(msg)).execute();

                return ERR.execute();
            case 6:
                if(isThereClientLoggedIn())
                    return new CheckKdamCourse(username, getCourseNumber(msg)).execute();

                return ERR.execute();
            case 7:
                if(isThereClientLoggedIn())
                    return new PrintCourseStatus(username,getCourseNumber(msg)).execute();

                return ERR.execute();
            case 8:
                if(isThereClientLoggedIn())
                    return new PrintStudentStatus(username).execute();

                return ERR.execute();
            case 9:
                if(isThereClientLoggedIn())
                    return new CheckIfRegistered(username, getCourseNumber(msg)).execute();

                return ERR.execute();
            case 10:
                if(isThereClientLoggedIn())
                    return new UnregisterToCourse(username, getCourseNumber(msg)).execute();

                return ERR.execute();
            case 11:
                if(isThereClientLoggedIn())
                    return new CheckMyCurrentCourses(username).execute();

                return ERR.execute();
        }
        return null;
    }

    //Return the course number out of the message
    private int getCourseNumber(String msg){
        String[] getCnum=msg.split(" ");
        return Integer.parseInt(getCnum[1]);
    }

    private boolean isThereClientLoggedIn() {
        return username != null;
    }
    //Return an array of username and password that client sent to the server
    private String[] getUserNameOrPassword(String msg){
        return msg.split(" ");
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
