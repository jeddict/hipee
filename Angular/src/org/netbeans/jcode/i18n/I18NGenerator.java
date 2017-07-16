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
package org.netbeans.jcode.i18n;

import java.io.IOException;
import java.util.function.Function;
import org.netbeans.api.project.Project;
import org.netbeans.jcode.angular2.domain.NG2ApplicationConfig;
import static org.netbeans.jcode.core.util.FileUtil.getSimpleFileName;
import org.netbeans.jcode.core.util.POMManager;
import org.netbeans.jcode.core.util.ProjectHelper;
import org.netbeans.jcode.layer.ConfigData;
import org.netbeans.jcode.layer.Generator;
import org.netbeans.jcode.layer.Technology;
import static org.netbeans.jcode.ng.main.AngularUtil.copyDynamicResource;
import org.netbeans.jcode.parser.ejs.EJSParser;
import org.netbeans.jcode.rest.controller.RESTGenerator;
import org.netbeans.jcode.stack.config.data.ApplicationConfigData;
import org.netbeans.jcode.task.progress.ProgressHandler;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * @author Gaurav Gupta
 */
@ServiceProvider(service = Generator.class)
@Technology(label = "i18n", panel = I18NConfigPanel.class, sibling = {RESTGenerator.class})
public final class I18NGenerator implements Generator {

    private static final String TEMPLATE = "org/netbeans/jcode/i18n/template/";

    @ConfigData
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
        FileObject configRoot = ProjectHelper.getResourceDirectory(project);

        NG2ApplicationConfig applicationConfig = new NG2ApplicationConfig(
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
