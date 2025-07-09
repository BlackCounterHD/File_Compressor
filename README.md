# Java FileCompressor (Huffman Coding)

## Overview

A Java-based file compression and decompression tool using **static Huffman coding**. This tool accepts any binary file, compresses it using Huffman encoding, and restores it to its original form through decompression.

## Features

- Supports compression and decompression of any file type (except files with a single character exceeding Integer.MAX_VALUE in frequency)
- **Three-pass static Huffman** compressor:
  1. **Count byte frequencies**.
  2. **Build the Huffman tree and write the header** (the original file-size and the frequencies of each character).
  3. **Encode the Data** : Re-read the input file again and Use the Huffman tree to encode each symbol into bits and write the compressed data.
- **Buffered I/O** (`BufferedInputStream` / `BufferedOutputStream`), plus custom `BitInputStream` and `BitOutputStream` for bit‐level reads/writes.
- **Pure Java** (no third-party libraries). Compatible with Java 8+.
- **Command-line interface** with modes `-c` (compress) and `-d` (decompress).

## Usage

### Clone the Repository

First, clone this repository using Git:
```bash
git clone https://github.com/BlackCounterHD/File_Compressor.git
cd File_Compressor
```
### Run with JAR
To use the precompiled `.jar`:

```bash
java -jar out/artifacts/File_Compressor_jar/File_Compressor.jar -c src/resources/sample/input.txt src/resources/sample/output.huff
java -jar out/artifacts/File_Compressor_jar/File_Compressor.jar -d src/resources/sample/output.huff src/resources/sample/res.txt 
```

### Compile the Code

**Make sure you have** all `.java` files in the same directory. Then run:
```bash
javac *.java
```

### Compress a File

```bash
java FileCompressor_Result -c input.txt output.huff
```

### Decompress a File

```bash
java FileCompressor_Result -d output.huff restored.txt
```


## Prerequisites

- **JDK 8** or later installed.
- **JDK 23** or lajter for .jar runnable
- A shell/terminal window (Windows CMD, PowerShell, Linux/macOS Terminal, etc.).
- (Optional) `jar` tool if you want to package into a runnable JAR.

## File Format Structure

The compressed file consists of three parts:

**Header:**

- 64-bit long: original number of bytes in the input
- 256 × 32-bit integers: frequency table for byte values 0–255 (lets decoder rebuild the Huffman tree)

**Payload Bits**

- The actual compressed bit-stream, written via encodeData()

**Padding**

-  Up to 7 zero bits to fill the final byte 

