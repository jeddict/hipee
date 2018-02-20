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

import io.github.jeddict.jcode.stack.config.data.LayerConfigData;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Gaurav Gupta
 */
public class CloudConfigData extends LayerConfigData {

    private KubernetesConfigData kubernetesConfigData;
    
    private OpenshiftConfigData openshiftConfigData;

    /**
     * @return the kubernetesConfigData
     */
    public KubernetesConfigData getKubernetesConfigData() {
        if(kubernetesConfigData==null){
            kubernetesConfigData = new KubernetesConfigData();
        }
        return kubernetesConfigData;
    }

    /**
     * @param kubernetesConfigData the kubernetesConfigData to set
     */
    public void setKubernetesConfigData(KubernetesConfigData kubernetesConfigData) {
        this.kubernetesConfigData = kubernetesConfigData;
    }
    
        /**
     * @return the openshiftConfigData
     */
    public OpenshiftConfigData getOpenshiftConfigData() {
        if(openshiftConfigData==null){
            openshiftConfigData = new OpenshiftConfigData();
        }
        return openshiftConfigData;
    }

    /**
     * @param openshiftConfigData the openshiftConfigData to set
     */
    public void setOpenshiftConfigData(OpenshiftConfigData openshiftConfigData) {
        this.openshiftConfigData = openshiftConfigData;
    }


    @Override
    public List<String> getUsageDetails() {
        return Arrays.asList(getKubernetesConfigData().isEnabled() ? "Kubernetes" : null,
                getOpenshiftConfigData().isEnabled() ? "Openshift" : null
        );
    }
}
