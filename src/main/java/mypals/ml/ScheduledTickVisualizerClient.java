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

import mypals.ml.command.ScheduledTickVisualizerCommandRegister;
import mypals.ml.config.ScheduledTickVisualizerConfig;
import mypals.ml.network.ScheduledTickDataPayload;
import net.fabricmc.api.ClientModInitializer;
import java.util.ArrayList;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
//#if MC >= 12109
//$$ import net.minecraft.util.Identifier;
//#endif
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static mypals.ml.config.ScheduledTickVisualizerConfig.sortSubOrderInfo;
import static org.apache.commons.lang3.ArrayUtils.isSorted;

public class ScheduledTickVisualizerClient implements ClientModInitializer {
	public static float textSize = 0.012f;
	private KeyBinding viewOrderKeyBindingUp;
	private KeyBinding viewOrderKeyBindingDown;
	private GLFWScrollCallback scrollCallback;
	public static int orderViewerIndex = 0;
	private static boolean viewOrderKeyPressed = false;
	private static final Comparator<SchedulTickObject> TICK_COMPARATOR =
			Comparator.comparingLong(t -> t.getTickOrder());
	//#if MC < 12109
	private static final String KEY_CATEGORY = "category.scheduledTickVisualizer.keys";
	//#else
	//$$ private static final KeyBinding.Category KEY_CATEGORY = KeyBinding.Category.create(Identifier.of("scheduledtickvisualizer", "keys"));
	//#endif


	@Override
	public void onInitializeClient() {
		UpadteSettings();

		viewOrderKeyBindingUp = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.scheduledTickVisualizer.up",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UP,
				KEY_CATEGORY
		));
		viewOrderKeyBindingDown = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.scheduledTickVisualizer.down",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_DOWN,
				KEY_CATEGORY
		));
		ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess) -> {
			ScheduledTickVisualizerCommandRegister.register(commandDispatcher);
		});
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.currentScreen == null && viewOrderKeyBindingDown.wasPressed()) {
				orderViewerIndex--;
			}
			if (client.currentScreen == null && viewOrderKeyBindingUp.wasPressed()) {
				orderViewerIndex++;
			}
		});
		//#if MC < 12005
		//$$ ClientPlayNetworking.registerGlobalReceiver(ScheduledTickVisualizer.TICK_PACKET_ID, (client, handler, buf, responseSender)->{
		//$$	String type=buf.readString();
		//$$	List<SchedulTickObject> ticks=buf.readCollection(ArrayList::new,
		//$$			packetByteBuf -> new SchedulTickObject(
		//$$					packetByteBuf.readBlockPos(),
		//$$					packetByteBuf.readLong(),
		//$$					packetByteBuf.readInt(),
		//$$					packetByteBuf.readLong(),
		//$$					packetByteBuf.readString()
		//$$			));
		//$$	if (sortSubOrderInfo && !isSorted(ticks)) {
		//$$		ticks.sort(TICK_COMPARATOR);
		//$$		int[] index = {1};
		//$$		ticks.forEach(t -> t.subTick = index[0]++);
		//$$	}
		//$$	MinecraftClient.getInstance().execute(()-> {
		//$$		if (Objects.equals(type, "Block")) {
		//$$			ScheduledTickVisualizerInfoRender.setScheduledTicksBlock(ticks);
		//$$		} else if (Objects.equals(type, "Fluid")) {
		//$$			ScheduledTickVisualizerInfoRender.setScheduledTicksFluid(ticks);
		//$$		}
		//$$	});
		//$$});
		//#else
		ClientPlayNetworking.registerGlobalReceiver(ScheduledTickDataPayload.ID, (payload, context) -> {
			List<SchedulTickObject> ticks = payload.ticks();
			if (sortSubOrderInfo && !isSorted(ticks)) {
				ticks.sort(TICK_COMPARATOR);
				int[] index = {1};
				ticks.forEach(t -> t.subTick = index[0]++);
			}
			MinecraftClient.getInstance().execute(()-> {
				if (Objects.equals(payload.type(), "Block")) {
					ScheduledTickVisualizerInfoRender.setScheduledTicksBlock(ticks);
				} else if (Objects.equals(payload.type(), "Fluid")) {
					ScheduledTickVisualizerInfoRender.setScheduledTicksFluid(ticks);
				}
			});
		});
		//#endif
	}
	private static boolean isSorted(List<SchedulTickObject> ticks) {
		for (int i = 1; i < ticks.size(); i++) {
			if (ticks.get(i - 1).getTickOrder() > ticks.get(i).getTickOrder()) {
				return false;
			}
		}
		return true;
	}
	public static void UpadteSettings()
	{
		var instance = ScheduledTickVisualizerConfig.CONFIG_HANDLER;
		instance.load();
	}
}
