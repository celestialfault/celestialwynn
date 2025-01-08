package dev.celestialfault.celestialwynn.mixin.render;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.celestialfault.celestialwynn.CelestialWynn;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.Nullables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
abstract class FixCharacterSelectionMainHand {
	@ModifyReturnValue(method = "getMainArm", at = @At("RETURN"))
	public Arm celestialwynn$fixCharacterSelectionMainHand(Arm original) {
		var player = (PlayerEntity)(Object)this;
		if(player instanceof OtherClientPlayerEntity && player.getUuid().version() != 4 && CelestialWynn.isOnWynn() && CelestialWynn.isInCharacterSelection()) {
			return Nullables.mapOrElse(MinecraftClient.getInstance().player, PlayerEntity::getMainArm, original);
		}
		return original;
	}
}
