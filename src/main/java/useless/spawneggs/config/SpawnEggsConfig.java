package useless.spawneggs.config;


import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class SpawnEggsConfig {
    @Expose
    public HashMap<String, Integer> keyToIdMap = new HashMap<>();
    @Expose
    public int startingIdOffset = 1000;
    @Expose
    public boolean regenAllIdsOnNextBoot = false;

}
