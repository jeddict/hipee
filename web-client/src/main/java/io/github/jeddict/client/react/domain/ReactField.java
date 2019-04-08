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
package io.github.jeddict.client.react.domain;

import io.github.jeddict.jpa.spec.extend.BaseAttribute;
import io.github.jeddict.client.web.main.domain.BaseField;

public class ReactField extends BaseField {

    public ReactField(BaseAttribute attribute) {
        super(attribute);
    }

    /**
     * @param fieldType the fieldType to set
     */
    @Override
    public void setFieldType(String fieldType, String databaseType) {
        if ("DateTime".equals(fieldType) 
                || "Date".equals(fieldType)) {
            fieldType = "Instant";
        }
        this.fieldType = fieldType;
    }

}
