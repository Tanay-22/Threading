# High-Performance Word Processing System

## Overview
This project is a high-performance, multi-threaded Java application designed to generate and analyze 100 million random words efficiently. It leverages multi-threading, file handling, and optimized data structures to enhance performance and reduce processing time significantly.

## Features
- **Random Word Generator**: Generates 100 million random words and writes them to a file.
- **Multi-Threaded Analyzer**: Uses Java's `FileChannel`, `ByteBuffer`, and a thread pool for concurrent file processing.
- **Performance Optimization**: Achieves a **3.12× speedup**, reducing processing time from **4977ms** (single-threaded) to **1596ms** (multi-threaded).

## Technologies Used
- **Java** (Multi-threading, File Handling)
- **Concurrent Data Structures** (`ConcurrentHashMap`, `LongAdder`)
- **NIO (New Input/Output)** (`FileChannel`, `ByteBuffer`)

## Usage
### Prerequisites
- JDK 11 or later

### Running the Generator
```sh
javac generator/RandomWordGenerator.java
java generator.RandomWordGenerator
```
This will generate `random_words.txt` containing 100 million random words.

### Running the Single-Threaded Analyzer
```sh
javac SingleThread.java
java SingleThread
```
This processes the file in a single thread.

### Running the Multi-Threaded Analyzer
```sh
javac MultiThread.java
java MultiThread
```
This processes the file using multiple threads for improved performance.

## Performance Comparison
| Method              | Time Taken  |
|---------------------|------------|
| Single-Threaded    | 4977ms      |
| Multi-Threaded     | 1596ms      |
| **Speedup**        | **3.12×**   |

## Repository
[GitHub Repository](https://github.com/Tanay-22/High-Performance-Word-Processing)

## Author
[**Tanay Pandey**](https://github.com/Tanay-22/)
