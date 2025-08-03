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

import net.minecraft.network.PacketByteBuf;
//#if MC >= 12005
import net.minecraft.network.codec.PacketCodec;
//#endif
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public final class SchedulTickObject{
    public BlockPos pos;
    public long time;
    public int priority;
    public long subTick;
    public String name;
    //#if MC >= 12005
    public static final PacketCodec<PacketByteBuf,SchedulTickObject> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeBlockPos(value.getPos());
                buf.writeLong(value.getTriggerTick());
                buf.writeInt(value.getPriority());
                buf.writeLong(value.getTickOrder());
                buf.writeString(value.getType());
            },
            buf -> new SchedulTickObject(
                    buf.readBlockPos(),
                    buf.readLong(),
                    buf.readInt(),
                    buf.readLong(),
                    buf.readString()
            )
    );
    //#endif
    public SchedulTickObject(BlockPos pos, long time, int priority, long subTick, String name) {
        this.pos=pos;
        this.time=time;
        this.priority=priority;
        this.subTick=subTick;
        this.name=name;
    }
    public BlockPos getPos(){
        return pos;
    }
    public long getTriggerTick(){
        return time;
    }
    public int getPriority(){
        return priority;
    }
    public long getTickOrder(){
        return subTick;
    }
    public String getType(){
        return name;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulTickObject obj = (SchedulTickObject) o;
        return pos==obj.pos && time==obj.time && priority==obj.priority && subTick==obj.subTick && name==obj.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, time, priority, subTick, name);
    }
}