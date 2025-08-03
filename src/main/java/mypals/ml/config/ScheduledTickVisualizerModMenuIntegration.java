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

package mypals.ml.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

import java.awt.*;

import static mypals.ml.ScheduledTickVisualizerClient.UpadteSettings;

public class ScheduledTickVisualizerModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        var instance = ScheduledTickVisualizerConfig.CONFIG_HANDLER;
        return screen -> YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("config.scheduledtickvisualizer.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.scheduledtickvisualizer.settings"))
                        .tooltip(Text.translatable("config.scheduledtickvisualizer.settings.tooltip"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.translatable("config.scheduledtickvisualizer.render"))
                                .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.render.description")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.simplifyInfo"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.simplifyInfo.description")))
                                        .binding(false, () -> ScheduledTickVisualizerConfig.simplify, newVal -> ScheduledTickVisualizerConfig.simplify = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.main_render"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.main_render.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.showInfo, newVal -> ScheduledTickVisualizerConfig.showInfo = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.render_box"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.render_box.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.showInfoBox, newVal -> ScheduledTickVisualizerConfig.showInfoBox = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.orderIndicator"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.orderIndicator.description")))
                                        .binding(false, () -> ScheduledTickVisualizerConfig.orderIndicator, newVal -> ScheduledTickVisualizerConfig.orderIndicator = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.shadow"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.shadow.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.shadow, newVal -> ScheduledTickVisualizerConfig.shadow = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.background"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.background.description")))
                                        .binding(false, () -> ScheduledTickVisualizerConfig.background, newVal -> ScheduledTickVisualizerConfig.background = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.tick_type_render_specific"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.tick_type_render_specific.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.showAccurateBlockType, newVal -> ScheduledTickVisualizerConfig.showAccurateBlockType = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.tick_type_render"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.tick_type_render.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.showTickTypeInfo, newVal -> ScheduledTickVisualizerConfig.showTickTypeInfo = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.sub_order_render"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.sub_order_render.description")))
                                        .binding(false, () -> ScheduledTickVisualizerConfig.showSubOrderInfo, newVal -> ScheduledTickVisualizerConfig.showSubOrderInfo = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.sub_order_sort"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.sub_order_sort.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.sortSubOrderInfo, newVal -> ScheduledTickVisualizerConfig.sortSubOrderInfo = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.trigger_tick_render"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.trigger_tick_render.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.showTriggerInfo, newVal -> ScheduledTickVisualizerConfig.showTriggerInfo = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.tick_priority_render"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.tick_priority_render.description")))
                                        .binding(true, () -> ScheduledTickVisualizerConfig.showPriorityInfo, newVal -> ScheduledTickVisualizerConfig.showPriorityInfo = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.font_size"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.font_size.description")))
                                        .binding(0.015F, () -> ScheduledTickVisualizerConfig.textSize, newVal -> ScheduledTickVisualizerConfig.textSize = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .range(0.01F, 0.02F)
                                                .step(0.001F)
                                                .formatValue(val -> Text.literal(val + "")))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.data_timeout"))
                                        .description(OptionDescription.of(Text.translatable("config.scheduledtickvisualizer.data_timeout.description")))
                                        .binding(30, () -> ScheduledTickVisualizerConfig.timeOutDelay, newVal -> ScheduledTickVisualizerConfig.timeOutDelay = newVal)
                                        .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                                .range(1, 200)
                                                .formatValue(val -> Text.literal(val + "")))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.block_tick_color"))
                                        .binding(Color.magenta, () -> ScheduledTickVisualizerConfig.blockTickColor, newVal -> ScheduledTickVisualizerConfig.blockTickColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.fluid_tick_color"))
                                        .binding(Color.magenta, () -> ScheduledTickVisualizerConfig.fluidTickColor, newVal -> ScheduledTickVisualizerConfig.fluidTickColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.sub_order_color"))
                                        .binding(Color.red, () -> ScheduledTickVisualizerConfig.subOrderColor, newVal -> ScheduledTickVisualizerConfig.subOrderColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.trigger_time_color"))
                                        .binding(Color.green, () -> ScheduledTickVisualizerConfig.triggerColor, newVal -> ScheduledTickVisualizerConfig.triggerColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.tick_priority_color"))
                                        .binding(Color.CYAN, () -> ScheduledTickVisualizerConfig.priorityColor, newVal -> ScheduledTickVisualizerConfig.priorityColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.background_color"))
                                        .binding(Color.CYAN, () -> ScheduledTickVisualizerConfig.backgroundColor, newVal -> ScheduledTickVisualizerConfig.backgroundColor = newVal)
                                        .controller(opt -> ColorControllerBuilder.create(opt)
                                                .allowAlpha(true))
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Text.translatable("config.scheduledtickvisualizer.box_alpha"))
                                        .binding(0.015F, () -> ScheduledTickVisualizerConfig.boxAlpha, newVal -> ScheduledTickVisualizerConfig.boxAlpha = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .range(0F, 1F)
                                                .step(0.05F)
                                                .formatValue(val -> Text.literal(val + "")))
                                        .build())
                                .build())
                        .build())
                .save(() -> {
                    instance.save();
                    UpadteSettings();
                })
                .build()
                .generateScreen(screen);
    }

}
