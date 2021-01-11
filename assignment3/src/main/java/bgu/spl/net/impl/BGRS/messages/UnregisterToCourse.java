package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsernameCourseumber;

public class UnregisterToCourse extends MessageUsernameCourseumber {

    public UnregisterToCourse(String username, int courseNumber) {
        super(username, courseNumber);
        this.opcode = 10;
    }

    public String execute() {
        return DB.unregisterToCourse(userName, courseNumber) ?
                new Acknowledgement(opcode).execute(): new Error(opcode).execute();
    }
}
