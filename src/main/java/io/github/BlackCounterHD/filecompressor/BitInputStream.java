package io.github.BlackCounterHD.filecompressor;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

interface bit_input{
    public int readBit() throws IOException;
    public long readBits(int n) throws IOException;
}
public class BitInputStream implements Closeable,bit_input {

    private final InputStream in;
    private int currentByte = 0;
    private int numBitsRemaining = 0;

    public BitInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int readBit() throws IOException {
        if (numBitsRemaining == 0) {
            currentByte = in.read();
            if (currentByte == -1) {
                throw new EOFException("No more bytes available when trying to read a bit.");
            }
            numBitsRemaining = 8;
        }
        numBitsRemaining--;
        return (currentByte >>> numBitsRemaining) & 1;
    }


    @Override
    public long readBits(int n) throws IOException {
        if (n < 0 || n > 64) {
            throw new IllegalArgumentException("Can only read between 0 and 64 bits at a time.");
        }
        long result = 0L;
        for (int i = 0; i < n; i++) {
            int bit = readBit();
            result = (result << 1) | bit;
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
