package dev.celestialfault.celestialwynn.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.CommandNode
import dev.celestialfault.celestialwynn.CelestialWynn
import dev.celestialfault.celestialwynn.config.Config
import dev.celestialfault.celestialwynn.config.ConfigGUI.getConfigScreen
import dev.celestialfault.celestialwynn.enums.FOVScaling
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.CommandSource
import net.minecraft.text.Text

@Suppress("SameReturnValue") // there's genuinely not much we can do here about this warning.
object CWCommand {
	private val FOV_SCALING: Map<String, FOVScaling> = FOVScaling.entries.toTypedArray().associateBy { it.name.lowercase() }
	private val SCALING_SUGGESTIONS = SuggestionProvider { _: CommandContext<FabricClientCommandSource>, builder: SuggestionsBuilder ->
		CommandSource.suggestMatching(FOV_SCALING.keys, builder)
	}

	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, ignored: CommandRegistryAccess) {
		val main: CommandNode<FabricClientCommandSource> =
			dispatcher.register(ClientCommandManager.literal("celestialwynn")
				// /celestialwynn fov <fov>
				.then(ClientCommandManager.literal("fov")
					.then(ClientCommandManager.argument("function", StringArgumentType.greedyString())
						.suggests(SCALING_SUGGESTIONS)
						.executes(this::scalingFunction))
					.build())
				// /celestialwynn
				.executes(this::openConfig))

		dispatcher.register(ClientCommandManager.literal("cw")
			.executes(this::openConfig)
			.redirect(main))
	}

	private fun openConfig(ctx: CommandContext<FabricClientCommandSource>): Int {
		MinecraftClient.getInstance().setScreen(getConfigScreen(null))
		return 0
	}

	private fun scalingFunction(ctx: CommandContext<FabricClientCommandSource>): Int {
		val function = StringArgumentType.getString(ctx, "function").lowercase()
		val enumVal = FOV_SCALING[function]
		if(enumVal == null) {
			ctx.source.sendError(Text.translatable("celestialwynn.command.fov_scaling.unknown", function))
			return 0
		}
		Config.fovScaling = enumVal
		Config.save()
		ctx.source.sendFeedback(CelestialWynn.prefix().append(Text.translatable("celestialwynn.command.fov_scaling.set", function)))
		return 0
	}
}
