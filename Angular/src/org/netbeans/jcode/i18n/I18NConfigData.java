/**
 * Copyright [2017] Gaurav Gupta
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
package org.netbeans.jcode.i18n;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toSet;
import static org.netbeans.jcode.i18n.LanguageUtil.getDefaultLanguage;
import org.netbeans.jcode.stack.config.data.LayerConfigData;

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
        return otherLanguages;
    }

    /**
     * @param otherLanguages the otherLanguages to set
     */
    public void setOtherLanguages(List<Language> otherLanguages) {
        this.otherLanguages = otherLanguages;
    }
    
    public Set<String> getOtherLanguagesKeyword(){
        return otherLanguages.stream()
                .map(lang -> lang.getValue())
                .collect(toSet());
    }
    
    @Override
    public List<String> getUsageDetails() {
        return Collections.EMPTY_LIST;
    }
}
