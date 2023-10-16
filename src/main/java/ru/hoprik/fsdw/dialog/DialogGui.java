package ru.hoprik.fsdw.dialog;



import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import ru.hoprik.fsdw.Fsdw;
import ru.hoprik.fsdw.dialog.Custom.ButtonWithoutBG;
import ru.hoprik.fsdw.network.Network;
import ru.hoprik.fsdw.network.Packets.SEndDialogPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class DialogGui extends Screen {
    String heroSay;
    Bench[] benches;
    String[] scripts;
    List<ButtonWithoutBG> buttons = new ArrayList<>();


    public DialogGui(String heroSay, Bench[] benches, String[] scripts) {
        super(new StringTextComponent("dialog"));
        this.heroSay = heroSay;
        this.benches = benches;
        this.scripts = scripts;
        Fsdw.LOGGER.warn("Vuzz, idi naxui");
    }

    public static List<String> usingSplitMethod(String text, int n) {
        String[] results = text.split("(?<=\\G.{" + n + "})");

        return Arrays.asList(results);
    }

    @Override
    protected void init() {
        int book_width = this.width/2+100;
        int y = 47;
        for (Bench bench: benches) {
            final String[] textArray = {""};

            final int[] count = {0};

            usingSplitMethod(bench.playerSay, 37).forEach(bench_text -> {
                textArray[0] += bench_text;
                count[0]++;
            });

            String text = textArray[0];
            int countText = count[0];

            ButtonWithoutBG button = this.addButton(new ButtonWithoutBG(this.width /2- book_width /2+ (book_width/16*3)-20, height / 4 + height/1080*40*10 + y, this.font.getStringWidth(text)+40, font.FONT_HEIGHT*countText, bench.playerSay, (p_213021_1_) -> {
                 String[] strings = bench.dialog.heroSay.split("\\.");
                 if (Objects.equals(strings[0], "end")){
                     this.minecraft.displayGuiScreen(null);
                     Network.sendToServer(new SEndDialogPacket(strings[1], Network.toByte(scripts)));
                 }
                 else {
                    this.minecraft.displayGuiScreen(new DialogGui(bench.dialog.heroSay, bench.dialog.benches, scripts));
                 }
             }));
             buttons.add(button);
             y+=30;
        }
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int book_width = this.width/2+100;
        renderBackground(matrixStack);
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Fsdw.MOD_ID, "textures/gui/bg.png"));
        blit(matrixStack, this.width/2-book_width/2-20, this.height/4-book_width/300*70, 0.0F, 0.0F, book_width+40, book_width-75 , book_width+40,book_width-125);
        float f = (float)Math.atan((float)(50 + 51) - mouseX / 40.0F);
        float f1 = (float)Math.atan((float)(48 + 75 - 50) - mouseY / 40.0F);
        drawEntityOnScreen((int) (this.width /2- book_width /2+ (book_width/16*13.8)), this.height/4-book_width/4+ (book_width/16*10), 90, 0, f1, this.minecraft.player);
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(Fsdw.MOD_ID, "textures/gui/fg.png"));
        blit(matrixStack, this.width/2-book_width/2-20, this.height/4-book_width/300*70, 0.0F, 0.0F, book_width+40, book_width-125, book_width+40,book_width-125);

        final String[] textArray = {""};

        final int[] count = {0};

        usingSplitMethod(heroSay, 35).forEach(hero_text -> {
            count[0]++;
        });


        usingSplitMethod(this.heroSay, 35).forEach(bench_text -> {
            this.font.drawText(matrixStack, new StringTextComponent(bench_text), this.width /2- book_width /2+ (book_width/16*3), (float)this.height/4-(float)book_width/4+(float)(book_width/16*4.5)-font.FONT_HEIGHT*count[0], Color.fromHex("#823F3B").getColor());
            count[0]--;
        });



        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }


    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float f1 = (float)Math.atan((double)(mouseY / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)posX, (float)posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float f2 = livingEntity.renderYawOffset;
        float f3 = livingEntity.rotationYaw;
        float f4 = livingEntity.rotationPitch;
        float f5 = livingEntity.prevRotationYawHead;
        float f6 = livingEntity.rotationYawHead;
        livingEntity.renderYawOffset = 180.0F + f * 20.0F;
        livingEntity.rotationYaw = 180.0F + f * 40.0F;
        livingEntity.rotationPitch = -f1 * 20.0F;
        livingEntity.rotationYawHead = livingEntity.rotationYaw;
        livingEntity.prevRotationYawHead = livingEntity.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        livingEntity.renderYawOffset = f2;
        livingEntity.rotationYaw = f3;
        livingEntity.rotationPitch = f4;
        livingEntity.prevRotationYawHead = f5;
        livingEntity.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
