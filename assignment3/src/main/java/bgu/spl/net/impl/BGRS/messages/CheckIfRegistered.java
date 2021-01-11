package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsernameCourseumber;

public class CheckIfRegistered extends MessageUsernameCourseumber {

    public CheckIfRegistered(String username, int courseNumber) {
        super(username, courseNumber);
        opcode = 9;
    }

    @Override
    public String execute() {
        String isRegisteredOutput = DB.isRegistered(userName, courseNumber);
         if(isRegisteredOutput.equals("ERR"))
             return new Error(opcode).execute();
         else
             return new Acknowledgement(opcode, isRegisteredOutput).execute();
    }
}
