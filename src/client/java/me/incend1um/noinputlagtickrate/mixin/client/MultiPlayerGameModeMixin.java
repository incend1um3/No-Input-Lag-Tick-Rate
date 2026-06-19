package me.incend1um.noinputlagtickrate.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.incend1um.noinputlagtickrate.NoInputLagTickRateClient;
import me.incend1um.noinputlagtickrate.mixin.client.access.MinecraftAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
	@Shadow @Final private Minecraft minecraft;
	@Shadow private float destroyTicks;
	@Shadow private int destroyDelay;

	@Unique
	long lastTick = -1;

	@Redirect(method = "continueDestroyBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyDelay:I", opcode = Opcodes.PUTFIELD, ordinal = 0))
	void syncDestroyingDelayWithActualTickRate(MultiPlayerGameMode instance, int value) {
		if (NoInputLagTickRateClient.isOptedOut()) {
			this.destroyDelay = value;
			return;
		}

		if (((MinecraftAccess) this.minecraft).getClientTickCount() != lastTick) {
			this.destroyDelay--;
		}
	}

	@ModifyExpressionValue(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F"))
	float syncDestroyingWithActualTickRate(float original) {
		if (NoInputLagTickRateClient.isOptedOut()) {
			return original;
		}

		return original * (50.0f / this.minecraft.level.tickRateManager().millisecondsPerTick());
	}

	@Redirect(method = "continueDestroyBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;destroyTicks:F", opcode = Opcodes.PUTFIELD, ordinal = 0))
	void syncSoundWithActualTickRate(MultiPlayerGameMode instance, float value) {
		if (NoInputLagTickRateClient.isOptedOut()) {
			this.destroyTicks = value;
			return;
		}

		long currentTick;
		if ((currentTick = ((MinecraftAccess) this.minecraft).getClientTickCount()) != lastTick) {
			lastTick = currentTick;
			this.destroyTicks++;
		}
	}
}
