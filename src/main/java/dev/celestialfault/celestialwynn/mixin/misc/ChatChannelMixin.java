package dev.celestialfault.celestialwynn.mixin.misc;

import dev.celestialfault.celestialwynn.CelestialWynn;
import dev.celestialfault.celestialwynn.config.Config;
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
		if(!CelestialWynn.isOnWynn()) {
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
}
