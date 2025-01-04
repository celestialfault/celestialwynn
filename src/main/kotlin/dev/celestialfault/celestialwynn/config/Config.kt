package dev.celestialfault.celestialwynn.config

import dev.celestialfault.celestialconfig.AbstractConfig
import dev.celestialfault.celestialconfig.ObjectProperty
import dev.celestialfault.celestialconfig.Property
import dev.celestialfault.celestialconfig.Serializer
import dev.celestialfault.celestialwynn.enums.Channel
import dev.celestialfault.celestialwynn.enums.FOVScaling
import net.fabricmc.loader.api.FabricLoader

object Config : AbstractConfig(FabricLoader.getInstance().configDir.resolve("celestial-wynn.json")) {
	@get:JvmStatic
	var fixSilverbullCapes by Property.of("fix_silverbull_capes", true)
	@get:JvmStatic
	var hideTerritoryBar by Property.of("hide_territory_bar", false)
	@get:JvmStatic
	var hideShouts by Property.of("hide_shouts", false)
	@get:JvmStatic
	var spellDingVolume by Property.of("spell_volume", Serializer.number(0f, 1f), 1f)
	@get:JvmStatic
	var fovScaling by Property.of("fov_scaling", Serializer.enum(), FOVScaling.VANILLA)

	@get:JvmStatic
	var channel by Property.of("channel", Serializer.enum(), Channel.ALL)

	object ItemScaling : ObjectProperty<ItemScaling>("item_scaling") {
		@get:JvmStatic
		var scale by Property.of("scale", Serializer.number(0.1f, 2f), 1f)

		@get:JvmStatic
		var x by Property.of("x", Serializer.number(-150, 150), 0)
		@get:JvmStatic
		var y by Property.of("y", Serializer.number(-150, 150), 0)
		@get:JvmStatic
		var z by Property.of("z", Serializer.number(-150, 50), 0)
	}
}
