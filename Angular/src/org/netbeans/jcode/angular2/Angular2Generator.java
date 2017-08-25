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
package org.netbeans.jcode.angular2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static java.util.stream.Collectors.toList;
import org.netbeans.jcode.angular2.domain.NG2ApplicationConfig;
import org.netbeans.jcode.angular2.domain.NG2Entity;
import org.netbeans.jcode.angular2.domain.NG2Relationship;
import org.netbeans.jcode.angular2.domain.NG2Field;
import org.netbeans.jcode.console.Console;
import static org.netbeans.jcode.console.Console.BOLD;
import static org.netbeans.jcode.console.Console.FG_RED;
import static org.netbeans.jcode.console.Console.UNDERLINE;
import static org.netbeans.jcode.core.util.FileUtil.getFileExt;
import org.netbeans.jcode.core.util.POMManager;
import static org.netbeans.jcode.core.util.ProjectHelper.getProjectWebRoot;
import org.netbeans.jcode.core.util.SourceGroupSupport;
import org.netbeans.jcode.layer.Technology;
import static org.netbeans.jcode.layer.Technology.Type.VIEWER;
import org.netbeans.jcode.rest.controller.RESTGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.netbeans.jcode.layer.Generator;
import org.netbeans.jcode.ng.main.AngularGenerator;
import static org.netbeans.jcode.ng.main.AngularUtil.copyDynamicFile;
import static org.netbeans.jcode.ng.main.AngularUtil.copyDynamicResource;
import static org.netbeans.jcode.ng.main.AngularUtil.getResource;
import static org.netbeans.jcode.ng.main.AngularUtil.insertNeedle;
import org.netbeans.jcode.ng.main.domain.ApplicationSourceFilter;
import org.netbeans.jcode.ng.main.domain.EntityConfig;
import org.netbeans.jcode.ng.main.domain.NGApplicationConfig;
import org.netbeans.jcode.ng.main.domain.NGEntity;
import org.netbeans.jcode.ng.main.domain.NGField;
import org.netbeans.jcode.ng.main.domain.NGRelationship;
import org.netbeans.jcode.ng.main.domain.Needle;
import org.netbeans.jcode.ng.main.domain.NeedleFile;
import org.netbeans.jcode.parser.ejs.EJSParser;
import org.netbeans.jpa.modeler.spec.Entity;
import org.netbeans.jpa.modeler.spec.extend.BaseAttribute;
import org.netbeans.jpa.modeler.spec.extend.RelationAttribute;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author Gaurav Gupta
 */
@ServiceProvider(service = Generator.class)
@Technology(type = VIEWER, label = "Angular 4", panel = Angular2Panel.class, parents = {RESTGenerator.class}, listIndex=1)
public class Angular2Generator extends AngularGenerator {

    private static final String TEMPLATE = "org/netbeans/jcode/angular2/template/";
    private static final String CLIENT_FRAMEWORK = "angular2";
    private FileObject projectRoot;
    private FileObject webRoot;
    private ApplicationSourceFilter fileFilter;

    private final Function<String, String> PATH_RESOLVER = (templatePath) -> {
        String ext = getFileExt(templatePath);
        if (!fileFilter.isEnable(templatePath)) {
            return null;
        }
        if (templatePath.contains("/_")) {
            templatePath = templatePath.replaceAll("/_", "/");
        } else if (templatePath.charAt(0) == '_') { //_index.html
            templatePath = templatePath.substring(1);
        }
        return templatePath;
    };

    @Override
    protected void generateClientSideComponent() {
        try {
            NGApplicationConfig applicationConfig = getAppConfig();
            fileFilter = getApplicationSourceFilter(applicationConfig);
            projectRoot = project.getProjectDirectory();
            webRoot = getProjectWebRoot(project);
            Map<String, String> templateLib = getResource(getTemplatePath() + "entity-include-resources.zip");
            List<NGEntity> ngEntities = new ArrayList<>();
            List<Entity> entities = entityMapping.getGeneratedEntity().collect(toList());
            if(!entities.isEmpty()){
                handler.append(Console.wrap(AngularGenerator.class, "MSG_Copying_Entity_Files", FG_RED, BOLD, UNDERLINE));
            }
            for (Entity entity : entities) {
                NGEntity ngEntity = getEntity(applicationConfig, entity);
                if (ngEntity != null) {
                    ngEntities.add(ngEntity);
                    generateNgEntity(applicationConfig, getEntityConfig(), ngEntity, templateLib);
                    generateNgEntityTest(applicationConfig, getEntityConfig(), ngEntity);
                    generateNgEntityi18nResource(applicationConfig, fileFilter, ngEntity);
                }
            }
            generateNgEnumi18nResource(applicationConfig, fileFilter);
            applicationConfig.setEntities(ngEntities);

            if (appConfigData.isCompleteApplication()) {
                EJSParser parser = new EJSParser();
                parser.addContext(applicationConfig);
                generateNgApplication(parser);
                generateNgTest(parser);
                generateNgApplicationi18nResource(applicationConfig, fileFilter);
//              generateNgLocaleResource(applicationConfig);
                generateNgHome(parser);

                addMavenDependencies("pom/_pom.xml");
                appConfigData.addProfile("webpack");
            }

            updateNgEntityNeedle(applicationConfig, ngEntities);

//            installYarn(project.getProjectDirectory());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

//    public void installYarn(FileObject workingFolder){
//        handler.append(Console.wrap(Angular2Generator.class, "YARN_INSTALL", FG_RED, BOLD, UNDERLINE));
//        executeCommand(workingFolder,handler, "yarn", "install");
//    }
    protected void generateNgEntity(NGApplicationConfig applicationConfig,
            EntityConfig config, NGEntity entity, Map<String, String> templateLib) throws IOException {
        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        parser.addContext(entity);
        parser.addContext(config);

        parser.setImportTemplate(templateLib);
        parser.eval("function toArrayString(array) { return '[\\'' + array.join('\\',\\'') + '\\']' }");//external util function
        parser.eval("  function generateEntityQueries(relationships, entityInstance, dto) {\n" +
"        var queries = [];\n" +
"        var variables = [];\n" +
"        var hasManyToMany = false;\n" +
"        relationships.forEach(function(relationship) {\n" +
"            var query;\n" +
"            var variableName;\n" +
"            hasManyToMany = hasManyToMany || relationship.relationshipType === 'many-to-many';\n" +
"            if (relationship.relationshipType === 'one-to-one' && relationship.ownerSide === true && relationship.otherEntityName !== 'user') {\n" +
"                variableName = relationship.relationshipFieldNamePlural.toLowerCase();\n" +
"                if (variableName === entityInstance) {\n" +
"                    variableName += 'Collection';\n" +
"                }\n" +
"                var relationshipFieldName = \"this.\" + entityInstance + \".\" + relationship.relationshipFieldName;\n" +
"                var relationshipFieldNameIdCheck = dto === 'no' ?\n" +
"                    \"!\"+ relationshipFieldName + \" || !\"+ relationshipFieldName+\".id\" :\n" +
"                    \"!\"+ relationshipFieldName+\"Id\";\n" +
"\n" +
"                query =\n" +
"        \"this.\"+relationship.otherEntityName+\"Service\" +\n" +
"            \".query({filter: '\"+relationship.otherEntityRelationshipName.toLowerCase()+\"}-is-null'})\"+\n" +
"            \".subscribe((res: ResponseWrapper) => {\"+\n" +
"                \"if (\"+relationshipFieldNameIdCheck+\") {\"\n" +
"                    \"this.\"+ variableName + \" = res.json;\"+\n" +
"                \"} else {\"+\n" +
"                    \"this.\"+relationship.otherEntityName+\"Service\"+\n" +
"                        \".find(\"+relationshipFieldName + (dto === 'no' ? '.id' : 'Id') + \")\"+\n" +
"                        \".subscribe((subRes: \"+relationship.otherEntityAngularName+\") => {\"+\n" +
"                            \"this.\"+variableName+\" = [subRes].concat(res.json);\"+\n" +
"                        \"}, (subRes: ResponseWrapper) => this.onError(subRes.json));\"+\n" +
"                \"}\"+\n" +
"            \"}, (res: ResponseWrapper) => this.onError(res.json));\";\n" +
"            } else if (relationship.relationshipType !== 'one-to-many') {\n" +
"                variableName = relationship.otherEntityNameCapitalizedPlural.toLowerCase();\n" +
"                if (variableName === entityInstance) {\n" +
"                    variableName += 'Collection';\n" +
"                }\n" +
"                query =\n" +
"        \"this.\"+relationship.otherEntityName+\"Service.query()\"+\n" +
"            \".subscribe((res: ResponseWrapper) => { this.\"+variableName+\" = res.json; }, (res: ResponseWrapper) => this.onError(res.json));\";\n" +
"            }\n" +
"            if (variableName && !this.contains(queries, query)) {\n" +
"                queries.push(query);\n" +
"                variables.push(variableName+\": \"+ relationship.otherEntityAngularName+\"[];\");\n" +
"            }\n" +
"        });\n" +
"        return {\n" +
"            queries : queries,\n" +
"            variables : variables,\n" +
"            hasManyToMany : hasManyToMany\n" +
"        }" +
"    }\n" +
"");
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "entity-resources.zip", webRoot, getEntityPathResolver(entity), handler);
    }

    private Function<String, String> getEntityPathResolver(NGEntity entity) {
        Function<String, String> ENTITY_PATH_RESOLVER = (templatePath) -> {
            String ext = templatePath.substring(templatePath.lastIndexOf('.') + 1);
            if (!fileFilter.isEnable(templatePath)) {
                return null;
            }
            if (templatePath.contains("_entity-management")) {
                templatePath = templatePath.replace("_entity-management", entity.getEntityFolderName() + '/' + entity.getEntityFileName());
            } else if (templatePath.contains("_entity.service.ts")
                    || templatePath.contains("_entity.model.ts")
                    || templatePath.contains("_entity-popup.service.ts")) {
                templatePath = templatePath.replace("_entity", entity.getEntityFolderName() + '/' + entity.getEntityServiceFileName());
            } else if (templatePath.contains("_index.ts")) {
                templatePath = templatePath.replace("_index", entity.getEntityFolderName() + "/index");
            } else if (templatePath.contains("_entity.spec.ts")) {
                templatePath = templatePath.replace("_entity", entity.getEntityServiceFileName());
            }

            return templatePath;
        };
        return ENTITY_PATH_RESOLVER;
    }

    private void updateNgEntityNeedle(NGApplicationConfig applicationConfig, List<NGEntity> ngEntities) {
        for (NeedleFile needleFilefile : getNeedleFiles(applicationConfig)) {
            for (String file : needleFilefile.getFile()) {
                needleFilefile.getNeedles().forEach(needle -> 
                        insertNeedle(file.startsWith("/") ? projectRoot:webRoot, 
                                file, needle.getInsertPointer(), 
                                needle.getTemplate(applicationConfig, 
                                file.startsWith("/") ?null:ngEntities), handler)
                );
            }
        }
    }

    private List<NeedleFile> getNeedleFiles(NGApplicationConfig applicationConfig) {
        List<NeedleFile> needleFiles = new ArrayList<>();
        
        NeedleFile ENTITY_MODULE_TS = new NeedleFile("app/entities/entity.module.ts");
        ENTITY_MODULE_TS.setNeedles(Arrays.asList(
                //appName -> getAngularXAppName
                new Needle("needle-add-entity-module-import", "import { ${appName}${entityAngularName}Module } from './${entityFolderName}/${entityFileName}.module';\n"),
                new Needle("needle-add-entity-module", "        ${appName}${entityAngularName}Module,\n")
        ));
        needleFiles.add(ENTITY_MODULE_TS);
                
        NeedleFile NAVBAR_COMPONENT_HTML = new NeedleFile("app/layouts/navbar/navbar.component.html");
        NAVBAR_COMPONENT_HTML.setNeedles(Arrays.asList(
                new Needle("needle-add-entity-to-menu",
                "                    <li uiSrefActive=\"active\">\n"
                + "                        <a class=\"dropdown-item\" routerLink=\"${routerName}\" (click)=\"collapseNavbar()\">\n"
                + "                            <i class=\"fa fa-fw fa-asterisk\" aria-hidden=\"true\"></i>\n"
                + "                            <span <#if enableTranslation>${prefix}Translate=\"global.menu.entities.${camelCase_routerName}\"</#if>>${startCase_routerName}</span>\n"
                + "                        </a>\n"
                + "                    </li>")
        ));
        needleFiles.add(NAVBAR_COMPONENT_HTML);

        
        if(applicationConfig.isEnableTranslation()){
            for (String language : applicationConfig.getLanguages()) {
                NeedleFile GLOBAL_JSON = new NeedleFile("i18n/"+language+"/global.json");
                GLOBAL_JSON.setNeedles(Arrays.asList(
                        new Needle("needle-menu-add-entry", "\t\t\"${entityTranslationKeyMenu}\": \"${startCase_entityClass}\",")
                ));
                needleFiles.add(GLOBAL_JSON);
            }
        }

        NeedleFile LANGUAGE_WEBPACK = new NeedleFile("/webpack/webpack.common.js");
        LANGUAGE_WEBPACK.setNeedles(Arrays.asList(
                new Needle("needle-i18n-language-webpack", 
                        "        <#list languages as language>" +
                        "                { pattern: \"./${srcDir}i18n/${language}/*.json\", fileName: \"./i18n/${language}.json\" }<#if language_has_next>,</#if>\n" +
                        "        </#list>")
        ));
        needleFiles.add(LANGUAGE_WEBPACK);
        
        return needleFiles;
    }

    private void addMavenDependencies(String pom) {
        if (POMManager.isMavenProject(project)) {
            POMManager pomManager = new POMManager(TEMPLATE + pom, project);
            pomManager.commit();
        } else {
            handler.warning(NbBundle.getMessage(Angular2Generator.class, "TITLE_Maven_Project_Not_Found"), NbBundle.getMessage(Angular2Generator.class, "MSG_Maven_Project_Not_Found"));
        }
    }

    private void generateNgHome(EJSParser parser) throws IOException {
        Function<String, String> pathResolver = templatePath -> templatePath.replace("_", "");// "_index.html" ->  "tabIndex.html";
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "project-resources.zip", projectRoot, pathResolver, handler);

//        parser.setDelimiter('#');//nested template
//        copyDynamicFile(parser.getParserManager(), getTemplatePath() + "_index.html", webRoot, "tabIndex.html", handler);
        FileObject nbactionsFile = projectRoot.getFileObject("nbactions.xml");
        if (nbactionsFile == null) {
            copyDynamicFile(parser.getParserManager(), getTemplatePath() + "webpack_nbactions.xml", projectRoot, "nbactions.xml", handler);
        }
        handler.info(NbBundle.getMessage(Angular2Generator.class, "ACTIVATE_PROFILE"),
                NbBundle.getMessage(Angular2Generator.class, "ACTIVATE_WEBPACK_PROFILE"));

//        removeUnwantedFile();
    }

    private void removeUnwantedFile() throws IOException {
//        rename tabIndex.html
        FileObject indexFile = webRoot.getFileObject("index.html");
        if (indexFile != null) {
            FileLock lock = indexFile.lock();
            indexFile.rename(lock, "index_" + new Date().getTime(), "html");
            lock.releaseLock();
        }
    }

    protected void generateNgApplication(EJSParser parser) throws IOException {
        handler.append(Console.wrap(AngularGenerator.class, "MSG_Copying_Application_Files", FG_RED, BOLD, UNDERLINE));
        List<String> skipList = Arrays.asList("_find-language-from-key.pipe.ts");//charset issue 
        copyDynamicResource(parser.getParserManager(skipList), getTemplatePath() + "web-resources.zip", webRoot, PATH_RESOLVER, handler);
    }

    protected void generateNgTest(EJSParser parser) throws IOException {
        FileObject testRoot = SourceGroupSupport.getTestSourceGroup(project).getRootFolder().getParent();//test/java => ../java
//        FileObject javascriptRoot = testRoot.createFolder("javascript");
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "karma-test.zip", testRoot, PATH_RESOLVER, handler);
        if (ngData.isProtractorTest()) {
            copyDynamicResource(parser.getParserManager(), getTemplatePath() + "protractor-test.zip", testRoot, PATH_RESOLVER, handler);
        }
    }

    protected void generateNgEntityTest(NGApplicationConfig applicationConfig, EntityConfig config, NGEntity entity) throws IOException {
        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);
        parser.addContext(entity);
        parser.addContext(config);

        FileObject testRoot = SourceGroupSupport.getTestSourceGroup(project).getRootFolder().getParent();//test/java => ../java
        copyDynamicResource(parser.getParserManager(), getTemplatePath() + "entity-karma-test.zip", testRoot, getEntityPathResolver(entity), handler);
        if (ngData.isProtractorTest()) {
            copyDynamicResource(parser.getParserManager(), getTemplatePath() + "entity-protractor-test.zip", testRoot, getEntityPathResolver(entity), handler);
        }
    }

    @Override
    public String getTemplatePath() {
        return TEMPLATE;
    }

    @Override
    protected String getClientFramework() {
        return CLIENT_FRAMEWORK;
    }

    @Override
    protected ApplicationSourceFilter getApplicationSourceFilter(NGApplicationConfig applicationConfig) {
        return new NG2SourceFilter(applicationConfig);
    }

    @Override
    protected NGApplicationConfig getAppConfig() {
        NGApplicationConfig config = super.getAppConfig();
        config.setClientPackageManager("yarn");
        config.setProtractorTests(ngData.isProtractorTest());
        return config;
    }

    @Override
    public NGApplicationConfig getNGApplicationConfig(String baseName, String buildTool) {
        return new NG2ApplicationConfig(baseName, buildTool);
    }

    @Override
    public NGEntity getNGEntity(String name, String entityAngularJSSuffix) {
        return new NG2Entity(name, entityAngularJSSuffix);
    }

    @Override
    public NGRelationship getNGRelationship(String angularAppName, String entityAngularJSSuffix, Entity entity, RelationAttribute relation) {
        return new NG2Relationship(angularAppName, entityAngularJSSuffix, entity, relation);
    }

    @Override
    public NGField getNGField(BaseAttribute attribute) {
        return new NG2Field(attribute);
    }

}
