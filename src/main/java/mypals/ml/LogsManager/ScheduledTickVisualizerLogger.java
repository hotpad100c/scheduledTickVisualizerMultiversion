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

package mypals.ml.LogsManager;

import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ScheduledTickVisualizerLogger {
    public static void writeLogFile(String fileName, String content) {
        try {
            File gameDir = FabricLoader.getInstance().getGameDir().toFile();

            File logDir = new File(gameDir, "scheduledtickvisualizerLogs");
            if (!logDir.exists()) {
                if (logDir.mkdirs()) {
                    System.out.println("Created directory: " + logDir.getAbsolutePath());
                }
            }

            File file = new File(logDir, fileName);

            if (!file.exists()) {
                if (file.createNewFile()) {
                    System.out.println("Created file: " + file.getAbsolutePath());
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
            writer.write(content);
            writer.newLine();
            writer.close();

            System.out.println("File written successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
