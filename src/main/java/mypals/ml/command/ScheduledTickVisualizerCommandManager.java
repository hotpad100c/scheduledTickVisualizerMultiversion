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

package mypals.ml.command;

import mypals.ml.config.ScheduledTickVisualizerConfig;

import java.lang.reflect.Field;

import static mypals.ml.ScheduledTickVisualizerClient.UpadteSettings;

public class ScheduledTickVisualizerCommandManager {
    public static void setStaticBooleanField(String fieldName, boolean flag) {
        try {
            Class<?> clazz = ScheduledTickVisualizerConfig.class;
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, flag);
            ScheduledTickVisualizerConfig.CONFIG_HANDLER.save();
            UpadteSettings();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Failed to update field '" + fieldName + "': " + e.getMessage());
        }
    }
    public static void setStaticFloatField(String fieldName, float value) {
        try {
            Class<?> clazz = ScheduledTickVisualizerConfig.class;
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
            ScheduledTickVisualizerConfig.CONFIG_HANDLER.save();
            UpadteSettings();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Failed to update field '" + fieldName + "': " + e.getMessage());
        }
    }
    public static void setStaticIntField(String fieldName, int value) {
        try {
            Class<?> clazz = ScheduledTickVisualizerConfig.class;
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, value);
            ScheduledTickVisualizerConfig.CONFIG_HANDLER.save();
            UpadteSettings();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Failed to update field '" + fieldName + "': " + e.getMessage());
        }
    }
}
