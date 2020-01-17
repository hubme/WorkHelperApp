package com.example.nio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * see: https://tech.meituan.com/nio.html
 * https://www.ibm.com/developerworks/cn/education/java/j-nio/j-nio.html
 *
 * @author VanceKing
 * @since 2018/4/23.
 */

class Sample1 {
    public static void main(String[] args) throws Exception {
        writeNIo("aaaabbbb");
    }

    private static void test1() throws Exception {
        RandomAccessFile file = new RandomAccessFile("g://aaa.txt", "rw");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        int readBytes = channel.read(buffer);
        while (readBytes != -1) {
            System.out.println(readBytes);
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.println((char) buffer.get());
            }
            buffer.clear();
            readBytes = channel.read(buffer);
        }
        file.close();
    }

    private static void testCopyFile() throws Exception {
        File inputFile = new File("g://readme.txt");
        File outputFile = new File("g://readme_backup.txt");

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);

        FileChannel inChannel = inputStream.getChannel();
        FileChannel outChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();
            int readCount = inChannel.read(buffer);
            if (readCount == -1) {
                break;
            }
            buffer.flip();
            outChannel.write(buffer);
        }
        outputStream.close();
        inputStream.close();
    }

    private static void testWriteSomeBytes() throws Exception {
        final byte message[] = {83, 111, 109, 101, 32, 98, 121, 116, 101, 115, 46};

        FileOutputStream fout = new FileOutputStream("writesomebytes.txt");
        FileChannel fc = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(128);
        for (int i = 0; i < message.length; ++i) {
            buffer.put(message[i]);
        }

        buffer.flip();
        fc.write(buffer);
        fout.close();
    }

    private static void testReadAndShow() throws Exception {
        FileInputStream fin = new FileInputStream("writesomebytes.txt");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(128);
        fc.read(buffer);
        buffer.flip();

        int i = 0;
        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.println("Character " + i + ": " + ((char) b));
            i++;
        }

        fin.close();
    }

    private static void writeNIo(String content) {
        Path path = Paths.get("c://bbb.txt");
        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))){
            writer.write(content);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
