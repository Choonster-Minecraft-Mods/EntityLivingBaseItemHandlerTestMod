package com.github.choonster.entitylivingbaseitemhandlertestmod.client.gui;

import com.github.choonster.entitylivingbaseitemhandlertestmod.client.gui.inventory.GuiEntityInventory;
import com.github.choonster.entitylivingbaseitemhandlertestmod.inventory.ContainerEntityInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
	public static final int ID_ENTITY_INVENTORY = 0;

	@Nullable
	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		switch (ID) {
			case ID_ENTITY_INVENTORY:
				final Entity entity = world.getEntityByID(x);

				if (entity instanceof EntityLivingBase) {
					return new ContainerEntityInventory((EntityLivingBase) entity, player);
				}

				break;
		}

		return null;
	}


	@Nullable
	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		switch (ID) {
			case ID_ENTITY_INVENTORY:
				final Entity entity = world.getEntityByID(x);

				if (entity instanceof EntityLivingBase) {
					return new GuiEntityInventory(new ContainerEntityInventory((EntityLivingBase) entity, player));
				}

				break;
		}

		return null;
	}
}
