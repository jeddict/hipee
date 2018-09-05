/**
 * Copyright [2018] Gaurav Gupta
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
package io.github.jeddict.client.react;

import static io.github.jeddict.client.angular.AngularGenerator.ANGULAR_TEMPLATE;
import io.github.jeddict.client.angular.AngularPanel;
import io.github.jeddict.client.react.domain.ReactApplicationConfig;
import io.github.jeddict.client.react.domain.ReactEntity;
import io.github.jeddict.client.react.domain.ReactField;
import io.github.jeddict.client.react.domain.ReactRelationship;
import io.github.jeddict.client.web.main.WebGenerator;
import io.github.jeddict.client.web.main.domain.ApplicationSourceFilter;
import io.github.jeddict.client.web.main.domain.BaseApplicationConfig;
import io.github.jeddict.client.web.main.domain.BaseEntity;
import io.github.jeddict.client.web.main.domain.BaseField;
import io.github.jeddict.client.web.main.domain.BaseRelationship;
import io.github.jeddict.client.web.main.domain.Needle;
import io.github.jeddict.client.web.main.domain.NeedleFile;
import io.github.jeddict.jcode.Generator;
import io.github.jeddict.jcode.annotation.Technology;
import static io.github.jeddict.jcode.annotation.Technology.Type.VIEWER;
import io.github.jeddict.jpa.spec.Entity;
import io.github.jeddict.jpa.spec.extend.BaseAttribute;
import io.github.jeddict.jpa.spec.extend.RelationAttribute;
import io.github.jeddict.rest.controller.RESTGenerator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Gaurav Gupta
 */
@ServiceProvider(service = Generator.class)
@Technology(
        type = VIEWER,
        label = "React",
        panel = AngularPanel.class,
        parents = {RESTGenerator.class},
        microservice = true,
        listIndex = 2
)
public class ReactGenerator extends WebGenerator {

    public static final String REACT_TEMPLATE = "io/github/jeddict/client/react/template/";
    private static final String CLIENT_FRAMEWORK = "react";
  
    @Override
    protected List<NeedleFile> getNeedleFiles(BaseApplicationConfig applicationConfig) {
        List<NeedleFile> needleFiles = new ArrayList<>();
            
        NeedleFile NAVBAR_ROUTE = new NeedleFile("app/entities/index.tsx");
        NAVBAR_ROUTE.setNeedles(Arrays.asList(
                new Needle("needle-add-route-import", "import ${entityReactName} from './${entityFolderName}';\n"),
                new Needle("needle-add-route-path", "<ErrorBoundaryRoute path={`${r\"${match.url}\"}/${entityFileName}`} component={${entityReactName}} />\n")
        ));
        needleFiles.add(NAVBAR_ROUTE);

        NeedleFile NAVBAR_COMPONENT_HTML = new NeedleFile("app/shared/layout/header/menus/entities.tsx");
        NAVBAR_COMPONENT_HTML.setNeedles(Arrays.asList(
                new Needle("needle-add-entity-to-menu",
                        "        <DropdownItem tag={Link} to=\"/entity/${routerName}\">\n"
                        + "             <FontAwesomeIcon icon=\"asterisk\" />&nbsp;"
                        + (applicationConfig.isEnableTranslation() ? "<Translate contentKey=\"global.menu.entities.${entityTranslationKeyMenu}\" />" : "${startCase_routerName}")
                        + "\n"
                        + "        </DropdownItem>\n")
        ));
        needleFiles.add(NAVBAR_COMPONENT_HTML);
        
        NeedleFile NAVBAR_REDUCER = new NeedleFile("app/shared/reducers/index.ts");
        NAVBAR_REDUCER.setNeedles(Arrays.asList(
                new Needle(
                        "needle-add-reducer-import",
                        "import ${entityInstance}, { ${entityReactName}State } from 'app/entities/${entityFolderName}/${entityFileName}.reducer';\n"
                ),
                new Needle(
                        "needle-add-reducer-type",
                        "  readonly ${entityInstance}: ${entityReactName}State;\n"
                ),
                new Needle(
                        "needle-add-reducer-combine",
                        "  ${entityInstance},\n"
                )
        ));
        needleFiles.add(NAVBAR_REDUCER);

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

//        NeedleFile LANGUAGE_KEYPIPE = new NeedleFile("app/shared/language/find-language-from-key.pipe.ts");
//        LANGUAGE_KEYPIPE.setNeedles(Arrays.asList(
//                new Needle("needle-i18n-language-key-pipe",
//                        "<#list languageInstances as language>"
//                        + "        '${language.value}': { name: '${language.dispName}' }<#if language_has_next>,</#if>\n"
//                        + "</#list>")
//        ));
//        LANGUAGE_KEYPIPE.setEntity(false);
//        needleFiles.add(LANGUAGE_KEYPIPE);
        return needleFiles;
    }

    @Override
    public String getTemplatePath() {
        return REACT_TEMPLATE;
    }

    @Override
    protected String getClientFramework() {
        return CLIENT_FRAMEWORK;
    }

    @Override
    protected ApplicationSourceFilter getApplicationSourceFilter(BaseApplicationConfig applicationConfig) {
        return new ReactSourceFilter(applicationConfig);
    }

    @Override
    public BaseApplicationConfig getApplicationConfig(String baseName, String buildTool) {
        return new ReactApplicationConfig(baseName, buildTool);
    }

    @Override
    public BaseEntity getEntity(String entityClass, String entitySuffix, String appName, String clientRootFolder) {
        return new ReactEntity(entityClass, entitySuffix, appName, clientRootFolder);
    }

    @Override
    public BaseRelationship getRelationship(String appName, String entitySuffix, Entity entity, RelationAttribute relation, String clientRootFolder) {
        return new ReactRelationship(appName, entitySuffix, entity, relation, clientRootFolder);
    }

    @Override
    public BaseField getField(BaseAttribute attribute) {
        return new ReactField(attribute);
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
