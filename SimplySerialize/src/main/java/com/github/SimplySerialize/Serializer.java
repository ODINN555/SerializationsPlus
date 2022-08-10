package com.github.SimplySerialize;
import java.util.HashMap;
import java.util.Map;

public abstract class Serializer<T> {

    /**
     * The serializer's name
     */
    public final String Name;

    /**
     * The serializer's type
     */
    public final Class Type;

    /**
     *
     * @param name a given name
     * @param type a given type
     */
    public Serializer(String name,Class type){
        this.Name = name;
        this.Type = type;
    }
    /**
     *
     * @param object a given object
     * @return the object serialized to byte[]
     */
    public abstract Map<String,Object> serializeValue(T object);

    /**
     * deserializes the object of the given map, note: will not check for the class of the object.So, if mismatched class- expect error!
     * @param map a given map
     * @return the deserialized object
     */
    public abstract T deserializeValue(Map<String,Object> map);

}
