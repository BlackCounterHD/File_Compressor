package io.github.BlackCounterHD.filecompressor;

import java.io.IOException;
import java.io.InputStream;
import java.util.PriorityQueue;

interface encoder_interface{
    public int[] countFreq(InputStream inputStream) throws IOException;
    public HuffmanNode buildTree(int freq[]);
    public String[] buildCodeTable(HuffmanNode root);
    public void writeHeader(BitOutputStream out, long originalLength) throws IOException;
    public void encodeData(InputStream in, BitOutputStream out, String[] codes) throws IOException;
}
public class HuffmanEncoder implements encoder_interface{
    private static int R=256;
    private int[] freq;
    private HuffmanNode root;
    private String[] codes;

    public HuffmanEncoder(InputStream in) throws IOException {
        this.freq=countFreq(in);
    }

    public HuffmanEncoder(){
    }
    //use inputstream-byte‐oriented I/O. \ read() returns an int;
    @Override
    public int[] countFreq(InputStream inputStream) throws IOException{
        int[] freq=new int[R];
        int b;
        while((b=inputStream.read())!=-1){
            freq[b]++;
        }
        return freq;
    }
    @Override
    public HuffmanNode buildTree(int[] freq){
        PriorityQueue<HuffmanNode> treeq=new PriorityQueue<>();
        for(int i=0;i<freq.length;i++){
            if(freq[i]>0){
                treeq.add(new HuffmanNode((byte)i,freq[i],null,null));
            }
        }
        while(treeq.size()>1){
            HuffmanNode left=treeq.remove();
            HuffmanNode right=treeq.remove();
            HuffmanNode parent=new HuffmanNode((byte)0,left.freq+right.freq,left,right);
            treeq.add(parent);
        }
        return treeq.remove();
    }
    @Override
    public String[] buildCodeTable(HuffmanNode root){
        String[] codes = new String[R];
        buildCodes(root, "", codes);
        return codes;
    }
    private void buildCodes(HuffmanNode root,String prefix,String[] codes){
        if (root.isLeaf()) {
            codes[root.value & 0xFF] = prefix;
        } else {
            buildCodes(root.left,  prefix + '0', codes);
            buildCodes(root.right, prefix + '1', codes);
        }
    }
    @Override
    public void writeHeader(BitOutputStream out, long originalLength) throws IOException {
        // 1) original file length
        out.write_Bits(originalLength, 64);
        // 2) frequency table
        for (int f : freq) {
            out.write_Bits(f, 32);
        }
    }
    @Override
    public void encodeData(InputStream in, BitOutputStream out, String[] codes) throws IOException {
        int b;
        while ((b = in.read()) != -1) {
            String code = codes[b & 0xFF];
            for (char bit : code.toCharArray()) {
                out.write_Bit(bit == '1' ? 1 : 0);
            }
        }
    }



}
