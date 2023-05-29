package me.alek.security.operator;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.JsonList;
import net.minecraft.server.v1_8_R3.JsonListEntry;
import net.minecraft.server.v1_8_R3.OpList;
import net.minecraft.server.v1_8_R3.OpListEntry;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Map;

public class OperatorInjector {

    private final OperatorManager operatorManager;

    public OperatorInjector(OperatorManager operatorManager) {
        this.operatorManager = operatorManager;
    }

    public void inject() {
        try {
            final OpList opList = ((CraftServer) Bukkit.getServer()).getHandle().getOPs();
            final Unsafe unsafe = getUnsafe();

            final Field mapField = JsonList.class.getDeclaredField("d");
            mapField.setAccessible(true);
            Map<String, OpListEntry> ops = (Map<String, OpListEntry>) mapField.get(opList);

            unsafe.putObject(opList, unsafe.objectFieldOffset(mapField), new HashMapProxy<>(operatorManager, ops));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Unsafe getUnsafe() throws Exception {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
    }
}
