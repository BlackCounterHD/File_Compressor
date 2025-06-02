package io.github.BlackCounterHD.filecompressor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileCompressor_Result {

    public static void compress(Path Input_Path,Path Output_Path) throws IOException{

        byte[] data=Files.readAllBytes(Input_Path);
        int Original_Length= data.length;

        HuffmanEncoder encoder = new HuffmanEncoder(new ByteArrayInputStream(data));

        int[] freq=encoder.countFreq(new ByteArrayInputStream(data));
        HuffmanNode root=encoder.buildTree(freq);
        String[] codes=encoder.buildCodeTable(root);
        try (BitOutputStream bout = new BitOutputStream(
                Files.newOutputStream(Output_Path))) {

            encoder.writeHeader(bout, Original_Length);

            encoder.encodeData(new ByteArrayInputStream(data), bout, codes);

        }

    }
    public static void decompress(Path inPath, Path outPath) throws IOException {
        try (BitInputStream bin = new BitInputStream(Files.newInputStream(inPath));
             OutputStream out = Files.newOutputStream(outPath)) {

            HuffmanDecoder decoder=new HuffmanDecoder();

            long originalLength = decoder.readOriginalLength(bin);

            int[] freq = decoder.readFreqTable(bin);

            HuffmanEncoder encoder = new HuffmanEncoder();
            HuffmanNode root = encoder.buildTree(freq);

            decoder.decode(bin, out, root, originalLength);
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
    }
}
