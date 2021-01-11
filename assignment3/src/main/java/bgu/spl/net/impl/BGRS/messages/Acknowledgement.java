package bgu.spl.net.impl.BGRS.messages;

public class Acknowledgement {
    private int opCode;
    private String message;


    public Acknowledgement(int opCode){

        this.opCode = opCode;
        this.message = "";
    }

    public Acknowledgement(int opCode, String message){

        this.opCode = opCode;
        this.message = message;
    }

    public String execute(){
        return  "12" + " " + opCode + " " + message;
    }
}
