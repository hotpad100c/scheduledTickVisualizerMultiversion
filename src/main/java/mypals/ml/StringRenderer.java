/*
 * This file is part of the ScheduledTickVisualizer project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
 *
 * ScheduledTickVisualizer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ScheduledTickVisualizer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ScheduledTickVisualizer.  If not, see <https://www.gnu.org/licenses/>.
 */

package mypals.ml;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import java.awt.*;
import java.util.ArrayList;

public class StringRenderer {
    public static double lastTickPosX = 0;
    public static double lastTickPosY = 0;
    public static double lastTickPosZ = 0;
    public static void renderTextList(MatrixStack matrixStack, BlockPos pos, float tickDelta, float line, ArrayList<String> texts, ArrayList<Integer> colors, float size) {
        drawStringList(matrixStack, pos, tickDelta, line, texts, colors, size) ;

        }
    private static void drawStringList(MatrixStack matrixStack, BlockPos pos,float tickDelta, float line, ArrayList<String> texts, ArrayList<Integer> colors, float size)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        if (camera.isReady() && client.getEntityRenderDispatcher().gameOptions != null && client.player != null)
        {
            /*double x = (double)pos.toCenterPos().getX();
            double y = (double)pos.toCenterPos().getY();
            double z = (double)pos.toCenterPos().getZ();
            double camX = camera.getPos().x;
            double camY = camera.getPos().y;
            double camZ = camera.getPos().z;*/

            float x = (float) (pos.toCenterPos().getX() - MathHelper.lerp(tickDelta, lastTickPosX, camera.getPos().getX()));
            float y = (float) (pos.toCenterPos().getY() - MathHelper.lerp(tickDelta, lastTickPosY, camera.getPos().getY()));
            float z = (float) (pos.toCenterPos().getZ() - MathHelper.lerp(tickDelta, lastTickPosZ, camera.getPos().getZ()));
            lastTickPosX = camera.getPos().getX();
            lastTickPosY = camera.getPos().getY();
            lastTickPosZ = camera.getPos().getZ();

            matrixStack.push();
            matrixStack.translate(x, y, z);
            //matrixStack.translate((float)(x - camX), (float)(y - camY), (float)(z - camZ));

            //#if MC >= 12101
            matrixStack.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());
            //#else
            //$$ matrixStack.multiplyPositionMatrix(new Matrix4f().rotation(camera.getRotation()));
            //#endif
            matrixStack.scale(
                    //#if MC < 12100
                    //$$ -
                    //#endif
                            size
                    , -size, 1);
            //#if MC < 12105
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            //#endif

            float totalHeight = 0.0F;
            for (String text : texts) {
                totalHeight += client.textRenderer.getWrappedLinesHeight(text, Integer.MAX_VALUE) * 1.25F;
            }
            float renderYBase = -totalHeight / 2.0F;
            for (int i = 0; i < texts.size(); i++) {
                float renderX = -client.textRenderer.getWidth(texts.get(i)) * 0.5F;
                float renderY = renderYBase + client.textRenderer.getWrappedLinesHeight(texts.get(i), Integer.MAX_VALUE) * 1.25F * i;
                //#if MC < 12100
                //$$VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
                //#else
                VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
                //#endif
                client.textRenderer.draw(texts.get(i), renderX, renderY, colors.get(i),
                        false, matrixStack.peek().getPositionMatrix(), immediate,
                        TextRenderer.TextLayerType.SEE_THROUGH, 0, 0xF000F0);
                immediate.draw();
            }
            //#if MC < 12105
            RenderSystem.enableDepthTest();
            //#endif
            matrixStack.pop();
        }
    }
    public static void drawCube(MatrixStack matrices, BlockPos pos, float size, float tickDelta, Color color, float alpha) {
        drawCube2(matrices, pos, size, tickDelta, color, alpha);
    }

    public static void drawCube2(MatrixStack matrices, BlockPos pos, float size, float tickDelta, Color color,float alpha) {
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        if (camera.isReady() && client.getEntityRenderDispatcher().gameOptions != null && client.player != null) {
            matrices.push();
            float x = (float) (pos.getX() - MathHelper.lerp(tickDelta, lastTickPosX, camera.getPos().getX()));
            float y = (float) (pos.getY() - MathHelper.lerp(tickDelta, lastTickPosY, camera.getPos().getY()));
            float z = (float) (pos.getZ() - MathHelper.lerp(tickDelta, lastTickPosZ, camera.getPos().getZ()));
            lastTickPosX = camera.getPos().getX();
            lastTickPosY = camera.getPos().getY();
            lastTickPosZ = camera.getPos().getZ();

            matrices.translate(x, y, z);
            Matrix4f modelViewMatrix = matrices.peek().getPositionMatrix();
            //#if MC < 12105
            RenderSystem.disableDepthTest();
            //#endif

            //#if MC <= 12100
            //$$VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            //#else
            VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
            //#endif
            VertexConsumer vertexConsumer = immediate.getBuffer(RenderLayer.getDebugQuads());

            float minOffset = -0.001F - size;
            float maxOffset = 1.001F + size;

            float red = ((color.getRGB() >> 16) & 0xFF) / 255.0f;
            float green = ((color.getRGB() >> 8) & 0xFF) / 255.0f;
            float blue = (color.getRGB() & 0xFF) / 255.0f;

           vertex(vertexConsumer, modelViewMatrix, minOffset, maxOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, maxOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, maxOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, minOffset, maxOffset, maxOffset, red, green, blue, alpha);

           vertex(vertexConsumer, modelViewMatrix, minOffset, minOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, minOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, minOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, minOffset, minOffset, minOffset, red, green, blue, alpha);

           vertex(vertexConsumer, modelViewMatrix, minOffset, maxOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, minOffset, maxOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, minOffset, minOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, minOffset, minOffset, minOffset, red, green, blue, alpha);

           vertex(vertexConsumer, modelViewMatrix, maxOffset, minOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, minOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, maxOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, maxOffset, minOffset, red, green, blue, alpha);

           vertex(vertexConsumer, modelViewMatrix, minOffset, minOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, minOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, maxOffset, minOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, minOffset, maxOffset, minOffset, red, green, blue, alpha);

           vertex(vertexConsumer, modelViewMatrix, minOffset, maxOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, maxOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, maxOffset, minOffset, maxOffset, red, green, blue, alpha);
           vertex(vertexConsumer, modelViewMatrix, minOffset, minOffset, maxOffset, red, green, blue, alpha);


            immediate.draw();
            matrices.pop();

            //#if MC < 12105
            RenderSystem.enableDepthTest();
            //#endif
        }
    }
    public static void vertex(VertexConsumer consumer, Matrix4f modelViewMatrix, float x, float y, float z, float red, float green, float blue, float alpha) {
        consumer.vertex(modelViewMatrix, x, y, z).color(red, green, blue, alpha)
        //#if MC <= 12100
        //$$        .next();
        //#else
        ;
        //#endif
    }
    public static void vertex(BufferBuilder bufferBuilder, Matrix4f modelViewMatrix, float x, float y, float z, float red, float green, float blue, float alpha) {
        bufferBuilder.vertex(modelViewMatrix, x, y, z).color(red, green, blue, alpha)
        //#if MC <= 12100
        //$$        .next();
        //#else
        ;
        //#endif
    }

}
