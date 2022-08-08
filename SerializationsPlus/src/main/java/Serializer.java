import java.util.HashMap;
import java.util.Map;

public abstract class Serializer<T> {

    public final String Name;
    public final Class Type;

    public Serializer(String name,Class type){
        this.Name = name;
        this.Type = type;
    }
    /**
     *
     * @param object a given object
     * @return the object serialized to byte[]
     */
    protected abstract Map<String,Object> compressValue(T object);

    /**
     *
     * @param object a given object
     * @return the object serialized to byte[]
     */
    public byte[] serializeValue(T object){
        Map<String,Object> map = new HashMap<>(compressValue(object));
        return Serializations.serialize(map);
    }
    /**
     * deserializes the object of the given map, note: will not check for the class of the object.So, if mismatched class- expect error!
     * @param map a given map
     * @return the deserialized object
     */
    protected abstract T decompressValue(Map<String,Object> map);

    /**
     *
     * @param object a given byte[]
     * @return the array deserialized to an object of this serializer's type
     */
    public T deserializeValue(byte[] object){
        return decompressValue((Map<String, Object>) Serializations.deserialize(object));
    }
}
