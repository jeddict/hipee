/**
 * Copyright 2013-2019 the original author or authors from the Jeddict project (https://jeddict.github.io/).
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
package io.github.jeddict.client.angular;

import io.github.jeddict.jcode.Generator;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.extend.BaseAttribute;
import io.github.jeddict.jpa.spec.extend.RelationAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import io.github.jeddict.client.angular.domain.NGApplicationConfig;
import io.github.jeddict.client.angular.domain.NGEntity;
import io.github.jeddict.client.angular.domain.NGField;
import io.github.jeddict.client.angular.domain.NGRelationship;
import io.github.jeddict.client.web.main.WebGenerator;
import io.github.jeddict.client.web.main.domain.ApplicationSourceFilter;
import io.github.jeddict.client.web.main.domain.BaseApplicationConfig;
import io.github.jeddict.client.web.main.domain.BaseEntity;
import io.github.jeddict.client.web.main.domain.BaseField;
import io.github.jeddict.client.web.main.domain.BaseRelationship;
import io.github.jeddict.client.web.main.domain.Needle;
import io.github.jeddict.client.web.main.domain.NeedleFile;
import io.github.jeddict.jcode.annotation.Technology;
import static io.github.jeddict.jcode.annotation.Technology.Type.VIEWER;
import io.github.jeddict.rest.controller.RESTGenerator;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Gaurav Gupta
 */
@ServiceProvider(service = Generator.class)
@Technology(
        type = VIEWER,
        label = "Angular",
        panel = AngularPanel.class,
        parents = {RESTGenerator.class},
        microservice = true,
        listIndex = 1
)
public class AngularGenerator extends WebGenerator {

    public static final String ANGULAR_TEMPLATE = "/io/github/jeddict/client/angular/template/";
    private static final String CLIENT_FRAMEWORK = "angular2";

    @Override
    protected List<NeedleFile> getNeedleFiles(BaseApplicationConfig applicationConfig) {
        List<NeedleFile> needleFiles = new ArrayList<>();

        NeedleFile ENTITY_MODULE_TS = new NeedleFile("app/entities/entity.module.ts");
        ENTITY_MODULE_TS.setNeedles(Arrays.asList(
                //appName -> getAngularXAppName
                new Needle(
                        "needle-add-entity-module-import",
                        "import { ${appName}${entityAngularName}Module } from './${entityFolderName}/${entityFileName}.module';\n"
                ),
                new Needle(
                        "needle-add-entity-module",
                        "        ${appName}${entityAngularName}Module,\n"
                )
        ));
        needleFiles.add(ENTITY_MODULE_TS);

        NeedleFile NAVBAR_COMPONENT_HTML = new NeedleFile("app/layouts/navbar/navbar.component.html");
        NAVBAR_COMPONENT_HTML.setNeedles(Arrays.asList(
                new Needle("needle-add-entity-to-menu",
                        "                    <li>\n"
                        + "                        <a class=\"dropdown-item\" routerLink=\"${routerName}\" routerLinkActive=\"active\" [routerLinkActiveOptions]=\"{ exact: true }\" (click)=\"collapseNavbar()\">\n"
                        + "                            <i class=\"fa fa-fw fa-asterisk\" aria-hidden=\"true\"></i>\n"
                        + "                            <span <#if enableTranslation>${prefix}Translate=\"global.menu.entities.${camelCase_routerName}\"</#if>>${startCase_routerName}</span>\n"
                        + "                        </a>\n"
                        + "                    </li>\n")
        ));
        needleFiles.add(NAVBAR_COMPONENT_HTML);

        if (applicationConfig.isEnableTranslation()) {
            for (String language : applicationConfig.getLanguages()) {
                NeedleFile GLOBAL_JSON = new NeedleFile("i18n/" + language + "/global.json");
                GLOBAL_JSON.setNeedles(Arrays.asList(
                        new Needle("needle-menu-add-entry", "\t\t\"${entityTranslationKeyMenu}\": \"${startCase_entityClass}\",\n")
                ));
                needleFiles.add(GLOBAL_JSON);
            }
        }

        NeedleFile LANGUAGE_WEBPACK = new NeedleFile("/webpack/webpack.common.js");
        LANGUAGE_WEBPACK.setNeedles(Arrays.asList(
                new Needle("needle-i18n-language-webpack",
                        "<#list languages as language>"
                        + "                               { pattern: \"./${srcDir}i18n/${language}/*.json\", fileName: \"./i18n/${language}.json\" }<#if language_has_next>,</#if>\n"
                        + "</#list>")
        ));
        LANGUAGE_WEBPACK.setEntity(false);
        needleFiles.add(LANGUAGE_WEBPACK);

        NeedleFile LANGUAGE_KEYPIPE = new NeedleFile("app/shared/language/find-language-from-key.pipe.ts");
        LANGUAGE_KEYPIPE.setNeedles(Arrays.asList(
                new Needle("needle-i18n-language-key-pipe",
                        "<#list languageInstances as language>"
                        + "        '${language.value}': { name: '${language.dispName}' }<#if language_has_next>,</#if>\n"
                        + "</#list>")
        ));
        LANGUAGE_KEYPIPE.setEntity(false);
        needleFiles.add(LANGUAGE_KEYPIPE);

        NeedleFile LANGUAGE_CONSTANT = new NeedleFile("app/core/language/language.constants.ts");
        LANGUAGE_CONSTANT.setNeedles(Arrays.asList(
                new Needle("needle-i18n-language-constant",
                        "<#list languageInstances as language>"
                        + "    '${language.value}',\n"
                        + "</#list>")
        ));
        LANGUAGE_CONSTANT.setEntity(false);
        needleFiles.add(LANGUAGE_CONSTANT);

        return needleFiles;
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

    @Override
    public String getTemplatePath() {
        return ANGULAR_TEMPLATE;
    }

    @Override
    protected String getClientFramework() {
        return CLIENT_FRAMEWORK;
    }

    @Override
    protected ApplicationSourceFilter getApplicationSourceFilter(BaseApplicationConfig applicationConfig) {
        return new NGSourceFilter(applicationConfig);
    }

    @Override
    public BaseApplicationConfig getApplicationConfig(String baseName, String buildTool) {
        return new NGApplicationConfig(baseName, buildTool);
    }

    @Override
    public BaseEntity getEntity(String entityClass, String entitySuffix, String appName, String clientRootFolder) {
        return new NGEntity(entityClass, entitySuffix, appName, clientRootFolder);
    }

    @Override
    public BaseRelationship getRelationship(String appName, String entitySuffix, Entity entity, RelationAttribute relation, String clientRootFolder) {
        return new NGRelationship(appName, entitySuffix, entity, relation, clientRootFolder);
    }

    @Override
    public BaseField getField(BaseAttribute attribute) {
        return new NGField(attribute);
    }

    @Override
    protected String getPomPath() {
        return ANGULAR_TEMPLATE + "pom/pom.xml.ejs";
    }

    @Override
    protected String getExtScriptPath() {
        return ANGULAR_TEMPLATE;
    }

}
