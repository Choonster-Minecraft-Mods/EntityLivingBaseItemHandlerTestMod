package com.github.choonster.entitylivingbaseitemhandlertestmod;

import com.github.choonster.entitylivingbaseitemhandlertestmod.client.gui.GuiHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventHandler {

	/**
	 * Open the Entity Inventory GUI when a player right clicks a living entity (except for horses) while holding a stick.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void entityInteract(final PlayerInteractEvent.EntityInteract event) {
		final World world = event.getWorld();
		final Entity entity = event.getTarget();
		final EntityPlayer player = event.getEntityPlayer();

		if (entity instanceof EntityLivingBase && !(entity instanceof AbstractHorse) && player.getHeldItem(event.getHand()).getItem() == Items.STICK) {
			if (!world.isRemote) {
				player.openGui(EntityLivingBaseItemHandlerTestMod.INSTANCE, GuiHandler.ID_ENTITY_INVENTORY, world, entity.getEntityId(), 0, 0);
			}

			event.setCancellationResult(EnumActionResult.SUCCESS);
			event.setCanceled(true);
		}
	}
}
