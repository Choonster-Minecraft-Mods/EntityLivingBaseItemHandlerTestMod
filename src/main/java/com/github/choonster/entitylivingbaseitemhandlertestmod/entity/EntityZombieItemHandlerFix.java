package com.github.choonster.entitylivingbaseitemhandlertestmod.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

/**
 * An extension of {@link EntityZombie} that exposes {@link EntityLiving#inventoryArmor} and {@link EntityLiving#inventoryHands}
 * through {@link ICapabilityProvider#getCapability} instead of exposing {@link EntityLivingBase#handInventory} and {@link EntityLivingBase#armorArray},
 * which have their contents replaced each tick with the contents of the corresponding {@link EntityLiving} inventories in {@link EntityLivingBase#onUpdate}.
 *
 * @author Choonster
 */
public class EntityZombieItemHandlerFix extends EntityZombie {
	public EntityZombieItemHandlerFix(final World worldIn) {
		super(worldIn);
	}

	private final IItemHandlerModifiable armorHandler = new ItemStackHandler((NonNullList<ItemStack>) getArmorInventoryList());
	private final IItemHandlerModifiable handHandler = new ItemStackHandler((NonNullList<ItemStack>) getHeldEquipment());
	private final IItemHandlerModifiable joinedHandler = new CombinedInvWrapper(armorHandler, handHandler);

	@Override
	public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing) {
		return capability == ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing) {
		if (capability == ITEM_HANDLER_CAPABILITY) {
			if (facing == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(joinedHandler);
			} else if (facing.getAxis().isVertical()) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(handHandler);
			} else if (facing.getAxis().isHorizontal()) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(armorHandler);
			}
		}

		return super.getCapability(capability, facing);
	}
}
