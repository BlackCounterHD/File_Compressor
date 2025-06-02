package io.github.BlackCounterHD.filecompressor;

interface huff_node{
    boolean isLeaf();
    public int compareTo(HuffmanNode that);
}
public class HuffmanNode implements Comparable<HuffmanNode>,huff_node {
    byte value; //we ll go each charac in the file and keep it as a byte value;
    int freq; // the nr of aparitions of each char
    HuffmanNode left, right;

    HuffmanNode(byte ch, int freq, HuffmanNode left, HuffmanNode right) {
        this.value = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isLeaf() {
        return (left == null) && (right == null);
    }

    @Override
    public int compareTo(HuffmanNode that) {
        return this.freq - that.freq;
    }
}
