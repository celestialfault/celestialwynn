package dev.celestialfault.celestialwynn.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions as BrigadierSuggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.celestialfault.celestialwynn.CelestialWynn
import dev.celestialfault.celestialwynn.config.Config
import dev.celestialfault.celestialwynn.enums.Channel
import dev.celestialfault.commander.Commander
import dev.celestialfault.commander.annotations.Command
import dev.celestialfault.commander.annotations.Suggestions
import dev.celestialfault.commander.client.ClientCommand
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.concurrent.CompletableFuture

object ChatCommand {
	private val CHANNELS: Map<String, Channel> = Channel.entries.toTypedArray().associateBy { it.name.lowercase() }

	object ChatSuggestionProvider : SuggestionProvider<FabricClientCommandSource> {
		override fun getSuggestions(context: CommandContext<FabricClientCommandSource>, builder: SuggestionsBuilder): CompletableFuture<BrigadierSuggestions> =
			CommandSource.suggestMatching(CHANNELS.keys, builder)
	}

	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
		Commander.register(ClientCommand(this::chat, this), dispatcher)
	}

	@Command("chat")
	fun chat(ctx: CommandContext<FabricClientCommandSource>, channel: @Suggestions(ChatSuggestionProvider::class) String) {
		val chat = Channel.entries.toTypedArray().firstOrNull { it.name.lowercase().startsWith(channel) }
		if(chat == null) {
			ctx.source.sendError(Text.translatable("celestialwynn.command.chat.unknown", channel))
			return
		}

		Config.channel = chat
		Config.save()
		val channelText = Text.literal(chat.name).formatted(Formatting.GOLD)
		ctx.source.sendFeedback(CelestialWynn.prefix()
			.append(Text.translatable("celestialwynn.command.chat.set", channelText)))

		return
	}
}
