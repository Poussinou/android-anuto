package ch.logixisland.anuto.util.data;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Commit;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Root
public class TowerSettings {

    @ElementList(entry = "tower", inline = true)
    private List<TowerConfig> mTowerConfigList = new ArrayList<>();
    private Map<String, TowerConfig> mTowerConfigMap = new HashMap<>();

    public static TowerSettings fromXml(InputStream stream) throws Exception {
        Serializer serializer = new SerializerFactory().createSerializer();
        return serializer.read(TowerSettings.class, stream);
    }

    public TowerConfig getTowerConfig(String name) {
        return mTowerConfigMap.get(name);
    }

    public Collection<TowerConfig> getTowerConfigs() {
        return mTowerConfigList;
    }

    @Commit
    private void commit() {
        mTowerConfigMap = new HashMap<>();
        for (TowerConfig config : mTowerConfigList) {
            mTowerConfigMap.put(config.getName(), config);
        }

        for (TowerConfig config : mTowerConfigList) {
            if (config.getUpgrade() != null) {
                TowerConfig upgradeConfig = mTowerConfigMap.get(config.getUpgrade());
                config.setUpgradeCost(upgradeConfig.getValue() - config.getValue());
            }
        }
    }

}
