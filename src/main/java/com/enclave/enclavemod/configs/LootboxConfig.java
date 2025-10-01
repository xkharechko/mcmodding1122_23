package com.enclave.enclavemod.configs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraftforge.fml.common.FMLLog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LootboxConfig {
    public static class LootItemJson {
        public String item;
        public float weight;

        public LootItemJson() {}

        public LootItemJson(String item, float weight) {
            this.item = item;
            this.weight = weight;
        }
    }

    public static class LootItemContainer {
        public Item lootItem;
        public float weight;

        public LootItemContainer(Item lootItem, float weight) {
            this.lootItem = lootItem;
            this.weight = weight;
        }
    }

    public static List<LootItemContainer> lootContainer = new ArrayList<>();

    public static void init(File configDirectory) throws IOException {
        File file = new File(configDirectory, "enclavemod/lootbox.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            createDefaults(file);
            return;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<ConfigWrapper>() {}.getType();
        ConfigWrapper wrapper = gson.fromJson(new FileReader(file), type);

        lootContainer.clear();
        for (LootItemJson i : wrapper.loot) {
            Item item = Item.getByNameOrId(i.item);
            if (item != null) {
                lootContainer.add(new LootItemContainer(item, i.weight));
            } else {
                FMLLog.log.warn("LootboxConfig: Unknown item id " + i.item);
            }
        }
    }

    private static void createDefaults(File file) throws IOException {
        Gson gson = new Gson();

        ConfigWrapper wrapper = new ConfigWrapper();
        wrapper.loot = new ArrayList<>();

        LootItemJson golden_apple = new LootItemJson("minecraft:golden_apple", 2.0F);
        LootItemJson diamond_hoe = new LootItemJson("minecraft:diamond_hoe", 7.0F);

        wrapper.loot.add(golden_apple);
        wrapper.loot.add(diamond_hoe);

        FileWriter writer = new FileWriter(file);
        gson.toJson(wrapper, writer);
        writer.close();
    }

    public static class ConfigWrapper {
        public List<LootItemJson> loot;
    }
}
