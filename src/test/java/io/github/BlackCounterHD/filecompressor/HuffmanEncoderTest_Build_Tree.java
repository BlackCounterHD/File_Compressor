package io.github.BlackCounterHD.filecompressor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanEncoderTest_Build_Tree {

    byte[] simpleData;
    InputStream in;
    HuffmanEncoder enc;

    @BeforeEach
    public void setup() throws IOException {
        simpleData = new byte[] { 'A', 'A', 'A','B','B','B','B','C','C'};
        in = new ByteArrayInputStream(simpleData);
        enc = new HuffmanEncoder(in);
    }
    @Test
    public void buildTree() throws IOException {

        int[] freq = enc.countFreq(new ByteArrayInputStream(simpleData));

        HuffmanNode root=enc.buildTree(freq);

        assertEquals(9, root.freq);

        assertTrue(root.left.isLeaf());
        assertFalse(root.right.isLeaf());

        Set<Byte> symbols = Set.of(root.left.value, root.right.value);
        assertEquals(Set.of((byte)'B',(byte)0), symbols);
    }

    @Test
    public void check_codes() throws IOException {
        int[] freq = enc.countFreq(new ByteArrayInputStream(simpleData));

        HuffmanNode root=enc.buildTree(freq);

        String[] codes=enc.buildCodeTable(root);

        assertEquals("10",codes[67]);
    }
}
