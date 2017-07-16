/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.jcode.i18n;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jGauravGupta
 */
public class LanguageUtil {
     /**
     * check if Right-to-Left support is necessary for i18n
     * @param {string[]} languages - languages array
     */
    public static boolean isI18nRTLSupportNecessary(Set<String> languages) {
        if (languages.isEmpty()) {
            return false;
        }
        return getAllSupportedLanguageOptions()
                .stream()
                .filter(Language::isRtl)
                .anyMatch(lang -> languages.contains(lang.getValue()));
    }
    
    public static Language getDefaultLanguage() {
        return new Language("English", "en");
    }
    
    /**
     * get all the languages options
     */
    public static List<Language> getAllSupportedLanguageOptions() {
        return Arrays.asList(
            new Language("Armenian", "hy"),
            new Language("Catalan", "ca"),
            new Language("Chinese (Simplified)", "zh-cn"),
            new Language("Chinese (Traditional)", "zh-tw"),
            new Language("Czech", "cs"),
            new Language("Danish", "da"),
            new Language("Dutch", "nl"),
            new Language("English", "en"),
            new Language("Estonian", "et"),
            new Language("Farsi", "fa", true),
            new Language("French", "fr"),
            new Language("Galician", "gl"),
            new Language("German", "de"),
            new Language("Greek", "el"),
            new Language("Hindi", "hi"),
            new Language("Hungarian", "hu"),
            new Language("Italian", "it"),
            new Language("Japanese", "ja"),
            new Language("Korean", "ko"),
            new Language("Marathi", "mr"),
            new Language("Polish", "pl"),
            new Language("Portuguese (Brazilian)", "pt-br"),
            new Language("Portuguese", "pt-pt"),
            new Language("Romanian", "ro"),
            new Language("Russian", "ru"),
            new Language("Slovak", "sk"),
            new Language("Serbian", "sr"),
            new Language("Spanish", "es"),
            new Language("Swedish", "sv"),
            new Language("Turkish", "tr"),
            new Language("Tamil", "ta"),
            new Language("Thai", "th"),
            new Language("Ukrainian", "ua"),
            new Language("Vietnamese", "vi")
        );
    }
    

}
