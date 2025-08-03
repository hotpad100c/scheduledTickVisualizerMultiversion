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

import mypals.ml.config.ScheduledTickVisualizerConfig;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static mypals.ml.StringRenderer.*;
import static mypals.ml.config.ScheduledTickVisualizerConfig.*;

public class ScheduledTickVisualizerInfoRender {

    public static List<SchedulTickObject> scheduledTicksFluid = new ArrayList<>();
    public static List<SchedulTickObject> scheduledTicksBlock = new ArrayList<>();
    public static int blockTickDataClearTimer = timeOutDelay;
    public static int fluidTickDataClearTimer = timeOutDelay;
    public static List<BlockPos> reorderedBlockTicks = new ArrayList<>();
    public static List<BlockPos> reorderedFluidTicks = new ArrayList<>();
    public static void setScheduledTicksBlock(List<SchedulTickObject> scheduledTicks){
        scheduledTicksBlock = scheduledTicks;
        blockTickDataClearTimer = timeOutDelay;
        reorderedBlockTicks = TickOrderResolver.resolveTickOrder(scheduledTicks);
    }
    public static void setScheduledTicksFluid(List<SchedulTickObject> scheduledTicks){
        scheduledTicksFluid = scheduledTicks;
        fluidTickDataClearTimer = timeOutDelay;
        reorderedFluidTicks = TickOrderResolver.resolveTickOrder(scheduledTicks);
    }
    @SuppressWarnings("ConstantConditions")
    public static void render(MatrixStack matrixStack, Float tickDelta) {
        if(showInfo) {
            
            if ((!reorderedBlockTicks.isEmpty() || !reorderedFluidTicks.isEmpty() )&& orderIndicator) {
                int totalSize = reorderedBlockTicks.size() + reorderedFluidTicks.size();

                if (ScheduledTickVisualizerClient.orderViewerIndex < 0 || ScheduledTickVisualizerClient.orderViewerIndex >= totalSize) {
                    ScheduledTickVisualizerClient.orderViewerIndex = 0;
                }

                BlockPos pos;
                if (ScheduledTickVisualizerClient.orderViewerIndex < reorderedBlockTicks.size()) {
                    pos = reorderedBlockTicks.get(ScheduledTickVisualizerClient.orderViewerIndex);
                } else {
                    int fluidIndex = ScheduledTickVisualizerClient.orderViewerIndex - reorderedBlockTicks.size();
                    pos = reorderedFluidTicks.get(fluidIndex);
                }

                drawCube2(matrixStack, pos, 0.02f, tickDelta,
                        new Color(225, 225, 225), Math.min(boxAlpha+0.3f,1f));
            }

            for (SchedulTickObject tick : scheduledTicksBlock) {
                ArrayList<Integer> colors = new ArrayList<>();
                ArrayList<String> text = new ArrayList<>();
                if(simplify){
                    text.add(
                            tick.subTick + "@" +
                            tick.time +
                            (tick.priority != 0 ? ("[" + tick.priority +"]"):"")
                    );
                    colors.add(blockTickColor.getRGB());
                }
                else{
                    if(showTickTypeInfo){
                        if(showAccurateBlockType)
                            text.add(tick.name);
                        else
                            text.add(Text.translatable("text.scheduledtick.block").getString());
                        colors.add(blockTickColor.getRGB());
                    }
                    if(showSubOrderInfo){
                        text.add( Text.translatable("text.scheduledtick.sub_order").getString() + ": " + tick.subTick);
                        colors.add(subOrderColor.getRGB());
                    }
                    if(showTriggerInfo){
                        text.add(  Text.translatable("text.scheduledtick.trigger").getString() + ": " + tick.time);
                        colors.add(triggerColor.getRGB());
                    }
                    if(showPriorityInfo){
                        text.add( Text.translatable("text.scheduledtick.priority").getString() + ": " + tick.priority);
                        colors.add(priorityColor.getRGB());
                    }
                }
                renderTextList(matrixStack, tick.pos, tickDelta, 5, text, colors, ScheduledTickVisualizerConfig.textSize);
                if(showInfoBox)
                    drawCube(matrixStack,tick.pos,0f,tickDelta,
                            new Color(blockTickColor.getRed(),blockTickColor.getGreen(),blockTickColor.getBlue()),boxAlpha);

            }
            for (SchedulTickObject tick : scheduledTicksFluid) {
                ArrayList<Integer> colors = new ArrayList<>();
                ArrayList<String> text = new ArrayList<>();
                if(simplify){
                    text.add(
                            tick.subTick + "@" +
                            tick.time +
                            (tick.priority != 0 ? ("[" + tick.priority +"]"):"")
                    );
                    colors.add(fluidTickColor.getRGB());
                }
                else {
                    if (showTickTypeInfo) {
                        if (showAccurateBlockType)
                            text.add(tick.name);
                        else
                            text.add(Text.translatable("text.scheduledtick.fluid").getString());
                        colors.add(fluidTickColor.getRGB());
                    }
                    if (showSubOrderInfo) {
                        text.add(Text.translatable("text.scheduledtick.sub_order").getString() + ": " + tick.subTick);
                        colors.add(subOrderColor.getRGB());
                    }
                    if (showTriggerInfo) {
                        text.add(Text.translatable("text.scheduledtick.trigger").getString() + ": " + tick.time);
                        colors.add(triggerColor.getRGB());
                    }
                    if (showPriorityInfo) {
                        text.add(Text.translatable("text.scheduledtick.priority").getString() + ": " + tick.priority);
                        colors.add(priorityColor.getRGB());
                    }
                }
                renderTextList(matrixStack, tick.pos, tickDelta, 5, text, colors, ScheduledTickVisualizerConfig.textSize);
                if (showInfoBox)
                    drawCube(matrixStack, tick.pos, 0f, tickDelta,
                            new Color(fluidTickColor.getRed(), fluidTickColor.getGreen(), fluidTickColor.getBlue()), boxAlpha);
            }
            if(fluidTickDataClearTimer>0)
                fluidTickDataClearTimer--;
            if(blockTickDataClearTimer>0)
                blockTickDataClearTimer--;
            if(blockTickDataClearTimer<=0){
                reorderedBlockTicks.clear();
                scheduledTicksBlock.clear();
            }
            if(fluidTickDataClearTimer<=0){
                reorderedFluidTicks.clear();
                scheduledTicksFluid.clear();
            }
        }
    }
}
