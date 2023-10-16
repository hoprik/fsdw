package ru.hoprik.fsdw.network.Packets;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import ru.hoprik.fsdw.dialog.Bench;
import ru.hoprik.fsdw.dialog.DialogGui;
import ru.hoprik.fsdw.network.Network;

import java.util.function.Supplier;

public class SDialogPacket  {
    private final String heroSay;
    private final byte[] benches;
    private final byte[] scripts;
    public SDialogPacket(String heroSay, byte[] bench, byte[] scripts) {
        this.heroSay = heroSay;
        this.benches = bench;
        this.scripts = scripts;
    }
    public SDialogPacket(PacketBuffer buf) {
        this.heroSay = buf.readString();
        this.benches = buf.readByteArray();
        this.scripts = buf.readByteArray();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(heroSay);
        buf.writeByteArray(benches);
        buf.writeByteArray(scripts);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.displayGuiScreen(new DialogGui(this.heroSay,(Bench[]) Network.toObj(benches), (String[]) Network.toObj(scripts)));
        });
        return true;
    }
}