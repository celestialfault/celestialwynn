package dev.celestialfault.celestialwynn.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// apply before most other mixins to avoid other mods that modify the player's fov interfering
@Mixin(value = AbstractClientPlayerEntity.class, priority = 900)
abstract class ModifyFovMultiplier {
	@ModifyExpressionValue(method = "getFovMultiplier", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;flying:Z"))
	public boolean celestialwynn$removeFlyingFovModifier(boolean original) {
		return original && !Config.getRemoveFlyingFovModifier();
	}

	// the ordinal ensures that we don't modify the spyglass FOV multiplier (which always returns a hardcoded 0.1f),
	// even though Wynn doesn't make use of spyglasses for anything.
	@ModifyReturnValue(method = "getFovMultiplier", at = @At(value = "RETURN", ordinal = 1))
	public float celestialwynn$alternativeFovScaling(float original) {
		var scaling = Config.getFovScaling();
		return scaling.calculate(original, (AbstractClientPlayerEntity)(Object)this);
	}
}
