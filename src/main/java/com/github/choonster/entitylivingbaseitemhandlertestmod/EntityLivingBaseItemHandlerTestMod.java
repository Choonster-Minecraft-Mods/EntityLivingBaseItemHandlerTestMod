package com.github.choonster.entitylivingbaseitemhandlertestmod;

import com.github.choonster.entitylivingbaseitemhandlertestmod.client.gui.GuiHandler;
import com.github.choonster.entitylivingbaseitemhandlertestmod.entity.EntityZombieItemHandlerFix;
import com.github.choonster.entitylivingbaseitemhandlertestmod.proxy.IProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = EntityLivingBaseItemHandlerTestMod.MODID, name = "EntityLivingBase ItemHandler Test Mod")
public class EntityLivingBaseItemHandlerTestMod {
	public static final String MODID = "entitylivingbaseitemhandlertestmod";

	@SidedProxy(clientSide = "com.github.choonster.entitylivingbaseitemhandlertestmod.proxy.CombinedClientProxy", serverSide = "com.github.choonster.entitylivingbaseitemhandlertestmod.proxy.DedicatedServerProxy")
	public static IProxy PROXY;

	@Instance
	public static EntityLivingBaseItemHandlerTestMod INSTANCE;

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		final ResourceLocation zombieID = new ResourceLocation(MODID, "zombie_item_handler_fix");
		EntityRegistry.registerModEntity(zombieID, EntityZombieItemHandlerFix.class, zombieID.toString(), 0, INSTANCE, 80, 3, true, 0xafaf, 0x799c65);

		PROXY.preInit();
	}
}
