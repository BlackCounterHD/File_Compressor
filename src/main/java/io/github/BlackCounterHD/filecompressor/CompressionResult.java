package io.github.BlackCounterHD.filecompressor;

public class CompressionResult {
    private final HuffmanNode root;
    private final String[] codes;

    public CompressionResult(HuffmanNode root,String[] codes){
        this.root=root;
        this.codes=codes;
    }

    public HuffmanNode getRoot(){
        return root;
    }

    public String[] getCodes(){
        return codes.clone(); // to not change the codes after the return
    }
}
