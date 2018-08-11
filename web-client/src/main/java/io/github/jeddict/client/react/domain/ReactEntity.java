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
package io.github.jeddict.client.react.domain;

import io.github.jeddict.client.web.main.domain.BaseRelationship;
import io.github.jeddict.client.web.main.domain.BaseEntity;
import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import static io.github.jeddict.jcode.util.StringHelper.firstUpper;
import static io.github.jeddict.jcode.util.StringHelper.kebabCase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import static org.apache.commons.lang.StringUtils.isNotEmpty;
import org.openide.util.Exceptions;

/**
 *
 * @author jGauravGupta
 */
public class ReactEntity extends BaseEntity {

    private final String entityReactName;
    private boolean fieldsIsReactAvField;
    private final Map<String, List<BaseRelationship>> differentRelationships = new HashMap<>();

    public ReactEntity(String name, String entityReactSuffix, String appName, String clientRootFolder) {
        super(name, entityReactSuffix, appName, clientRootFolder);
        this.entityReactName = this.entityClass + firstUpper(camelCase(entityReactSuffix));
        this.entityStateName = kebabCase(entityReactName);
        this.entityUrl = this.entityStateName;
        this.entityTranslationKeyMenu = camelCase(isNotEmpty(clientRootFolder) ? clientRootFolder+"-"+entityStateName : entityStateName);
    }

    @Override
    public void addRelationship(BaseRelationship relationship) {
        super.addRelationship(relationship);
        String entityType = relationship.getOtherEntityNameCapitalized();
        if (!getDifferentTypes().contains(entityType)) {
            getDifferentTypes().add(entityType);
        }

        if (!differentRelationships.containsKey(entityType)) {
            differentRelationships.put(entityType, new ArrayList<>());
        }
        differentRelationships.get(entityType).add(relationship);
    }

    @Override
    public String getEntityName() {
        return getEntityReactName();
    }
    
    public String getEntityAngularName() { //remove after stable
        return getEntityReactName();
    }
    
    public String getEntityReactName() {
        return entityReactName;
    }

    /**
     * @return the differentRelationships
     */
    public String getDifferentRelationshipsJSON() {
        try {
            //map issue in nashorn
            Jsonb jsonb = JsonbBuilder.create();
            return jsonb.toJson(differentRelationships);
        } catch (JsonbException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "{}";
    }

    public boolean getFieldsIsReactAvField() {
        return !(isFieldsContainZonedDateTime() && isFieldsContainInstant() && isFieldsContainBoolean());
    }

}
