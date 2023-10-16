package ru.hoprik.fsdw.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ComponentArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import ru.hoprik.fsdw.Fsdw;
import ru.hoprik.fsdw.dialog.Bench;
import ru.hoprik.fsdw.dialog.Dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DialogCommand {
    public DialogCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("dialog").
            then(Commands.argument("msg_hero",  ComponentArgument.component()).
                then(Commands.argument("msg",  ComponentArgument.component()).
                        then(Commands.argument("msg_script",  ComponentArgument.component()).executes((command) ->
            dialog_show(command.getSource(), ComponentArgument.getComponent(command, "msg_hero"),
                            ComponentArgument.getComponent(command, "msg"),
                                    ComponentArgument.getComponent(command, "msg_script")))))));

    }


    private int dialog_show(CommandSource source, ITextComponent hero, ITextComponent text_1, ITextComponent script_1) {
        String[] strings = text_1.getString().split("/");
        String[] commands = script_1.getString().split("/");
        List<Bench> benches = new ArrayList<>();
        int id = 1;
        for (String text:
             strings) {
            benches.add(new Bench(text, new Dialog(id)));
            id++;
        }
        Bench[] finalBenches = benches.toArray(new Bench[id]);
        source.getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
            Dialog dialog = new Dialog(hero.getString(), finalBenches, commands);

            dialog.show(serverPlayerEntity);
        });
        return 1;

    }


}