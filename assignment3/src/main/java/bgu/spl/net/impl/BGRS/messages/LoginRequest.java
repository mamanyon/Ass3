package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsernamePassword;

public class LoginRequest extends MessageUsernamePassword {

    public  LoginRequest(String user, String pass){
        super(user,pass);
        opcode = 3;
    }
    @Override
    public String execute() {
        if(DB.Login(userName,pass))
            return new Acknowledgement(opcode).execute();
        else
            return new Error(opcode).execute();
    }
}
