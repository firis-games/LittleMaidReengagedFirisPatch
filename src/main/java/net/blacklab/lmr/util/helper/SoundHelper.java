package net.blacklab.lmr.util.helper;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundHelper {

	public static void playSoundOnEntity(Entity pEntity, String pEventName, float volume, float pitch) {
		SoundEvent sEvent = SoundEvent.soundEventRegistry.getObject(new ResourceLocation(pEventName));
		if (sEvent != null) {
			pEntity.playSound(sEvent, volume, pitch);
		}
	}

}
