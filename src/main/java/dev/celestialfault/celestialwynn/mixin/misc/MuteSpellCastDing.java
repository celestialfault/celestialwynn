package dev.celestialfault.celestialwynn.mixin.misc;

import dev.celestialfault.celestialwynn.Util;
import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MuteSpellCastDing {
	/*? if >=1.21*/
	private static final @Unique Identifier DING = Identifier.ofVanilla("entity.arrow.hit_player");
	/*? if <1.21*/
	/*private static final @Unique Identifier DING = new Identifier("minecraft", "entity.arrow.hit_player");*/

	@Inject(method = "onPlaySound", at = @At("HEAD"), cancellable = true)
	public void celestialwynn$muteDing(PlaySoundS2CPacket packet, CallbackInfo ci) {
		if(!Config.getMuteSpellCastDing()) return;
		SoundEvent sound = Util.getSoundFromPacket(packet);
		if(sound == null) {
			return;
		}
		if(sound.getId().equals(DING) && packet.getCategory() == SoundCategory.PLAYERS && packet.getPitch() == 0.5f) {
			ci.cancel();
		}
	}
}
