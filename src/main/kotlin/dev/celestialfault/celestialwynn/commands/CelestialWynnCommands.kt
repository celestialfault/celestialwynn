package dev.celestialfault.celestialwynn.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.celestialfault.celestialwynn.CelestialWynn
import dev.celestialfault.celestialwynn.config.Config
import dev.celestialfault.celestialwynn.config.ConfigGUI
import dev.celestialfault.celestialwynn.enums.Channel
import dev.celestialfault.celestialwynn.enums.FOVScaling
import dev.celestialfault.commander.Commander
import dev.celestialfault.commander.annotations.Command
import dev.celestialfault.commander.annotations.Group
import dev.celestialfault.commander.annotations.RootCommand
import dev.celestialfault.commander.annotations.Suggestions
import dev.celestialfault.commander.client.ClientCommand
import dev.celestialfault.commander.client.ClientCommandGroup
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

@Suppress("unused")
@Group("celestialwynn", "cw")
object CelestialWynnCommands {
	private val FOV_SCALING: Map<String, FOVScaling> = FOVScaling.entries.toTypedArray().associateBy { it.name.lowercase() }
	private val CHANNELS: Map<String, Channel> = Channel.entries.toTypedArray().associateBy { it.name.lowercase() }

	object ChatSuggestionProvider : SuggestionProvider<FabricClientCommandSource> {
		override fun getSuggestions(context: CommandContext<FabricClientCommandSource>, builder: SuggestionsBuilder) =
			CommandSource.suggestMatching(CHANNELS.keys, builder)!!
	}

	object FovScalingSuggestionProvider : SuggestionProvider<FabricClientCommandSource> {
		override fun getSuggestions(context: CommandContext<FabricClientCommandSource>, builder: SuggestionsBuilder) =
			CommandSource.suggestMatching(FOV_SCALING.keys, builder)!!
	}

	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
		Commander.register(ClientCommandGroup(this), dispatcher)
		Commander.register(ClientCommand(this::chat, this), dispatcher)
	}

	@RootCommand
	fun openConfig(ctx: CommandContext<FabricClientCommandSource>) {
		val client = MinecraftClient.getInstance()
		client.send { client.setScreen(ConfigGUI.getConfigScreen(null)) }
	}

	@Command
	fun fov(ctx: CommandContext<FabricClientCommandSource>, function: @Suggestions(FovScalingSuggestionProvider::class) String) {
		val enumVal = FOV_SCALING[function]
		if(enumVal == null) {
			ctx.source.sendError(Text.translatable("celestialwynn.command.fov_scaling.unknown", function))
			return
		}

		Config.fovScaling = enumVal
		Config.save()
		ctx.source.sendFeedback(CelestialWynn.prefix().append(Text.translatable("celestialwynn.command.fov_scaling.set", function)))
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
