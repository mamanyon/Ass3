package bgu.spl.net.impl.BGRS.messages.BasicMessage;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.BGRS.Database;

public abstract  class MessageUsernamePassword {
    protected String userName;
    protected String pass;
    protected int opcode;
    protected Database DB=Database.getInstance();
    public MessageUsernamePassword(String username , String password){
        this.userName=username;
        this.pass=password;
    }
    public abstract String execute();
}
