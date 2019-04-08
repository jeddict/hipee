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

import io.github.jeddict.bv.constraints.Constraint;
import io.github.jeddict.bv.constraints.Max;
import io.github.jeddict.bv.constraints.Min;
import io.github.jeddict.bv.constraints.NotBlank;
import io.github.jeddict.bv.constraints.NotEmpty;
import io.github.jeddict.bv.constraints.NotNull;
import io.github.jeddict.bv.constraints.Pattern;
import io.github.jeddict.bv.constraints.Size;
import static io.github.jeddict.jcode.util.StringHelper.firstUpper;
import static io.github.jeddict.jcode.util.StringHelper.snakeCase;
import static io.github.jeddict.jcode.util.StringHelper.startCase;
import io.github.jeddict.jpa.spec.extend.BaseAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import io.github.jeddict.util.StringUtils;
import static io.github.jeddict.util.StringUtils.EMPTY;

public abstract class BaseField {

    protected String fieldName;
    protected String fieldType;
    protected String javadoc;
    
    protected final String fieldNameCapitalized;
    protected String fieldNameHumanized;
    protected final String fieldNameUnderscored;

    protected boolean fieldIsEnum;
    protected String fieldTypeBlobContent;//any , image, text

    protected String fieldValidateRulesMax;
    protected String fieldValidateRulesMin;
    protected String fieldValidateRulesMaxlength;
    protected String fieldValidateRulesMinlength;
    protected String fieldValidateRulesMaxbytes;
    protected String fieldValidateRulesMinbytes;
    protected String fieldValidateRulesPattern;
    protected List<String> fieldValidateRules;
    protected boolean fieldValidate;

    protected String fieldValues;
    protected final String fieldInJavaBeanMethod;

    public BaseField(BaseAttribute attribute) {
        this.fieldName = StringUtils.isEmpty(attribute.getJsonbProperty())
                ? attribute.getName() : attribute.getJsonbProperty();
//        this.javadoc = attribute.getDescription();
        loadValidation(attribute.getAttributeConstraintsMap());
        
                    fieldNameCapitalized = firstUpper(fieldName);
            fieldNameUnderscored = snakeCase(fieldName);
            fieldNameHumanized = startCase(fieldName);
            if (fieldName.length() > 1) {
                Character firstLetter = fieldName.charAt(0);
                Character secondLetter = fieldName.charAt(1);
                if (firstLetter == Character.toLowerCase(firstLetter) && secondLetter == Character.toUpperCase(secondLetter)) {
                    fieldInJavaBeanMethod = Character.toLowerCase(firstLetter) + fieldName.substring(1);
                } else {
                    fieldInJavaBeanMethod = firstUpper(fieldName);
                }
            } else {
                fieldInJavaBeanMethod = firstUpper(fieldName);
            }
    }

    //['required', 'max', 'min', 'maxlength', 'minlength', 'maxbytes', 'minbytes', 'pattern'];
    private void loadValidation(Map<String, Constraint> constraints) {
        List<String> validationRules = new ArrayList<>();

        NotNull notNull = (NotNull) constraints.get(NotNull.class.getSimpleName());
        if (notNull != null && notNull.getSelected()) {
            validationRules.add("required");
        }
        
        NotEmpty notEmpty = (NotEmpty) constraints.get(NotEmpty.class.getSimpleName());
        if (notEmpty != null && notEmpty.getSelected()) {
            validationRules.add("required");
        }
        
        NotBlank notBlank = (NotBlank) constraints.get(NotBlank.class.getSimpleName());
        if (notBlank != null && notBlank.getSelected()) {
            validationRules.add("required");
        }

        Pattern pattern = (Pattern) constraints.get(Pattern.class.getSimpleName());
        if (pattern != null && pattern.getSelected()) {
            validationRules.add("pattern");
            fieldValidateRulesPattern = pattern.getRegexp();
        }
        
//        Email email = (Email) constraints.get(Email.class.getSimpleName());
//        if (email != null && email.getSelected()) {
//            validationRules.add("pattern");
//            fieldValidateRulesPattern = "/^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$/";
//        }

        Min min = (Min) constraints.get(Min.class.getSimpleName());
        if (min != null && min.getSelected()) {
            validationRules.add("min");
            fieldValidateRulesMin = String.valueOf(min.getValue());
        }

        Max max = (Max) constraints.get(Max.class.getSimpleName());
        if (max != null && max.getSelected()) {
            validationRules.add("max");
            fieldValidateRulesMax = String.valueOf(max.getValue());
        }

        if (constraints.get(Size.class.getSimpleName()) != null) {
            Size size = (Size) constraints.get(Size.class.getSimpleName());
            if (size.getMax() != null) {
                fieldValidateRulesMaxlength = String.valueOf(size.getMax());
                validationRules.add("maxlength");
            }
            if (size.getMin() != null) {
                fieldValidateRulesMinlength = String.valueOf(size.getMin());
                validationRules.add("minlength");
            }
        }
        setFieldValidate(validationRules);
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the fieldNameCapitalized
     */
    public String getFieldNameCapitalized() {
        return fieldNameCapitalized;
    }

    /**
     * @return the fieldNameHumanized
     */
    public String getFieldNameHumanized() {
        return fieldNameHumanized;
    }

    /**
     * @param fieldNameHumanized the fieldNameHumanized to set
     */
    public void setFieldNameHumanized(String fieldNameHumanized) {
        this.fieldNameHumanized = fieldNameHumanized;
    }

    /**
     * @return the fieldNameUnderscored
     */
    public String getFieldNameUnderscored() {
        return fieldNameUnderscored;
    }


    /**
     * @return the fieldTypeBlobContent
     */
    public String getFieldTypeBlobContent() {
        return fieldTypeBlobContent;
    }

    /**
     * @param fieldTypeBlobContent the fieldTypeBlobContent to set
     */
    public void setFieldTypeBlobContent(String fieldTypeBlobContent) {
        this.fieldTypeBlobContent = fieldTypeBlobContent;
    }

    /**
     * @return the javadoc
     */
    public String getJavadoc() {
        return javadoc;
    }

    /**
     * @return the fieldValidateRulesMax
     */
    public String getFieldValidateRulesMax() {
        return fieldValidateRulesMax;
    }

    /**
     * @return the fieldValidateRulesMin
     */
    public String getFieldValidateRulesMin() {
        return fieldValidateRulesMin;
    }

    /**
     * @return the fieldValidateRulesMaxlength
     */
    public String getFieldValidateRulesMaxlength() {
        return fieldValidateRulesMaxlength;
    }

    /**
     * @return the fieldValidateRulesMinlength
     */
    public String getFieldValidateRulesMinlength() {
        return fieldValidateRulesMinlength;
    }

    /**
     * @return the fieldValidateRulesMaxbytes
     */
    public String getFieldValidateRulesMaxbytes() {
        return fieldValidateRulesMaxbytes;
    }

    /**
     * @return the fieldValidateRulesMinbytes
     */
    public String getFieldValidateRulesMinbytes() {
        return fieldValidateRulesMinbytes;
    }

    /**
     * @return the fieldValidateRulesPattern
     */
    public String getFieldValidateRulesPattern() {
        if(fieldValidateRulesPattern == null){
            return EMPTY;
        }
        return fieldValidateRulesPattern;
    }

    /**
     * @param fieldIsEnum the fieldIsEnum to set
     */
    public void setFieldIsEnum(boolean fieldIsEnum) {
        this.fieldIsEnum = fieldIsEnum;
    }

    /**
     * @return the fieldIsEnum
     */
    public boolean isFieldIsEnum() {
        return fieldIsEnum;
    }

    /**
     * @return the fieldIsEnum
     */
    public boolean getFieldIsEnum() {
        return isFieldIsEnum();
    }

    /**
     * @return the fieldValidate
     */
    public boolean isFieldValidate() {
        return fieldValidate;
    }

    /**
     * @return the fieldValidateRules
     */
    public List<String> getFieldValidateRules() {
        if(fieldValidateRules==null){
            return Collections.<String>emptyList();
        }
        return fieldValidateRules;
    }

    public void setFieldValidate(List<String> fieldValidateRules) {
        if (fieldValidateRules != null && fieldValidateRules.size() >= 1) {
            fieldValidate = true;
        } else {
            fieldValidate = false;
        }
        this.fieldValidateRules = fieldValidateRules;
    }

    /**
     * @return the fieldValues
     */
    public String getFieldValues() {
        if (fieldValues == null) {
            return EMPTY;
        }
        return fieldValues;
    }

    /**
     * @param fieldValues the fieldValues to set
     */
    public void setFieldValues(String fieldValues) {
        this.fieldValues = fieldValues;
    }

    /**
     * @return the fieldInJavaBeanMethod
     */
    public String getFieldInJavaBeanMethod() {
        return fieldInJavaBeanMethod;
    }

    /**
     * @return the fieldType
     */
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        setFieldType(fieldType, "sql");
    }

    /**
     * @param fieldType the fieldType to set
     */
    public abstract void setFieldType(String fieldType, String databaseType);

}
