package ru.hoprik.fsdw.network.Packets;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import ru.hoprik.fsdw.dialog.Dialog;
import ru.hoprik.fsdw.network.Network;

import java.util.function.Supplier;

public class SEndDialogPacket {
    private final String end;
    private final byte[] instance;
    public SEndDialogPacket(String end, byte[] instance) {
        this.end = end;
        this.instance = instance;
    }

    public SEndDialogPacket(PacketBuffer buf) {
        this.end = buf.readString();
        this.instance = buf.readByteArray();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(end);
        buf.writeByteArray(instance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        String[] scripts = (String[]) Network.toObj(instance);
        context.enqueueWork(() -> {
            int end_id = Integer.parseInt(end);
            World world = context.getSender().world;
            world.getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK).set(false, world.getServer());
            world.getGameRules().get(GameRules.COMMAND_BLOCK_OUTPUT).set(false, world.getServer());
            world.getGameRules().get(GameRules.LOG_ADMIN_COMMANDS).set(false, world.getServer());
            world.getServer().getCommandManager().handleCommand(context.getSender().getCommandSource(), "/"+scripts[end_id-1]);
        });
        return true;
    }
}