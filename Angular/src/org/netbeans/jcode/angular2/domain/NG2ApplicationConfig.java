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
package org.netbeans.jcode.angular2.domain;

import static org.netbeans.jcode.core.util.StringHelper.camelCase;
import static org.netbeans.jcode.core.util.StringHelper.firstUpper;
import org.netbeans.jcode.ng.main.domain.*;

/**
 *
 * @author jGauravGupta
 */
public class NG2ApplicationConfig extends NGApplicationConfig {

    private String angularAppName;
    private String angularXAppName;
    private String serverPort = "8080";
    private String microserviceAppName;

    public NG2ApplicationConfig(String baseName, String buildTool) {
        super(baseName, buildTool);
    }

    /**
     * get the Angular application name.
     */
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
}
