package net.blacklab.lmc.common.helper;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;

/**
 * Reflectionヘルパー
 * @author computer
 *
 */
public class ReflectionHelper {

	/**
	 * リフレクションでFieldを取得する
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public static <T> T getField(@Nonnull Class<?> clazz, Object clazzObj, @Nonnull String fieldName, @Nullable String fieldObfName) {
		Preconditions.checkNotNull(clazz);
		String nameToFind;
        if (fieldObfName == null || (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            nameToFind = fieldName;
        } else {
            nameToFind = fieldObfName;
        }
        
        Field f = null;
        while (clazz != null) {
        	try
            {
                f = clazz.getDeclaredField(nameToFind);
                f.setAccessible(true);
                break;
            }
            catch (Exception e)
            {
            	clazz = clazz.getSuperclass();
            }
        }
        
        T result = null;
        if (f == null) {
        	throw new UnableToFindMethodException(new NoSuchFieldException());
        }
        
        try {
        	result = (T) f.get(clazzObj);
		} catch (Exception e) {
			throw new UnableToFindMethodException(new NoSuchFieldException());
		}
        
        return result;
	}
}
