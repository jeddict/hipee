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
package org.netbeans.jcode.angular2.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.netbeans.jcode.ng.main.domain.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.netbeans.jcode.core.util.StringHelper.camelCase;
import static org.netbeans.jcode.core.util.StringHelper.firstLower;
import static org.netbeans.jcode.core.util.StringHelper.firstUpper;
import static org.netbeans.jcode.core.util.StringHelper.kebabCase;
import static org.netbeans.jcode.core.util.StringHelper.pluralize;
import static org.netbeans.jcode.core.util.StringHelper.startCase;
import static org.netbeans.jcode.ng.main.domain.NGRelationship.MANY_TO_MANY;
import static org.netbeans.jcode.ng.main.domain.NGRelationship.MANY_TO_ONE;
import static org.netbeans.jcode.ng.main.domain.NGRelationship.ONE_TO_MANY;
import static org.netbeans.jcode.ng.main.domain.NGRelationship.ONE_TO_ONE;
import org.openide.util.Exceptions;

/**
 *
 * @author jGauravGupta
 */
public class NG2Entity extends NGEntity {

    private String entityAngularName;
    private final Map<String, List<NGRelationship>> differentRelationships = new HashMap<>();

    public NG2Entity(String name, String entityAngularSuffix) {
        super(name, entityAngularSuffix);
        String entityNameSpinalCased = kebabCase(firstLower(name));
        String entityNamePluralizedAndSpinalCased = kebabCase(firstLower(pluralize(name)));

        this.entityFileName = kebabCase(this.entityNameCapitalized + firstUpper(entityAngularSuffix));
        this.entityPluralFileName = entityNamePluralizedAndSpinalCased + entityAngularSuffix;
        this.entityServiceFileName = this.entityFileName;
        this.entityAngularName = this.entityClass + firstUpper(camelCase(entityAngularSuffix));
        this.entityStateName = kebabCase(entityAngularName);
        this.entityUrl = this.entityStateName;
        this.entityTranslationKey = this.entityInstance;
        this.entityTranslationKeyMenu = camelCase(this.entityStateName);
    }

    public void addRelationship(NGRelationship relationship) {
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

    public String getEntityAngularName() {
        return entityAngularName;
    }

    /**
     * @return the differentRelationships
     */
    public String getDifferentRelationshipsJSON() {
        try {
            //map issue in nashorn
            return new ObjectMapper().writeValueAsString(differentRelationships);
        } catch (JsonProcessingException ex) {
            Exceptions.printStackTrace(ex);
        }
        return "{}";
    }

}
