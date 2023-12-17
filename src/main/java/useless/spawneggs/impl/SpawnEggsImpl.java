package useless.spawneggs.impl;

import net.minecraft.core.entity.animal.EntityChicken;
import net.minecraft.core.entity.animal.EntityCow;
import net.minecraft.core.entity.animal.EntityFireflyCluster;
import net.minecraft.core.entity.animal.EntityPig;
import net.minecraft.core.entity.animal.EntitySheep;
import net.minecraft.core.entity.animal.EntitySquid;
import net.minecraft.core.entity.animal.EntityWolf;
import net.minecraft.core.entity.monster.EntityArmoredZombie;
import net.minecraft.core.entity.monster.EntityCreeper;
import net.minecraft.core.entity.monster.EntityGhast;
import net.minecraft.core.entity.monster.EntityGiant;
import net.minecraft.core.entity.monster.EntityHuman;
import net.minecraft.core.entity.monster.EntityPigZombie;
import net.minecraft.core.entity.monster.EntityScorpion;
import net.minecraft.core.entity.monster.EntitySkeleton;
import net.minecraft.core.entity.monster.EntitySlime;
import net.minecraft.core.entity.monster.EntitySnowman;
import net.minecraft.core.entity.monster.EntitySpider;
import net.minecraft.core.entity.monster.EntityZombie;
import net.minecraft.core.entity.vehicle.EntityBoat;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.item.Item;
import useless.spawneggs.SpawnEggsEntrypoint;

import java.awt.*;

import static useless.spawneggs.SpawnEggsMod.assignPickEntity;
import static useless.spawneggs.SpawnEggsMod.createSpawnEgg;
import static useless.spawneggs.SpawnEggsMod.createSpawnEggWithNameOverride;

public class SpawnEggsImpl implements SpawnEggsEntrypoint {
    @Override
    public void onLoad() {
        createSpawnEgg(EntityChicken.class, new Color(0XA1A1A1), new Color(0XFF0000));
        createSpawnEgg(EntityCow.class, new Color(0X443626),  new Color(0XA1A1A1));
        createSpawnEgg(EntityCreeper.class, new Color(0X0DA70B),  new Color(0X000000));
        createSpawnEgg(EntityGhast.class, new Color(0xF9F9F9),  new Color(0xBCBCBC));
        createSpawnEgg(EntityPig.class, new Color(0xF0A5A2),  new Color(0xDB635F));
        createSpawnEgg(EntitySheep.class, new Color(0xE7E7E7), new Color(0xFFB5B5));
        createSpawnEgg(EntitySkeleton.class, new Color(0xC1C1C1),  new Color(0x494949));
        createSpawnEgg(EntitySlime.class, new Color(0x51A03E),  new Color(0x7EBF6E));
        createSpawnEgg(EntitySpider.class, new Color(0x342D27),  new Color(0xA80E0E));
        createSpawnEgg(EntitySquid.class, new Color(0x223B4D),  new Color(0x708899));
        createSpawnEgg(EntityWolf.class, new Color(0xD7D3D3),  new Color(0xCEAF96));
        createSpawnEgg(EntityZombie.class, new Color(0x00AFAF),  new Color(0x799C65));
        createSpawnEgg(EntityScorpion.class, new Color(0x888362),  new Color(0xE0D989));
        createSpawnEgg(EntitySnowman.class, new Color(0x0f6496),  new Color(0xd3d3d3));
        createSpawnEgg(EntityPigZombie.class, new Color(0xEA9393),  new Color(0x4C7129));
        createSpawnEgg(EntityArmoredZombie.class, new Color(0x735300),  new Color(0x799C65));
        createSpawnEggWithNameOverride(EntityHuman.class, "mob.human", new Color(0x4639a4),  new Color(0x00AFAF));
        createSpawnEgg(EntityGiant.class,new Color(0x009F9F), new Color(0x496C35));
        createSpawnEgg(EntityFireflyCluster.class,new Color(0x000000),  new Color(0xFFFF00));

        assignPickEntity(EntityBoat.class, Item.boat);
        assignPickEntity(EntityMinecart.class, Item.minecart);
    }
}
