package ru.hoprik.fsdw.dialog.Custom;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ButtonWithoutBG extends AbstractButton {
    public static final OnTooltip NO_TOOLTIP = (p_93740_, p_93741_, p_93742_, p_93743_) -> {
    };
    protected final OnPress onPress;
    protected final OnTooltip onTooltip;

    public ButtonWithoutBG(int p_93721_, int p_93722_, int p_93723_, int p_93724_, String p_93725_, OnPress p_93726_) {
        this(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_, NO_TOOLTIP);
    }

    public ButtonWithoutBG(int p_93728_, int p_93729_, int p_93730_, int p_93731_, String p_93732_, OnPress p_93733_, OnTooltip p_93734_) {
        super(p_93728_, p_93729_, p_93730_, p_93731_, new StringTextComponent(p_93732_));
        this.onPress = p_93733_;
        this.onTooltip = p_93734_;
    }

    public void onPress() {
        this.onPress.onPress(this);
    }

    public static List<String> usingSplitMethod(String text, int n) {
        String[] results = text.split("(?<=\\G.{" + n + "})");

        return Arrays.asList(results);
    }


    public void renderWidget(MatrixStack p_93746_, int p_93747_, int p_93748_, float p_93749_) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer font = minecraft.fontRenderer;

        final int[] count = {-1};

        usingSplitMethod(this.getMessage().getString(), 37).forEach(bench_text -> {
            count[0]++;
            if (this.isHovered()) {
                font.drawText(p_93746_, new StringTextComponent(bench_text).mergeStyle(TextFormatting.UNDERLINE), this.x + 20, this.y+font.FONT_HEIGHT*count[0],  Color.fromHex("#763c39").getColor());
            }
            else {
                font.drawText(p_93746_, new StringTextComponent(bench_text), this.x + 20, this.y+font.FONT_HEIGHT*count[0], Color.fromHex("#823F3B").getColor());
            }
        });


    }
    public static void drawCenteredStringWithoutShadow(MatrixStack p_93216_, FontRenderer p_93217_, StringTextComponent p_93218_, int p_93219_, int p_93220_, int p_93221_) {
        p_93217_.drawText(p_93216_, p_93218_, (float)(p_93219_ - p_93217_.getStringWidth(p_93218_.getText()) / 2), (float)p_93220_, p_93221_);
    }

    public static void drawCenteredStringWithoutShadow(MatrixStack p_93216_, FontRenderer p_93217_, ITextComponent p_93218_, int p_93219_, int p_93220_, int p_93221_) {
        p_93217_.drawText(p_93216_, p_93218_, (float)(p_93219_ - p_93217_.getStringWidth(p_93218_.getString()) / 2), (float)p_93220_, p_93221_);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(ButtonWithoutBG p_93751_);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnTooltip {
        void onTooltip(ButtonWithoutBG p_93753_, MatrixStack p_93754_, int p_93755_, int p_93756_);

        default void narrateTooltip(Consumer<StringTextComponent> p_168842_) {
        }
    }
}

