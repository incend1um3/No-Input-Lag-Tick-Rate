package me.incend1um.noinputlagtickrate;

import me.incend1um.noinputlagtickrate.mixin.client.access.MinecraftAccess;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

public class NoInputLagTickRateClient implements ClientModInitializer {
	public static DeltaTracker.Timer inputDeltaTracker = new DeltaTracker.Timer(20.0f, 0, NoInputLagTickRateClient::tickTargetMspt);

	@Override
	public void onInitializeClient() {
		WorldRenderEvents.START_MAIN.register((worldRenderContext) -> {
			Minecraft mc = Minecraft.getInstance();

			if (mc.screen != null) {
				mc.missTime = 1000;
			}

			if (mc.getOverlay() == null && mc.screen == null) {
				int ticksDue = Math.min(10, inputDeltaTracker.advanceTime(Util.getMillis(), true));
				for (int i = 0; i < ticksDue; i++) {
					MinecraftAccess access = (MinecraftAccess) mc;

					int rightClickDelay = access.getRightClickDelay();
					if (rightClickDelay > 0) {
						access.setRightClickDelay(rightClickDelay - 1);
					}
					access.invokeHandleKeybinds();
				}

				if (mc.missTime > 0) {
					mc.missTime--;
				}
			}
		});
	}

	static float tickTargetMspt(float defaultValue) {
		return defaultValue;
	}
}