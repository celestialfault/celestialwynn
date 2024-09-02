package dev.celestialfault.celestialwynn.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// apply before most other mixins to avoid potentially breaking any zoom mods that might
// modify this for whatever reason
@Mixin(value = AbstractClientPlayerEntity.class, priority = 900)
abstract class ModifyFovMultiplier {
	// the ordinal ensures that we don't modify the spyglass FOV multiplier (which always returns a hardcoded 0.1f),
	// even though Wynn doesn't make use of spyglasses for anything.
	@ModifyReturnValue(method = "getFovMultiplier", at = @At(value = "RETURN", ordinal = 1))
	public float celestialwynn$alternativeFovScaling(float original) {
		var scaling = Config.getFovScaling();
		return scaling.calculate(original, (AbstractClientPlayerEntity)(Object)this);
	}
}
