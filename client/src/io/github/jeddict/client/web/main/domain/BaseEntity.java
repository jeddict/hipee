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

import static io.github.jeddict.jcode.util.StringHelper.firstLower;
import static io.github.jeddict.jcode.util.StringHelper.firstUpper;
import static io.github.jeddict.jcode.util.StringHelper.kebabCase;
import static io.github.jeddict.jcode.util.StringHelper.pluralize;
import static io.github.jeddict.jcode.util.StringHelper.startCase;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import static io.github.jeddict.client.web.main.domain.BaseRelationship.MANY_TO_MANY;
import static io.github.jeddict.client.web.main.domain.BaseRelationship.MANY_TO_ONE;
import static io.github.jeddict.client.web.main.domain.BaseRelationship.ONE_TO_MANY;
import static io.github.jeddict.client.web.main.domain.BaseRelationship.ONE_TO_ONE;
import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 *
 * @author jGauravGupta
 */
public abstract class BaseEntity {

    private String name;
    protected String entityNameCapitalized;
    protected String entityClass;
    private String entityClassHumanized;
    private String entityClassPlural;
    private String entityClassPluralHumanized;
    protected String entityInstance;
    private String entityInstancePlural;
    private String entityApiUrl;
    private String entityFolderName;
    private final String entityModelFileName;
    private final String entityParentPathAddition;

    protected String entityFileName;
    protected String entityPluralFileName;
    protected String entityServiceFileName;
    protected String entityStateName;
    protected String entityUrl;
    protected String entityTranslationKey;
    protected String entityTranslationKeyMenu;

    private boolean fieldsContainDate;
    private boolean fieldsContainInstant;
    private boolean fieldsContainZonedDateTime;
    private boolean fieldsContainLocalDate;
    private boolean fieldsContainBigDecimal;
    private boolean fieldsContainBlob;
    private boolean fieldsContainImageBlob;
    private boolean fieldsContainBlobOrImage;
    private boolean validation;
    private boolean fieldsContainOwnerManyToMany;
    private boolean fieldsContainNoOwnerOneToOne;
    private boolean fieldsContainOwnerOneToOne;
    private boolean fieldsContainOneToMany;
    private boolean fieldsContainManyToOne;
    private List<String> differentTypes = new ArrayList<>();
    private final List<BaseField> fields = new ArrayList<>();
    private final List<BaseRelationship> relationships = new ArrayList<>();
    private String pkType;

    private boolean haveFieldWithJavadoc;

    private boolean upgrade;
    
    private String i18nKeyPrefix;

    public BaseEntity(String name, String entityAngularSuffix, String appName, String clientRootFolder) {

        this.name = name;
        String entityName = this.name;
        String entityNamePluralizedAndSpinalCased = kebabCase(pluralize(entityName));

        this.entityNameCapitalized = firstUpper(name);
        this.entityClass = this.entityNameCapitalized;
        this.entityClassHumanized = startCase(this.entityNameCapitalized);
        this.entityClassPlural = pluralize(this.entityClass);
        this.entityClassPluralHumanized = startCase(this.entityClassPlural);
        this.entityInstance = firstLower(entityName);
        this.entityInstancePlural = pluralize(this.entityInstance);
        
        String entityNameSpinalCased = kebabCase(firstLower(name));
        this.entityApiUrl = entityNameSpinalCased;// entityNamePluralizedAndSpinalCased; //make singular url
        this.entityFileName = kebabCase(this.entityNameCapitalized + firstUpper(entityAngularSuffix));

        this.entityFolderName = getEntityFolderName(clientRootFolder, this.entityFileName);
        this.entityModelFileName = this.entityFolderName;
        this.entityParentPathAddition = getEntityParentPathAddition(clientRootFolder);
        this.entityPluralFileName = entityNamePluralizedAndSpinalCased + entityAngularSuffix;
        this.entityServiceFileName = this.entityFileName;
        this.entityTranslationKey = isNotEmpty(clientRootFolder) ? camelCase(clientRootFolder + "-" + entityInstance) : entityInstance;

        this.differentTypes.add(this.entityClass);
        this.i18nKeyPrefix = appName + "." + entityTranslationKey;
    }

    public static String getEntityFolderName(String clientRootFolder, String entityFileName) {
        if (StringUtils.isNotEmpty(clientRootFolder)) {
            return clientRootFolder + "/" + entityFileName;
        }
        return entityFileName;
    }

    public static String getEntityParentPathAddition(String clientRootFolder) {
        if (StringUtils.isNotEmpty(clientRootFolder)) {
            return "../";
        }
        return "";
    }

    public void addRelationship(BaseRelationship relationship) {
        getRelationships().add(relationship);
        // Load in-memory data for root
        if (MANY_TO_MANY.equals(relationship.getRelationshipType()) && relationship.isOwnerSide()) {
            setFieldsContainOwnerManyToMany(true);
        } else if (ONE_TO_ONE.equals(relationship.getRelationshipType()) && !relationship.isOwnerSide()) {
            setFieldsContainNoOwnerOneToOne(true);
        } else if (ONE_TO_ONE.equals(relationship.getRelationshipType()) && relationship.isOwnerSide()) {
            setFieldsContainOwnerOneToOne(true);
        } else if (ONE_TO_MANY.equals(relationship.getRelationshipType())) {
            setFieldsContainOneToMany(true);
        } else if (MANY_TO_ONE.equals(relationship.getRelationshipType())) {
            setFieldsContainManyToOne(true);
        }

        if (relationship.getRelationshipValidateRules() != null && relationship.getRelationshipValidateRules().contains("required")) {
            relationship.setRelationshipValidate(true);
            relationship.setRelationshipRequired(true);
            setValidation(true);
        }
    }

    public void removeRelationship(BaseRelationship relationship) {
        getRelationships().remove(relationship);
    }

    public void addField(BaseField field) {
//        if (!StringUtils.isEmpty(field.getJavadoc())) {
//            haveFieldWithJavadoc = true;
//        }
        fields.add(field);
    }

    public void removeField(BaseField field) {
        fields.remove(field);
    }

    /**
     * @return the fields
     */
    public List<BaseField> getFields() {
        return fields;
    }

    /**
     * @return the relationships
     */
    public List<BaseRelationship> getRelationships() {
        return relationships;
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
     * @return the entityNameCapitalized
     */
    public String getEntityNameCapitalized() {
        return entityNameCapitalized;
    }

    /**
     * @param entityNameCapitalized the entityNameCapitalized to set
     */
    public void setEntityNameCapitalized(String entityNameCapitalized) {
        this.entityNameCapitalized = entityNameCapitalized;
    }

    /**
     * @return the entityClass
     */
    public String getEntityClass() {
        return entityClass;
    }

    /**
     * @param entityClass the entityClass to set
     */
    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * @return the entityClassHumanized
     */
    public String getEntityClassHumanized() {
        return entityClassHumanized;
    }

    /**
     * @param entityClassHumanized the entityClassHumanized to set
     */
    public void setEntityClassHumanized(String entityClassHumanized) {
        this.entityClassHumanized = entityClassHumanized;
    }

    /**
     * @return the entityClassPlural
     */
    public String getEntityClassPlural() {
        return entityClassPlural;
    }

    /**
     * @param entityClassPlural the entityClassPlural to set
     */
    public void setEntityClassPlural(String entityClassPlural) {
        this.entityClassPlural = entityClassPlural;
    }

    /**
     * @return the entityClassPluralHumanized
     */
    public String getEntityClassPluralHumanized() {
        return entityClassPluralHumanized;
    }

    /**
     * @param entityClassPluralHumanized the entityClassPluralHumanized to set
     */
    public void setEntityClassPluralHumanized(String entityClassPluralHumanized) {
        this.entityClassPluralHumanized = entityClassPluralHumanized;
    }

    public String getEntityModelFileName() {
        return entityModelFileName;
    }

    public String getEntityParentPathAddition() {
        return entityParentPathAddition;
    }

    /**
     * @return the entityInstance
     */
    public String getEntityInstance() {
        return entityInstance;
    }

    /**
     * @param entityInstance the entityInstance to set
     */
    public void setEntityInstance(String entityInstance) {
        this.entityInstance = entityInstance;
    }

    /**
     * @return the entityInstancePlural
     */
    public String getEntityInstancePlural() {
        return entityInstancePlural;
    }

    /**
     * @param entityInstancePlural the entityInstancePlural to set
     */
    public void setEntityInstancePlural(String entityInstancePlural) {
        this.entityInstancePlural = entityInstancePlural;
    }

    /**
     * @return the entityApiUrl
     */
    public String getEntityApiUrl() {
        return entityApiUrl;
    }

    /**
     * @param entityApiUrl the entityApiUrl to set
     */
    public void setEntityApiUrl(String entityApiUrl) {
        this.entityApiUrl = entityApiUrl;
    }

    /**
     * @return the entityFolderName
     */
    public String getEntityFolderName() {
        return entityFolderName;
    }

    /**
     * @param entityFolderName the entityFolderName to set
     */
    public void setEntityFolderName(String entityFolderName) {
        this.entityFolderName = entityFolderName;
    }

    /**
     * @return the entityFileName
     */
    public String getEntityFileName() {
        return entityFileName;
    }

    /**
     * @param entityFileName the entityFileName to set
     */
    public void setEntityFileName(String entityFileName) {
        this.entityFileName = entityFileName;
    }

    /**
     * @return the entityPluralFileName
     */
    public String getEntityPluralFileName() {
        return entityPluralFileName;
    }

    /**
     * @param entityPluralFileName the entityPluralFileName to set
     */
    public void setEntityPluralFileName(String entityPluralFileName) {
        this.entityPluralFileName = entityPluralFileName;
    }

    /**
     * @return the entityServiceFileName
     */
    public String getEntityServiceFileName() {
        return entityServiceFileName;
    }

    /**
     * @param entityServiceFileName the entityServiceFileName to set
     */
    public void setEntityServiceFileName(String entityServiceFileName) {
        this.entityServiceFileName = entityServiceFileName;
    }

    /**
     * @return the entityStateName
     */
    public String getEntityStateName() {
        return entityStateName;
    }

    /**
     * @param entityStateName the entityStateName to set
     */
    public void setEntityStateName(String entityStateName) {
        this.entityStateName = entityStateName;
    }

    /**
     * @return the entityUrl
     */
    public String getEntityUrl() {
        return entityUrl;
    }

    /**
     * @param entityUrl the entityUrl to set
     */
    public void setEntityUrl(String entityUrl) {
        this.entityUrl = entityUrl;
    }

    /**
     * @return the entityTranslationKey
     */
    public String getEntityTranslationKey() {
        return entityTranslationKey;
    }

    /**
     * @param entityTranslationKey the entityTranslationKey to set
     */
    public void setEntityTranslationKey(String entityTranslationKey) {
        this.entityTranslationKey = entityTranslationKey;
    }

    /**
     * @return the entityTranslationKeyMenu
     */
    public String getEntityTranslationKeyMenu() {
        return entityTranslationKeyMenu;
    }

    /**
     * @param entityTranslationKeyMenu the entityTranslationKeyMenu to set
     */
    public void setEntityTranslationKeyMenu(String entityTranslationKeyMenu) {
        this.entityTranslationKeyMenu = entityTranslationKeyMenu;
    }
    
    public boolean isFieldsContainDate() {
        return fieldsContainDate;
    }

    public void setFieldsContainDate(boolean fieldsContainDate) {
        this.fieldsContainDate = fieldsContainDate;
    }
    
    /**
     * @return the fieldsContainInstant
     */
    public boolean isFieldsContainInstant() {
        return fieldsContainInstant;
    }

    /**
     * @param fieldsContainInstant the fieldsContainInstant to set
     */
    public void setFieldsContainInstant(boolean fieldsContainInstant) {
        this.fieldsContainInstant = fieldsContainInstant;
    }

    /**
     * @return the fieldsContainZonedDateTime
     */
    public boolean isFieldsContainZonedDateTime() {
        return fieldsContainZonedDateTime;
    }

    /**
     * @param fieldsContainZonedDateTime the fieldsContainZonedDateTime to set
     */
    public void setFieldsContainZonedDateTime(boolean fieldsContainZonedDateTime) {
        this.fieldsContainZonedDateTime = fieldsContainZonedDateTime;
    }

    /**
     * @return the fieldsContainLocalDate
     */
    public boolean isFieldsContainLocalDate() {
        return fieldsContainLocalDate;
    }

    /**
     * @param fieldsContainLocalDate the fieldsContainLocalDate to set
     */
    public void setFieldsContainLocalDate(boolean fieldsContainLocalDate) {
        this.fieldsContainLocalDate = fieldsContainLocalDate;
    }

    /**
     * @return the fieldsContainBigDecimal
     */
    public boolean isFieldsContainBigDecimal() {
        return fieldsContainBigDecimal;
    }

    /**
     * @param fieldsContainBigDecimal the fieldsContainBigDecimal to set
     */
    public void setFieldsContainBigDecimal(boolean fieldsContainBigDecimal) {
        this.fieldsContainBigDecimal = fieldsContainBigDecimal;
    }

    /**
     * @return the fieldsContainBlob
     */
    public boolean isFieldsContainBlob() {
        return fieldsContainBlob;
    }

    /**
     * @param fieldsContainBlob the fieldsContainBlob to set
     */
    public void setFieldsContainBlob(boolean fieldsContainBlob) {
        this.fieldsContainBlob = fieldsContainBlob;
    }

    /**
     * @return the fieldsContainImageBlob
     */
    public boolean isFieldsContainImageBlob() {
        return fieldsContainImageBlob;
    }

    /**
     * @param fieldsContainImageBlob the fieldsContainImageBlob to set
     */
    public void setFieldsContainImageBlob(boolean fieldsContainImageBlob) {
        this.fieldsContainImageBlob = fieldsContainImageBlob;
    }

    public boolean isFieldsContainBlobOrImage() {
        return fieldsContainBlobOrImage;
    }

    public void setFieldsContainBlobOrImage(boolean fieldsContainBlobOrImage) {
        this.fieldsContainBlobOrImage = fieldsContainBlobOrImage;
    }

    /**
     * @return the validation
     */
    public boolean isValidation() {
        return validation;
    }

    /**
     * @param validation the validation to set
     */
    public void setValidation(boolean validation) {
        this.validation = validation;
    }

    /**
     * @return the fieldsContainOwnerManyToMany
     */
    public boolean isFieldsContainOwnerManyToMany() {
        return fieldsContainOwnerManyToMany;
    }

    /**
     * @param fieldsContainOwnerManyToMany the fieldsContainOwnerManyToMany to
     * set
     */
    public void setFieldsContainOwnerManyToMany(boolean fieldsContainOwnerManyToMany) {
        this.fieldsContainOwnerManyToMany = fieldsContainOwnerManyToMany;
    }

    /**
     * @return the fieldsContainNoOwnerOneToOne
     */
    public boolean isFieldsContainNoOwnerOneToOne() {
        return fieldsContainNoOwnerOneToOne;
    }

    /**
     * @param fieldsContainNoOwnerOneToOne the fieldsContainNoOwnerOneToOne to
     * set
     */
    public void setFieldsContainNoOwnerOneToOne(boolean fieldsContainNoOwnerOneToOne) {
        this.fieldsContainNoOwnerOneToOne = fieldsContainNoOwnerOneToOne;
    }

    /**
     * @return the fieldsContainOwnerOneToOne
     */
    public boolean isFieldsContainOwnerOneToOne() {
        return fieldsContainOwnerOneToOne;
    }

    /**
     * @param fieldsContainOwnerOneToOne the fieldsContainOwnerOneToOne to set
     */
    public void setFieldsContainOwnerOneToOne(boolean fieldsContainOwnerOneToOne) {
        this.fieldsContainOwnerOneToOne = fieldsContainOwnerOneToOne;
    }

    /**
     * @return the fieldsContainOneToMany
     */
    public boolean isFieldsContainOneToMany() {
        return fieldsContainOneToMany;
    }

    /**
     * @param fieldsContainOneToMany the fieldsContainOneToMany to set
     */
    public void setFieldsContainOneToMany(boolean fieldsContainOneToMany) {
        this.fieldsContainOneToMany = fieldsContainOneToMany;
    }

    /**
     * @return the fieldsContainManyToOne
     */
    public boolean isFieldsContainManyToOne() {
        return fieldsContainManyToOne;
    }

    /**
     * @param fieldsContainManyToOne the fieldsContainManyToOne to set
     */
    public void setFieldsContainManyToOne(boolean fieldsContainManyToOne) {
        this.fieldsContainManyToOne = fieldsContainManyToOne;
    }

    /**
     * @return the differentTypes
     */
    public List<String> getDifferentTypes() {
        return differentTypes;
    }

    /**
     * @return the pkType
     */
    public String getPkType() {
        return pkType;
    }

    /**
     * @param pkType the pkType to set
     */
    public void setPkType(String pkType) {
        this.pkType = pkType;
    }

    /**
     * @return the entityName
     */
    public abstract String getEntityName();

    /**
     * @return the upgrade
     */
    public boolean isUpgrade() {
        return upgrade;
    }

    /**
     * @param upgrade the upgrade to set
     */
    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    /**
     * @return the haveFieldWithJavadoc
     */
    public boolean isHaveFieldWithJavadoc() {
        return haveFieldWithJavadoc;
    }

    public String getI18nKeyPrefix() {
        return i18nKeyPrefix;
    }
}
