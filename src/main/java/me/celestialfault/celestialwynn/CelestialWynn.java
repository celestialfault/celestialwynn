package me.celestialfault.celestialwynn;

import com.mojang.logging.LogUtils;
import me.celestialfault.celestialwynn.config.Config;
import me.celestialfault.celestialwynn.config.ConfigGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;

import java.io.IOException;

public class CelestialWynn implements ClientModInitializer {

	public static final Logger LOGGER = LogUtils.getLogger();
	public static boolean isOnWynn = false;
	public static boolean openConfigUi = false;

	@Override
	public void onInitializeClient() {
		try {
			Config.INSTANCE.load();
		} catch(IOException e) {
			LOGGER.warn("Failed to load config file", e);
		}
		ClientPlayConnectionEvents.JOIN.register(this::onJoin);
		ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect);
		ClientCommandRegistrationCallback.EVENT.register(new CWCommand()::register);
		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
	}

	private void onTick(MinecraftClient client) {
		if(openConfigUi) {
			client.setScreen(ConfigGUI.getConfigScreen(null));
			openConfigUi = false;
		}
	}

	private void onJoin(ClientPlayNetworkHandler handler, PacketSender ignored, MinecraftClient client) {
		ServerInfo info = handler.getServerInfo();
		if(info != null && info.address.endsWith(".wynncraft.com")) {
			LOGGER.info("Detected joining Wynncraft");
			isOnWynn = true;
		}
	}

	private void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
		isOnWynn = false;
	}

	public static MutableText prefix() {
		return Text.empty().append(
			Text.translatable("celestialwynn.prefix")
				.setStyle(Style.EMPTY.withColor(0xAE3EC8).withBold(true)))
			.append(Text.literal(" > ").formatted(Formatting.DARK_GRAY));
	}
}
