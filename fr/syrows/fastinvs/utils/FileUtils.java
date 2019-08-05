package fr.syrows.fastinvs.utils;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class FileUtils {

    public static void createFileFromResource(Plugin plugin, Path path, String resourcePath, boolean override) {

        if(Files.exists(path) && !override) return;

        InputStream inputStream = plugin.getResource(resourcePath);

        if(inputStream == null)
            throw new NullPointerException(String.format("No resource found at '%s'.", resourcePath));

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {

            Logger.log(Level.INFO, "Reading resource file...");

            StringBuilder sb = new StringBuilder();
            String l;

            while((l = reader.readLine()) != null)
                sb.append(l).append("\n");

            Logger.log(Level.INFO, "Writing data...");

            BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));

            writer.write(sb.toString());
            writer.flush();
            writer.close();

            Logger.log(Level.INFO, String.format("File %s was created.", path.getFileName()));

        } catch (IOException e) {

            Logger.log(Level.SEVERE, String.format("Cannot create file %s.", path.getFileName()));

            e.printStackTrace();
        }
    }

    public static void createDirectory(Path path) {

        if(Files.exists(path)) return;

        try {

            Files.createDirectory(path);

            Logger.log(Level.INFO, String.format("Directory %s was created.", path.getFileName()));

        } catch (IOException e) {

            Logger.log(Level.SEVERE, String.format("Cannot create directory %s.", path.getFileName()));

            e.printStackTrace();
        }
    }
}
