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
package io.github.jeddict.client.i18n;

import static io.github.jeddict.jcode.util.FileUtil.getSimpleFileName;
import io.github.jeddict.jcode.util.POMManager;
import io.github.jeddict.jcode.util.ProjectHelper;
import io.github.jeddict.jcode.Generator;
import io.github.jeddict.jcode.parser.ejs.EJSParser;
import static io.github.jeddict.jcode.parser.ejs.EJSUtil.copyDynamicResource;
import io.github.jeddict.jcode.ApplicationConfigData;
import io.github.jeddict.jcode.task.progress.ProgressHandler;
import java.io.IOException;
import java.util.function.Function;
import org.netbeans.api.project.Project;
import io.github.jeddict.client.angular.domain.NGApplicationConfig;
import io.github.jeddict.jcode.annotation.ConfigData;
import io.github.jeddict.jcode.annotation.Technology;
import io.github.jeddict.rest.controller.RESTGenerator;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Gaurav Gupta
 */
@ServiceProvider(service = Generator.class)
@Technology(
        label = "i18n", 
        panel = I18NConfigPanel.class, 
        sibling = {RESTGenerator.class}
)
public final class I18NGenerator implements Generator {

    private static final String TEMPLATE = "io/github/jeddict/client/i18n/template/";

    protected Project project;

    @ConfigData
    protected ApplicationConfigData appConfigData;

    @ConfigData
    protected ProgressHandler handler;

    @ConfigData
    private I18NConfigData i18NConfig;

    @Override
    public void execute() throws IOException {
        if (!appConfigData.isCompleteApplication()) {
            return;
        }
        if(appConfigData.isGateway() || appConfigData.isMonolith()){
            return;
        }
        project = appConfigData.isGateway() ? appConfigData.getGatewayProject() : appConfigData.getTargetProject();

        FileObject configRoot = ProjectHelper.getResourceDirectory(project);

        NGApplicationConfig applicationConfig = new NGApplicationConfig(
                new POMManager(project, true).getArtifactId(), "maven");
        applicationConfig.setEnableSocialSignIn(false);
        applicationConfig.setLanguages(i18NConfig.getOtherLanguagesKeyword());

        EJSParser parser = new EJSParser();
        parser.addContext(applicationConfig);

        Function<String, String> pathResolver = (templatePath) -> {
            String simpleFileName = getSimpleFileName(templatePath);
            String lang = simpleFileName.substring("_messages_".length());
            if (!applicationConfig.getLanguages().contains(lang.replace("_", "-"))) {
                return null;
            }
            return String.format("i18n/messages_%s.properties", lang);
        };
        copyDynamicResource(parser.getParserManager(), TEMPLATE + "rest-resources-i18n.zip", configRoot, pathResolver, handler);
    }

}
