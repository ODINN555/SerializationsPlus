import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Serializers {
    /**
     * a map containing all serializers
     */
    private static final List<Serializer> serializers = new ArrayList<Serializer>();

    public Serializers(){
        addDefaults();
    }
    public void addDefaults(){
        add(new Serializer<ItemStack>("ItemStack",ItemStack.class){
            @Override
            public Map<String, Object> serializeValue(ItemStack object) {
                Map<String,Object> map = new HashMap<>();
               /* ItemMeta meta = object.getItemMeta();
                map.put("name",meta.getDisplayName());
                map.put("lore",meta.getLore());
                map.put("count",object.getAmount());

                Map<Enchantment,Integer> enchants = meta.getEnchants();
                Map<byte[],Integer> serializedEnchants = new HashMap<>();
                enchants.forEach((ench,lvl) ->
                    serializedEnchants.put(Serializations.serialize(ench),lvl)
                );
                map.put("Enchants",serializedEnchants);
                map.put("ItemFlags",meta.getItemFlags());
                map.put("material",object.getType());
                map.put("durability",object.getDurability());

                return null;*/
                return object.serialize();
            }

            @Override
            public ItemStack deserializeValue(Map<String, Object> map) {
                return ItemStack.deserialize(map);
            }
        });
        add(new Serializer<ArrayList>("ArrayList",ArrayList.class) {
            @Override
            public Map<String, Object> serializeValue(ArrayList object) {
                Map<String,Object> map = new HashMap<>();
                List<byte[]> arr = new ArrayList<>();
                for (Object o : object)
                    arr.add(Serializations.serialize(o));
                map.put("list",arr);

                return map;
            }

            @Override
            public ArrayList deserializeValue(Map<String, Object> map) {
                ArrayList objects = new ArrayList();
                List<byte[]> arr = (List<byte[]>) map.get("list");
                for (byte[] bytes : arr)
                    objects.add(Serializations.deserialize(bytes));

                return objects;
            }
        });
        add(new Serializer<HashMap>("HashMap",HashMap.class) {
            @Override
            public Map<String, Object> serializeValue(HashMap object) {
                Map<String,Object> map = new HashMap<>();
                Map serializedMap = new HashMap();
                object.forEach((k,v) -> serializedMap.put(Serializations.serialize(k),Serializations.serialize(v)));
                map.put("map",serializedMap);
                return map;
            }

            @Override
            public HashMap deserializeValue(Map<String, Object> map) {
                HashMap deserializedMap = new HashMap();
                Map objects = (Map) map.get("map");
                objects.forEach((k,v) -> deserializedMap.put(Serializations.deserialize((byte[]) k),Serializations.deserialize((byte[]) v)));

                return deserializedMap;
            }
        });
        add(new Serializer<Location>("Location",Location.class) {
            @Override
            public Map<String, Object> serializeValue(Location object) {
                Map<String,Object> map = new HashMap<>();
                map.put("world",object.getWorld().getName());
                map.put("X",object.getX());
                map.put("y",object.getY());
                map.put("z",object.getZ());
                map.put("yaw",object.getYaw());
                map.put("pitch",object.getYaw());
                return map;
            }

            @Override
            public Location deserializeValue(Map<String, Object> map) {
                Location loc = new Location(Bukkit.getWorld((String) map.get("world")),(double) map.get("x"),(double) map.get("y"),(double) map.get("z"),(float)map.get("yaw"),(float) map.get("pitch"));
                return loc;
            }
        });
    }

    /**
     *
     * @param c a given class
     * @return if the serializers contains a serializer which can serialize the given class
     */
    public boolean contains(Class c){
        for (Serializer serializer : serializers)
            if(serializer.Type.isAssignableFrom(c))
                return true;

            return false;
    }

    /**
     *
     * @param c a given class
     * @return if the serializers contains a serializer which can serialize the given class, and specifically it- not it's super class
     */
    public boolean containsExact(Class c){
        for (Serializer serializer : serializers)
            if(serializer.Type.equals(c))
                return true;
        return false;
    }

    /**
     *
     * @param name a given name
     * @return if the serializers contains a serializer with the given name
     */
    public boolean contains(String name){
        for (Serializer serializer : serializers)
            if(serializer.Name.equalsIgnoreCase(name))
                return true;

            return false;
    }

    /**
     * adds the given serializer to the serializers, overrides if already exists.
     * @param serializer a given serializer
     */
    public void add(Serializer serializer){
        if(containsExact(serializer.Type))
            serializers.remove(get(serializer.Type));

            serializers.add(serializer);
    }

    /**
     *
     * @param index a given index
     * @return the serializer at the given index
     */
    public Serializer get(int index){
        return serializers.get(index);
    }

    /**
     *
     * @param c a given class
     * @return the most matching serializer for the given class, null if not found.
     */
    public Serializer get(Class c){
        // for improved efficiency, get the matching serializers
        List<Serializer> matchingSerializers = new ArrayList<>();
        for (Serializer serializer : serializers)
            if(serializer.Type.isAssignableFrom(c))
                matchingSerializers.add(serializer);

        if(matchingSerializers.size() == 1)
            return matchingSerializers.get(0);
        if(matchingSerializers.size() == 0)
            return null;

        Class instances = c;
        // loop for each super class and get the closest match to the given class
        while(instances != null){
            for (Serializer serializer : matchingSerializers)
                if(serializer.Type.equals(instances))
                    return serializer;
            instances = instances.getSuperclass();
        }

        return null;
    }

    /**
     *
     * @param name a given name
     * @return the serializer with the given name, null if not found
     */
    public Serializer get(String name){
        for (Serializer serializer : serializers)
            if(serializer.Name.equalsIgnoreCase(name))
                return serializer;
            return null;
    }
}
