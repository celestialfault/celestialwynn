package dev.celestialfault.celestialwynn.enums

enum class Channel(val command: String?) {
	ALL(null),
	PARTY("/p"),
	GUILD("/g")
}
