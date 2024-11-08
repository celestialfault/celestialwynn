package dev.celestialfault.celestialwynn.config

import com.google.gson.JsonPrimitive
import dev.celestialfault.celestialwynn.enums.Channel
import dev.celestialfault.celestialwynn.enums.FOVScaling
import dev.isxander.yacl3.api.Binding
import me.celestialfault.celestialconfig.AbstractConfig
import me.celestialfault.celestialconfig.PrimitiveProperty
import me.celestialfault.celestialconfig.Property
import me.celestialfault.celestialconfig.properties.*
import net.fabricmc.loader.api.FabricLoader
import java.util.function.Consumer
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.jvm.isAccessible

object Config : AbstractConfig(FabricLoader.getInstance().configDir.resolve("celestial-wynn.json")) {
	@get:JvmStatic
	var fixSilverbullCapes by Property.boolean("fix_silverbull_capes", true).notNullable()
	@get:JvmStatic
	var hideTerritoryBar by Property.boolean("hide_territory_bar", false).notNullable()
	@get:JvmStatic
	var hideShouts by Property.boolean("hide_shouts", false).notNullable()
	@get:JvmStatic
	var spellDingVolume by Property.float("spell_volume", default = 1f, min = 0f, max = 1f).notNullable()
	@get:JvmStatic
	var fovScaling by Property.enum<FOVScaling>("fov_scaling", FOVScaling.VANILLA).notNullable()

	@get:JvmStatic
	var channel by Property.enum<Channel>("channel", Channel.ALL).notNullable()

	object ItemScaling : ObjectProperty<ItemScaling>("item_scaling") {
		@get:JvmStatic
		var scale by Property.float("scale", 1f, 0.1f, 2f).notNullable()

		@get:JvmStatic
		var x by Property.int("x", 0, -150, 150).notNullable()
		@get:JvmStatic
		var y by Property.int("y", 0, -150, 150).notNullable()
		@get:JvmStatic
		var z by Property.int("z", 0, -150, 50).notNullable()
	}

	@Suppress("unused")
	private val muteSpellMigration by MigrateProperty("mute_spells") {
		if(spellDingVolume != 1f) return@MigrateProperty
		if(it is JsonPrimitive && it.isBoolean) {
			spellDingVolume = if(it.asBoolean) 0f else 1f
		}
	}

	@Suppress("UNCHECKED_CAST")
	internal fun <T> KMutableProperty0<T>.binding(): Binding<T> {
		this.isAccessible = true
		return when(val delegate: Property<T> = this.getDelegate() as Property<T>) {
			is NoNullProperty<T> -> Binding.generic(delegate.wrapped.default, delegate::get, delegate::set)
			is PrimitiveProperty<T> -> Binding.generic(delegate.default, delegate::get, delegate::set as Consumer<T>)
			else -> {
				throw UnsupportedOperationException("property must be NoNull or Primitive")
			}
		}
	}
}
