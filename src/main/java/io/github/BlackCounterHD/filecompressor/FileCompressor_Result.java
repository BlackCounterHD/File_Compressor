package io.github.BlackCounterHD.filecompressor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileCompressor_Result {

    public static void compress(Path Input_Path,Path Output_Path) throws IOException{

        HuffmanEncoder encoder;
        int[] freq;
        try (InputStream in = new BufferedInputStream(Files.newInputStream(Input_Path))) {
            encoder = new HuffmanEncoder(in);
        }

        try (InputStream in = new BufferedInputStream(Files.newInputStream(Input_Path))) {
            freq = encoder.countFreq(in);
        }


        HuffmanNode root = encoder.buildTree(freq);
        String[] codes = encoder.buildCodeTable(root);

        long Original_Length = Files.size(Input_Path);

        try (InputStream in = new BufferedInputStream(Files.newInputStream(Input_Path));
                BitOutputStream bout = new BitOutputStream(new BufferedOutputStream(Files.newOutputStream(Output_Path)))
        ) {
            encoder.writeHeader(bout, Original_Length);
            encoder.encodeData(in, bout, codes);
        }

    }
    public static void decompress(Path inPath, Path outPath) throws IOException {
        try (BitInputStream bin = new BitInputStream(new BufferedInputStream(Files.newInputStream(inPath)));
                OutputStream out = new BufferedOutputStream(Files.newOutputStream(outPath))
        ) {
            HuffmanDecoder decoder = new HuffmanDecoder();

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
