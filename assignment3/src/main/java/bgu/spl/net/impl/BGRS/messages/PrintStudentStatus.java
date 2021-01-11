package bgu.spl.net.impl.BGRS.messages;

import bgu.spl.net.impl.BGRS.messages.BasicMessage.MessageUsername;

public class PrintStudentStatus extends MessageUsername {
    String studentUsername;

    public PrintStudentStatus(String user,String studentUsername) {
        super(user);
        opcode = 8;
        this.studentUsername = studentUsername;
    }

    @Override
    public String execute() {
        String output = DB.StudentsStats(userName, studentUsername);
        if(output!=null)
            return new Acknowledgement(opcode,output).execute();
        else
            return new Error(opcode).execute();
    }
}
