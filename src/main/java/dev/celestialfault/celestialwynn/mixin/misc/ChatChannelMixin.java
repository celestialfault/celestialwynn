package dev.celestialfault.celestialwynn.mixin.misc;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.config.Config;
import dev.celestialfault.celestialwynn.enums.ChannelMode;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
abstract class ChatChannelMixin {
	@Shadow
	protected TextFieldWidget chatField;

	@Inject(method = "init", at = @At("TAIL"))
	public void celestialwynn$prefillChannelCommand(CallbackInfo ci) {
		if(!CelestialWynn.isOnWynn() || Config.getChannelMode() != ChannelMode.PREFILL) {
			return;
		}
		if(!chatField.getText().isEmpty()) {
			return;
		}

		var channel = Config.getChannel();
		if(channel.getCommand() != null) {
			chatField.setText(channel.getCommand() + " ");
		}
	}

	@Inject(method = "sendMessage", at = @At("HEAD"))
	public void celestialwynn$prependChannelCommand(CallbackInfo ci, @Local(argsOnly = true) LocalRef<String> chatText) {
		if(!CelestialWynn.isOnWynn() || Config.getChannelMode() != ChannelMode.ON_SEND) {
			return;
		}

		var channel = Config.getChannel();
		if(channel.getCommand() != null && !chatText.get().startsWith("/")) {
			// TODO ensure character limit? (does wynn strictly enforce that for commands like /p and /g?)
			chatText.set(channel.getCommand() + " " + chatText.get());
		}
	}
}
