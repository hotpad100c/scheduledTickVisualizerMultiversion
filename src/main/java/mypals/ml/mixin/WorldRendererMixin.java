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

import com.llamalad7.mixinextras.sugar.Local;
import mypals.ml.ScheduledTickVisualizerInfoRender;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12102
//$$ import org.joml.Matrix4f;
//#endif

//#if MC >= 12100
//$$ import net.minecraft.client.render.RenderTickCounter;
//#endif

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
	@Inject(
			//#if MC >= 12111
			//$$ method = "render",
			//#elseif MC >= 12110
			//$$ method = "method_72915",
			//#else
			method = "render",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12111
					//$$ target = "Lnet/minecraft/client/render/debug/DebugRenderer;renderLate(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V"
					//#elseif MC >= 12110
					//$$ target = "Lnet/minecraft/client/render/debug/DebugRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/Frustum;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDDZ)V"
					//#elseif MC >= 12106
					//$$ target = "Lnet/minecraft/client/render/WorldRenderer;renderLateDebug(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/util/math/Vec3d;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"
					//#elseif MC >= 12103
					//$$ target = "Lnet/minecraft/client/render/WorldRenderer;renderLateDebug(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/client/render/Fog;)V"
					//#else
					target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/Camera;)V",
					ordinal = 0
					//#endif
			)
	)
	private void render(
			CallbackInfo ci
			/* --- Capture Matrix Data --- */
			//#if MC >= 12110
			//$$ , @Local MatrixStack capturedStack
			//#elseif MC >= 12102
			//$$ , @Local(argsOnly = true, ordinal = 0) Matrix4f capturedMatrix
			//#else
			, @Local MatrixStack capturedStack
			//#endif
			/* --- Capture Timing Data --- */
			//#if MC >= 12110
			//$$ // For MC 1.21.10+, use a fixed delta of 1.0f
			//#elseif MC >= 12100
			//$$ , @Local(argsOnly = true) RenderTickCounter capturedCounter
			//#else
			, @Local(argsOnly = true) float capturedDelta
			//#endif
	) {
		/* --- Standardize MatrixStack --- */
		//#if MC >= 12110
		//$$ // For MC 1.21.10+, capture the MatrixStack from the frame pass lambda
		//$$ MatrixStack finalStack = capturedStack;
		//#elseif MC >= 12102
		//$$ MatrixStack finalStack = new MatrixStack();
		//$$ finalStack.multiplyPositionMatrix(capturedMatrix);
		//#else
		MatrixStack finalStack = capturedStack;
		//#endif

		/* --- Standardize Delta --- */
		//#if MC >= 12110
		//$$ float finalDelta = 1.0f;
		//#elseif MC >= 12100
		//$$ float finalDelta = capturedCounter.getTickDelta(false);
		//#else
		float finalDelta = capturedDelta;
		//#endif

		// Call the mod's rendering logic using the standardized variables
		ScheduledTickVisualizerInfoRender.render(finalStack, finalDelta);
	}
}