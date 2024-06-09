package me.celestialfault.celestialwynn.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.celestialfault.celestialwynn.config.Config;
import me.celestialfault.celestialwynn.config.FOVScaling;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// apply before most other mixins to avoid potentially breaking any zoom mods that might
// modify this for whatever reason
@Mixin(value = AbstractClientPlayerEntity.class, priority = 750)
abstract class AbstractClientPlayerEntityMixin {
	// the ordinal ensures that we don't modify the spyglass FOV multiplier (which always returns a hardcoded 0.1f),
	// even though Wynn doesn't make use of spyglasses for anything.
	@ModifyReturnValue(method = "getFovMultiplier", at = @At(value = "RETURN", ordinal = 1))
	public float applyFOVScalingFunction(float original) {
		FOVScaling scaling = Config.INSTANCE.fovScaling.get();
		if(scaling == null) {
			return original;
		}

		return scaling.calculate(original, (AbstractClientPlayerEntity)(Object)this);
	}
}
