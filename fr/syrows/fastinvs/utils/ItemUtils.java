package fr.syrows.fastinvs.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;

public class ItemUtils {

    public static String serializeToBase64(ItemStack stack) {

        String inBase64 = null;

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOutputStream = new BukkitObjectOutputStream(outputStream);

            bukkitOutputStream.writeObject(stack);

            inBase64 = Base64Coder.encodeLines(outputStream.toByteArray());

            bukkitOutputStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return inBase64;
    }

    public static ItemStack deserializeFromBase64(String inBase64) {

        ItemStack fromBase64 = null;

        try {

            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(inBase64));
            BukkitObjectInputStream bukkitInputStream = new BukkitObjectInputStream(inputStream);

            fromBase64 = (ItemStack) bukkitInputStream.readObject();

            bukkitInputStream.close();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();
        }
        return fromBase64;
    }
}
