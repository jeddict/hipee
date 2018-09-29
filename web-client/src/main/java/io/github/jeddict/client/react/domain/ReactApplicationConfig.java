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
package io.github.jeddict.client.react.domain;

import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import static io.github.jeddict.jcode.util.StringHelper.firstUpper;
import io.github.jeddict.client.web.main.domain.BaseApplicationConfig;

/**
 *
 * @author jGauravGupta
 */
public class ReactApplicationConfig extends BaseApplicationConfig {

    // templates depends on angularAppName variable
    private String angularAppName;
    private String angularXAppName;
    private String serverPort = "8080";
    private String microserviceAppName;
    
    public final String REACT_DIR = MAIN_DIR + "webapp/app/";

    public ReactApplicationConfig(String baseName, String buildTool) {
        super(baseName, buildTool);
    }

    /**
     * get the Angular application name.
     */
    @Override
    public String getAngularAppName() {
        if (angularAppName == null) {
            angularAppName = camelCase(baseName) + (baseName.endsWith("App") ? "" : "App");
        }
        return angularAppName;
    }

    /**
     * get the Angular 2+ application name.
     */
    public String getAngular2AppName() {
        return this.getAngularXAppName();
    }

    public String getAngularXAppName() {
        if (angularXAppName == null) {
            angularXAppName = firstUpper(getCamelizedBaseName());
        }
        return angularXAppName;
    }

    /**
     * @return the serverPort
     */
    public String getServerPort() {
        return serverPort;
    }

    /**
     * @return the microserviceAppName
     */
    public String getMicroserviceAppName() {
        return microserviceAppName;
    }
    
    @Override
    public String getJsType(){
        return "React";
    }
}
