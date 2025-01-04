package dev.celestialfault.celestialwynn

import com.mojang.logging.LogUtils
import dev.celestialfault.celestialwynn.commands.CWCommand
import dev.celestialfault.celestialwynn.commands.ChatCommand
import dev.celestialfault.celestialwynn.config.Config
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.slf4j.Logger
import java.io.IOException

object CelestialWynn : ClientModInitializer {
	val LOGGER: Logger = LogUtils.getLogger()
	@JvmStatic val isOnWynn: Boolean
		get() = MinecraftClient.getInstance().networkHandler?.brand == "Wynn"

	fun prefix(): MutableText =
		Text.empty()
			.append(Text.translatable("celestialwynn.prefix").setStyle(Style.EMPTY.withColor(0xAE3EC8).withBold(true)))
			.append(Text.literal(" ⟫ ").formatted(Formatting.DARK_GRAY))

	override fun onInitializeClient() {
		try {
			Config.load()
		} catch(e: IOException) {
			LOGGER.warn("Failed to load config file", e)
		}

		ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			CWCommand.register(dispatcher)
			ChatCommand.register(dispatcher)
		}
	}
}
