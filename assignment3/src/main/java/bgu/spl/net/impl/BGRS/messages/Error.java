package bgu.spl.net.impl.BGRS.messages;

public class Error {
    private int opCode;


    public Error(int opCode){
        this.opCode = opCode;
    }


    public String execute(){
        return  "13" + " " + opCode+ " ";
    }
}
