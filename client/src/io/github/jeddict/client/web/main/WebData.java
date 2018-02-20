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

import static io.github.jeddict.jcode.util.StringHelper.camelCase;
import io.github.jeddict.jcode.stack.config.data.LayerConfigData;
import java.util.Arrays;
import java.util.List;
import io.github.jeddict.client.web.main.domain.ClientPackager;
import static io.github.jeddict.client.web.main.domain.ClientPackager.NPM;
import io.github.jeddict.client.web.main.domain.EntityConfig;
import io.github.jeddict.rest.controller.RESTData;

/**
 *
 * @author Gaurav Gupta
 */
public class WebData extends LayerConfigData<RESTData> {

    public static final String DEFAULT_PREFIX = "jee";
    
    private String prefix;
    private String module;
    private String applicationTitle;
    private EntityConfig applicationConfig;
    private ClientPackager clientPackager;
    private boolean protractorTest;
    private boolean sass;

    /**
     * @return the applicationConfig
     */
    public EntityConfig getApplicationConfig() {
        return applicationConfig;
    }

    /**
     * @param applicationConfig the applicationConfig to set
     */
    public void setApplicationConfig(EntityConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        if(prefix == null){
            return DEFAULT_PREFIX;
        }
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the applicationTitle
     */
    public String getApplicationTitle() {
        return applicationTitle;
    }

    /**
     * @param applicationTitle the applicationTitle to set
     */
    public void setApplicationTitle(String applicationTitle) {
        this.applicationTitle = applicationTitle;
    }
    
    @Override
    protected void onLayerConnection(){
        if(getModule()!=null){
            getParentLayerConfigData().setFrontendAppName(camelCase(getModule()) + (getModule().endsWith("App") ? "" : "App"));
        }
    }

    /**
     * @return the clientPackager
     */
    public ClientPackager getClientPackager() {
        if(clientPackager == null){
            return NPM;
        }
        return clientPackager;
    }

    /**
     * @param clientPackager the clientPackager to set
     */
    public void setClientPackager(ClientPackager clientPackager) {
        this.clientPackager = clientPackager;
    }

    /**
     * @return the protractorTest
     */
    public boolean isProtractorTest() {
        return protractorTest;
    }

    /**
     * @param protractorTest the protractorTest to set
     */
    public void setProtractorTest(boolean protractorTest) {
        this.protractorTest = protractorTest;
    }

    /**
     * @return the sass
     */
    public boolean isSass() {
        return sass;
    }

    /**
     * @param sass the sass to set
     */
    public void setSass(boolean sass) {
        this.sass = sass;
    }
    
    @Override
    public List<String> getUsageDetails() {
        return Arrays.asList(isProtractorTest() ? "ProtractorTest" : null);
    }
}
