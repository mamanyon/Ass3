package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsername;

public class LogoutRequest extends MessageUsername {

    public LogoutRequest(String user){
        super(user);
        opcode = 4;
    }

    @Override
    public String execute() {

         if(DB.Logout(userName))
             return new Acknowledgement(opcode).execute();
         else
             return new Error(opcode).execute();
    }
}
