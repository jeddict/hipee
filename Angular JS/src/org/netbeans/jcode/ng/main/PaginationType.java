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
package org.netbeans.jcode.ng.main;

/**
 *
 * @author jGauravGupta
 */
public enum PaginationType {
    NO("no", "No"), PAGER("pager", "Pager"), PAGINATION("pagination", "Pagination"), INFINITE_SCROLL("infinite-scroll", "Infinite Scroll");

    private final String keyword;
    private final String title;

    private PaginationType(String keyword, String title) {
        this.keyword = keyword;
        this.title = title;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

}
