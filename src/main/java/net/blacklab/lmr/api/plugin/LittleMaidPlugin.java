package net.blacklab.lmr.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

/**
 * LittleMaid連携用アノテーション
 * @author computer
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LittleMaidPlugin {
	
	/** ModId */
	String modid() default "";
	
	/** 優先度 */
	EventPriority priority() default EventPriority.NORMAL;
	
}
