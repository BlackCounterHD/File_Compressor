package io.github.BlackCounterHD.filecompressor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.input.EOFException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Huffman_Header_Test {

    @Test
    void writeHeader_example() throws IOException {
        byte[] data = new byte[]{ 'A','A','B','B','B' };
        HuffmanEncoder enc = new HuffmanEncoder(new ByteArrayInputStream(data));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bout = new BitOutputStream(baos)) {
            enc.writeHeader(bout, data.length);
        }

        try (DataInputStream dis = new DataInputStream(
                new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(5L, dis.readLong());

            for (int i = 0; i < 256; i++) {
                int f = dis.readInt();
                if (i == 'A')        assertEquals(2, f);
                else if (i == 'B')   assertEquals(3, f);
                else                 assertEquals(0, f);
            }

            assertEquals(-1, dis.read(), "no extra bytes after header");
        }
    }
    @Test
    void encode_data_example() throws IOException{
        byte[] data = new byte[]{ 'A','A','B','B','B' };
        HuffmanEncoder enc = new HuffmanEncoder(new ByteArrayInputStream(data));

        int[] freq = enc.countFreq(new ByteArrayInputStream(data));
        HuffmanNode root = enc.buildTree(freq);
        String[] codes = enc.buildCodeTable(root);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (BitOutputStream bout = new BitOutputStream(baos)) {
            enc.encodeData(new ByteArrayInputStream(data), bout, codes);
        }

        byte[] out = baos.toByteArray();

        assertEquals(1, out.length, "Should pack all into a single byte");
        assertEquals((byte)0x38, out[0], "Bits for AABBB should be 0,0,1,1,1 padded to 00111000");
    }
}
