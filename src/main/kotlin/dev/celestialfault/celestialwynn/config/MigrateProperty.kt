package dev.celestialfault.celestialwynn.config

import com.google.gson.JsonElement
import me.celestialfault.celestialconfig.Property
import kotlin.reflect.KProperty

class MigrateProperty(override val key: String, private val migration: (JsonElement) -> Unit) : Property<Any?> {
	override fun load(element: JsonElement) {
		migration(element)
	}

	override fun save(): JsonElement? = null

	// TODO this is an ugly bodge for an issue with celestial-config not seemingly supporting properties as
	//		actual variables in kotlin; not sure why, but I don't have enough of an interest to properly hunt
	//		down this issue and fix it right now.
	//		so in the meantime, this quick and dirty workaround works well enough.
	operator fun getValue(config: Any, property: KProperty<*>): Any? = null
}
