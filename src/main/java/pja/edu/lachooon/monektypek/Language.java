package pja.edu.lachooon.monektypek;

import java.util.ArrayList;

public class Language {
    private String language;
    private ArrayList<String> dictionary;

    public Language(String language, ArrayList<String> dictionary) {
        this.language = language;
        this.dictionary = dictionary;
    }

    public String getLanguage() {
        return this.language;
    }

    public ArrayList<String> getDictionary() {
        return this.dictionary;
    }

    @Override
    public String toString() {
        return this.language;
    }
}
