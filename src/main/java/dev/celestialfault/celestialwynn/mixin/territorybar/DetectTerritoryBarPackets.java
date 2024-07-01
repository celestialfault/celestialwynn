package dev.celestialfault.celestialwynn.mixin.territorybar;

import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.TerritoryBarHider;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.text.LiteralTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
abstract class DetectTerritoryBarPackets {
	@Inject(method = "onBossBar", at = @At("HEAD"))
	public void celestialwynn$detectTerritoryBar(BossBarS2CPacket packet, CallbackInfo ci) {
		if(!CelestialWynn.isOnWynn()) return;

		if(packet.action instanceof BossBarS2CPacket.AddAction addAction && addAction.name.getContent() instanceof LiteralTextContent literalText) {
			TerritoryBarHider.maybeHideBossBar(packet.uuid, literalText);
		} else if(packet.action instanceof BossBarS2CPacket.UpdateNameAction updateNameAction && updateNameAction.name.getContent() instanceof LiteralTextContent literalText) {
			TerritoryBarHider.maybeHideBossBar(packet.uuid, literalText);
		}
	}
}
