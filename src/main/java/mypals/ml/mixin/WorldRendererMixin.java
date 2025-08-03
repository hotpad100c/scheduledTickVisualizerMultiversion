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

//#if MC >= 12100
//$$ import net.minecraft.client.render.RenderTickCounter;
//#endif

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
	@Inject(
			//#if MC >= 12103
			//$$ method = "method_62212",
			//#else
			method = "render",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12103
					//$$ target = "Lnet/minecraft/client/render/debug/DebugRenderer;renderLate(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;DDD)V"
					//#else
					target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/Camera;)V",
					ordinal = 0
					//#endif
			)
	)
	private void render(
			CallbackInfo ci,

					//#if MC < 12005
					//$$ @Local(argsOnly = true)MatrixStack matrixStack,
					//#elseif MC < 12103
			 		@Local MatrixStack matrixStack,
					//#else
					//$$ @Local MatrixStack stack
					//#endif

			//#if MC >= 12103
			//$$
			//#elseif MC > 12100
			@Local(argsOnly = true)RenderTickCounter tickCounter
			//#else
			//$$@Local(argsOnly = true) float tickDelta
			//#endif
	)
	{
		ScheduledTickVisualizerInfoRender.render(
				//#if MC >= 12103
				//$$ stack,
				//#else
				matrixStack,
				//#endif

				//#if MC >= 12103
				//$$ 1f
				//#elseif MC >= 12100
				tickCounter.getTickDelta(false)
				//#else
				//$$ tickDelta
				//#endif
		);
	}
}