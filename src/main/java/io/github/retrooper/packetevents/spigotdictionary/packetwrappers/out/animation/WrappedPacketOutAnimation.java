/*
 * MIT License
 *
 * Copyright (c) 2020 retrooper
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.retrooper.packetevents.spigotdictionary.packetwrappers.out.animation;

import io.github.retrooper.packetevents.spigotdictionary.packettype.PacketTypeClasses;
import io.github.retrooper.packetevents.spigotdictionary.packetwrappers.SendableWrapper;
import io.github.retrooper.packetevents.spigotdictionary.packetwrappers.WrappedPacket;
import io.github.retrooper.packetevents.spigotdictionary.utils.nms.NMSUtils;
import org.bukkit.entity.Entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class WrappedPacketOutAnimation extends WrappedPacket implements SendableWrapper {
    private static final Map<Integer, EntityAnimationType> cachedAnimationIDS = new HashMap<>();
    private static final Map<EntityAnimationType, Integer> cachedAnimations = new HashMap<>();
    private static Class<?> nmsEntityClass;
    private static Constructor<?> animationConstructor;
    private Entity entity;
    private int entityID = -1;
    private EntityAnimationType type;
    private boolean isListening = false;

    public WrappedPacketOutAnimation(final Object packet) {
        super(packet);
        isListening = true;
    }

    public WrappedPacketOutAnimation(final Entity target, final EntityAnimationType type) {
        super();
        this.entityID = target.getEntityId();
        this.entity = target;
        this.type = type;
    }

    public static void load() {
        Class<?> animationClass = PacketTypeClasses.Server.ANIMATION;
        try {
            nmsEntityClass = NMSUtils.getNMSClass("Entity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            animationConstructor = animationClass.getConstructor(nmsEntityClass, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        cachedAnimationIDS.put(0, EntityAnimationType.SWING_MAIN_ARM);
        cachedAnimationIDS.put(1, EntityAnimationType.TAKE_DAMAGE);
        cachedAnimationIDS.put(2, EntityAnimationType.LEAVE_BED);
        cachedAnimationIDS.put(3, EntityAnimationType.SWING_OFFHAND);
        cachedAnimationIDS.put(4, EntityAnimationType.CRITICAL_EFFECT);
        cachedAnimationIDS.put(5, EntityAnimationType.MAGIC_CRITICAL_EFFECT);

        cachedAnimations.put(EntityAnimationType.SWING_MAIN_ARM, 0);
        cachedAnimations.put(EntityAnimationType.TAKE_DAMAGE, 1);
        cachedAnimations.put(EntityAnimationType.LEAVE_BED, 2);
        cachedAnimations.put(EntityAnimationType.SWING_OFFHAND, 3);
        cachedAnimations.put(EntityAnimationType.CRITICAL_EFFECT, 4);
        cachedAnimations.put(EntityAnimationType.MAGIC_CRITICAL_EFFECT, 5);
    }

    /**
     * Lookup the associated entity by the ID that was sent in the packet.
     *
     * @return Entity
     */
    public Entity getEntity() {
        if (entity != null) {
            return entity;
        }
        return entity = NMSUtils.getEntityById(getEntityId());
    }

    /**
     * Get the ID of the entity.
     * If you do not want to use {@link #getEntity()},
     * you lookup the entity by yourself with this entity ID.
     *
     * @return Entity ID
     */
    public int getEntityId() {
        if (entityID == -1) {
            entityID = readInt(0);
        }
        return entityID;
    }

    /**
     * Get the entity animation type.
     *
     * @return Get Entity Animation Type
     */
    public EntityAnimationType getAnimationType() {
        if (isListening) {
            return cachedAnimationIDS.get(readInt(1));
        } else {
            return type;
        }
    }

    @Override
    public Object asNMSPacket() {
        final Object nmsEntity = NMSUtils.getNMSEntity(this.entity);
        final int index = cachedAnimations.get(type);
        try {
            return animationConstructor.newInstance(nmsEntity, index);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum EntityAnimationType {
        SWING_MAIN_ARM, TAKE_DAMAGE, LEAVE_BED, SWING_OFFHAND, CRITICAL_EFFECT, MAGIC_CRITICAL_EFFECT
    }
}
