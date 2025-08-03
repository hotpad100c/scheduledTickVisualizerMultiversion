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

package mypals.ml.network;

//#if MC >= 12005
import mypals.ml.SchedulTickObject;
import mypals.ml.ScheduledTickVisualizer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

import java.util.List;

public record ScheduledTickDataPayload(List<SchedulTickObject> ticks, String type) implements CustomPayload {
    public static final Id<ScheduledTickDataPayload> ID = new Id<>(ScheduledTickVisualizer.TICK_PACKET_ID);
    public static final PacketCodec<PacketByteBuf, ScheduledTickDataPayload> CODEC = PacketCodec.of(
            (value, buf) -> {
                buf.writeCollection(value.ticks(), SchedulTickObject.CODEC);
                buf.writeString(value.type);
            },
            buf -> new ScheduledTickDataPayload(
                    buf.readList(SchedulTickObject.CODEC),
                    buf.readString()
            )
    );
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
//#else
//$$public record ScheduledTickDataPayload(){}
//#endif
