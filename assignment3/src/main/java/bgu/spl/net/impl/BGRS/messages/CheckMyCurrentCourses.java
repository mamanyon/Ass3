package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsername;

public class CheckMyCurrentCourses extends MessageUsername {

    public CheckMyCurrentCourses(String username) {
        super(username);
        this.opcode = 11;
    }

    public String execute() {
        return new Acknowledgement(opcode, DB.CheckMyCurrentCourses(userName)).execute();
    }
}
