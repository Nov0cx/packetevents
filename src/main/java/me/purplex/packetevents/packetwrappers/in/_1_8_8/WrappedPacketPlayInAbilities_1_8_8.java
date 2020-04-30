package me.purplex.packetevents.packetwrappers.in._1_8_8;

import net.minecraft.server.v1_8_R3.PacketPlayInAbilities;

import java.lang.Object;
import java.lang.reflect.Field;

public class WrappedPacketPlayInAbilities_1_8_8 {
    private final Object packet;

    private Field fieldE;
    private Field fieldF;
    public WrappedPacketPlayInAbilities_1_8_8(Object packet) {
        this.packet = packet;
        try {
            this.fieldE = PacketPlayInAbilities.class.getDeclaredField("e");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            this.fieldF = PacketPlayInAbilities.class.getDeclaredField("f");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        setupFields();
    }


    private void setupFields() {
        PacketPlayInAbilities p = (PacketPlayInAbilities) packet;
        this.a = p.a();
        this.b = p.isFlying();
        this.c = p.c();
        this.d = p.d();
        try {
            this.e = fieldE.getFloat(p);
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        try {
            this.f = fieldF.getFloat(p);
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
    }

    public boolean a;
    public boolean b;
    public boolean c;
    public boolean d;
    public float e;
    public float f;
}