/**
 * Copyright 2013-2019 the original author or authors from the Jeddict project (https://jeddict.github.io/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.jeddict.client.i18n;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import static io.github.jeddict.client.i18n.LanguageUtil.getDefaultLanguage;
import io.github.jeddict.jcode.LayerConfigData;
import jakarta.json.bind.annotation.JsonbTransient;
import static java.util.Collections.emptyList;

/**
 *
 * @author Gaurav Gupta
 */
public class I18NConfigData extends LayerConfigData {
    
    private boolean enabled = true;
    
    private Language nativeLanguage;
    
    private List<Language> otherLanguages;

    /**
     * @return the enable
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enable the enable to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the nativeLanguage
     */
    public Language getNativeLanguage() {
        if(nativeLanguage == null){
            return getDefaultLanguage();
        }
        return nativeLanguage;
    }

    /**
     * @param nativeLanguage the nativeLanguage to set
     */
    public void setNativeLanguage(Language nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    /**
     * @return the otherLanguages
     */
    public List<Language> getOtherLanguages() {
        if (otherLanguages == null) {
            return emptyList();
        }
        return otherLanguages;
    }

    /**
     * @param otherLanguages the otherLanguages to set
     */
    public void setOtherLanguages(List<Language> otherLanguages) {
        this.otherLanguages = otherLanguages;
    }
    
    @JsonbTransient
    public Set<String> getOtherLanguagesKeyword(){
        return getOtherLanguages()
                .stream()
                .map(Language::getValue)
                .collect(toSet());
    }
    
    @JsonbTransient
    public Set<Language> getLanguageInstances() {
        Set<Language> languages = new HashSet<>(getOtherLanguages());
        languages.add(getNativeLanguage());
        return languages;
    }
    
    @Override
    public List<String> getUsageDetails() {
        return Collections.<String>emptyList();
    }
}
