package bgu.spl.net.impl.BGRS.messages.BasicMessage;

import bgu.spl.net.impl.BGRS.Database;

public abstract class MessageUsername {
    protected String userName;
    protected Database DB=Database.getInstance();
    protected int opcode;
    public MessageUsername(String user){
        this.userName=user;
    }

    public abstract String execute();

}
