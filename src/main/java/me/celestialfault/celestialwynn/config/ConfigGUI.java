package me.celestialfault.celestialwynn.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import lombok.experimental.UtilityClass;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.io.UncheckedIOException;

@UtilityClass
public final class ConfigGUI {
	public static Screen getConfigScreen(Screen parent) {
		return YetAnotherConfigLib.createBuilder()
			.title(Text.translatable("celestialwynn.name"))
			.category(ConfigCategory.createBuilder()
				.name(Text.translatable("celestialwynn.config.main"))
				.group(genericGroup())
				.group(itemScaling())
				.build())
			.save(() -> {
				try {
					Config.INSTANCE.save();
				} catch(IOException e) {
					throw new UncheckedIOException(e);
				}
			})
			.build().generateScreen(parent);
	}

	private static OptionGroup genericGroup() {
		return OptionGroup.createBuilder()
			.name(Text.translatable("celestialwynn.config.main"))
			.option(Option.<Boolean>createBuilder()
				.name(Text.translatable("celestialwynn.cape_fix"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.cape_fix.description")))
				.controller(TickBoxControllerBuilder::create)
				.binding(Binding.generic(true, Config.INSTANCE.fixSilverbullCapes::get, Config.INSTANCE.fixSilverbullCapes::set))
				.build())
			.option(Option.<Boolean>createBuilder()
				.name(Text.translatable("celestialwynn.hide_territory_bar"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.hide_territory_bar.description.line1")
					.append("\n\n")
					.append(Text.translatable("celestialwynn.hide_territory_bar.description.line2").formatted(Formatting.YELLOW))))
				.controller(TickBoxControllerBuilder::create)
				.binding(Binding.generic(false, Config.INSTANCE.hideTerritoryBar::get, Config.INSTANCE.hideTerritoryBar::set))
				.build())
			.option(Option.<FOVScaling>createBuilder()
				.name(Text.translatable("celestialwynn.fov_scaling"))
				.description(OptionDescription.of(Text.translatable("celestialwynn.fov_scaling.description.line1")
					.append("\n\n")
					.append(Text.translatable("celestialwynn.fov_scaling.description.line2"))))
				.controller(option -> EnumControllerBuilder.create(option)
					.enumClass(FOVScaling.class)
					.formatValue(value -> Text.translatable("celestialwynn.scaling." + value.name().toLowerCase())))
				.binding(Binding.generic(FOVScaling.VANILLA, Config.INSTANCE.fovScaling::get, Config.INSTANCE.fovScaling::set))
				.build())
			.build();
	}

	private static OptionGroup itemScaling() {
		return OptionGroup.createBuilder()
			.name(Text.translatable("celestialwynn.config.item_scale"))
			.option(Option.<Integer>createBuilder()
				.name(Text.translatable("celestialwynn.item.x"))
				.description(OptionDescription.EMPTY)
				.controller(option -> IntegerSliderControllerBuilder.create(option).range(-150, 150).step(1))
				.binding(Binding.generic(0, Config.INSTANCE.itemScaling.x::get, Config.INSTANCE.itemScaling.x::set))
				.build())
			.option(Option.<Integer>createBuilder()
				.name(Text.translatable("celestialwynn.item.y"))
				.description(OptionDescription.EMPTY)
				.controller(option -> IntegerSliderControllerBuilder.create(option).range(-150, 150).step(1))
				.binding(Binding.generic(0, Config.INSTANCE.itemScaling.y::get, Config.INSTANCE.itemScaling.y::set))
				.build())
			.option(Option.<Integer>createBuilder()
				.name(Text.translatable("celestialwynn.item.z"))
				.description(OptionDescription.EMPTY)
				.controller(option -> IntegerSliderControllerBuilder.create(option).range(-150, 50).step(1))
				.binding(Binding.generic(0, Config.INSTANCE.itemScaling.z::get, Config.INSTANCE.itemScaling.z::set))
				.build())
			.option(Option.<Float>createBuilder()
				.name(Text.translatable("celestialwynn.item.scale"))
				.description(OptionDescription.EMPTY)
				.controller(option -> FloatSliderControllerBuilder.create(option).range(0.1f, 2f).step(0.1f))
				.binding(Binding.generic(1f, Config.INSTANCE.itemScaling.scale::get, Config.INSTANCE.itemScaling.scale::set))
				.build())
			.build();
	}
}
