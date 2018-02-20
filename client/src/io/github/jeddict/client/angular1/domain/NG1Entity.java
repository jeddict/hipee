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
package io.github.jeddict.client.angular1.domain;

import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import static io.github.jeddict.jcode.util.StringHelper.firstLower;
import static io.github.jeddict.jcode.util.StringHelper.firstUpper;
import static io.github.jeddict.jcode.util.StringHelper.kebabCase;
import static io.github.jeddict.jcode.util.StringHelper.pluralize;
import java.util.ArrayList;
import java.util.List;
import io.github.jeddict.client.web.main.domain.BaseEntity;
import io.github.jeddict.client.web.main.domain.BaseRelationship;

/**
 *
 * @author jGauravGupta
 */
public class NG1Entity extends BaseEntity {

    private String entityAngularJSName;
    private final List<BaseRelationship> differentRelationships = new ArrayList<>();

    public NG1Entity(String name, String entityAngularSuffix) {
        super(name, entityAngularSuffix);
        String entityNameSpinalCased = kebabCase(firstLower(name));
        String entityNamePluralizedAndSpinalCased = kebabCase(firstLower(pluralize(name)));

        this.entityFileName = entityNameSpinalCased + entityAngularSuffix;
        this.entityPluralFileName = entityNamePluralizedAndSpinalCased + entityAngularSuffix;
        this.entityServiceFileName = entityNameSpinalCased + entityAngularSuffix;
        this.entityAngularJSName = this.entityClass + firstUpper(camelCase(entityAngularSuffix));
        this.entityStateName = entityNameSpinalCased + entityAngularSuffix;
        this.entityUrl = entityNameSpinalCased + entityAngularSuffix;
        this.entityTranslationKey = this.entityInstance;
        this.entityTranslationKeyMenu = camelCase(this.entityStateName);
    }

    @Override
    public void addRelationship(BaseRelationship relationship) {
        super.addRelationship(relationship);

        String entityType = relationship.getOtherEntityNameCapitalized();
        if (!getDifferentTypes().contains(entityType)) {
            getDifferentTypes().add(entityType);
            getDifferentRelationships().add(relationship);
        }
    }

    @Override
    public String getEntityName() {
        return getEntityAngularJSName();
    }

    /**
     * @return the entityAngularJSName
     */
    public String getEntityAngularJSName() {
        return entityAngularJSName;
    }

    /**
     * @return the differentRelationships
     */
    public List<BaseRelationship> getDifferentRelationships() {
        return differentRelationships;
    }

}
