package dev.celestialfault.celestialwynn

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.mojang.brigadier.tree.CommandNode
import dev.celestialfault.celestialwynn.config.Config
import dev.celestialfault.celestialwynn.config.ConfigGUI.getConfigScreen
import dev.celestialfault.celestialwynn.config.FOVScaling
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.CommandSource
import net.minecraft.text.Text
import java.io.IOException
import java.io.UncheckedIOException
import java.util.*

@Suppress("SameReturnValue") // there's genuinely not much we can do here about this warning.
object CWCommand {
	private val FUNCTIONS: Map<String, FOVScaling> = buildMap {
		FOVScaling.entries.toTypedArray().forEach {
			put(it.name.lowercase().replace("_", ""), it)
		}
	}

	private val SCALING_FUNCTIONS = SuggestionProvider { context: CommandContext<FabricClientCommandSource?>?, builder: SuggestionsBuilder? ->
		CommandSource.suggestMatching(FUNCTIONS.keys, builder)
	}

	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>, ignored: CommandRegistryAccess) {
		val main: CommandNode<FabricClientCommandSource> =
			dispatcher.register(ClientCommandManager.literal("celestialwynn")
				// /celestialwynn fov <fov>
				.then(ClientCommandManager.literal("fov")
					.then(ClientCommandManager.argument("function", StringArgumentType.greedyString())
						.suggests(SCALING_FUNCTIONS)
						.executes(this::scalingFunction))
					.build())
				// /celestialwynn territorybar
				.then(ClientCommandManager.literal("territorybar")
					.executes(this::toggleTerritoryBar)
					.build())
				.then(ClientCommandManager.literal("tb")
					.executes(this::toggleTerritoryBar)
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

	private fun toggleTerritoryBar(ctx: CommandContext<FabricClientCommandSource>): Int {
		Config.hideTerritoryBar = !Config.hideTerritoryBar
		Config.save()
		ctx.source.sendFeedback(CelestialWynn.prefix()
			.append(Text.translatable("celestialwynn.command.hide_territory_bar." + (if(Config.hideTerritoryBar) "hidden" else "shown"))))
		return 0
	}

	private fun scalingFunction(ctx: CommandContext<FabricClientCommandSource>): Int {
		val function = StringArgumentType.getString(ctx, "function").lowercase(Locale.getDefault())
		if(!FUNCTIONS.containsKey(function)) {
			ctx.source.sendError(Text.translatable("celestialwynn.command.fov_scaling.unknown", function))
			return 0
		}
		Config.fovScaling = FUNCTIONS[function]!!
		try {
			Config.save()
		} catch(e: IOException) {
			throw UncheckedIOException(e)
		}
		ctx.source.sendFeedback(
			CelestialWynn.prefix().append(Text.translatable("celestialwynn.command.fov_scaling.set", function))
		)
		return 0
	}
}
