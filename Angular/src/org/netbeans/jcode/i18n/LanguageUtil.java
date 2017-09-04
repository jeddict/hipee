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
     *
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
        return new Language("English", "English", "en");
    }

    /**
     * get all the languages options
     */
    public static List<Language> getAllSupportedLanguageOptions() {
        return Arrays.asList(
                new Language("Arabic (Libya)", "العربية", "ar-ly", true, true),
                new Language("Armenian", "Հայերեն", "hy"),
                new Language("Catalan", "Català", "ca"),
                new Language("Chinese (Simplified)", "中文（简体）", "zh-cn"),
                new Language("Chinese (Traditional)", "繁體中文", "zh-tw"),
                new Language("Czech", "Český", "cs"),
                new Language("Danish", "Dansk", "da"),
                new Language("Dutch", "Nederlands", "nl"),
                new Language("English", "English", "en"),
                new Language("Estonian", "Eesti", "et"),
                new Language("Farsi", "فارسی", "fa", true),
                new Language("French", "Français", "fr"),
                new Language("Galician", "Galego", "gl"),
                new Language("German", "Deutsch", "de"),
                new Language("Greek", "Ελληνικά", "el"),
                new Language("Hindi", "हिंदी", "hi"),
                new Language("Hungarian", "Magyar", "hu"),
                new Language("Indonesian", "Bahasa Indonesia", "id"),
                new Language("Italian", "Italiano", "it"),
                new Language("Japanese", "日本語", "ja"),
                new Language("Korean", "한국어", "ko"),
                new Language("Marathi", "मराठी", "mr"),
                new Language("Polish", "Polski", "pl"),
                new Language("Portuguese (Brazilian)", "Português (Brasil)", "pt-br"),
                new Language("Portuguese", "Português", "pt-pt"),
                new Language("Romanian", "Română", "ro"),
                new Language("Russian", "Русский", "ru"),
                new Language("Slovak", "Slovenský", "sk"),
                new Language("Serbian", "Srpski", "sr"),
                new Language("Spanish", "Español", "es"),
                new Language("Swedish", "Svenska", "sv"),
                new Language("Turkish", "Türkçe", "tr"),
                new Language("Tamil", "தமிழ்", "ta"),
                new Language("Thai", "ไทย", "th"),
                new Language("Ukrainian", "Українська", "ua"),
                new Language("Vietnamese", "Tiếng Việt", "vi")
        );
    }

}
