package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsername;

public class PrintStudentStatus extends MessageUsername {
    public PrintStudentStatus(String user) {
        super(user);
        opcode=8;
    }

    @Override
    public String execute() {
        String output = DB.StudentsStats(userName);
        if(output!=null)
            return new Acknowledgement(opcode,output).execute();
        else
            return new Error(opcode).execute();
    }
}
