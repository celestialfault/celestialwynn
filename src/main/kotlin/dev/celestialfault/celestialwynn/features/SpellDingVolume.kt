package dev.celestialfault.celestialwynn.features

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import kotlin.jvm.optionals.getOrNull

object SpellDingVolume {
	private val DING = Identifier.ofVanilla("entity.arrow.hit_player")

	private fun getSoundFromPacket(packet: PlaySoundS2CPacket): SoundEvent? {
		val sound = packet.sound.keyOrValue
		return sound.left().map { Registries.SOUND_EVENT[it.value] }.orElseGet { sound.right().getOrNull() }
	}

	@JvmStatic
	fun isDing(packet: PlaySoundS2CPacket): Boolean {
		val sound = getSoundFromPacket(packet) ?: return false
		return sound.id == DING && packet.category == SoundCategory.PLAYERS && packet.pitch == 0.5f
	}
}
