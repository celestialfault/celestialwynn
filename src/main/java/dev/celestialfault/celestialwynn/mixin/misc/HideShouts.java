package dev.celestialfault.celestialwynn.mixin.misc;

import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.config.Config;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(ChatHud.class)
abstract class HideShouts {
	private static final @Unique Pattern SHOUT_PATTERN = Pattern.compile("^[A-z0-9_]+ \\[[A-Z0-9]+] shouts: .*");

	@Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
	public void celestialwynn$hideShouts(Text message, CallbackInfo ci) {
		if(CelestialWynn.isOnWynn() && Config.getHideShouts() && SHOUT_PATTERN.matcher(Formatting.strip(message.asTruncatedString(96))).find()) {
			ci.cancel();
		}
	}
}
