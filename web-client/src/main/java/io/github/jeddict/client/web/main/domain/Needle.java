/**
 * Copyright 2013-2018 the original author or authors from the Jeddict project (https://jeddict.github.io/).
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
package io.github.jeddict.client.web.main.domain;

import static io.github.jeddict.jcode.util.FileUtil.expandTemplateContent;
import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import static io.github.jeddict.jcode.util.StringHelper.startCase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.github.jeddict.client.angular.domain.NGApplicationConfig;

/**
 *
 * @author jGauravGupta
 */
public class Needle {

    private final String insertPointer;
    private final String template;

    public Needle(String insertPointer, String template) {
        this.insertPointer = insertPointer;
        this.template = template;
    }

    /**
     * @return the insertPointer
     */
    public String getInsertPointer() {
        return insertPointer;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }
    
    public String getTemplate(BaseApplicationConfig applicationConfig, List<BaseEntity> ngEntities) {
        StringBuilder content = new StringBuilder();
        if (ngEntities != null) {
            for (BaseEntity entity : ngEntities) {
                if(entity.isUpgrade()){
                    continue;
                }
                Map<String, Object> param = new HashMap<>();
                param.put("entityFolderName", entity.getEntityFolderName());
                param.put("entityFileName", entity.getEntityFileName());
                param.put("entityClass", entity.getEntityClass());
                param.put("entity" + applicationConfig.getJsType() + "Name", entity.getEntityName());
                param.put("entityInstance", entity.getEntityInstance());
                param.put("routerName", entity.getEntityStateName());
                param.put("enableTranslation", applicationConfig.isEnableTranslation());
                param.put("camelCase_routerName", camelCase(entity.getEntityStateName()));
                param.put("startCase_routerName", startCase(entity.getEntityStateName()));
                param.put("entityTranslationKeyMenu", entity.getEntityTranslationKeyMenu());
                param.put("startCase_entityClass", startCase(entity.getEntityClass()));
                if(applicationConfig instanceof NGApplicationConfig){
                    param.put("appName", ((NGApplicationConfig)applicationConfig).getAngularXAppName());
                }
                param.put("prefix", applicationConfig.getJhiPrefix());
                content.append(expandTemplateContent(template, param));
            }
        } else {
            Map<String, Object> param = new HashMap<>();
            param.put("srcDir", applicationConfig.getSrcDir());
            param.put("languages", applicationConfig.getLanguages());
            param.put("languageInstances", applicationConfig.getLanguageInstances());
            content.append(expandTemplateContent(template, param));
        }
        return content.toString();
    }

}
