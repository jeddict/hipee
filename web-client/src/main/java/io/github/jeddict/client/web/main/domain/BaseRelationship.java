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

import io.github.jeddict.jcode.util.StringHelper;
import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import static io.github.jeddict.jcode.util.StringHelper.firstLower;
import static io.github.jeddict.jcode.util.StringHelper.firstUpper;
import static io.github.jeddict.jcode.util.StringHelper.kebabCase;
import static io.github.jeddict.jcode.util.StringHelper.startCase;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.ManyToMany;
import io.github.jeddict.jpa.spec.ManyToOne;
import io.github.jeddict.jpa.spec.OneToMany;
import io.github.jeddict.jpa.spec.OneToOne;
import io.github.jeddict.jpa.spec.extend.RelationAttribute;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.github.jeddict.util.StringUtils;
import static io.github.jeddict.util.StringUtils.EMPTY;
import static io.github.jeddict.util.StringUtils.isEmpty;
import static io.github.jeddict.util.StringUtils.isNotEmpty;

public abstract class BaseRelationship {

    private static final Logger LOG = Logger.getLogger(BaseRelationship.class.getName());

    public static final String ONE_TO_ONE = "one-to-one";
    public static final String ONE_TO_MANY = "one-to-many";
    public static final String MANY_TO_ONE = "many-to-one";
    public static final String MANY_TO_MANY = "many-to-many";

    private String name;

    private String relationshipType;//MANY_TO_ONE, ONE_TO_ONE ,MANY_TO_MANY
    private boolean ownerSide;

    private final String relationshipName;
    private final String relationshipFieldName;
    private final String relationshipFieldNamePlural;
    private String relationshipNameHumanized;
    private final String relationshipNamePlural;

    private String otherEntityRelationshipNamePlural;
    private final String otherEntityName;
    private final String otherEntityNameCapitalized;
    private final String otherEntityNamePlural;
    private final String otherEntityNameCapitalizedPlural;

    private String otherEntityField;
    private final String otherEntityFieldCapitalized;
    private String otherEntityRelationshipName;

    private final String otherEntityAngularName;
    private final String otherEntityRelationshipNameCapitalized;
    private final String otherEntityRelationshipNameCapitalizedPlural;
    private final String otherEntityStateName;

    private final String relationshipNameCapitalized;
    private final String relationshipNameCapitalizedPlural;

    private boolean relationshipRequired;
    private List<String> relationshipValidateRules;
    private boolean relationshipValidate;

    private final String otherEntityModuleName;
    private String otherEntityFileName;

    private String otherEntityClientRootFolder;
    private String otherEntityModelName;
    private String otherEntityPath;
    private String otherEntityModulePath;
    protected String entitySuffix;

    public BaseRelationship(String appName, String entitySuffix, Entity entity, RelationAttribute relation, String clientRootFolder) {
        this.entitySuffix = entitySuffix;
        this.relationshipName = isEmpty(relation.getJsonbProperty()) ? relation.getName() : relation.getJsonbProperty();
        if (relation instanceof ManyToMany) {
            relationshipType = MANY_TO_MANY;
        } else if (relation instanceof OneToMany) {
            relationshipType = ONE_TO_MANY;
        } else if (relation instanceof ManyToOne) {
            relationshipType = MANY_TO_ONE;
        } else if (relation instanceof OneToOne) {
            relationshipType = ONE_TO_ONE;
        }
        this.name = entity.getClazz();
        this.ownerSide = relation.isOwner();
        this.otherEntityName = firstLower(relation.getConnectedEntity().getClazz());
        
        this.otherEntityRelationshipName = relation.getConnectedAttributeName();
        if (otherEntityRelationshipName == null) {
            if (ONE_TO_MANY.equals(relationshipType)
                    || (MANY_TO_MANY.equals(relationshipType) && ownerSide == false)
                    || (ONE_TO_ONE.equals(relationshipType))) {
                otherEntityRelationshipName = firstLower(getName());//warning
                LOG.log(Level.WARNING, "otherEntityRelationshipName is missing in {0} for relationship , using {1} as fallback", new Object[]{this.getName(), firstLower(this.getName())});
            } else {
                otherEntityRelationshipName = EMPTY;
            }
        }
        
        relationshipNameCapitalized = firstUpper(relationshipName);
        if (relationshipName.length() > 1) {
            relationshipNameCapitalizedPlural = pluralize(firstUpper(relationshipName));
        } else {
            relationshipNameCapitalizedPlural = firstUpper(pluralize(relationshipName));
        }
        relationshipNameHumanized = startCase(relationshipName);
        relationshipNamePlural = pluralize(relationshipName);
        relationshipFieldName = firstLower(relationshipName);
        relationshipFieldNamePlural = pluralize(firstLower(relationshipName));
        
        if (MANY_TO_ONE.equals(relationshipType)
                || (MANY_TO_MANY.equals(relationshipType) && ownerSide == true)
                || (ONE_TO_ONE.equals(relationshipType) && ownerSide == true)) {
            otherEntityField = "id";
            LOG.log(Level.WARNING, "otherEntityField is missing in {0} for relationship , using id as fallback", this.getName());
        }

        if (ONE_TO_MANY.equals(relationshipType)
                || (MANY_TO_MANY.equals(relationshipType) && ownerSide == false)
                || (ONE_TO_ONE.equals(relationshipType) && "user".equals(otherEntityName.toLowerCase()))) {
            otherEntityRelationshipNamePlural = pluralize(otherEntityRelationshipName);
        }
        otherEntityRelationshipNameCapitalized = firstUpper(otherEntityRelationshipName);
        otherEntityRelationshipNameCapitalizedPlural = pluralize(firstUpper(otherEntityRelationshipName));

        otherEntityNamePlural = pluralize(otherEntityName);
        otherEntityNameCapitalized = firstUpper(otherEntityName);
        if (!"User".equals(otherEntityNameCapitalized)) {
            String otherEntitySuffix = this.entitySuffix != null ? entitySuffix : EMPTY;
            this.otherEntityAngularName = firstUpper(this.otherEntityName) + firstUpper(camelCase(otherEntitySuffix));
        } else {
            this.otherEntityAngularName = "User";
        }
        otherEntityNameCapitalizedPlural = pluralize(firstUpper(otherEntityName));

        otherEntityFieldCapitalized = otherEntityField != null ? firstUpper(otherEntityField) : null;
        otherEntityStateName = kebabCase(otherEntityAngularName);

        String entityParentPathAddition = BaseEntity.getEntityParentPathAddition(clientRootFolder);
        if (!"User".equals(otherEntityNameCapitalized)) {
            this.otherEntityModuleName = appName + this.otherEntityNameCapitalized + "Module";
            this.otherEntityFileName = kebabCase(this.otherEntityAngularName);
            this.otherEntityClientRootFolder = clientRootFolder;//otherEntityData.clientRootFolder;
            this.otherEntityModulePath = kebabCase(firstLower(otherEntityName));

            if (isNotEmpty(otherEntityClientRootFolder)) {
                if (StringUtils.equals(clientRootFolder, otherEntityClientRootFolder)) {
                    this.otherEntityModulePath = this.otherEntityFileName;
                } else {
                    this.otherEntityModulePath = (isNotEmpty(entityParentPathAddition) ? entityParentPathAddition + '/' : "") + otherEntityClientRootFolder + '/' + otherEntityFileName;
                }
                this.otherEntityModelName = otherEntityClientRootFolder + '/' + otherEntityFileName;
                this.otherEntityPath = otherEntityClientRootFolder + "/" + this.otherEntityFileName;
            } else {
                this.otherEntityModulePath = (isNotEmpty(entityParentPathAddition) ? entityParentPathAddition + '/' : "") + otherEntityFileName;
                this.otherEntityModelName = this.otherEntityFileName;
                this.otherEntityPath = this.otherEntityFileName;
            }
        } else {
            this.otherEntityModuleName = appName + "SharedModule";
            this.otherEntityModulePath = "app/core";
        }
        
        this.relationshipValidateRules = Collections.emptyList();
        if (!relationshipValidateRules.isEmpty() && relationshipValidateRules.contains("required")) {
            relationshipValidate = relationshipRequired = true;//validation
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the relationshipRequired
     */
    public boolean isRelationshipRequired() {
        return relationshipRequired;
    }

    /**
     * @param relationshipRequired the relationshipRequired to set
     */
    public void setRelationshipRequired(boolean relationshipRequired) {
        this.relationshipRequired = relationshipRequired;
    }

    /**
     * @return the relationshipValidateRules
     */
    public List<String> getRelationshipValidateRules() {
        return relationshipValidateRules;
    }

    /**
     * @param relationshipValidateRules the relationshipValidateRules to set
     */
    public void setRelationshipValidateRules(List<String> relationshipValidateRules) {
        this.relationshipValidateRules = relationshipValidateRules;
    }

    /**
     * @return the relationshipValidate
     */
    public boolean isRelationshipValidate() {
        return relationshipValidate;
    }

    /**
     * @param relationshipValidate the relationshipValidate to set
     */
    public void setRelationshipValidate(boolean relationshipValidate) {
        this.relationshipValidate = relationshipValidate;
    }

    /**
     * @return the relationshipType
     */
    public String getRelationshipType() {
        if (relationshipType == null) {
            throw new IllegalStateException("relationshipType is missing in " + this.getName() + " for relationship");
        }
        return relationshipType;
    }

    /**
     * @param relationshipType the relationshipType to set
     */
    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * @return the ownerSide
     */
    public boolean isOwnerSide() {

        return ownerSide;
    }

    /**
     * @param ownerSide the ownerSide to set
     */
    public void setOwnerSide(boolean ownerSide) {
        this.ownerSide = ownerSide;
    }

    /**
     * @return the relationshipName
     */
    public String getRelationshipName() {
        return relationshipName;
    }

    /**
     * @return the relationshipFieldName
     */
    public String getRelationshipFieldName() {
        return relationshipFieldName;
    }

    /**
     * @return the relationshipFieldNamePlural
     */
    public String getRelationshipFieldNamePlural() {
        return relationshipFieldNamePlural;
    }

    /**
     * @return the relationshipNameHumanized
     */
    public String getRelationshipNameHumanized() {
        return relationshipNameHumanized;
    }

    /**
     * @param relationshipNameHumanized the relationshipNameHumanized to set
     */
    public void setRelationshipNameHumanized(String relationshipNameHumanized) {
        this.relationshipNameHumanized = relationshipNameHumanized;
    }
    
    /**
     * @return the relationshipNamePlural
     */
    public String getRelationshipNamePlural() {
        return relationshipNamePlural;
    }

    /**
     * @return the relationshipNameCapitalized
     */
    public String getRelationshipNameCapitalized() {
        return relationshipNameCapitalized;
    }

    /**
     * @return the relationshipNameCapitalizedPlural
     */
    public String getRelationshipNameCapitalizedPlural() {
        return relationshipNameCapitalizedPlural;
    }

    /**
     * @return the otherEntityName
     */
    public String getOtherEntityName() {
        return otherEntityName;
    }

    /**
     * @return the otherEntityField
     */
    public String getOtherEntityField() {
        return otherEntityField;
    }

    /**
     * @param otherEntityField the otherEntityField to set
     */
    public void setOtherEntityField(String otherEntityField) {
        this.otherEntityField = otherEntityField;
    }

    /**
     * @return the otherEntityFieldCapitalized
     */
    public String getOtherEntityFieldCapitalized() {
        return otherEntityFieldCapitalized;
    }

    /**
     * @return the otherEntityRelationshipName
     */
    public String getOtherEntityRelationshipName() {
        return otherEntityRelationshipName;
    }

    /**
     * @return the otherEntityNameCapitalized
     */
    public String getOtherEntityNameCapitalized() {
        return otherEntityNameCapitalized;
    }

    /**
     * @return the otherEntityNamePlural
     */
    public String getOtherEntityNamePlural() {
        return otherEntityNamePlural;
    }

    /**
     * @return the otherEntityNameCapitalizedPlural
     */
    public String getOtherEntityNameCapitalizedPlural() {
        return otherEntityNameCapitalizedPlural;
    }

    /**
     * @return the otherEntityAngularName
     */
    public String getOtherEntityAngularName() {
        return otherEntityAngularName;
    }

    /**
     * @return the otherEntityRelationshipNameCapitalized
     */
    public String getOtherEntityRelationshipNameCapitalized() {
        return otherEntityRelationshipNameCapitalized;
    }

    /**
     * @return the otherEntityRelationshipNameCapitalizedPlural
     */
    public String getOtherEntityRelationshipNameCapitalizedPlural() {
        return otherEntityRelationshipNameCapitalizedPlural;
    }

    /**
     * @return the otherEntityModuleName
     */
    public String getOtherEntityModuleName() {
        return otherEntityModuleName;
    }
    
    public String getOtherEntityFileName() {
        return otherEntityFileName;
    }

    /**
     * @return the otherEntityModulePath
     */
    public String getOtherEntityModulePath() {
        return otherEntityModulePath;
    }

    /**
     * @return the otherEntityRelationshipNamePlural
     */
    public String getOtherEntityRelationshipNamePlural() {
        return otherEntityRelationshipNamePlural;
    }

    /**
     * @return the otherEntityStateName
     */
    public String getOtherEntityStateName(){
        return otherEntityStateName;
    }
    
    public String getOtherEntityClientRootFolder() {
        return otherEntityClientRootFolder;
    }

    public String getOtherEntityModelName() {
        return otherEntityModelName;
    }
        
    public String getOtherEntityPath() {
        return otherEntityPath;
    }
    
    protected String pluralize(String data) {
        if (relationshipType.equals(ONE_TO_MANY) || relationshipType.equals(MANY_TO_MANY)) {
            return data;
        } else {
            return StringHelper.pluralize(data);
        }
    }

}
