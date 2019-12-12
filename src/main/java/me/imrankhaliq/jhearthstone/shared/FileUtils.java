package me.imrankhaliq.jhearthstone.shared;

import java.io.*;

public class FileUtils {
    public static void writeFile(String location, String contents) {
        try {
            FileWriter configFile = new FileWriter(location);
            configFile.write(contents);
            configFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String location) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(location));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
