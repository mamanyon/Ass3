package bgu.spl.net.impl.BGRS;

import bgu.spl.net.api.MessageEncoderDecoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImpl<T> implements MessageEncoderDecoder<String> {

    private byte[] bytes = new byte[1 << 10]; //start with 1k
    private int len = 0;
    private int numOfZeros = 0;
    private short opCode = 0;


    @Override
    public String decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if (nextByte == '\0')
            numOfZeros++;
        if (len == 2) {
            opCode = bytesToShort(bytes);
            if (opCode == 4 | opCode == 11)
                return popString();
        }
        //between 1 to 3
        if (opCode >= 1 & opCode <= 3 & numOfZeros == 3)
            return popString();
            //between 5 to 10 (not include 8)
        else if ((opCode >= 5 & opCode <= 10 & opCode != 8 & len == 4)) {
            System.out.println(Arrays.toString(bytes));
            return popString();
        } else if (opCode == 8 & numOfZeros == 2)
            return popString();

        return null;
    }


    private String popString() {
        String result;

        if (opCode >= 5 & opCode <= 10) {
            byte[] bytesNum = new byte[2];
            bytesNum[0] = bytes[2];
            bytesNum[1] = bytes[3];
            short num = bytesToShort(bytesNum);
            result = String.valueOf(opCode) + " " + num;
        } else {
            String messageString = new String(bytes, 2, len, StandardCharsets.UTF_8);
            String[] splitMessage = messageString.split("\0");
            result = String.valueOf(opCode) + " ";
            for (String curr : splitMessage)
                result += curr + " ";
        }

        len = 0;
        opCode = 0;
        numOfZeros = 0;

        return result;
    }

    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    @Override
    public byte[] encode(String message) {
        System.out.println( message + " _______________"+ message );
        String[] splitMessage = new String[3];
        splitMessage[0] = message.substring(0, 2); //ack or error
        short ackError = Short.parseShort(splitMessage[0]);
        int index = 3;
        String a;
        while (message.charAt(index) != ' ')
            index++;
        if (index == 4) {
            a = message.substring(3, 4);
            splitMessage[2] = message.substring(5);
        } else//for 11 messageopcode
        {
            a = message.substring(3, 5);
            splitMessage[2] = message.substring(6);
        }
        splitMessage[1] = a;

        System.out.println(splitMessage[0] + "----" + splitMessage[1] + "-------" + splitMessage[2]);

        //String[] splitMessage = message.split(" ");// split between spaces

        byte[] ackOrError = shortToBytes(ackError);

        short MessageOpCode = Short.parseShort(splitMessage[1]);
        byte[] ByteOpCodeMessage = shortToBytes(MessageOpCode);
        byte[] optional = null;
        if (ackError == 12) {//ack
            if (MessageOpCode == 6 | MessageOpCode == 7 | MessageOpCode == 8 | MessageOpCode == 9 | MessageOpCode == 11) {
                System.out.println("dabun ya sharmutaaaaa - ze baror ?? :" + splitMessage[2]);
                optional = (splitMessage[2] + "\0").getBytes(StandardCharsets.UTF_8);
                System.out.println("optional1 -->  " + Arrays.toString(optional));
            }
        }

        //append 4 bytes
        byte[] messageByte = new byte[ackOrError.length + ByteOpCodeMessage.length];
        System.arraycopy(ackOrError, 0, messageByte, 0, ackOrError.length);
        System.arraycopy(ByteOpCodeMessage, 0, messageByte, ackOrError.length, ByteOpCodeMessage.length);


        if (optional != null) {
            byte[] result = new byte[messageByte.length + optional.length];
            System.arraycopy(messageByte, 0, result, 0, messageByte.length);
            System.arraycopy(optional, 0, result, messageByte.length, optional.length);
            System.out.println("result -->  " + Arrays.toString(result));

            return result;
        }
        return messageByte;
    }
}
