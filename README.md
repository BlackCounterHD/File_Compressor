# Java FileCompressor (Huffman Coding)

## Overview

This project implements a simple **file compression/decompression** utility using **static Huffman coding** in Java. It is designed to handle larger files (over 2 GB) by using **streamed I/O with buffered streams** rather than loading entire files into memory.

- **Compression**: Scans the input stream twice to build a Huffman tree based on symbol frequencies and then writes a header (original length + frequency table) followed by the compressed bit‐stream.
- **Decompression**: Reads the header from the compressed file, rebuilds the same Huffman tree, and reconstructs the original file byte‐by‐byte in a streaming fashion.

## Features

- **Handles arbitrarily large files** (no `readAllBytes` or large in‐RAM buffers).
- **Two-pass static Huffman** compressor:
    1. Count byte frequencies (first pass).
    2. Build the Huffman tree and write compressed bits (second pass).
- **Buffered I/O** (`BufferedInputStream` / `BufferedOutputStream`), plus custom `BitInputStream` and `BitOutputStream` for bit‐level reads/writes.
- **Pure Java** (no third-party libraries). Compatible with Java 8+.
- **Command-line interface** with modes `-c` (compress) and `-d` (decompress).

## Prerequisites

- **JDK 8** or later installed.
- A shell/terminal window (Windows CMD, PowerShell, Linux/macOS Terminal, etc.).
- (Optional) `jar` tool if you want to package into a runnable JAR.



