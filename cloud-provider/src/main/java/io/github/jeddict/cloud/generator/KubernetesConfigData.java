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
package io.github.jeddict.cloud.generator;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author jGauravGupta
 */
public class KubernetesConfigData implements Serializable {

    private boolean enabled;
    private String namespace;
    private String serviceType;
    private String ingressDomain;

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        if(StringUtils.isBlank(namespace)){
            return "default";
        }
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the ingressDomain
     */
    public String getIngressDomain() {
        return ingressDomain;
    }

    /**
     * @param ingressDomain the ingressDomain to set
     */
    public void setIngressDomain(String ingressDomain) {
        this.ingressDomain = ingressDomain;
    }

}
