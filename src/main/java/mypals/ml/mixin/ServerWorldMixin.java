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

package mypals.ml.mixin;

import mypals.ml.LogsManager.ScheduledTickVisualizerLogger;
import mypals.ml.SchedulTickObject;
import mypals.ml.ScheduledTickVisualizer;
import mypals.ml.network.ScheduledTickDataPayload;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
//#if MC >= 12100
import net.minecraft.server.ServerTickManager;
import net.minecraft.world.tick.TickManager;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.GameRules;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import net.minecraft.world.tick.WorldTickScheduler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static mypals.ml.ScheduledTickVisualizer.*;
import static mypals.ml.ScheduledTickVisualizer.SORT_SUBORDER;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements StructureWorldAccess{
    @Shadow @Final private WorldTickScheduler<Block> blockTickScheduler;
    @Shadow @Final private WorldTickScheduler<Fluid> fluidTickScheduler;
    //#if MC >= 12103
    //$$protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
    //$$    super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    //$$}
    //#else
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }
    //#endif
    @Shadow @NotNull public abstract MinecraftServer getServer();

    //#if MC >= 12103
    //$$@Shadow public abstract GameRules getGameRules();
    //#endif

    //#if MC >= 12100
    @Shadow public abstract TickManager getTickManager();
    //#endif

    @Inject(method = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",
            at = @At("HEAD"))
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {

        if(
            //#if MC >= 12100
                !((ServerTickManager) this.getTickManager()).isSprinting()
            //#else
            //$$   true
            //#endif
        ){
            List<SchedulTickObject> orderedFluidTicks = new ArrayList<>();
            List<SchedulTickObject> orderedBlockTicks = new ArrayList<>();
            Set<ServerPlayerEntity> players = new HashSet<>();
            long time = this.getTime();

            // Logging
            if (ScheduledTickVisualizer.logManager != null && ScheduledTickVisualizer.logManager.ticks > 0) {
                ScheduledTickVisualizerLogger.writeLogFile(ScheduledTickVisualizer.logManager.fileName,
                        "====================================");
                ScheduledTickVisualizerLogger.writeLogFile(ScheduledTickVisualizer.logManager.fileName,
                        "|ServerWorld:Started a tick, current tick is: tick" + time);
            }

            Map<Object, String> typeNameCache = new HashMap<>();

            processTicks(blockTickScheduler, orderedBlockTicks, "Block", time, players, typeNameCache);
            processTicks(fluidTickScheduler, orderedFluidTicks, "Fluid", time, players, typeNameCache);

            if (ScheduledTickVisualizer.server != null) {
                //#if MC >= 12005
                for (ServerPlayerEntity player : players) {
                    ServerPlayNetworking.send(player, new ScheduledTickDataPayload(orderedBlockTicks, "Block"));
                    ServerPlayNetworking.send(player, new ScheduledTickDataPayload(orderedFluidTicks, "Fluid"));
                }
                //#else
                //$$for(ServerPlayerEntity player : players){
                //$$    ServerPlayNetworking.send(player,ScheduledTickVisualizer.TICK_PACKET_ID,ScheduledTickData(orderedBlockTicks,"Block"));
                //$$    ServerPlayNetworking.send(player,ScheduledTickVisualizer.TICK_PACKET_ID,ScheduledTickData(orderedFluidTicks,"Fluid"));
                //$$}
                //#endif
            }
        }
    }
    protected PacketByteBuf ScheduledTickData(List<SchedulTickObject> ticks, String type) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(type);
        buf.writeCollection(ticks, (packetByteBuf, schedulTickObject) -> {
            packetByteBuf.writeBlockPos(schedulTickObject.pos);
            packetByteBuf.writeLong(schedulTickObject.time);
            packetByteBuf.writeInt(schedulTickObject.priority);
            packetByteBuf.writeLong(schedulTickObject.subTick);
            packetByteBuf.writeString(schedulTickObject.name);
        });
        return buf;
    }
    @Unique
    private void processTicks(WorldTickScheduler<?> scheduler, List<SchedulTickObject> targetList, String typeKey,
                              long time, Set<ServerPlayerEntity> players, Map<Object, String> typeNameCache) {
        List<SchedulTickObject> tempList = new ArrayList<>();
        scheduler.chunkTickSchedulers.values().forEach(chunkTickScheduler -> {
            chunkTickScheduler.getQueueAsStream().forEach(orderedTick -> {
                if (orderedTick.triggerTick() - time > 0) {
                    List<ServerPlayerEntity> nearbyPlayers = getPlayersNearBy(
                            orderedTick.pos(), this.getGameRules().getInt(SCHEDULED_TICK_PACK_RANGE));
                    if (!nearbyPlayers.isEmpty()) {
                        players.addAll(nearbyPlayers);
                        String typeName = typeNameCache.computeIfAbsent(orderedTick.type(), key ->
                                typeKey.equals("Block")
                                        ? Text.translatable(((Block)orderedTick.type()).getTranslationKey()).getString()
                                        : Text.translatable(((Fluid)orderedTick.type()).getStateManager().getDefaultState().getBlockState().getBlock().getTranslationKey()).getString()
                        );
                        tempList.add(new SchedulTickObject(
                                orderedTick.pos(),
                                orderedTick.triggerTick() - time,
                                orderedTick.priority().getIndex(),
                                orderedTick.subTickOrder(),
                                typeName
                        ));
                    }
                }
            });
        });
        if(this.getServer().getGameRules().getBoolean(SORT_SUBORDER)) {
            tempList.sort(Comparator.comparingLong(SchedulTickObject::getTickOrder));
            for (int i = 0; i < tempList.size(); i++) {
                tempList.get(i).subTick = i + 1;
            }
        }
        targetList.addAll(tempList);
    }
    /*@Inject(method = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.BEFORE,ordinal = 0))
    public void tickBlockTickSchedulerStart(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if(ScheduledTickVisualizer.logManager != null && ScheduledTickVisualizer.logManager.ticks > 0){
            ScheduledTickVisualizerLogger.writeLogFile(ScheduledTickVisualizer.logManager.fileName,"|--ServerWorld:Ticking BlockTickScheduler..");
        }
    }
    @Inject(method = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.AFTER,ordinal = 0))
    public void tickBlockTickSchedulerEnd(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if(ScheduledTickVisualizer.logManager != null && ScheduledTickVisualizer.logManager.ticks > 0){
            ScheduledTickVisualizerLogger.writeLogFile(ScheduledTickVisualizer.logManager.fileName,"L_ServerWorld:Finished ticking BlockTickScheduler..");
        }
    }
    @Inject(method = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.BEFORE,ordinal = 1))
    public void tickFluidTickSchedulerStart(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if(ScheduledTickVisualizer.logManager != null && ScheduledTickVisualizer.logManager.ticks > 0){
            ScheduledTickVisualizerLogger.writeLogFile(ScheduledTickVisualizer.logManager.fileName,"|--ServerWorld:Ticking FluidTickScheduler...");
        }
    }
    @Inject(method = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V",
                    shift = At.Shift.AFTER,ordinal = 1))
    public void tickFluidTickSchedulerEnd(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if(ScheduledTickVisualizer.logManager != null && ScheduledTickVisualizer.logManager.ticks > 0){
            ScheduledTickVisualizerLogger.writeLogFile(ScheduledTickVisualizer.logManager.fileName,"L_ServerWorld:Finished ticking FluidTickScheduler..");
        }
    }*/

}
