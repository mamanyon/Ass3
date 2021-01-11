package bgu.spl.net.impl.BGRS.messages.BasicMessage;

import bgu.spl.net.impl.BGRS.Database;

public abstract class MessageUsernameCourseumber {
    protected String userName;
    protected int courseNumber;
    protected int opcode;
    protected Database DB = Database.getInstance();

    public MessageUsernameCourseumber(String username , int  courseNumber){
        this.userName=username;
        this.courseNumber=courseNumber;
    }
    public abstract String execute();
}
