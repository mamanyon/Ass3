package BGRSMain;

import bgu.spl.net.impl.BGRS.Database;
import bgu.spl.net.impl.BGRS.MessageEncoderDecoderImpl;
import bgu.spl.net.impl.BGRS.MessagingProtocolImpl;
import bgu.spl.net.srv.Reactor;
import bgu.spl.net.srv.Server;

public class ReactorMain {

    public static void main(String[] args) {
        Database DB = Database.getInstance();
        int numOfThread = Integer.parseInt("3");
        int port = Integer.parseInt("7777");

        Server reactor = new Reactor(numOfThread, port, () -> new MessagingProtocolImpl<>(), ()-> new MessageEncoderDecoderImpl());

        reactor.serve();
    }
}
