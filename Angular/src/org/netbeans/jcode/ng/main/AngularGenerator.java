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
package org.netbeans.jcode.ng.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import org.apache.commons.lang.StringUtils;
import org.netbeans.api.project.Project;
import static org.netbeans.jcode.core.util.AttributeType.BIGDECIMAL;
import static org.netbeans.jcode.core.util.AttributeType.INSTANT;
import static org.netbeans.jcode.core.util.AttributeType.LOCAL_DATE;
import static org.netbeans.jcode.core.util.AttributeType.ZONED_DATE_TIME;
import static org.netbeans.jcode.core.util.FileUtil.getSimpleFileName;
import org.netbeans.jcode.core.util.JavaSourceHelper;
import static org.netbeans.jcode.core.util.JavaSourceHelper.getSimpleClassName;
import org.netbeans.jcode.core.util.JavaUtil;
import static org.netbeans.jcode.core.util.ProjectHelper.getProjectWebRoot;
import static org.netbeans.jcode.core.util.StringHelper.pluralize;
import org.netbeans.jcode.i18n.I18NConfigData;
import org.netbeans.jcode.i18n.Language;
import static org.netbeans.jcode.i18n.LanguageUtil.isI18nRTLSupportNecessary;
import org.netbeans.jcode.layer.ConfigData;
import org.netbeans.jcode.layer.Generator;
import static org.netbeans.jcode.parser.ejs.EJSUtil.*;
import org.netbeans.jcode.ng.main.domain.ApplicationSourceFilter;
import org.netbeans.jcode.ng.main.domain.EntityConfig;
import org.netbeans.jcode.ng.main.domain.NGApplicationConfig;
import org.netbeans.jcode.ng.main.domain.NGEntity;
import org.netbeans.jcode.ng.main.domain.NGField;
import org.netbeans.jcode.ng.main.domain.NGRelationship;
import org.netbeans.jcode.parser.ejs.EJSParser;
import org.netbeans.jcode.rest.controller.RESTData;
import org.netbeans.jcode.stack.config.data.ApplicationConfigData;
import org.netbeans.jcode.task.progress.ProgressHandler;
import org.netbeans.jpa.modeler.spec.*;
import org.netbeans.jpa.modeler.spec.extend.Attribute;
import org.netbeans.jpa.modeler.spec.extend.BaseAttribute;
import static org.netbeans.jpa.modeler.spec.extend.BlobContentType.IMAGE;
import org.netbeans.jpa.modeler.spec.extend.EnumTypeHandler;
import org.netbeans.jpa.modeler.spec.extend.RelationAttribute;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

/**
 *
 * @author Gaurav Gupta
 */
public abstract class AngularGenerator implements Generator {

    @ConfigData
    protected EntityMappings entityMapping;

    @ConfigData
    protected AngularData ngData;

    @ConfigData
    protected RESTData restData;
    
    @ConfigData
    protected I18NConfigData i18nData;

    @ConfigData
    protected Project project;

    @ConfigData
    protected ApplicationConfigData appConfigData;
    
    @ConfigData
    protected ProgressHandler handler;

    protected List<String> entityScriptFiles;
    protected List<String> scriptFiles;
    public abstract String getTemplatePath();

    @Override
    public void execute() throws IOException {
        entityScriptFiles = new ArrayList<>();
        scriptFiles = new ArrayList<>();
        generateClientSideComponent();
    }

    protected abstract ApplicationSourceFilter getApplicationSourceFilter(NGApplicationConfig applicationConfig);

    protected abstract void generateClientSideComponent();

    protected void generateNgEntityi18nResource(NGApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter, NGEntity entity) throws IOException {
        FileObject webRoot = getProjectWebRoot(project);

        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        parser.addContext(entity);
        Set<String> languages = applicationConfig.getLanguages();
        
        Function<String, String> pathResolver = (templatePath) -> {
            String simpleFileName = getSimpleFileName(templatePath);
            String[] pathSplitter = simpleFileName.split("_");
            String type = pathSplitter[1];
            if (!"entity".equals(type)){
                 return null;
            }
            String lang = pathSplitter[2];
            if (!languages.contains(lang)) {
                return null;
            }
            templatePath = String.format("i18n/%s/%s.json", lang, entity.getEntityTranslationKey());
            return templatePath;
        };
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "entity-resource-i18n.zip", webRoot, pathResolver, handler);
    }
    
    protected void generateNgEnumi18nResource(NGApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter) throws IOException {
        FileObject webRoot = getProjectWebRoot(project);
        Set<String> languages = applicationConfig.getLanguages();
        for (Entry<String, List<String>> entry : applicationConfig.getEnumTypes().entrySet()) {
            String enumTypeFQ = entry.getKey();
            String enumType = enumTypeFQ.substring(enumTypeFQ.lastIndexOf(".")+1); // strip the package name
            Map param = new HashMap<>();
            param.put("enumName", enumType);
            param.put("enums", entry.getValue());
            EJSParser parser = new EJSParser();
            parser.addContext(param);
            parser.addContext(applicationConfig);
            for (String lang : languages) {
                String templatePath = String.format("i18n/%s/%s.json", lang, enumType.toLowerCase());
                copyDynamicFile(parser.getParserManager(), getTemplatePath() + "enum-resource-i18n/i18n/_enum.json", webRoot, templatePath, handler);
            }
        }
    }

    protected void generateNgApplicationi18nResource(NGApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter) throws IOException {
        FileObject webRoot = getProjectWebRoot(project);
        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        Set<String> languages = applicationConfig.getLanguages();
        
        Function<String, String> pathResolver = (templatePath) -> {
            String[] paths = templatePath.split("/");
            String lang = paths[1]; //"i18n/en/password.json" 
            String file = paths[2];
            if (!languages.contains(lang)) { //if lang selected by dev 
                return null;
            }
            if (!fileFilter.isEnable(file)) {
                return null;
            }
            if (templatePath.contains("/_")) {
                templatePath = templatePath.replaceAll("/_", "/");
            }
            //path modification not required
            return templatePath;
        };
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "web-resources-i18n.zip", webRoot, pathResolver, handler);
    }

    protected EntityConfig getEntityConfig(Entity entity) {
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setPagination(entity.getPaginationType().getKeyword());
        return entityConfig;
    }

    protected abstract String getClientFramework();

    protected NGApplicationConfig getAppConfig() {
        NGApplicationConfig applicationConfig = getNGApplicationConfig(ngData.getModule(), "maven");
        applicationConfig.setEnableTranslation(true);
        applicationConfig.setJhiPrefix(ngData.getPrefix());
        applicationConfig.setUseSass(ngData.isSass());
        
        if (i18nData.isEnabled()) {
            applicationConfig.setNativeLanguage(i18nData.getNativeLanguage().getValue());
            applicationConfig.setLanguages(i18nData.getOtherLanguagesKeyword());
            applicationConfig.setLanguageInstances(i18nData.getLanguageInstances());
        } 
        applicationConfig.setEnableTranslation(i18nData.isEnabled());
        applicationConfig.setEnableI18nRTL(isI18nRTLSupportNecessary(applicationConfig.getLanguages()));
        
        applicationConfig.setApplicationPath(restData.getRestConfigData().getApplicationPath());
        applicationConfig.setEnableMetrics(restData.isMetrics());
        applicationConfig.setEnableLogs(restData.isLogger());
        applicationConfig.setRestPackage(restData.getPackage());
        applicationConfig.setEnableDocs(restData.isDocsEnable());
        applicationConfig.setClientFramework(getClientFramework());
        applicationConfig.setSkipClient(false);
        applicationConfig.setSkipServer(false);
        
        applicationConfig.setClientPackageManager(ngData.getClientPackager().toString());
        applicationConfig.setProtractorTests(ngData.isProtractorTest());
        return applicationConfig;
    }
    
    public abstract NGApplicationConfig getNGApplicationConfig(String baseName, String buildTool);

    public abstract NGEntity getNGEntity(String name, String entityAngularJSSuffix);

    public abstract NGRelationship getNGRelationship(String angularAppName, String entityAngularJSSuffix, Entity entity, RelationAttribute relation);

    public abstract NGField getNGField(BaseAttribute attribute);
 

    protected NGEntity getEntity(NGApplicationConfig applicationConfig, Entity entity) {
        String angularAppName = applicationConfig.getAngularAppName();
        Attribute idAttribute = entity.getAttributes().getIdField();
        if (idAttribute instanceof EmbeddedId || idAttribute instanceof DefaultAttribute) {
            handler.error(NbBundle.getMessage(AngularGenerator.class, "TITLE_Composite_Key_Not_Supported"),
                    NbBundle.getMessage(AngularGenerator.class, "MSG_Composite_Key_Not_Supported", entity.getClazz()));
            return null;
        } else if (!"id".equals(idAttribute.getName())) {
            handler.error(NbBundle.getMessage(AngularGenerator.class, "TITLE_PK_Field_Named_Id_Missing"),
                    NbBundle.getMessage(AngularGenerator.class, "MSG_PK_Field_Named_Id_Missing", entity.getClazz(), idAttribute.getName()));
            return null;
        }
        String entityAngularJSSuffix = "";
        NGEntity ngEntity = getNGEntity(entity.getClazz(), entityAngularJSSuffix);
        if(StringUtils.isNotBlank(entity.getLabel())){
            ngEntity.setEntityClassHumanized(entity.getLabel());
            ngEntity.setEntityClassPluralHumanized(pluralize(entity.getLabel()));
        }
        
        ngEntity.setPkType(entity.getAttributes().getIdField().getDataTypeLabel());
        List<Attribute> attributes = entity.getAttributes().getAllAttribute();
//      Uncomment for inheritance support
//        if(entitySpec.getSubclassList().size() > 1){
//            return null;
//        }
//        attributes.addAll(entitySpec.getSuperclassAttributes());
        for (Attribute attribute : attributes) {
            if (attribute instanceof Id && ((Id) attribute).isGeneratedValue()) {
                continue;
            }
            if (!attribute.getIncludeInUI()) {//system attribute
                continue;
            }
            if (attribute.isOptionalReturnType()) {//todo dto
                continue;
            }

            if (attribute instanceof RelationAttribute) {
                RelationAttribute relationAttribute = (RelationAttribute) attribute;
                NGRelationship ngRelationship = getNGRelationship(angularAppName, entityAngularJSSuffix, entity, relationAttribute);
                Entity mappedEntity = relationAttribute.getConnectedEntity();
                if (mappedEntity.getLabelAttribute() == null || mappedEntity.getLabelAttribute().getName().equals("id")) {
                    handler.warning(NbBundle.getMessage(AngularGenerator.class, "TITLE_Entity_Label_Missing"),
                            NbBundle.getMessage(AngularGenerator.class, "MSG_Entity_Label_Missing", mappedEntity.getClazz()));
                } else {
                    ngRelationship.setOtherEntityField(mappedEntity.getLabelAttribute().getName());
                }
//                if (entity == mappedEntity) {
//                    handler.warning(NbBundle.getMessage(AngularGenerator.class, "TITLE_Self_Relation_Not_Supported"),
//                            NbBundle.getMessage(AngularGenerator.class, "MSG_Self_Relation_Not_Supported", attribute.getName(), ngEntity.getName()));
//                    continue;
//                }
                if (StringUtils.isNotBlank(attribute.getLabel())) {
                    ngRelationship.setRelationshipNameHumanized(attribute.getLabel());
                }
                ngEntity.addRelationship(ngRelationship);
            } else if (attribute instanceof BaseAttribute) {
                if (attribute instanceof Embedded) {
                    handler.warning(NbBundle.getMessage(AngularGenerator.class, "TITLE_Embedded_Type_Not_Supported"),
                            NbBundle.getMessage(AngularGenerator.class, "MSG_Embedded_Type_Not_Supported", attribute.getName(), ngEntity.getName()));
                    continue;
                }
                if (attribute instanceof ElementCollection) {
                    handler.warning(NbBundle.getMessage(AngularGenerator.class, "TITLE_ElementCollection_Type_Not_Supported"),
                            NbBundle.getMessage(AngularGenerator.class, "MSG_ElementCollection_Type_Not_Supported", attribute.getName(), ngEntity.getName()));
                    continue;
                }
                if (attribute instanceof Version) {
                    continue;
                }
                
                NGField ngField = getNGField((BaseAttribute) attribute);
                Class<?> primitiveType = JavaUtil.getPrimitiveType(attribute.getDataTypeLabel());
                if (primitiveType != null) {
                    ngField.setFieldType(primitiveType.getSimpleName());//todo short, byte, char not supported in ui template
                } else {
                    ngField.setFieldType(getSimpleClassName(attribute.getDataTypeLabel()));
                }
                if (StringUtils.isNotBlank(attribute.getLabel())) {
                    ngField.setFieldNameHumanized(attribute.getLabel());
                }
                
                if (null != ngField.getFieldType()) {
                    if (INSTANT.contains(ngField.getFieldType())) {
                        ngEntity.setFieldsContainInstant(true);
                    } else if (ZONED_DATE_TIME.contains(ngField.getFieldType())) {
                        ngEntity.setFieldsContainZonedDateTime(true);
                    } else if (LOCAL_DATE.contains(ngField.getFieldType())) {
                        ngEntity.setFieldsContainLocalDate(true);
                    } else if (BIGDECIMAL.contains(ngField.getFieldType())) {
                        ngEntity.setFieldsContainBigDecimal(true);
                    } else if (((BaseAttribute) attribute).isBlobAttributeType()) {
                        ngEntity.setFieldsContainBlob(true);
                        if (attribute.getBlobContentType() != null) {
                            ngField.setFieldTypeBlobContent(attribute.getBlobContentType().getValue());
                            if (attribute.getBlobContentType() == IMAGE) {
                                ngEntity.setFieldsContainImageBlob(true);
                            }
                        }
                    } else if (attribute instanceof EnumTypeHandler && ((EnumTypeHandler) attribute).getEnumerated() != null) {
                        ngField.setFieldIsEnum(true);
                        String enumType = ((BaseAttribute) attribute).getAttributeType();
                        try {
                          List elements = JavaSourceHelper.getEnumVariableElements(project, enumType);
                          String enumElement = (String) elements.stream()
                                    .map(var -> var.toString())
                                    .collect(Collectors.joining(","));
                            ngField.setFieldValues(enumElement);
                            applicationConfig.addEnumType(enumType, (List<String>) elements.stream()
                                    .map(var -> var.toString())
                                    .collect(Collectors.toList()));
                        } catch (IOException ex) {
                            handler.warning(NbBundle.getMessage(AngularGenerator.class, "TITLE_Enum_Type_Not_Found"),
                            NbBundle.getMessage(AngularGenerator.class, "MSG_Enum_Type_Not_Found", attribute.getName(), ngEntity.getName(), enumType));
                            continue;
                        }
                    }
                }
                ngEntity.addField(ngField);
            }
        }
        return ngEntity;
    }
    
}
