package io.github.BlackCounterHD.filecompressor;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

interface decoder_interface{
    public long readOriginalLength(BitInputStream bin) throws IOException;
    public int[] readFreqTable(BitInputStream bin) throws IOException;
    public void decode(BitInputStream bin,OutputStream out,HuffmanNode root,long originalLength) throws IOException;
}
public class HuffmanDecoder implements decoder_interface {

    public HuffmanDecoder() { }

    @Override
    public long readOriginalLength(BitInputStream bin) throws IOException {
        return bin.readBits(64);
    }

    @Override
    public int[] readFreqTable(BitInputStream bin) throws IOException{
        int[] freq=new int[256];
        for(int i=0;i<256;i++){
            freq[i]=(int)bin.readBits(32);
        }
        return freq;
    }


    @Override
    public void decode(BitInputStream bin,
                       OutputStream out,
                       HuffmanNode root,
                       long originalLength) throws IOException{
        if(originalLength==0){
            return;
        }
        if(root.isLeaf()){
            byte value=root.value;
            for(long i=0;i<originalLength;i++){
                out.write(value & 0xFF);
            }
            return;
        }
        long written=0;
        HuffmanNode node = root;
        while(written<originalLength){
            int bit;
            try {
                bit = bin.readBit();
            } catch (EOFException eof) {
                break;
            }
            if(bit==0){
                node=node.left;
            }
            else {
                node=node.right;
            }
            if(node.isLeaf()){
                written++;
                out.write(node.value & 0xFF);
                node=root;
            }
        }
    }
}
