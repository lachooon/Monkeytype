package pja.edu.lachooon.monektypek;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class DictionaryLoader {
    public static ArrayList<Language> loadDictionary() {
        File dictionary = new File("dictionary");
        File[] langs = dictionary.listFiles();

        ArrayList<Language> langList = new ArrayList<>();

        if (langs != null) {
            for (File lang : langs) {
                String name = lang.getName();
                name = name.substring(0, name.lastIndexOf('.'));

                try {
                    ArrayList<String> words = (ArrayList<String>) Files.readAllLines(lang.toPath().toAbsolutePath());
                    Language language = new Language(name, words);
                    langList.add(language);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Problem z ladowaniem bibliotek");
                    System.exit(1);
                }
            }
        }

        return langList;
    }

    public static String[] toStringArray(ArrayList<Language> langList) {
        String[] langNames = new String[langList.size()];

        for (int i = 0; i < langList.size(); i++){
            langNames[i] = langList.get(i).getLanguage();
        }

        return langNames;
    }
}

