package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsernameCourseumber;

public class CheckKdamCourse extends MessageUsernameCourseumber {

    public CheckKdamCourse(String username, int courseName) {
        super(username, courseName);
        opcode = 6;
    }

    @Override
    public String execute() {
        String output = DB.kdamCheck(userName, courseNumber);

        if(output != null)
            return new Acknowledgement(opcode, output).execute();
        else
            return new Error(opcode).execute();

    }
}
