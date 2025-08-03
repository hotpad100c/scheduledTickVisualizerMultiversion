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

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import mypals.ml.ScheduledTickVisualizer;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;

public class ScheduledTickVisualizerConfig {
    public static ConfigClassHandler<ScheduledTickVisualizerConfig> CONFIG_HANDLER = ConfigClassHandler.createBuilder(ScheduledTickVisualizerConfig.class)
            .id(ScheduledTickVisualizer.id("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("ScheduledTickVisualizer.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();
    @SerialEntry
    public static boolean simplify = false;
    @SerialEntry
    public static boolean showInfo = false;
    @SerialEntry
    public static boolean showInfoBox = false;
    @SerialEntry
    public static boolean shadow = true;
    @SerialEntry
    public static boolean background = false;
    @SerialEntry
    public static boolean showAccurateBlockType = false;
    @SerialEntry
    public static boolean showTickTypeInfo = true;
    @SerialEntry
    public static boolean showSubOrderInfo = false;
    @SerialEntry
    public static boolean sortSubOrderInfo = true;
    @SerialEntry
    public static boolean showTriggerInfo = true;
    @SerialEntry
    public static boolean showPriorityInfo = true;
    @SerialEntry
    public static float textSize = 0.012F;
    @SerialEntry
    public static int timeOutDelay = 30;
    @SerialEntry
    public static boolean orderIndicator = false;
    @SerialEntry
    public static Color blockTickColor = Color.magenta;

    @SerialEntry
    public static Color fluidTickColor = Color.green;
    @SerialEntry
    public static Color backgroundColor = Color.darkGray;
    @SerialEntry
    public static Color subOrderColor = Color.red;
    @SerialEntry
    public static Color triggerColor = Color.green;
    @SerialEntry
    public static Color priorityColor = Color.CYAN;
    @SerialEntry
    public static float boxAlpha = 0.12F;

}
