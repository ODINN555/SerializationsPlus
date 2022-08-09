
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

        Object toSerialize = obj;

        Class c = obj.getClass();
        if(serializers.contains(c))
            toSerialize = new SerializedMap(serializers.get(c).serializeValue(toSerialize),serializers.get(c).Name);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(byteOut);
            bukkitOut.writeObject(toSerialize);
            bukkitOut.flush();

            return byteOut.toByteArray();
        } catch (IOException e) {
            System.out.println("There was no Serializer for "+toSerialize.getClass()+" and it couldn't be serialized. make sure there are no errors or you need to make a serializer!");
            e.printStackTrace();
            return null;
        }
    }


    /**
     *
     * @param arr a given byte[]
     * @return the array deserialized, preferably to the deserializeTo class
     */
    public static Object deserialize(byte[] arr) {
        if (arr == null)
            return null;

        ByteArrayInputStream byteIn = new ByteArrayInputStream(arr);
        try {
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(byteIn);
            Object obj = bukkitIn.readObject();

            if(obj instanceof SerializedMap) {
                SerializedMap map = (SerializedMap) obj;
                return (serializers.get(map.Name).deserializeValue(map.Values));
            }

            return obj;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not deserialize object, could be not serializable.");

            e.printStackTrace();
            return null;
        }
    }

    }
