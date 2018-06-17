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
package io.github.jeddict.client.web.main;

import static io.github.jeddict.jcode.util.AttributeType.BIGDECIMAL;
import static io.github.jeddict.jcode.util.AttributeType.INSTANT;
import static io.github.jeddict.jcode.util.AttributeType.LOCAL_DATE;
import static io.github.jeddict.jcode.util.AttributeType.ZONED_DATE_TIME;
import static io.github.jeddict.jcode.util.FileUtil.getSimpleFileName;
import io.github.jeddict.jcode.util.JavaSourceHelper;
import static io.github.jeddict.jcode.util.JavaSourceHelper.getSimpleClassName;
import io.github.jeddict.jcode.util.JavaUtil;
import static io.github.jeddict.jcode.util.ProjectHelper.getProjectWebRoot;
import io.github.jeddict.jcode.util.SourceGroupSupport;
import static io.github.jeddict.jcode.util.StringHelper.pluralize;
import io.github.jeddict.jcode.Generator;
import io.github.jeddict.jcode.parser.ejs.EJSParser;
import static io.github.jeddict.jcode.parser.ejs.EJSUtil.copyDynamicFile;
import io.github.jeddict.jcode.ApplicationConfigData;
import io.github.jeddict.jcode.task.progress.ProgressHandler;
import io.github.jeddict.jpa.spec.DefaultAttribute;
import io.github.jeddict.jpa.spec.ElementCollection;
import io.github.jeddict.jpa.spec.Embedded;
import io.github.jeddict.jpa.spec.EmbeddedId;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.EntityMappings;
import io.github.jeddict.jpa.spec.Id;
import io.github.jeddict.jpa.spec.Version;
import io.github.jeddict.jpa.spec.extend.Attribute;
import io.github.jeddict.jpa.spec.extend.BaseAttribute;
import static io.github.jeddict.jpa.spec.extend.BlobContentType.IMAGE;
import io.github.jeddict.jpa.spec.extend.EnumTypeHandler;
import io.github.jeddict.jpa.spec.extend.RelationAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.netbeans.api.project.Project;
import io.github.jeddict.client.i18n.I18NConfigData;
import static io.github.jeddict.client.i18n.LanguageUtil.isI18nRTLSupportNecessary;
import io.github.jeddict.client.web.main.domain.ApplicationSourceFilter;
import io.github.jeddict.client.web.main.domain.EntityConfig;
import io.github.jeddict.client.web.main.domain.BaseApplicationConfig;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.GATEWAY_APPLICATION_TYPE;
import static io.github.jeddict.client.web.main.domain.BaseApplicationConfig.MONOLITH_APPLICATION_TYPE;
import io.github.jeddict.client.web.main.domain.BaseEntity;
import io.github.jeddict.client.web.main.domain.BaseField;
import io.github.jeddict.client.web.main.domain.BaseRelationship;
import io.github.jeddict.jcode.annotation.ConfigData;
import static io.github.jeddict.jcode.parser.ejs.EJSUtil.copyDynamicResource;
import static io.github.jeddict.jcode.util.AttributeType.BOOLEAN;
import static io.github.jeddict.jpa.spec.extend.BlobContentType.TEXT;
import io.github.jeddict.rest.controller.RESTData;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

/**
 *
 * @author Gaurav Gupta
 */
public abstract class BaseWebGenerator implements Generator {

    @ConfigData
    protected EntityMappings entityMapping;

    @ConfigData
    protected WebData webData;

    @ConfigData
    protected RESTData restData;
    
    @ConfigData
    protected I18NConfigData i18nData;

    @ConfigData
    protected ApplicationConfigData appConfigData;
    
    @ConfigData
    protected ProgressHandler handler;
    
    private BaseApplicationConfig applicationConfig;
    
    protected Project project;
    protected Project javaProject;
    protected FileObject projectRoot;
    protected FileObject testRoot;
    protected FileObject webRoot;

    protected List<String> entityScriptFiles;
    protected List<String> scriptFiles;
    
    public static final String I18N_TEMPLATE = "io/github/jeddict/client/i18n/template/";
    
    public abstract String getTemplatePath();

    @Override
    public void execute() throws IOException {
        project = appConfigData.isMicroservice() || appConfigData.isGateway()? 
                    appConfigData.getGatewayProject(): appConfigData.getTargetProject();
        javaProject =  appConfigData.getTargetProject();
        testRoot = SourceGroupSupport.getTestSourceGroup(project).getRootFolder().getParent();//test/java => ../java
        webRoot = getProjectWebRoot(project);
        projectRoot = project.getProjectDirectory();
        entityScriptFiles = new ArrayList<>();
        scriptFiles = new ArrayList<>();
        generateClientSideComponent();
    }

    protected abstract ApplicationSourceFilter getApplicationSourceFilter(BaseApplicationConfig applicationConfig);

    protected abstract void generateClientSideComponent();

    protected void generateEntityi18nResource(BaseApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter, BaseEntity entity) throws IOException {
        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        parser.addContext(entity);
        Set<String> languages = applicationConfig.getLanguages();
        
        Function<String, String> pathResolver = (templatePath) -> {
            String simpleFileName = getSimpleFileName(templatePath);
            String[] pathSplitter = simpleFileName.split("_");
            String type = pathSplitter[0];
            if (!"entity".equals(type)){
                 return null;
            }
            String lang = pathSplitter[1];
            if (!languages.contains(lang)) {
                return null;
            }
            templatePath = String.format("i18n/%s/%s.json", lang, entity.getEntityTranslationKey());
            return templatePath;
        };
        copyDynamicResource(parser.getParserManager(), I18N_TEMPLATE + "entity-resource-i18n.zip", webRoot, pathResolver, handler);
    }
    
    protected void generateEnumi18nResource(BaseApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter) throws IOException {
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
                copyDynamicFile(parser.getParserManager(), I18N_TEMPLATE + "enum-resource-i18n/i18n/enum.json.ejs", webRoot, templatePath, handler);               
            }
        }
    }

    protected void generateApplicationi18nResource(BaseApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter) throws IOException {
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
        copyDynamicResource(parser.getParserManager(), I18N_TEMPLATE + "web-resources-i18n.zip", webRoot, pathResolver, handler);
    }

    protected EntityConfig getEntityConfig(Entity entity) {
        EntityConfig entityConfig = new EntityConfig();
        entityConfig.setPagination(entity.getPaginationType().getKeyword());
        return entityConfig;
    }

    protected abstract String getClientFramework();

    protected BaseApplicationConfig getAppConfig() {
        if(applicationConfig != null){
            return applicationConfig;
        }
        applicationConfig = getApplicationConfig(webData.getModule(), "maven");
        applicationConfig.setJhiPrefix(webData.getPrefix());
        applicationConfig.setUseSass(webData.isSass());
        
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
        applicationConfig.setEnableDocs(false);//restData.isOpenAPI());
        applicationConfig.setEnableHealth(true);
        applicationConfig.setEnableConfiguration(false);
        applicationConfig.setEnableAudits(false);
        applicationConfig.setEnableProfile(false);
        applicationConfig.setServiceDiscoveryType(appConfigData.getRegistryType().name().toLowerCase());
        
        applicationConfig.setClientFramework(getClientFramework());
        applicationConfig.setClientPackageManager(webData.getClientPackager().toString());
        applicationConfig.setProtractorTests(webData.isProtractorTest());
        
        applicationConfig.setApplicationType(
                appConfigData.isMonolith() ? MONOLITH_APPLICATION_TYPE : GATEWAY_APPLICATION_TYPE
        );
        if (appConfigData.isMicroservice()) {
            //microservice context path + application path
            applicationConfig.setMicroserviceName(appConfigData.getTargetContextPath()+ '/' + restData.getRestConfigData().getApplicationPath());
        }
        return applicationConfig;
    }
    
    public abstract BaseApplicationConfig getApplicationConfig(String baseName, String buildTool);

    public abstract BaseEntity getEntity(String entityClass, String entitySuffix, String appName, String clientRootFolder);

    public abstract BaseRelationship getRelationship(String appName, String entitySuffix, Entity entity, RelationAttribute relation, String clientRootFolder);

    public abstract BaseField getField(BaseAttribute attribute);
 

    protected BaseEntity getEntity(BaseApplicationConfig applicationConfig, Entity entity) {
        String clientRootFolder = appConfigData.isMicroservice() ?appConfigData.getTargetContextPath() : null;
        String appName = applicationConfig.getAppName();
        Attribute idAttribute = entity.getAttributes().getIdField();
        if (idAttribute instanceof EmbeddedId || idAttribute instanceof DefaultAttribute) {
            handler.error(NbBundle.getMessage(BaseWebGenerator.class, "TITLE_Composite_Key_Not_Supported"),
                    NbBundle.getMessage(BaseWebGenerator.class, "MSG_Composite_Key_Not_Supported", entity.getClazz()));
            return null;
        } else if (!"id".equals(idAttribute.getName())) {
            handler.error(NbBundle.getMessage(BaseWebGenerator.class, "TITLE_PK_Field_Named_Id_Missing"),
                    NbBundle.getMessage(BaseWebGenerator.class, "MSG_PK_Field_Named_Id_Missing", entity.getClazz(), idAttribute.getName()));
            return null;
        }
        String entitySuffix = "";
        BaseEntity webEntity = getEntity(entity.getClazz(), entitySuffix, appName, clientRootFolder);
        if(StringUtils.isNotBlank(entity.getLabel())){
            webEntity.setEntityClassHumanized(entity.getLabel());
            webEntity.setEntityClassPluralHumanized(pluralize(entity.getLabel()));
        }
        
        webEntity.setPkType(entity.getAttributes().getIdField().getDataTypeLabel());
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
                BaseRelationship webRelationship = getRelationship(appName, entitySuffix, entity, relationAttribute, clientRootFolder);
                Entity mappedEntity = relationAttribute.getConnectedEntity();
                if (mappedEntity.getLabelAttribute() == null && !mappedEntity.getAttributes().getBasic().isEmpty()){
                    mappedEntity.setLabelAttribute(mappedEntity.getAttributes().getBasic().get(0));
                }
                if (mappedEntity.getLabelAttribute() == null || mappedEntity.getLabelAttribute().getName().equals("id")) {
                    handler.warning(NbBundle.getMessage(BaseWebGenerator.class, "TITLE_Entity_Label_Missing"),
                            NbBundle.getMessage(BaseWebGenerator.class, "MSG_Entity_Label_Missing", mappedEntity.getClazz()));
                } else {
                    Attribute labelAttribute = mappedEntity.getLabelAttribute();
                    webRelationship.setOtherEntityField(StringUtils.isEmpty(labelAttribute.getJsonbProperty())
                            ? labelAttribute.getName() : labelAttribute.getJsonbProperty());
                }
//                if (entity == mappedEntity) {
//                    handler.warning(NbBundle.getMessage(AngularGenerator.class, "TITLE_Self_Relation_Not_Supported"),
//                            NbBundle.getMessage(AngularGenerator.class, "MSG_Self_Relation_Not_Supported", attribute.getName(), ngEntity.getName()));
//                    continue;
//                }
                if (StringUtils.isNotBlank(attribute.getLabel())) {
                    webRelationship.setRelationshipNameHumanized(attribute.getLabel());
                }
                webEntity.addRelationship(webRelationship);
            } else if (attribute instanceof BaseAttribute) {
                if (attribute instanceof Embedded) {
                    handler.warning(NbBundle.getMessage(BaseWebGenerator.class, "TITLE_Embedded_Type_Not_Supported"),
                            NbBundle.getMessage(BaseWebGenerator.class, "MSG_Embedded_Type_Not_Supported", attribute.getName(), webEntity.getName()));
                    continue;
                }
                if (attribute instanceof ElementCollection) {
                    handler.warning(NbBundle.getMessage(BaseWebGenerator.class, "TITLE_ElementCollection_Type_Not_Supported"),
                            NbBundle.getMessage(BaseWebGenerator.class, "MSG_ElementCollection_Type_Not_Supported", attribute.getName(), webEntity.getName()));
                    continue;
                }
                if (attribute instanceof Version) {
                    continue;
                }
                
                BaseField webField = getField((BaseAttribute) attribute);
                Class<?> primitiveType = JavaUtil.getPrimitiveType(attribute.getDataTypeLabel());
                if (primitiveType != null) {
                    webField.setFieldType(primitiveType.getSimpleName());//todo short, byte, char not supported in ui template
                } else {
                    webField.setFieldType(getSimpleClassName(attribute.getDataTypeLabel()));
                }
                if (StringUtils.isNotBlank(attribute.getLabel())) {
                    webField.setFieldNameHumanized(attribute.getLabel());
                }
                
                if (null != webField.getFieldType()) {
                     if (ZONED_DATE_TIME.contains(webField.getFieldType())) {
                        webEntity.setFieldsContainZonedDateTime(true);
                        webEntity.setFieldsContainDate(true);
                    } else if (INSTANT.contains(webField.getFieldType())) {
                        webEntity.setFieldsContainInstant(true);
                        webEntity.setFieldsContainDate(true);
                    } else if (LOCAL_DATE.contains(webField.getFieldType())) {
                        webEntity.setFieldsContainLocalDate(true);
                        webEntity.setFieldsContainDate(true);
                    } else if (BIGDECIMAL.contains(webField.getFieldType())) {
                        webEntity.setFieldsContainBigDecimal(true);
                    } else if (((BaseAttribute) attribute).isBlobAttributeType()) {
                        webEntity.addBlobField(attribute.getName());
                        webEntity.setFieldsContainBlob(true);
                        if (attribute.getBlobContentType() != null) {
                            webField.setFieldTypeBlobContent(attribute.getBlobContentType().getValue());
                            if (attribute.getBlobContentType() == IMAGE) {
                                webEntity.setFieldsContainImageBlob(true);
                            }
                            if (attribute.getBlobContentType() != TEXT) {
                                webEntity.setFieldsContainBlobOrImage(true);
                            }
                        }
                    }  else if (BOOLEAN.equalsIgnoreCase(webField.getFieldType())) {
                        webEntity.setFieldsContainBoolean(true);
                    } else if (attribute instanceof EnumTypeHandler && ((EnumTypeHandler) attribute).getEnumerated() != null) {
                        webField.setFieldIsEnum(true);
                        String enumType = ((BaseAttribute) attribute).getAttributeType();
                        try {
                          List elements = JavaSourceHelper.getEnumVariableElements(javaProject, enumType);
                          String enumElement = (String) elements.stream()
                                    .map(var -> var.toString())
                                    .collect(Collectors.joining(","));
                            webField.setFieldValues(enumElement);
                            applicationConfig.addEnumType(enumType, (List<String>) elements.stream()
                                    .map(var -> var.toString())
                                    .collect(Collectors.toList()));
                        } catch (IOException ex) {
                            handler.warning(NbBundle.getMessage(BaseWebGenerator.class, "TITLE_Enum_Type_Not_Found"),
                            NbBundle.getMessage(BaseWebGenerator.class, "MSG_Enum_Type_Not_Found", attribute.getName(), webEntity.getName(), enumType));
                            continue;
                        }
                    }
                }
                webEntity.addField(webField);
            }
        }
        return webEntity;
    }
    
}
