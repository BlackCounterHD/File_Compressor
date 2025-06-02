package io.github.BlackCounterHD.filecompressor;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BitInputStream_Test {

    private BitInputStream make_input_stream(byte[] data){
        return new BitInputStream(new ByteArrayInputStream(data));
    }

    @Test
    public void func1() throws IOException{
        byte[] data=new byte[] { (byte) 0b10110011 };
        BitInputStream in=make_input_stream(data);

        assertEquals(1, in.readBit());
        assertEquals(0, in.readBit());
        assertEquals(1, in.readBit());
        assertEquals(1, in.readBit());
        assertEquals(0, in.readBit());
        assertEquals(0, in.readBit());
        assertEquals(1, in.readBit());
        assertEquals(1, in.readBit());

        assertThrows(EOFException.class, () -> in.readBit());
        in.close();
    }

    @Test
    public void func2() throws IOException {
        byte[] data={0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
        BitInputStream in=make_input_stream(data);

        long read_long=in.readBits(64);
        long expected_long=0x0102030405060708L;
        assertEquals(expected_long,read_long);

        assertThrows(EOFException.class, () -> in.readBits(1));
        in.close();
    }

    @Test
    public void func3() throws IOException{
        byte[] data = new byte[] { (byte) 0xAA, (byte) 0x55, (byte) 0xFF };
        BitInputStream in = make_input_stream(data);

        long first_four=in.readBits(4);
        assertEquals(0b1010,first_four);

        long secondFour = in.readBits(4);
        assertEquals(0b1010, secondFour);

        long twelveBits = in.readBits(12);
        assertEquals(0b010101011111, twelveBits);

        long last_bits=in.readBits(4);
        assertEquals(0b1111, last_bits);

        assertThrows(EOFException.class, () -> in.readBits(1));
        in.close();
    }
}
