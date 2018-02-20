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
package io.github.jeddict.client.angular1;

import io.github.jeddict.jcode.console.Console;
import static io.github.jeddict.jcode.console.Console.BOLD;
import static io.github.jeddict.jcode.console.Console.FG_DARK_RED;
import static io.github.jeddict.jcode.console.Console.UNDERLINE;
import io.github.jeddict.jcode.util.FileUtil;
import static io.github.jeddict.jcode.util.FileUtil.getFileExt;
import static io.github.jeddict.jcode.util.FileUtil.getSimpleFileNameWithExt;
import io.github.jeddict.jcode.layer.Generator;
import io.github.jeddict.jcode.layer.Technology;
import static io.github.jeddict.jcode.layer.Technology.Type.VIEWER;
import io.github.jeddict.jcode.parser.ejs.EJSParser;
import static io.github.jeddict.jcode.parser.ejs.EJSUtil.copyDynamicFile;
import static io.github.jeddict.jcode.parser.ejs.EJSUtil.copyDynamicResource;
import static io.github.jeddict.jcode.parser.ejs.EJSUtil.getResource;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.extend.BaseAttribute;
import io.github.jeddict.jpa.spec.extend.RelationAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import io.github.jeddict.client.angular1.domain.NG1ApplicationConfig;
import io.github.jeddict.client.angular1.domain.NG1Entity;
import io.github.jeddict.client.angular1.domain.NG1Field;
import io.github.jeddict.client.angular1.domain.NG1Relationship;
import io.github.jeddict.client.web.main.BaseWebGenerator;
import io.github.jeddict.client.web.main.domain.ApplicationSourceFilter;
import io.github.jeddict.client.web.main.domain.EntityConfig;
import io.github.jeddict.client.web.main.domain.BaseApplicationConfig;
import io.github.jeddict.client.web.main.domain.BaseEntity;
import io.github.jeddict.client.web.main.domain.BaseField;
import io.github.jeddict.client.web.main.domain.BaseRelationship;
import io.github.jeddict.rest.controller.RESTGenerator;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Gaurav Gupta
 */
@ServiceProvider(service = Generator.class)
@Technology(type = VIEWER, label = "AngularJS 1 (Obsolete)", panel = Angular1Panel.class, parents = {RESTGenerator.class})
public class Angular1Generator extends BaseWebGenerator {

    private static final String TEMPLATE = "io/github/jeddict/client/angular1/template/";
    private static final String CLIENT_FRAMEWORK = "angular1";
    private final static String MODULE_JS = "app/app.module.js";
    private ApplicationSourceFilter sourceFilter;

    @Override
    public String getTemplatePath() {
        return TEMPLATE;
    }

    @Override
    protected String getClientFramework() {
        return CLIENT_FRAMEWORK;
    }

    @Override
    protected ApplicationSourceFilter getApplicationSourceFilter(BaseApplicationConfig applicationConfig) {
        if (sourceFilter == null) {
            sourceFilter = new NG1SourceFilter(applicationConfig);
        }
        return sourceFilter;
    }

    @Override
    protected void generateClientSideComponent() {
        try {
            BaseApplicationConfig applicationConfig = getAppConfig();
            ApplicationSourceFilter fileFilter = getApplicationSourceFilter(applicationConfig);

            handler.append(Console.wrap(Angular1Generator.class, "MSG_Copying_Entity_Files", FG_DARK_RED, BOLD, UNDERLINE));
            Map<String, String> templateLib = getResource(getTemplatePath() + "entity-include-resources.zip");
            List<BaseEntity> entities = new ArrayList<>();
            for (Entity entity : entityMapping.getGeneratedEntity().collect(toList())) {
                BaseEntity ngEntity = getEntity(applicationConfig, entity);
                if (ngEntity != null) {
                    entities.add(ngEntity);
                    generateNgEntity(applicationConfig, fileFilter, getEntityConfig(entity), ngEntity, templateLib);
                    generateEntityi18nResource(applicationConfig, fileFilter, ngEntity);
                }
            }
            applicationConfig.setEntities(entities);

            if (appConfigData.isCompleteApplication()) {
                generateNgApplication(applicationConfig, fileFilter);
                generateApplicationi18nResource(applicationConfig, fileFilter);
                generateNgLocaleResource(applicationConfig, fileFilter);
                generateNgHome(applicationConfig, fileFilter);
            }
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    protected void generateNgEntity(BaseApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter,
            EntityConfig config, BaseEntity entity, Map<String, String> templateLib) throws IOException {
        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        parser.addContext(entity);
        parser.addContext(config);

        Function<String, String> pathResolver = (templatePath) -> {
            String simpleFileName = getSimpleFileNameWithExt(templatePath);
            String ext = templatePath.substring(templatePath.lastIndexOf('.') + 1);
            if (!fileFilter.isEnable(simpleFileName)) {
                return null;
            }
            if (templatePath.contains("_entity-management" + ".html")) {
                templatePath = templatePath.replace("_entity-management", entity.getEntityFolderName() + '/' + entity.getEntityPluralFileName());
            } else if (templatePath.contains("services/_entity.service.js")) {
                templatePath = templatePath.replace("services/_entity.service.js",
                        "entities/" + entity.getEntityFolderName() + '/' + entity.getEntityServiceFileName() + ".service.js");
            } else if (templatePath.contains("services/_entity-search.service.js")) {
                templatePath = templatePath.replace("services/_entity-search.service.js",
                        "entities/" + entity.getEntityFolderName() + '/' + entity.getEntityServiceFileName() + ".search.service.js");
            } else {
                templatePath = templatePath.replace("_entity-management", entity.getEntityFolderName() + '/' + entity.getEntityFileName());
            }

            if ("js".equals(ext)) {
                entityScriptFiles.add(templatePath);
            }
            return templatePath;
        };
        parser.setImportTemplate(templateLib);
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "entity-resources.zip", webRoot, pathResolver, handler);
    }

    protected void generateNgLocaleResource(BaseApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter) throws IOException {
        Map<String, Object> data = new HashMap();//todo remove
        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        parser.addContext(data);

        Function<String, String> pathResolver = (templatePath) -> {
            String lang = templatePath.substring(templatePath.indexOf('_') + 1, templatePath.lastIndexOf('.')); //angular-locale_en.js 
            if (!applicationConfig.getLanguages().contains(lang)) { //if lang selected by dev 
                return null;
            }
            //path modification not required
            return templatePath;
        };
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "angular-locale.zip", webRoot, pathResolver, handler);
    }

    private void generateNgHome(BaseApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter) throws IOException {
        Map<String, Object> data = new HashMap();
        data.put("entityScriptFiles", entityScriptFiles);
        scriptFiles.remove(MODULE_JS);
        scriptFiles.add(0, MODULE_JS);
        data.put("scriptFiles", scriptFiles);

        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        parser.addContext(data);

        copyDynamicFile(parser.getParserManager(), getTemplatePath() + "_index.html", webRoot, "index.html", handler);
        copyDynamicFile(parser.getParserManager(), getTemplatePath() + "_bower.json", projectRoot, "bower.json", handler);
        handler.append(Console.wrap(BaseWebGenerator.class, "MSG_Copying_Bower_Lib_Files", FG_DARK_RED, BOLD));
        FileUtil.copyStaticResource(getTemplatePath() + "bower_components.zip", webRoot, null, handler);
    }

    protected void generateNgApplication(BaseApplicationConfig applicationConfig, ApplicationSourceFilter fileFilter) throws IOException {
        handler.append(Console.wrap(Angular1Generator.class, "MSG_Copying_Application_Files", FG_DARK_RED, BOLD, UNDERLINE));
        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);

        Function<String, String> pathResolver = (templatePath) -> {
            String simpleFileName = getSimpleFileNameWithExt(templatePath);
            String ext = getFileExt(templatePath);

            if (!templatePath.startsWith("app")) {
                if (!fileFilter.isEnable(templatePath)) {
                    return null;
                }
            } else if (!fileFilter.isEnable(simpleFileName)) {
                return null;
            }
            if (templatePath.contains("/_")) {
                templatePath = templatePath.replaceAll("/_", "/");
            }
            if ("js".equals(ext)) {
                scriptFiles.add(templatePath);
            }
            return templatePath;
        };
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "web-resources.zip", webRoot, pathResolver, handler);
    }
    
        
    @Override
    public BaseApplicationConfig getApplicationConfig(String baseName, String buildTool) {
        return new NG1ApplicationConfig(baseName, buildTool);
    }

    @Override
    public BaseEntity getEntity(String appName, String entitySuffix) {
        return new NG1Entity(appName, entitySuffix);
    }

    @Override
    public BaseRelationship getRelationship(String appName, String entitySuffix, Entity entity, RelationAttribute relation) {
        return new NG1Relationship(appName, entitySuffix, entity, relation);
    }

    @Override
    public BaseField getField(BaseAttribute attribute) {
        return new NG1Field(attribute);
    }

}
