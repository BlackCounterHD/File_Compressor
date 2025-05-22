package io.github.BlackCounterHD.filecompressor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class HuffmanEncoderTest_Count_Freq {
    private byte[] simpleData;
    private byte[] mixedData;

    @BeforeEach
    void setUp() {
        // simple: three 'A's
        simpleData = new byte[] { 'A', 'A', 'A' };

        // mixed: values 0,1,1,255,255,255
        mixedData = new byte[] { 0, 1, 1, (byte)255, (byte)255, (byte)255 };
    }

    @Test
    @DisplayName("Testing with letter 'A' array")
    void countFreq_singleByte() throws Exception {
        InputStream in = new ByteArrayInputStream(simpleData);
        HuffmanEncoder enc = new HuffmanEncoder(in);

        int[] freq = enc.countFreq(new ByteArrayInputStream(simpleData));

        assertEquals(3, freq['A'], "freq['A'] should be 3");

        for (int i = 0; i < freq.length; i++) {
            if (i != 'A') {
                int finalI = i;
                assertEquals(0, freq[i], () -> "freq[" + finalI + "] should be 0");
            }
        }
    }

    @Test
    void countFreq_variousBytes() throws Exception {
        InputStream in = new ByteArrayInputStream(mixedData);
        HuffmanEncoder enc = new HuffmanEncoder(in);
        int[] freq = enc.countFreq(new ByteArrayInputStream(mixedData));

        int[] expected = new int[256];
        expected[0]   = 1;
        expected[1]   = 2;
        expected[255] = 3;

        assertArrayEquals(expected, freq, "Frequency array should match expected counts");
    }

    @Test
    void countFreq_emptyStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        HuffmanEncoder enc = new HuffmanEncoder(in);
        int[] freq = enc.countFreq(new ByteArrayInputStream(new byte[0]));

        // all zeros
        for (int count : freq) {
            assertEquals(0, count);
        }
    }
}
