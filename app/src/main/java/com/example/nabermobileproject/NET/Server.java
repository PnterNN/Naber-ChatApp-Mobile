package com.example.nabermobileproject.NET;

import android.util.Log;

import com.example.nabermobileproject.NET.IO.PacketBuilder;
import com.example.nabermobileproject.NET.IO.PacketReader;
import com.example.nabermobileproject.services.DataService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server {
    private String host = "45.141.149.133";//45.141.149.133
    private int port = 9001;
    private Socket socket;
    private PacketReader packetReader;
    private OutputStream outputStream;
    public PacketReader getPacketReader() {
        return packetReader;
    }
    public Socket getSocket() {
        return socket;
    }
    public Consumer<Void> userConnectedEvent;
    public Consumer<Void> userRegisterConnectedEvent;
    public Consumer<Void> userDisconnectedEvent;
    public Consumer<Void> messageReceivedEvent;
    public Consumer<Void> voiceMessageReceivedEvent;


    public Consumer<Void> tweetReceivedEvent;
    public Consumer<Void> getTweetsEvent;
    public Consumer<Void> deleteTweetEvent;
    public Consumer<Void> likeEvent;

    public Consumer<Void> friendRequestEvent;
    public Consumer<Void> friendRequestCancelEvent;
    public Consumer<Void> friendRequestAcceptEvent;
    public Consumer<Void> friendRequestDeclineEvent;
    public Consumer<Void> friendRemoveEvent;
    public Consumer<Void> getFriendEvent;

    public Consumer<Void> groupCreatedEvent;

    public Consumer<Void> deleteMessageEvent;
    public Consumer<Void> micMutedEvent;
    public Consumer<Void> tooManyPacketsEvent;

    public Consumer<Void> loginCorrectEvent;
    public Consumer<Void> loginFailedEvent;

    public Consumer<Void> registerSuccessEvent;
    public Consumer<Void> registerFailEvent;
    public void login(String email, String password) throws IOException {
        if(socket==null){
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            packetReader = new PacketReader(socket.getInputStream());
            readPackets(packetReader);
        }

        PacketBuilder loginPacket = new PacketBuilder();
        loginPacket.writeOpCode((byte) 1);
        loginPacket.writeMessage(email);
        loginPacket.writeMessage(password);
        outputStream.write(loginPacket.getPacketBytes(), 0, loginPacket.getPacketBytes().length);
    }
    public void register(String username, String email, String password) throws IOException {
        if(socket==null){
            socket = new Socket(host, port);
            outputStream = socket.getOutputStream();
            packetReader = new PacketReader(socket.getInputStream());
            readPackets(packetReader);
        }
        PacketBuilder registerPacket = new PacketBuilder();
        registerPacket.writeOpCode((byte) 0);
        registerPacket.writeMessage(username);
        registerPacket.writeMessage(email);
        registerPacket.writeMessage(password);
        outputStream.write(registerPacket.getPacketBytes(), 0, registerPacket.getPacketBytes().length);
    }
    public void sendVoiceMessage(byte[] voiceMessage, String UID, String ContactUID) throws IOException {
        PacketBuilder voicePacket = new PacketBuilder();
        voicePacket.writeOpCode((byte) 22);
        voicePacket.writeMessage(new String(voiceMessage));
        voicePacket.writeMessage(UID);
        voicePacket.writeMessage(ContactUID);
        outputStream.write(voicePacket.getPacketBytes(), 0, voicePacket.getPacketBytes().length);
    }

    public void sendFriendRemove(String username) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 19);
        tweetPacket.writeMessage(username);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void sendFriendDecline(String username) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 18);
        tweetPacket.writeMessage(username);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void sendFriendAccept(String username) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 17);
        tweetPacket.writeMessage(username);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void sendFriendRequestCancel(String username) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 16);
        tweetPacket.writeMessage(username);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void sendFriendRequest(String username) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 15);
        tweetPacket.writeMessage(username);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void createGroup(String groupName, String clientIDS) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 6);
        tweetPacket.writeMessage(groupName);
        tweetPacket.writeMessage(clientIDS);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void sendMessage(String message, String contactUID, String firstMessage, String messageUID) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 5);
        tweetPacket.writeMessage(message);
        tweetPacket.writeMessage(contactUID);
        tweetPacket.writeMessage(firstMessage);
        tweetPacket.writeMessage(messageUID);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void deleteTweet(String tweetUID) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 14);
        tweetPacket.writeMessage(tweetUID);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void likeTweet(String tweetUID) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 11);
        tweetPacket.writeMessage(tweetUID);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void sendTweet(String message, String tweetUID) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 12);
        tweetPacket.writeMessage(message);
        tweetPacket.writeMessage(tweetUID);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }
    public void deleteMessage(String messageUID, String contactUID) throws IOException {
        PacketBuilder tweetPacket = new PacketBuilder();
        tweetPacket.writeOpCode((byte) 7);
        tweetPacket.writeMessage(messageUID);
        tweetPacket.writeMessage(contactUID);
        outputStream.write(tweetPacket.getPacketBytes(), 0, tweetPacket.getPacketBytes().length);
    }

    private void readPackets(PacketReader packetReader) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            while (true) {
                try {
                    byte opcode = packetReader.readByte();
                    switch (opcode) {
                        case 0:
                            String registerStatus = packetReader.readMessage();
                            if (registerStatus.equals("True")) {
                                registerSuccessEvent.accept(null);
                            } else {
                                registerFailEvent.accept(null);
                            }
                            break;
                        case 1:
                            String loginStatus = packetReader.readMessage();
                            if (loginStatus.equals("True")) {
                                loginCorrectEvent.accept(null);
                            } else {
                                loginFailedEvent.accept(null);
                            }
                            break;
                        case 2:
                            DataService.username = packetReader.readMessage();
                            DataService.UID = packetReader.readMessage();
                            break;
                        case 3:
                            userConnectedEvent.accept(null);
                            break;
                        case 4:
                            userDisconnectedEvent.accept(null);
                            break;
                        case 5:
                            messageReceivedEvent.accept(null);
                            break;
                        case 6:
                            groupCreatedEvent.accept(null);
                            break;
                        case 7:
                            deleteMessageEvent.accept(null);
                            break;
                        case 8:
                            micMutedEvent.accept(null);
                            break;
                        case 10:
                            tooManyPacketsEvent.accept(null);
                            break;
                        case 11:
                            likeEvent.accept(null);
                            break;
                        case 12:
                            tweetReceivedEvent.accept(null);
                            break;
                        case 13:
                            getTweetsEvent.accept(null);
                            break;
                        case 14:
                            deleteTweetEvent.accept(null);
                            break;
                        case 15:
                            friendRequestEvent.accept(null);
                            break;
                        case 16:
                            friendRequestCancelEvent.accept(null);
                            break;
                        case 17:
                            friendRequestAcceptEvent.accept(null);
                            break;
                        case 18:
                            friendRequestDeclineEvent.accept(null);
                            break;
                        case 19:
                            friendRemoveEvent.accept(null);
                            break;
                        case 20:
                            getFriendEvent.accept(null);
                            break;
                        case 21:
                            userRegisterConnectedEvent.accept(null);
                            break;
                        case 22:
                            voiceMessageReceivedEvent.accept(null);
                            break;
                        case 23:
                            System.exit(1);
                        default:
                            System.out.println("Unknown opcode: " + opcode);
                            break;
                    }
                } catch (IOException e) {
                    Log.d("NaberApp", "Sunucu Kapandı Uygulama Kapanıyor");
                    System.exit(1);
                }
            }
        });
    }
}
