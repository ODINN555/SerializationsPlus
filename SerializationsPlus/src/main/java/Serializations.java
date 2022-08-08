
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serializations {


    public static final Serializers serializers = new Serializers();


    private Serializations(){}


    /**
     * registers a serializer
     * @param serializer a given serializer
     * @param <T> the type
     */
    public static <T> void registerSerializer(Serializer<T> serializer){
        serializers.add(serializer);
    }

    /**
     *
     * @param obj a given object
     * @return the object serialized to a byte[]
     */
    public static byte[] serialize(Object obj){
        if(obj == null)
            return null;
        Class c = obj.getClass();
        while(c != null)
        if(serializers.contains(c)) {
            Serializer serial = serializers.get(c);
            byte[] arr = serial.serializeValue(obj);
            return arr;
        }else c = c.getSuperclass();
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(byteOut);
            bukkitOut.writeObject(obj);
            bukkitOut.flush();

            return byteOut.toByteArray();
        } catch (IOException e) {
            System.out.println("There was no Serializer for "+obj.getClass()+" and it couldn't be serialized. make sure there are no errors or you need to make a serializer!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param arr a given byte[]
     * @return the array deserialized
     */
    public static Object deserialize(byte[] arr){
        return deserialize(arr,null);
    }

    /**
     *
     * @param arr a given byte[]
     * @param deserializeTo a given class to try to deserialize to
     * @return the array deserialized, preferably to the deserializeTo class
     */
    public static Object deserialize(byte[] arr,Class deserializeTo) {
        if (arr == null)
            return null;
        if (deserializeTo != null && serializers.contains(deserializeTo))
            return serializers.get(deserializeTo).deserializeValue(arr);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(arr);
        try {
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(byteIn);

            return bukkitIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not deserialize object, could be not serializable.");

            e.printStackTrace();
            return null;
        }
    }

    }
