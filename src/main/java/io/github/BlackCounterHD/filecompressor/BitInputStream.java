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
    private int currentByte = 0;       // Holds the last byte fetched from 'in'
    private int numBitsRemaining = 0;  // How many bits are still unused in 'currentByte'

    public BitInputStream(InputStream in) {
        this.in = in;
    }

    /**
     * Reads a single bit from the stream. Returns 0 or 1.
     * If we have exhausted the bits in `currentByte`, we read the next byte from `in`.
     * @return 0 or 1, or throws EOFException if stream ends unexpectedly.
     * @throws IOException
     */
    @Override
    public int readBit() throws IOException {
        if (numBitsRemaining == 0) {
            // Refill the buffer by reading one byte
            currentByte = in.read();
            if (currentByte == -1) {
                throw new EOFException("No more bytes available when trying to read a bit.");
            }
            numBitsRemaining = 8;
        }
        // Take the next-most-significant bit of currentByte:
        numBitsRemaining--;
        return (currentByte >>> numBitsRemaining) & 1;
    }

    /**
     * Reads exactly `n` bits (1 ≤ n ≤ 64) and returns them as a long.
     * The first bit read becomes the highest-order bit of the result.
     * @param n number of bits to read (must be between 0 and 64)
     * @return a long in which the last n bits have been assembled from the stream
     * @throws IOException
     */
    @Override
    public long readBits(int n) throws IOException {
        if (n < 0 || n > 64) {
            throw new IllegalArgumentException("Can only read between 0 and 64 bits at a time.");
        }
        long result = 0L;
        for (int i = 0; i < n; i++) {
            int bit = readBit(); // may throw EOFException if no more bits
            result = (result << 1) | bit;
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
