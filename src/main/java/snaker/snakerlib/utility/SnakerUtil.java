package snaker.snakerlib.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Rarity;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import snaker.snakerlib.SnakerLib;
import snaker.snakerlib.internal.SnakerLogger;
import snaker.snakerlib.internal.StringNuker;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by SnakerBone on 20/02/2023
 **/
public class SnakerUtil
{
    public static final String PLACEHOLDER = SnakerLib.MODID + ":" + PlaceHolders.PH8;
    public static final String PLACEHOLDER_NO_MODID = PlaceHolders.PH8;

    public static String getBaseName(Class<?> clazz)
    {
        String pkg = clazz.getPackage().getName();
        return pkg.substring(pkg.lastIndexOf('.')).replace(".", "");
    }

    public static boolean keyPressed(int... keys)
    {
        for (int key : keys) {
            return GLFW.glfwGetKey(Minecraft.getInstance().getWindow().getWindow(), key) == GLFW.GLFW_PRESS;
        }
        return false;
    }

    @SafeVarargs
    public static <V> V randomFromObjects(final RandomSource random, V... values)
    {
        return random.nextBoolean() ? values[random.nextInt(1, values.length) % values.length] : values[random.nextInt(values.length)];
    }

    @SafeVarargs
    public static <V> V randomFromObjects(final RandomSource random, boolean copy, V... values)
    {
        if (copy) {
            return random.nextBoolean() ? randomFromObjects(random, values) : random.nextBoolean() ? values[random.nextInt(1, values.length) % values.length] : values[random.nextInt(values.length)];
        } else {
            return randomFromObjects(random, values);
        }
    }

    public static <V> V[] filterEnumValues(V[] values, Predicate<? super V> filter, IntFunction<V[]> function)
    {
        if (values != null && values.length > 0) {
            return Arrays.stream(values).filter(filter).toArray(function);
        } else {
            SnakerLogger.error("Invalid enum or enum values");
            return null;
        }
    }

    public static <S extends AbstractSet<V>, V> void populateSet(S set, V value)
    {
        if (set.isEmpty()) {
            set.add(value);
        } else {
            set.clear();
            set.add(value);
        }
    }

    public static <M extends AbstractMap<K, V>, K, V> void populateMap(M map, K key, V value)
    {
        if (map.isEmpty()) {
            map.put(key, value);
        } else {
            map.clear();
            map.put(key, value);
        }
    }

    public static <T extends LivingEntity> boolean isEntityRotating(@NotNull T entity)
    {
        return entity.getYRot() != entity.yRotO || entity.yBodyRot != entity.yBodyRotO || entity.yHeadRot != entity.yHeadRotO;
    }

    public static <T extends LivingEntity> boolean isEntityYRotating(@NotNull T entity)
    {
        return entity.getYRot() != entity.yRotO;
    }

    public static <T extends LivingEntity> boolean isEntityYBodyRotating(@NotNull T entity)
    {
        return entity.yBodyRot != entity.yBodyRotO;
    }

    public static <T extends LivingEntity> boolean isEntityYHeadRotating(@NotNull T entity)
    {
        return entity.yHeadRot != entity.yHeadRotO;
    }

    public static <T extends Entity> boolean isEntityMoving(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getY() != entity.yo || entity.getZ() != entity.zo;
    }

    public static <T extends Entity> boolean isEntityMovingX(@NotNull T entity)
    {
        return entity.getX() != entity.xo;
    }

    public static <T extends Entity> boolean isEntityMovingY(@NotNull T entity)
    {
        return entity.getY() != entity.yo;
    }

    public static <T extends Entity> boolean isEntityMovingZ(@NotNull T entity)
    {
        return entity.getZ() != entity.zo;
    }

    public static <T extends Entity> boolean isEntityMovingXZ(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getZ() != entity.zo;
    }

    public static <T extends Entity> boolean isEntityMovingXY(@NotNull T entity)
    {
        return entity.getX() != entity.xo || entity.getY() != entity.yo;
    }

    public static <T extends Entity> boolean isEntityMovingYZ(@NotNull T entity)
    {
        return entity.getY() != entity.yo || entity.getZ() != entity.zo;
    }

    public static String untranslate(String text)
    {
        return !text.isEmpty() ? text.replaceAll("\\s+", "_").toLowerCase() : text;
    }

    public static String translate(String text)
    {
        if (!text.isEmpty()) {
            return Stream.of(text.trim().split("\\s|\\p{Pc}")).filter(word -> word.length() > 0).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
        } else {
            return text;
        }
    }

    public static String translate(String text, Rarity rarity)
    {
        switch (rarity) {
            case UNCOMMON -> {
                return "§e" + translate(text);
            }
            case RARE -> {
                return "§b" + translate(text);
            }
            case EPIC -> {
                return "§d" + translate(text);
            }
            default -> {
                return translate(text);
            }
        }
    }

    public static String untranslateComponent(MutableComponent component, boolean leaveCaps)
    {
        StringNuker nuker = new StringNuker(component.getString());
        nuker.replaceAllAndDestroy("\\p{P}");
        return leaveCaps ? nuker.result() : nuker.result().toLowerCase();
    }

    public static String untranslateComponent(MutableComponent component)
    {
        return untranslateComponent(component, false);
    }

    public static int randomHex()
    {
        Random random = new Random();
        return random.nextInt(0xffffff + 1);
    }

    public static int hexToInt(String hexCode)
    {
        StringNuker nuker = new StringNuker(hexCode);
        nuker.replaceAllAndDestroy("#");
        return Integer.parseInt(nuker.result(), 16);
    }

    public static float hexToFloat(String hexCode)
    {
        StringNuker nuker = new StringNuker(hexCode);
        nuker.replaceAllAndDestroy("#");
        return Float.parseFloat(nuker.result());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Contract("null->null;!null->!null")
    public static <Anything> Anything cast(@Nullable Object object)
    {
        return (Anything) object;
    }

    private static String generatePlaceholder(int limit)
    {
        return RandomStringUtils.randomAlphanumeric(limit).toUpperCase();
    }

    static class PlaceHolders
    {
        static String PH2 = generatePlaceholder(2);
        static String PH4 = generatePlaceholder(4);
        static String PH8 = generatePlaceholder(8);
        static String PH16 = generatePlaceholder(16);
        static String PH32 = generatePlaceholder(32);
        static String PH64 = generatePlaceholder(64);
    }
}