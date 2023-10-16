package ru.hoprik.fsdw.network;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import ru.hoprik.fsdw.Fsdw;
import ru.hoprik.fsdw.network.Packets.SDialogPacket;
import ru.hoprik.fsdw.network.Packets.SEndDialogPacket;

import java.io.*;

public class Network{
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Fsdw.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        net.messageBuilder(SDialogPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(SDialogPacket::new)
                .encoder(SDialogPacket::toBytes)
                .consumer(SDialogPacket::handle)
                .add();

        net.messageBuilder(SEndDialogPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SEndDialogPacket::new)
                .encoder(SEndDialogPacket::toBytes)
                .consumer(SEndDialogPacket::handle)
                .add();

        INSTANCE = net;
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, PlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

    public static byte[] toByte(Object object){
        try (
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object toObj(byte[] bytes){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = null;
            ois = new ObjectInputStream(bis);
            Object object = ois.readObject();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            Fsdw.LOGGER.info(e);
            throw new RuntimeException(e);
        }
    }
}
