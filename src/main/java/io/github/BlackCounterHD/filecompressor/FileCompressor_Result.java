package io.github.BlackCounterHD.filecompressor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileCompressor_Result {

    public static void compress(Path Input_Path,Path Output_Path) throws IOException{

        byte[] data=Files.readAllBytes(Input_Path);
        long Original_Length= data.length;

        HuffmanEncoder encoder = new HuffmanEncoder(new ByteArrayInputStream(data));

        int[] freq=encoder.countFreq(new ByteArrayInputStream(data));
        HuffmanNode root=encoder.buildTree(freq);
        String[] codes=encoder.buildCodeTable(root);
        try (BitOutputStream bout = new BitOutputStream(
                Files.newOutputStream(Output_Path))) {

            // 4) Write header: length + freq table
            encoder.writeHeader(bout, Original_Length);

            // 5) Write payload bits
            encoder.encodeData(
                    new ByteArrayInputStream(data),
                    bout,
                    codes
            );
            // bout.close() will flush partial byte
        }

    }
    /*public static void decompress(Path inPath, Path outPath) throws IOException {
        try (BitInputStream bin = new BitInputStream(
                 Files.newInputStream(inPath));
             OutputStream out = Files.newOutputStream(outPath)) {

            // 1) Read original length
            long originalLength = HuffmanDecoder.readOriginalLength(bin);
            // 2) Read freq table
            int[] freq = HuffmanDecoder.readFreqTable(bin);
            // 3) Rebuild tree
            HuffmanNode root = new HuffmanEncoder(null).buildTree(freq);
            // 4) Decode payload
            HuffmanDecoder.decode(bin, out, root, originalLength); //originalLenght-trebe long
        }
    }
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java -jar compressor.jar -c|-d <in> <out>");
            return;
        }
        switch (args[0]) {
            case "-c":
                compress(Path.of(args[1]), Path.of(args[2]));
                break;
            case "-d":
                decompress(Path.of(args[1]), Path.of(args[2]));
                break;
            default:
                System.out.println("Unknown mode: " + args[0]);
        }
    }*/
}
