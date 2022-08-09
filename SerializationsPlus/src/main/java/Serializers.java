import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serializers {
    /**
     * a map containing all serializers
     */
    private static final List<Serializer> serializers = new ArrayList<Serializer>();

    public Serializers(){
        addDefaults();
    }
    public void addDefaults(){

        serializers.add(new Serializer<Enchantment>("Enchantment",Enchantment.class) {
            @Override
            public Map<String, Object> serializeValue(Enchantment object) {
                Map<String,Object> map = new HashMap<>();
                map.put("name",object.getName());
                return map;
            }

            @Override
            public Enchantment deserializeValue(Map<String, Object> map) {
                return Enchantment.getByName((String) map.get("name"));
            }
        });
        serializers.add(new Serializer<PotionEffectType>("PotionEffectType",PotionEffectType.class) {
            @Override
            public Map<String, Object> serializeValue(PotionEffectType object) {
                Map<String,Object> map = new HashMap<>();
                map.put("name",object.getName());
                return map;
            }

            @Override
            public PotionEffectType deserializeValue(Map<String, Object> map) {
                return PotionEffectType.getByName((String) map.get("name"));
            }
        });
    }

    public boolean contains(Class c){
        for (Serializer serializer : serializers)
            if(serializer.Type.isAssignableFrom(c))
                return true;

            return false;
    }

    public boolean contains(String name){
        for (Serializer serializer : serializers)
            if(serializer.Name.equalsIgnoreCase(name))
                return true;

            return false;
    }

    public void add(Serializer serializer){
        serializers.add(serializer);
    }

    public Serializer get(Serializer c){
        if(serializers.indexOf(c) == -1)
            return null;
        return serializers.get(serializers.indexOf(c));
    }
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

    public Serializer get(String name){
        for (Serializer serializer : serializers)
            if(serializer.Name.equalsIgnoreCase(name))
                return serializer;
            return null;
    }
}
