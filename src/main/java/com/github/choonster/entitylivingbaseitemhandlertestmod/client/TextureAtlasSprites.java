package com.github.choonster.entitylivingbaseitemhandlertestmod.client;

import com.github.choonster.entitylivingbaseitemhandlertestmod.EntityLivingBaseItemHandlerTestMod;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Stores this mod's {@link TextureAtlasSprite}s.
 *
 * @author Choonster
 */
public class TextureAtlasSprites {
	public static TextureAtlasSprite EMPTY_SLOT_SWORD;

	@Mod.EventBusSubscriber
	private static class EventHandler {
		@SubscribeEvent
		public static void textureStitch(TextureStitchEvent.Pre event) {
			EMPTY_SLOT_SWORD = event.getMap().registerSprite(new ResourceLocation(EntityLivingBaseItemHandlerTestMod.MODID, "items/empty_slot_sword"));
		}
	}
}
