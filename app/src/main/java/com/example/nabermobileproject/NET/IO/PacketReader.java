package com.example.nabermobileproject.NET.IO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class PacketReader {
    private InputStream inputStream;

    public PacketReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private void readFully(byte[] buffer) throws IOException {
        int bytesRead = 0;
        while (bytesRead < buffer.length) {
            int result = inputStream.read(buffer, bytesRead, buffer.length - bytesRead);
            if (result == -1) {
                throw new IOException("End of stream reached before reading fully");
            }
            bytesRead += result;
        }
    }

    public byte readByte() throws IOException {
        int result = inputStream.read();
        if (result == -1) {  // EOF kontrolü
            throw new IOException("End of stream reached");
        }
        return (byte) result;
    }

    private byte[] reverseByteArray(byte[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            byte temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }

    private int readInt() throws IOException {
        byte[] intBytes = new byte[4];
        readFully(intBytes);
        intBytes = reverseByteArray(intBytes);  // Bayt sıralaması kontrolü
        return ByteBuffer.wrap(intBytes).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public String readMessage() {
        try {
            int length = readInt();  // Paket uzunluğu kontrolü
            byte[] msgBuffer = new byte[length];
            readFully(msgBuffer);  // Paketi tamamen okuyun
            return new String(msgBuffer, StandardCharsets.UTF_8);  // Mesajı oluşturun
        } catch (IOException e) {
            e.printStackTrace();  // Hata yönetimi için
            return null;  // Null döndürün veya uygun bir hata işleme stratejisi kullanın
        }
    }
}