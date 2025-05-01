package me.incend1um.noinputlagtickrate.mixin.client.access;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftAccess {
	@Invoker
	void invokeHandleKeybinds();

	@Accessor("rightClickDelay")
	int getRightClickDelay();

	@Accessor("rightClickDelay")
	void setRightClickDelay(int delay);

	@Accessor("clientTickCount")
	long getClientTickCount();
}
