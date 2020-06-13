package com.example.tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Decrypt {

    public static void main(String[] args) {
        String filePath = "/Users/maxy/desktop/temp/fb_1571661723913";
        String outfilePath = filePath + "_decrypt";
        try {
            FileInputStream fin = new FileInputStream(new File(filePath));
            FileOutputStream fos = new FileOutputStream(new File(outfilePath));
            decryptFile(fin, new File(filePath).length(), fos);
            fin.close();
            fos.close();
            System.out.println("========================completed!=================");
        } catch (Exception e) {}
    }
    
    private static void decryptFile(InputStream in, long length, OutputStream os) {
        byte buffer[] = new byte[64 * 1024];
        long completed = 0;
        while (completed < length) {
            if (Thread.currentThread().isInterrupted())
                break;

            int bytesRead = 0;
            try {
                while(bytesRead < buffer.length && completed + bytesRead < length) {
                    int read = in.read(buffer, bytesRead, buffer.length - bytesRead);
                    if (read <= 0)
                        break;
                    bytesRead += read;
                    os.write(decrypt(buffer, bytesRead));
                    
                }
            } catch (IOException e) {
            }
            completed += bytesRead;

        }
    }
    
    private static byte[] decrypt(byte[] buffer, int length) {
        byte[] output = new byte[length];
        for (int i = 0; i < length; i++)
            output[i] = (byte)(buffer[i]-1);
        return output;
    }
}
