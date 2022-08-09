import java.io.Serializable;
import java.util.Map;

public class SerializedMap implements Serializable {
    public Map<String,Object> Values;
    public String Name;

    public SerializedMap(Map<String,Object> values,String name){
        this.Values = values;
        this.Name = name;
    }

    public SerializedMap(Map<String,Object> values,Class serializer){
        this.Values = values;
        this.Name = new Serializers().get(serializer).Name;
    }

    public SerializedMap(Map<String,Object> values,Serializer serializer){
        this.Values = values;
        this.Name = serializer.Name;
    }
}
