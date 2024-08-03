package dev.celestialfault.celestialwynn.mixin.misc;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/*
 * this is definitely a fix of all time.
 *
 * maybe a bit out of scope for this mod, but it makes my life a bit easier with not having to work around this
 * in /cw, so whatever.
 */
@Mixin(ChatScreen.class)
abstract class FixChatClosingOtherMenus {
	@WrapWithCondition(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
	public boolean celestialwynn$onlyCloseChatScreen(MinecraftClient instance, Screen screen) {
		return screen != null || instance.currentScreen == null || instance.currentScreen instanceof ChatScreen;
	}
}
