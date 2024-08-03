package dev.celestialfault.celestialwynn

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.registry.Registries
import net.minecraft.sound.SoundEvent
import kotlin.jvm.optionals.getOrNull

object Util {
	/**
	 * mojang, wtf is this.
	 */
	@JvmStatic
	fun getSoundFromPacket(packet: PlaySoundS2CPacket): SoundEvent? {
		val sound = packet.sound.keyOrValue
		return sound.left().map { Registries.SOUND_EVENT[it.value] }.orElseGet { sound.right().getOrNull() }
	}
}
