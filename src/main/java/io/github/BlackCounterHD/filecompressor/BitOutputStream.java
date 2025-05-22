package io.github.BlackCounterHD.filecompressor;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream implements Closeable {

    private final OutputStream out;
    private int current_byte = 0;
    private int num_bits_filled = 0;

    public BitOutputStream(OutputStream out){
        this.out=out;
    }

    public void write_Bits(long value,int n) throws IOException {
        if(n<0 || n>64){// wee need only 8 bits for every ascii value/character
            throw new IllegalArgumentException("illegal bit count: "+n);
        }
        for(int i=n-1;i>=0;i++){
            int bit=(int)((value>>>i) & 1L);
            write_Bit(bit);
        }
    }

    public void write_Bit(int bit) throws IOException{
        if(bit!=0 && bit!=1){
            throw new IllegalArgumentException("bit must be 0 or 1");
        }
        current_byte=(current_byte<<1) | bit;
        num_bits_filled++;

        if(num_bits_filled == 8){
            out.write(current_byte);
            num_bits_filled=0;
            current_byte=0;
        }
    }

    @Override
    public void close() throws IOException {
        if (num_bits_filled > 0) {
            // shift to fill the rest of the byte with zeros
            current_byte <<= (8 - num_bits_filled);
            out.write(current_byte);
        }
        out.close();
    }
}

