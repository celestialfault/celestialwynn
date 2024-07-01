package dev.celestialfault.celestialwynn

import com.mojang.brigadier.CommandDispatcher
import com.mojang.logging.LogUtils
import dev.celestialfault.celestialwynn.config.Config
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.slf4j.Logger
import java.io.IOException

object CelestialWynn : ClientModInitializer {
	@JvmField val LOGGER: Logger = LogUtils.getLogger()
	@JvmStatic var isOnWynn: Boolean = false
		private set

	fun prefix(): MutableText {
		return Text.empty()
			.append(Text.translatable("celestialwynn.prefix").setStyle(Style.EMPTY.withColor(0xAE3EC8).withBold(true)))
			.append(Text.literal(" > ").formatted(Formatting.DARK_GRAY))
	}

	override fun onInitializeClient() {
		try {
			Config.load()
		} catch (e: IOException) {
			LOGGER.warn("Failed to load config file", e)
		}
		ClientPlayConnectionEvents.JOIN.register(this::onJoin)
		ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect)
		ClientCommandRegistrationCallback.EVENT.register(CWCommand::register)
	}

	@Suppress("UNUSED_PARAMETER")
	private fun onJoin(handler: ClientPlayNetworkHandler, ignored: PacketSender, client: MinecraftClient) {
		val info = handler.serverInfo
		if (info != null && info.address.endsWith(".wynncraft.com")) {
			LOGGER.info("Detected joining Wynncraft")
			isOnWynn = true
		}
	}

	@Suppress("UNUSED_PARAMETER")
	private fun onDisconnect(handler: ClientPlayNetworkHandler, client: MinecraftClient) {
		isOnWynn = false
	}
}
