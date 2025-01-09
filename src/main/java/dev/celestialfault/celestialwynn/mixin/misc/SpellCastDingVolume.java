package dev.celestialfault.celestialwynn.mixin.misc;

import com.llamalad7.mixinextras.sugar.Local;
import dev.celestialfault.celestialwynn.config.Config;
import dev.celestialfault.celestialwynn.features.SpellDingVolume;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
abstract class SpellCastDingVolume {
	@Inject(method = "onPlaySound", at = @At("HEAD"), cancellable = true)
	public void celestialwynn$muteDing(PlaySoundS2CPacket packet, CallbackInfo ci) {
		float volume = Config.getSpellDingVolume();
		if(volume > 0f) return;
		if(SpellDingVolume.isDing(packet)) {
			ci.cancel();
		}
	}

	@ModifyArg(
		method = "onPlaySound",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/world/ClientWorld;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FFJ)V"
		),
		index = 6
	)
	public float celestialwynn$reduceDingVolume(float original, @Local(argsOnly = true) PlaySoundS2CPacket packet) {
		return SpellDingVolume.isDing(packet) ? original * Config.getSpellDingVolume() : original;
	}
}
