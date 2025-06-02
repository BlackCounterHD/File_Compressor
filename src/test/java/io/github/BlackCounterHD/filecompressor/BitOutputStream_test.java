package io.github.BlackCounterHD.filecompressor;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitOutputStream_test {

    @Test
    void write_individual_bits() throws Exception {
        ByteArrayOutputStream bout1 = new ByteArrayOutputStream();
        try (BitOutputStream bout2 = new BitOutputStream(bout1)) {
            bout2.write_Bit(1);
            bout2.write_Bit(0);
            bout2.write_Bit(1);
            bout2.write_Bit(1);
        }
        byte[] data = bout1.toByteArray();
        assertEquals(1, data.length);
        assertEquals((byte) 0xB0, data[0]);
    }

    @Test
    void writeBitsMethod() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bout = new BitOutputStream(baos)) {
            // write the 8-bit pattern 0xA5
            bout.write_Bits(0xA5, 8);
        }
        byte[] data = baos.toByteArray();
        assertArrayEquals(new byte[]{(byte) 0xA5}, data);
    }

    @Test
    void writeBitsMethod2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bout = new BitOutputStream(baos)) {
            // write the 8-bit pattern 0xA5
            bout.write_Bits(0xFFFFFFFF, 32);
        }
        byte[] data = baos.toByteArray();
        System.out.print(data.length);
    }
    @Test
    void writeBitsMethod3() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bout = new BitOutputStream(baos)) {
            // write the 8-bit pattern 0xA5
            bout.write_Bits(0x7FFFFFFFFL, 40);
        }
        byte[] data = baos.toByteArray();
        System.out.print(data.length);
    }
}
