/**
 * Copyright 2013-2019 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see https://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Portions Copyright 2013-2019 Gaurav Gupta
 */

var LANGUAGES = [
    {name: 'Arabic (Libya)', dispName: 'العربية', value: 'ar-ly', rtl: true, skipForLocale: true},
    {name: 'Armenian', dispName: 'Հայերեն', value: 'hy'},
    {name: 'Catalan', dispName: 'Català', value: 'ca'},
    {name: 'Chinese (Simplified)', dispName: '中文（简体）', value: 'zh-cn'},
    {name: 'Chinese (Traditional)', dispName: '繁體中文', value: 'zh-tw'},
    {name: 'Czech', dispName: 'Český', value: 'cs'},
    {name: 'Danish', dispName: 'Dansk', value: 'da'},
    {name: 'Dutch', dispName: 'Nederlands', value: 'nl'},
    {name: 'English', dispName: 'English', value: 'en'},
    {name: 'Estonian', dispName: 'Eesti', value: 'et'},
    {name: 'Farsi', dispName: 'فارسی', value: 'fa', rtl: true},
    {name: 'French', dispName: 'Français', value: 'fr'},
    {name: 'Galician', dispName: 'Galego', value: 'gl'},
    {name: 'German', dispName: 'Deutsch', value: 'de'},
    {name: 'Greek', dispName: 'Ελληνικά', value: 'el'},
    {name: 'Hindi', dispName: 'हिंदी', value: 'hi'},
    {name: 'Hungarian', dispName: 'Magyar', value: 'hu'},
    {name: 'Indonesian', dispName: 'Bahasa Indonesia', value: 'id'},
    {name: 'Italian', dispName: 'Italiano', value: 'it'},
    {name: 'Japanese', dispName: '日本語', value: 'ja'},
    {name: 'Korean', dispName: '한국어', value: 'ko'},
    {name: 'Marathi', dispName: 'मराठी', value: 'mr'},
    {name: 'Polish', dispName: 'Polski', value: 'pl'},
    {name: 'Portuguese (Brazilian)', dispName: 'Português (Brasil)', value: 'pt-br'},
    {name: 'Portuguese', dispName: 'Português', value: 'pt-pt'},
    {name: 'Romanian', dispName: 'Română', value: 'ro'},
    {name: 'Russian', dispName: 'Русский', value: 'ru'},
    {name: 'Slovak', dispName: 'Slovenský', value: 'sk'},
    {name: 'Serbian', dispName: 'Srpski', value: 'sr'},
    {name: 'Spanish', dispName: 'Español', value: 'es'},
    {name: 'Swedish', dispName: 'Svenska', value: 'sv'},
    {name: 'Turkish', dispName: 'Türkçe', value: 'tr'},
    {name: 'Tamil', dispName: 'தமிழ்', value: 'ta'},
    {name: 'Thai', dispName: 'ไทย', value: 'th'},
    {name: 'Ukrainian', dispName: 'Українська', value: 'ua'},
    {name: 'Vietnamese', dispName: 'Tiếng Việt', value: 'vi'}
];

function skipLanguageForLocale(language) {
    var out = this.getAllSupportedLanguageOptions().filter(function (lang) {
        return language === lang.value;
    });
    return out && out[0] && !!out[0].skipForLocale;
}

function getAllSupportedLanguageOptions() {
    return LANGUAGES;
}

/**
 * return the localeId from the given language key (from constants.LANGUAGES)
 * if no localeId is defined, return the language key (which is a localeId itself)
 * @param {string} language - language key
 */
function getLocaleId(language) {
    const langObj = getAllSupportedLanguageOptions().find(function(langObj) { return langObj.value === language});
    return langObj.localeId || language;
}

/**
 * Generate a primary key, according to the type
 *
 * @param {any} pkType - the type of the primary key
 * @param {any} prodDatabaseType - the database type
 */
function generateTestEntityId(pkType, prodDatabaseType) {
    if (pkType === 'String') {
        if (prodDatabaseType === 'cassandra') {
            return '\'9fec3727-3421-4967-b213-ba36557ca194\'';
        }
        return '\'123\'';
    }
    return 123;
}
