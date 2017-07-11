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

import org.netbeans.jcode.ng.main.domain.*;
import java.util.List;
import java.util.Set;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.netbeans.jcode.core.util.StringHelper.camelCase;
import static org.netbeans.jcode.core.util.StringHelper.firstUpper;
import static org.netbeans.jcode.core.util.StringHelper.kebabCase;

/**
 *
 * @author jGauravGupta
 */
public class NG2ApplicationConfig implements NGApplicationConfig {

    //Path
    public final String MAIN_DIR = "src/main/";
    public final String TEST_DIR = "src/test/";
    public final String CLIENT_MAIN_SRC_DIR = MAIN_DIR + "webapp/";
    public final String CLIENT_TEST_SRC_DIR = TEST_DIR + "javascript/";
    public final String MAIN_SRC_DIR = CLIENT_MAIN_SRC_DIR;
    public final String TEST_SRC_DIR = CLIENT_TEST_SRC_DIR;
    public final String CLIENT_DIST_DIR = "www/";
    public final String ANGULAR_DIR = MAIN_DIR + "webapp/app/";
    public final String BUILD_DIR;
    public final String DIST_DIR;

    private String applicationPath;//rest path
    private String buildTool;
    private String clientPackageManager;

    private String angularAppName;
    private String angularXAppName;
    private String applicationType;//gateway , monolith
    private String serverPort = "8080";
    private String microserviceAppName;
    private String baseName;
    private String capitalizedBaseName;
    private String camelizedBaseName;
    private String dasherizedBaseName;
    private String lowercaseBaseName;
    private List<NGEntity> entities;
    private String restPackage;
    private String authenticationType;

    //i18n
    private boolean enableTranslation;
    private String nativeLanguage = "en";
    private Set<String> languages;
    private boolean enableI18nRTL;

    private String jhiPrefix;
    private String jhiPrefixCapitalized;

    //Persistence
    private String searchEngine;
    private String databaseType;
    private String devDatabaseType;
    private String hibernateCache;//ehcache2

    private String websocket;
    private String messageBroker;//kafka
    private String clientFramework;// angular1
    private boolean useSass;

    //test
    private String[] testFrameworks = {};//protractor
    private boolean protractorTests;

    //filter    
    private boolean enableSocialSignIn;
    private boolean skipUserManagement;
    private boolean skipClient;
    private boolean skipServer;

    private boolean enableMetrics;
    private boolean enableLogs;
    private boolean enableHealth;
    private boolean enableConfiguration;
    private boolean enableAudits;
    private boolean enableProfile;
    private boolean enableDocs;

    public NG2ApplicationConfig(String baseName, String buildTool) {
        this.baseName = baseName;
        this.buildTool = buildTool;
        if ("maven".equals(buildTool)) {
            BUILD_DIR = "target/";
        } else {
            BUILD_DIR = "build/";
        }
        DIST_DIR = BUILD_DIR + CLIENT_DIST_DIR;
    }

    /**
     * @return the jhiPrefixCapitalized
     */
    @Override
    public String getJhiPrefixCapitalized() {
        if (jhiPrefixCapitalized == null) {
            jhiPrefixCapitalized = firstUpper(getJhiPrefix());
        }
        return jhiPrefixCapitalized;
    }

    /**
     * @return the authenticationType
     */
    @Override
    public String getAuthenticationType() {
        if (authenticationType == null) {
            authenticationType = "jwt";
        }
        return authenticationType;
    }

    /**
     * @param authenticationType the authenticationType to set
     */
    @Override
    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    /**
     * @return the applicationType
     */
    @Override
    public String getApplicationType() {
        if (applicationType == null) {
            applicationType = MONOLITH_APPLICATION_TYPE;
        }
        return applicationType;
    }

    /**
     * @return the enableTranslation
     */
    @Override
    public boolean isEnableTranslation() {
        return enableTranslation;
    }

    /**
     * @param enableTranslation the enableTranslation to set
     */
    @Override
    public void setEnableTranslation(boolean enableTranslation) {
        this.enableTranslation = enableTranslation;
    }

    /**
     * @return the enableSocialSignIn
     */
    @Override
    public boolean isEnableSocialSignIn() {
        return enableSocialSignIn;
    }

    /**
     * @return the websocket
     */
    @Override
    public String getWebsocket() {
        return websocket;
    }

    /**
     * @param websocket the websocket to set
     */
    @Override
    public void setWebsocket(String websocket) {
        this.websocket = websocket;
    }

    /**
     * @return the searchEngine
     */
    @Override
    public String getSearchEngine() {
        return searchEngine;
    }

    /**
     * @param searchEngine the searchEngine to set
     */
    @Override
    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    /**
     * @return the databaseType
     */
    @Override
    public String getDatabaseType() {
        if (databaseType == null) {
            databaseType = SQL_DATABASE_TYPE;
        }
        return databaseType;
    }

    /**
     * @param databaseType the databaseType to set
     */
    @Override
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * @return the jhiPrefix
     */
    @Override
    public String getJhiPrefix() {
        if (jhiPrefix == null) {
            jhiPrefix = EMPTY;
        }
        return jhiPrefix;
    }

    /**
     * @param jhiPrefix the jhiPrefix to set
     */
    @Override
    public void setJhiPrefix(String jhiPrefix) {
        this.jhiPrefix = jhiPrefix;
    }

    /**
     * @return the devDatabaseType
     */
    @Override
    public String getDevDatabaseType() {
        return devDatabaseType;
    }

    /**
     * @param devDatabaseType the devDatabaseType to set
     */
    @Override
    public void setDevDatabaseType(String devDatabaseType) {
        this.devDatabaseType = devDatabaseType;
    }

    /**
     * @return the baseName
     */
    @Override
    public String getBaseName() {
        if (baseName == null) {
            baseName = EMPTY;
        }
        return baseName;
    }

    /**
     * @return the capitalizedBaseName
     */
    @Override
    public String getCapitalizedBaseName() {
        if (capitalizedBaseName == null) {
            capitalizedBaseName = firstUpper(getBaseName());
        }
        return capitalizedBaseName;
    }

    /**
     * @return the camelizedBaseName
     */
    @Override
    public String getCamelizedBaseName() {
        if (camelizedBaseName == null) {
            camelizedBaseName = camelCase(baseName);
        }
        return camelizedBaseName;
    }

    @Override
    public boolean isSkipUserManagement() {
        return skipUserManagement;
    }

    /**
     * @return the dasherizedBaseName
     */
    @Override
    public String getDasherizedBaseName() {
        if (dasherizedBaseName == null) {
            dasherizedBaseName = kebabCase(baseName);
        }
        return dasherizedBaseName;
    }

    /**
     * @return the lowercaseBaseName
     */
    @Override
    public String getLowercaseBaseName() {
        if (lowercaseBaseName == null) {
            lowercaseBaseName = baseName.toLowerCase();
        }
        return lowercaseBaseName;
    }

    /**
     * @return the useSass
     */
    @Override
    public boolean isUseSass() {
        return useSass;
    }

    /**
     * @param useSass the useSass to set
     */
    @Override
    public void setUseSass(boolean useSass) {
        this.useSass = useSass;
    }

    /**
     * @return the hibernateCache
     */
    @Override
    public String getHibernateCache() {
        return hibernateCache;
    }

    /**
     * @param hibernateCache the hibernateCache to set
     */
    @Override
    public void setHibernateCache(String hibernateCache) {
        this.hibernateCache = hibernateCache;
    }

    /**
     * @return the languages
     */
    @Override
    public Set<String> getLanguages() {
        return languages;
    }

    /**
     * @param languages the languages to set
     */
    @Override
    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }

    /**
     * @return the enableI18nRTL
     */
    public boolean isEnableI18nRTL() {
        return enableI18nRTL;
    }

    /**
     * @param enableI18nRTL the enableI18nRTL to set
     */
    public void setEnableI18nRTL(boolean enableI18nRTL) {
        this.enableI18nRTL = enableI18nRTL;
    }

    /**
     * @return the applicationPath
     */
    @Override
    public String getApplicationPath() {
        return applicationPath;
    }

    /**
     * @param applicationPath the applicationPath to set
     */
    @Override
    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
    }

    /**
     * @return the entities
     */
    @Override
    public List<NGEntity> getEntities() {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    @Override
    public void setEntities(List<NGEntity> entities) {
        this.entities = entities;
    }

    /**
     * @return the enableMetrics
     */
    @Override
    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    /**
     * @param enableMetrics the enableMetrics to set
     */
    @Override
    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    /**
     * @return the enableLogs
     */
    @Override
    public boolean isEnableLogs() {
        return enableLogs;
    }

    /**
     * @param enableLogs the enableLogs to set
     */
    @Override
    public void setEnableLogs(boolean enableLogs) {
        this.enableLogs = enableLogs;
    }

    /**
     * @return the enableHealth
     */
    @Override
    public boolean isEnableHealth() {
        return enableHealth;
    }

    /**
     * @param enableHealth the enableHealth to set
     */
    @Override
    public void setEnableHealth(boolean enableHealth) {
        this.enableHealth = enableHealth;
    }

    /**
     * @return the enableConfiguration
     */
    @Override
    public boolean isEnableConfiguration() {
        return enableConfiguration;
    }

    /**
     * @param enableConfiguration the enableConfiguration to set
     */
    @Override
    public void setEnableConfiguration(boolean enableConfiguration) {
        this.enableConfiguration = enableConfiguration;
    }

    /**
     * @return the enableAudits
     */
    @Override
    public boolean isEnableAudits() {
        return enableAudits;
    }

    /**
     * @param enableAudits the enableAudits to set
     */
    @Override
    public void setEnableAudits(boolean enableAudits) {
        this.enableAudits = enableAudits;
    }

    /**
     * @return the enableProfile
     */
    @Override
    public boolean isEnableProfile() {
        return enableProfile;
    }

    /**
     * @param enableProfile the enableProfile to set
     */
    @Override
    public void setEnableProfile(boolean enableProfile) {
        this.enableProfile = enableProfile;
    }

    /**
     * @return the enableDocs
     */
    @Override
    public boolean isEnableDocs() {
        return enableDocs;
    }

    /**
     * @param enableDocs the enableDocs to set
     */
    @Override
    public void setEnableDocs(boolean enableDocs) {
        this.enableDocs = enableDocs;
    }

    /**
     * @return the restPackage
     */
    @Override
    public String getRestPackage() {
        return restPackage;
    }

    /**
     * @param restPackage the restPackage to set
     */
    @Override
    public void setRestPackage(String restPackage) {
        this.restPackage = restPackage;
    }

    /**
     * @return the messageBroker
     */
    @Override
    public String getMessageBroker() {
        return messageBroker;
    }

    /**
     * @param messageBroker the messageBroker to set
     */
    @Override
    public void setMessageBroker(String messageBroker) {
        this.messageBroker = messageBroker;
    }

    /**
     * @return the clientFramework
     */
    @Override
    public String getClientFramework() {
        return clientFramework;
    }

    /**
     * @param clientFramework the clientFramework to set
     */
    @Override
    public void setClientFramework(String clientFramework) {
        this.clientFramework = clientFramework;
    }

    @Override
    public String getBuildTool() {
        return buildTool;
    }

    /**
     * @return the protractorTests
     */
    @Override
    public boolean isProtractorTests() {
        return protractorTests;
    }

    /**
     * @param protractorTests the protractorTests to set
     */
    @Override
    public void setProtractorTests(boolean protractorTests) {
        this.protractorTests = protractorTests;
    }

    /**
     * @return the clientPackageManager
     */
    @Override
    public String getClientPackageManager() {
        return clientPackageManager;
    }

    /**
     * @param clientPackageManager the clientPackageManager to set
     */
    @Override
    public void setClientPackageManager(String clientPackageManager) {
        this.clientPackageManager = clientPackageManager;
    }

    /**
     * @return the skipClient
     */
    @Override
    public boolean isSkipClient() {
        return skipClient;
    }

    /**
     * @param skipClient the skipClient to set
     */
    @Override
    public void setSkipClient(boolean skipClient) {
        this.skipClient = skipClient;
    }

    /**
     * @return the skipServer
     */
    @Override
    public boolean isSkipServer() {
        return skipServer;
    }

    /**
     * @param skipServer the skipServer to set
     */
    @Override
    public void setSkipServer(boolean skipServer) {
        this.skipServer = skipServer;
    }

    public String getSrcDir() {
        return MAIN_SRC_DIR;
    }

    public String getTestDir() {
        return TEST_SRC_DIR;
    }

    public String getBuildDir() {
        return BUILD_DIR;
    }

    public String getDistDir() {
        return DIST_DIR;
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

    /**
     * @return the nativeLanguage
     */
    public String getNativeLanguage() {
        return nativeLanguage;
    }

    /**
     * @return the testFrameworks
     */
    public String[] getTestFrameworks() {
        return testFrameworks;
    }
}