package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsernameCourseumber;

public class PrintCourseStatus extends MessageUsernameCourseumber {
    public PrintCourseStatus(String username, int courseNumber) {
        super(username, courseNumber);
        opcode = 7;
    }

    @Override
    public String execute() {
        String output = DB.CourseStats(userName,courseNumber);
        if(output!=null)
            return new Acknowledgement(opcode,output).execute();
        else
            return new Error(opcode).execute();
    }
}
