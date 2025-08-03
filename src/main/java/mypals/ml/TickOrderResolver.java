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

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TickOrderResolver {
    public static List<BlockPos> resolveTickOrder(List<SchedulTickObject> scheduledTicks) {

        scheduledTicks.sort(Comparator
                .comparingLong((SchedulTickObject d) -> d.time)
                .thenComparingInt(d -> d.priority)
                .thenComparingLong(d -> d.subTick)
        );
        List<BlockPos> ordered= new ArrayList<>();
        for (SchedulTickObject tick : scheduledTicks) {
            ordered.add(new BlockPos(tick.pos));
        }
        return ordered;
    }
}
