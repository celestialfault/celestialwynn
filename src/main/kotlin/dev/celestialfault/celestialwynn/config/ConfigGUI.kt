package dev.celestialfault.celestialwynn.config

import dev.celestialfault.celestialwynn.enums.FOVScaling
import dev.celestialfault.celestialwynn.config.Config.binding
import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.round
import kotlin.reflect.KClass

private val waveyCapesInstalled = FabricLoader.getInstance().isModLoaded("waveycapes")

object ConfigGUI {
	@JvmStatic
	fun getConfigScreen(parent: Screen?): Screen {
		return YetAnotherConfigLib.createBuilder()
			.title(Text.translatable("celestialwynn.name"))
			.category(
				ConfigCategory.createBuilder()
					.name(Text.translatable("celestialwynn.config.main"))
					.group(bugFixes())
					.group(chat())
					.group(itemScaling())
					.group(misc())
					.build()
			)
			.save(Config::save)
			.build().generateScreen(parent)
	}

	private fun bugFixes(): OptionGroup {
		return OptionGroup.createBuilder()
			.name(Text.translatable("celestialwynn.config.bug_fixes"))
			.description(OptionDescription.of(Text.translatable("celestialwynn.config.bug_fixes.description")))
			.option(Option.createBuilder<Boolean>()
				.name(Text.translatable("celestialwynn.cape_fix"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.cape_fix.description")
					.also {
						if(waveyCapesInstalled) {
							it.append("\n\n")
								.append(Text.translatable("celestialwynn.cape_fix.wavey_capes_installed"))
						}
					}))
				.controller(TickBoxControllerBuilder::create)
				.available(!waveyCapesInstalled)
				.binding(Config::fixSilverbullCapes.binding())
				.build())
			.build()
	}

	private fun chat(): OptionGroup {
		return OptionGroup.createBuilder()
			.name(Text.translatable("celestialwynn.config.chat"))
			.description(OptionDescription.of(Text.translatable("celestialwynn.config.chat.description")))
			.option(Option.createBuilder<Boolean>()
				.name(Text.translatable("celestialwynn.hide_shouts"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.hide_shouts.description")))
				.controller(TickBoxControllerBuilder::create)
				.binding(Config::hideShouts.binding())
				.build())
			.build()
	}

	private fun misc(): OptionGroup {
		return OptionGroup.createBuilder()
			.name(Text.translatable("celestialwynn.config.misc"))
			.description(OptionDescription.of(Text.translatable("celestialwynn.config.misc.description")))
			.option(Option.createBuilder<Boolean>()
				.name(Text.translatable("celestialwynn.hide_territory_bar"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.hide_territory_bar.description.line1")
					.append("\n\n")
					.append(Text.translatable("celestialwynn.hide_territory_bar.description.line2")
						.formatted(Formatting.YELLOW))))
				.controller(TickBoxControllerBuilder::create)
				.binding(Config::hideTerritoryBar.binding())
				.build())
			.option(Option.createBuilder<Float>()
				.name(Text.translatable("celestialwynn.mute_spells"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.mute_spells.description")))
				.controller {
					FloatSliderControllerBuilder.create(it)
						.range(0f, 1f)
						.step(0.01f)
						.formatValue { v -> Text.literal("${round(v * 100f).toInt()}%") }
				}
				.binding(Config::spellDingVolume.binding())
				.build())
			.option(Option.createBuilder<FOVScaling>()
				.name(Text.translatable("celestialwynn.fov_scaling"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.fov_scaling.description.line1")
					.append("\n\n")
					.append(Text.translatable("celestialwynn.fov_scaling.description.line2"))))
				.controller {
					EnumControllerBuilder.create(it)
						.enumClass(FOVScaling::class.java)
						.formatValue { Text.translatable("celestialwynn.scaling." + it.name.lowercase()) }
				}
				.binding(Config::fovScaling.binding())
				.build())
			.build()
	}

	private fun itemScaling(): OptionGroup {
		return OptionGroup.createBuilder()
			.name(Text.translatable("celestialwynn.config.item_scale"))
			.description(OptionDescription.of(Text.translatable("celestialwynn.config.item_scale.description")))
			.option(Option.createBuilder<Int>()
				.name(Text.translatable("celestialwynn.item.x"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.item.x.description")))
				.controller { option: Option<Int> -> IntegerSliderControllerBuilder.create(option).range(-150, 150).step(1) }
				.binding(Config.ItemScaling::x.binding())
				.build())
			.option(Option.createBuilder<Int>()
				.name(Text.translatable("celestialwynn.item.y"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.item.y.description")))
				.controller { option: Option<Int> -> IntegerSliderControllerBuilder.create(option).range(-150, 150).step(1) }
				.binding(Config.ItemScaling::y.binding())
				.build())
			.option(Option.createBuilder<Int>()
				.name(Text.translatable("celestialwynn.item.z"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.item.z.description")))
				.controller { option: Option<Int> -> IntegerSliderControllerBuilder.create(option).range(-150, 50).step(1) }
				.binding(Config.ItemScaling::z.binding())
				.build())
			.option(Option.createBuilder<Float>()
				.name(Text.translatable("celestialwynn.item.scale"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.item.scale.description")))
				.controller { option: Option<Float> -> FloatSliderControllerBuilder.create(option).range(0.1f, 2f).step(0.1f) }
				.binding(Config.ItemScaling::scale.binding())
				.build())
			.build()
	}
}
