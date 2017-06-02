package com.github.choonster.entitylivingbaseitemhandlertestmod.proxy;

import com.github.choonster.entitylivingbaseitemhandlertestmod.EntityLivingBaseItemHandlerTestMod;
import com.github.choonster.entitylivingbaseitemhandlertestmod.entity.EntityZombieItemHandlerFix;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class CombinedClientProxy implements IProxy {
	@Override
	public void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieItemHandlerFix.class, renderManager -> new RenderZombie(renderManager) {
			private final ResourceLocation ENTITY_TEXTURE = new ResourceLocation(EntityLivingBaseItemHandlerTestMod.MODID, "textures/entity/zombie_item_handler_fix.png");

			@Override
			protected ResourceLocation getEntityTexture(final EntityZombie entity) {
				return ENTITY_TEXTURE;
			}
		});
	}
}
