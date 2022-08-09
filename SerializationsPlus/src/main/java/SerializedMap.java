import java.io.Serializable;
import java.util.Map;

public class SerializedMap implements Serializable {
    /**
     * The values
     */
    public Map<String,Object> Values;

    /**
     * The name of the serializer responsible for the values
     */
    public String Name;

    /**
     *
     * @param values given values
     * @param name a given serializer name
     */
    public SerializedMap(Map<String,Object> values,String name){
        this.Values = values;
        this.Name = name;
    }

    /**
     *
     * @param values given values
     * @param serializer a given class of serializer
     */
    public SerializedMap(Map<String,Object> values,Class serializer){
        this.Values = values;
        this.Name = new Serializers().get(serializer).Name;
    }

    /**
     *
     * @param values given values
     * @param serializer a given serializer
     */
    public SerializedMap(Map<String,Object> values,Serializer serializer){
        this.Values = values;
        this.Name = serializer.Name;
    }
}
