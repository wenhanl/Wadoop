package file;

import Debug.Debug;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by wenhanl on 14-11-11.
 */
public class FileManager {

    /**
     * Transfer local file to remote host
     * Connection reestablished every file transfer.
     * @param local
     * @param hostname
     * @param port
     */
    public static void transferFile(File local, String hostname, int port){
        SocketChannel fileChannel = null;
        try {
            fileChannel = SocketChannel.open();
            // Only for test
            fileChannel.connect(new InetSocketAddress(hostname, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RandomAccessFile accessFile = null;
        try {
            accessFile = new RandomAccessFile(local, "r");
            FileChannel channel = accessFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(channel.read(buffer) > 0){
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
            fileChannel.close();
            channel.close();
            accessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive a file from socket to a local file.
     * Remember to close socket outside!
     * @param localStore
     * @param sc
     */
    public static void receiveFile(String localStore, SocketChannel sc){
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile(localStore, "rw");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            FileChannel fileChannel = aFile.getChannel();
            while (sc.read(buffer) > 0) {
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
            fileChannel.close();
            aFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a header to the first line of file.
     * @param file
     * @param header
     */
    public static void addHeader(File file, String header){
        String tempPath = "/tmp/headerTemp";

        try {
            Scanner scanner = new Scanner(file);
            FileWriter writer = new FileWriter(tempPath);

            // Write in header first
            writer.write(header);

            while(scanner.hasNextLine()){
                writer.write(scanner.nextLine());
                writer.write("\n");

            }

            writer.flush();

            // Save old path and delete
            String oldPath = file.getAbsolutePath();
            file.delete();

            // Move added header temp to old path
            File temp = new File(tempPath);
            temp.renameTo(new File(oldPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Retrieve header of file
     * @param file
     * @return
     */
    public static String retrieveHeader(File file){
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String header = scanner.nextLine();
        scanner.close();
        return header;
    }

    /**
     * Remove first line of original file and move content to target
     *
     * Caution! First line will be moved!
     *
     * @param original
     * @param target
     */
    public static void mv(File original, String target){
        try {
            Scanner scanner = new Scanner(original);
            FileWriter writer = new FileWriter(target);


            // Read first line for nothing
            scanner.nextLine();

            while(scanner.hasNextLine()){
                writer.write(scanner.nextLine());
                writer.write("\n");
            }

            scanner.close();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
}

    /**
     * Create a directory if not exist
     * @param dirPath
     * @return
     */
    public static File createDir(String dirPath){
        File tempDir = new File(dirPath);
        if(!tempDir.exists()){
            if (tempDir.mkdir()) {
                System.out.println("Directory " + tempDir.getAbsolutePath() + "is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        return tempDir;
    }
}
