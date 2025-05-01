package me.incend1um.noinputlagtickrate.mixin.client;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Definition(id = "overlay", field = "Lnet/minecraft/client/Minecraft;overlay:Lnet/minecraft/client/gui/screens/Overlay;")
	@Expression("this.overlay == null")
	@ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
	boolean preventInputHandling(boolean original) {
		return false;
	}

	@Definition(id = "rightClickDelay", field = "Lnet/minecraft/client/Minecraft;rightClickDelay:I")
	@Expression("this.rightClickDelay > 0")
	@ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
	boolean preventSubtractionOfDelay(boolean original) {
		return false;
	}

	@Definition(id = "screen", field = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;")
	@Expression("this.screen != null")
	@ModifyExpressionValue(method = "tick", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
	boolean doNotSetMissTime(boolean original) {
		return false;
	}
}
