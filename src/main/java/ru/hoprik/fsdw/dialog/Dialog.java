package ru.hoprik.fsdw.dialog;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import ru.hoprik.fsdw.network.Network;
import ru.hoprik.fsdw.network.Packets.SDialogPacket;

import java.io.*;

public class Dialog implements Serializable{
    String heroSay;
    Bench[] benches;
    String[] scripts;

    public Dialog(String heroSay, Bench[] benches, String[] scripts){
        this.heroSay = heroSay;
        this.benches = benches;
        this.scripts = scripts;

    }
    public Dialog(int id){
        this.heroSay = "end."+id;
    }


    public void show(PlayerEntity entity) {
        if (!entity.world.isRemote) {
            for (ServerPlayerEntity player : entity.getServer().getPlayerList().getPlayers()) {
                if (entity instanceof ServerPlayerEntity) {
                    Network.sendToPlayer(new SDialogPacket(heroSay, Network.toByte(benches), Network.toByte(scripts)), player);
                }
            }
        }
        else {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.displayGuiScreen(new DialogGui(heroSay, benches, scripts));
        }
    }

//    public void setEnd(String end) {
//        this.end = end;
//    }
//
//
//    public void end() {
//        StoryMod.logger.info(this.heroSay);
//        StoryMod.logger.info(this.end);
//    }
}