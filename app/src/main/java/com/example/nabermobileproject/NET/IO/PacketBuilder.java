package com.example.nabermobileproject.NET.IO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class PacketBuilder {
    private final ByteArrayOutputStream byteArrayOutputStream;

    public PacketBuilder() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    private byte[] reverseBytes(byte[] bytes) {
        byte[] reversed = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversed[i] = bytes[bytes.length - 1 - i];
        }
        return reversed;
    }

    public void writeOpCode(byte opcode) {
        byteArrayOutputStream.write(opcode);
    }

    public void writeMessage(String msg) throws IOException {
        byte[] messageBytes = msg.getBytes(StandardCharsets.UTF_8);
        int messageLength = messageBytes.length;

        // Uzunluk baytlarını tersine çeviriyoruz (eğer sunucu küçük endian bekliyorsa)
        byte[] lengthBytes = ByteBuffer.allocate(4)
                .order(ByteOrder.BIG_ENDIAN) // Default Java ordering (big endian)
                .putInt(messageLength)
                .array();

        byteArrayOutputStream.write(reverseBytes(lengthBytes));
        byteArrayOutputStream.write(messageBytes);
    }

    public byte[] getPacketBytes() {
        return byteArrayOutputStream.toByteArray();
    }
}
