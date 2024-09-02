package dev.celestialfault.celestialwynn.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.celestialfault.celestialwynn.CelestialWynn
import dev.celestialfault.celestialwynn.config.Config
import dev.celestialfault.celestialwynn.enums.Channel
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.CommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object ChatCommand {
	private val CHANNELS: Map<String, Channel> = Channel.entries.toTypedArray().associateBy { it.name.lowercase() }
	private val CHANNEL_SUGGESTIONS = SuggestionProvider { _: CommandContext<FabricClientCommandSource>, builder: SuggestionsBuilder ->
		CommandSource.suggestMatching(CHANNELS.keys, builder)
	}

	@Suppress("UNUSED_PARAMETER")
	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, ignored: CommandRegistryAccess) {
		dispatcher.register(ClientCommandManager.literal("a")
			.then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
				.executes(this::allChat)))

		dispatcher.register(ClientCommandManager.literal("chat")
			.then(ClientCommandManager.argument("channel", StringArgumentType.string())
				.suggests(CHANNEL_SUGGESTIONS)
				.executes(this::chat)))
	}

	private fun allChat(ctx: CommandContext<FabricClientCommandSource>): Int {
		var message = StringArgumentType.getString(ctx, "message")
		if(message.startsWith('/')) {
			message = ".$message"
		}
		MinecraftClient.getInstance().networkHandler!!.sendChatMessage(message)
		return 0
	}

	@Suppress("SameReturnValue")
	private fun chat(ctx: CommandContext<FabricClientCommandSource>): Int {
		val input = StringArgumentType.getString(ctx, "channel").lowercase()
		// allow for inputs like '/chat a' by matching with startsWith to match the Hypixel /chat behavior, as that
		// is what this is designed to replicate.
		val channel = Channel.entries.toTypedArray().firstOrNull { it.name.lowercase().startsWith(input) }
		if(channel == null) {
			ctx.source.sendError(Text.translatable("celestialwynn.command.chat.unknown", input))
			return 0
		}

		Config.channel = channel
		Config.save()
		val channelText = Text.literal(channel.name).formatted(Formatting.GOLD)
		ctx.source.sendFeedback(CelestialWynn.prefix()
			.append(Text.translatable("celestialwynn.command.chat.set", channelText)))

		return 0
	}
}
