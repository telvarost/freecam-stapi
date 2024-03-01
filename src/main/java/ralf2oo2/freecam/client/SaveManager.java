package ralf2oo2.freecam.client;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import ralf2oo2.freecam.util.CameraPosition;
import ralf2oo2.freecam.util.SavedCameraPosition;
import ralf2oo2.freecam.util.SavedCameraPositions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveManager {
    Minecraft minecraft;
    File saveDirectory;

    public SaveManager() {
        minecraft = Minecraft.class.cast(FabricLoader.getInstance().getGameInstance());
        saveDirectory = new File(FabricLoader.getInstance().getConfigDir() + "/freecam/camerapositions/");
        if(!saveDirectory.exists()){
            saveDirectory.mkdirs();
        }
    }

    public SavedCameraPosition[] load(long seed, String worldName){

        Jankson jankson = Jankson.builder().build();
        SavedCameraPositions savedCameraPositions = null;
        try{
            File configFile = new File(saveDirectory + "/" + worldName + "-" + seed + ".json");
            JsonObject configJson = jankson.load(configFile);

             savedCameraPositions = jankson.fromJson(configJson, SavedCameraPositions.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return savedCameraPositions.cameraPositions;
    }
    public void save(long seed, String worldName, SavedCameraPosition[] cameraPositions){

        SavedCameraPositions savedCameraPositions = new SavedCameraPositions();
        savedCameraPositions.worldName = worldName;
        savedCameraPositions.seed = seed;
        savedCameraPositions.cameraPositions = cameraPositions;

        File configFile = new File(saveDirectory + "/" + worldName + "-" + seed + ".json");
        Jankson jankson = Jankson.builder().build();
        String result = jankson
                .toJson(savedCameraPositions)
                .toJson(true, true, 0);
        try {
            if(!configFile.exists()) configFile.createNewFile();
            FileOutputStream out = new FileOutputStream(configFile, false);

            out.write(result.getBytes());
            out.flush();
            out.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
