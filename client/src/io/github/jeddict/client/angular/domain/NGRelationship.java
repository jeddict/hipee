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
package io.github.jeddict.client.angular.domain;

import static io.github.jeddict.jcode.util.StringHelper.kebabCase;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.extend.RelationAttribute;
import io.github.jeddict.client.web.main.domain.BaseRelationship;

public class NGRelationship extends BaseRelationship {

    private String otherEntityStateName;
    private String otherEntityRelationshipNamePlural;

    public NGRelationship(String appName, String entitySuffix, Entity entity, RelationAttribute relation) {
      super(appName, entitySuffix, entity, relation);
    }

    /**
     * @return the otherEntityStateName
     */
    @Override
    public String getOtherEntityStateName() {
        if (otherEntityStateName == null) {
            otherEntityStateName = kebabCase(getOtherEntityAngularName());
        }
        return otherEntityStateName;
    }

    /**
     * @return the otherEntityRelationshipNamePlural
     */
    @Override
    public String getOtherEntityRelationshipNamePlural() {
        if (otherEntityRelationshipNamePlural == null
                && (ONE_TO_MANY.equals(getRelationshipType())
                || (MANY_TO_MANY.equals(getRelationshipType()) && isOwnerSide() == false)
                || (ONE_TO_ONE.equals(getRelationshipType()) && "user".equals(getOtherEntityName().toLowerCase())))) {
            otherEntityRelationshipNamePlural = pluralize(getOtherEntityRelationshipName());
        }
        return otherEntityRelationshipNamePlural;
    }

}
