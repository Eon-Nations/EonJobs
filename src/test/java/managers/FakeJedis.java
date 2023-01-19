package managers;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class FakeJedis extends Jedis {
    private final Map<String, Map<String, String>> map = new HashMap<>();

    @Override
    public Map<String, String> hgetAll(String key) {
        return map.getOrDefault(key, Map.of());
    }

    @Override
    public boolean exists(String key) {
        Map<String, String> otherMap = map.get(key);
        return otherMap != null;
    }

    @Override
    public String hmset(String key, Map<String, String> value) {
        map.put(key, value);
        return "Not sure what the key is";
    }
}
