package me.celestialfault.celestialwynn;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import me.celestialfault.celestialwynn.config.Config;
import me.celestialfault.celestialwynn.config.ConfigGUI;
import me.celestialfault.celestialwynn.config.FOVScaling;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SameReturnValue") // there's genuinely not much we can do here about this warning.
public class CWCommand {
	private static final Map<String, FOVScaling> FUNCTIONS = new HashMap<>() {{
		Arrays.stream(FOVScaling.values()).forEach(func -> put(func.name().toLowerCase().replace('_', ' '), func));
	}};

	@SuppressWarnings("CodeBlock2Expr")
	private static final SuggestionProvider<FabricClientCommandSource> SCALING_FUNCTIONS = (context, builder) -> {
		return CommandSource.suggestMatching(FUNCTIONS.keySet(), builder);
	};

	public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
		CommandNode<FabricClientCommandSource> main = dispatcher.register(ClientCommandManager.literal("celestialwynn")
			// /celestialwynn fov <fov>
			.then(ClientCommandManager.literal("fov")
				.then(ClientCommandManager.argument("function", StringArgumentType.greedyString())
					.suggests(SCALING_FUNCTIONS)
					.executes(this::scalingFunction))
				.build())
			// /celestialwynn territorybar
			.then(ClientCommandManager.literal("territorybar").executes(this::toggleTerritoryBar).build())
			.then(ClientCommandManager.literal("tb").executes(this::toggleTerritoryBar).build())
			// /celestialwynn
			.executes(this::openConfig));

		dispatcher.register(ClientCommandManager.literal("cw").executes(this::openConfig).redirect(main));
	}

	private int openConfig(CommandContext<FabricClientCommandSource> ctx) {
		// this is required as if we attempt to open the config screen here, the chat screen will
		// simply close it; instead, we need to do this in the next client tick after the chat
		// screen has already been closed.
		CelestialWynn.openConfigUi = true;
		return 0;
	}

	private int toggleTerritoryBar(CommandContext<FabricClientCommandSource> ctx) {
		boolean newValue = !Config.INSTANCE.hideTerritoryBar.get();
		Config.INSTANCE.hideTerritoryBar.set(newValue);
		try {
			Config.INSTANCE.save();
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
		ctx.getSource().sendFeedback(CelestialWynn.prefix().append(Text.translatable("celestialwynn.command.hide_territory_bar." + (newValue ? "hidden" : "shown"))));
		return 0;
	}

	private int scalingFunction(CommandContext<FabricClientCommandSource> ctx) {
		String function = StringArgumentType.getString(ctx, "function").toLowerCase();
		if(!FUNCTIONS.containsKey(function)) {
			ctx.getSource().sendError(Text.translatable("celestialwynn.command.fov_scaling.unknown", function));
			return 0;
		}
		Config.INSTANCE.fovScaling.set(FUNCTIONS.get(function));
		try {
			Config.INSTANCE.save();
		} catch(IOException e) {
			throw new UncheckedIOException(e);
		}
		ctx.getSource().sendFeedback(CelestialWynn.prefix().append(Text.translatable("celestialwynn.command.fov_scaling.set", function)));
		return 0;
	}
}
