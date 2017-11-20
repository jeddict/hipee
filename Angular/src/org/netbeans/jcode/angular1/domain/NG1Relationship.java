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
package org.netbeans.jcode.angular1.domain;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.netbeans.jcode.core.util.StringHelper.kebabCase;
import static org.netbeans.jcode.core.util.StringHelper.trim;
import org.netbeans.jcode.ng.main.domain.NGRelationship;
import static org.netbeans.jcode.ng.main.domain.NGRelationship.MANY_TO_MANY;
import static org.netbeans.jcode.ng.main.domain.NGRelationship.ONE_TO_MANY;
import static org.netbeans.jcode.ng.main.domain.NGRelationship.ONE_TO_ONE;
import org.netbeans.jpa.modeler.spec.Entity;
import org.netbeans.jpa.modeler.spec.extend.RelationAttribute;

public class NG1Relationship extends NGRelationship {

    private String otherEntityStateName;
    private String otherEntityRelationshipNamePlural;

    public NG1Relationship(String angularAppName, String entityAngularJSSuffix, Entity entity, RelationAttribute relation) {
        super(angularAppName, entityAngularJSSuffix, entity, relation);
    }

    /**
     * @return the otherEntityStateName
     */
    @Override
    public String getOtherEntityStateName() {
        if (otherEntityStateName == null) {
            otherEntityStateName = trim(kebabCase(getOtherEntityName()), '-') + (entityAngularJSSuffix != null ? entityAngularJSSuffix : EMPTY);
        }
        return otherEntityStateName;
    }

    /**
     * @return the otherEntityRelationshipNamePlural
     */
    @Override
    public String getOtherEntityRelationshipNamePlural() {
        if (otherEntityRelationshipNamePlural != null
                && (ONE_TO_MANY.equals(getRelationshipType())
                || (MANY_TO_MANY.equals(getRelationshipType()) && isOwnerSide() == false)
                || (ONE_TO_ONE.equals(getRelationshipType())))) {
            otherEntityRelationshipNamePlural = pluralize(getOtherEntityRelationshipName());
        }
        return otherEntityRelationshipNamePlural;
    }

}
