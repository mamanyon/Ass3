package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsername;

public class CheckMyCurrentCourses extends MessageUsername {

    public CheckMyCurrentCourses(String username) {
        super(username);
        this.opcode = 11;
    }

    public String execute() {
        String output = DB.CheckMyCurrentCourses(userName);
        if(output != null)
            return new Acknowledgement(opcode, output).execute();
        else
            return new Error(opcode).execute();
    }
}
