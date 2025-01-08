package dev.celestialfault.celestialwynn.features

import dev.celestialfault.celestialwynn.CelestialWynn
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import org.lwjgl.glfw.GLFW

object OffHandItems {
	val LANTERN = KeyBinding("key.celestialwynn.lantern", GLFW.GLFW_KEY_UNKNOWN, "celestialwynn.name")
	val TORCH = KeyBinding("key.celestialwynn.torch", GLFW.GLFW_KEY_UNKNOWN, "celestialwynn.name")
	val COMPASS = KeyBinding("key.celestialwynn.compass", GLFW.GLFW_KEY_UNKNOWN, "celestialwynn.name")

	private var currentlyHolding: ItemStack? = null

	fun init() {
		KeyBindingHelper.registerKeyBinding(LANTERN)
		KeyBindingHelper.registerKeyBinding(TORCH)
		KeyBindingHelper.registerKeyBinding(COMPASS)
		ClientTickEvents.END_CLIENT_TICK.register(this::onTick)
	}

	private fun onTick(client: MinecraftClient) {
		if(!CelestialWynn.isOnWynn) {
			currentlyHolding = null
			return
		}

		when {
			LANTERN.wasPressed() -> equip(client, Items.LANTERN)
			TORCH.wasPressed() -> equip(client, Items.TORCH)
			COMPASS.wasPressed() -> equip(client, Items.COMPASS)
		}

		if(currentlyHolding == null) return
		client.player?.setStackInHand(Hand.OFF_HAND, currentlyHolding)
	}

	private fun equip(client: MinecraftClient, item: Item) {
		val item = if(currentlyHolding?.item == item) null else item
		currentlyHolding = item?.defaultStack?.also {
			it.set(DataComponentTypes.LORE, LoreComponent(listOf(
				Text.translatable(
					"celestialwynn.equippedOffhand",
					Text.translatable("celestialwynn.name").withColor(CelestialWynn.COLOR)
				).formatted(Formatting.DARK_GRAY)
			)))
		}
		client.player?.setStackInHand(Hand.OFF_HAND, currentlyHolding ?: Items.AIR.defaultStack)
	}
}
