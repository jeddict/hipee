/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.jeddict.client.i18n;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author jGauravGupta
 */
public class Language implements Serializable {

    private String name, dispName, value;
    private boolean rtl, skipForLocale;

    public Language() {
    }
    
    public Language(String name, String dispName, String value) {
        this.name = name;
        this.dispName = dispName;
        this.value = value;
    }

    public Language(String name, String dispName, String value, boolean rtl) {
        this.name = name;
        this.dispName = dispName;
        this.value = value;
        this.rtl = rtl;
    }

    public Language(String name, String dispName, String value, boolean rtl, boolean skipForLocale) {
        this.name = name;
        this.dispName = dispName;
        this.value = value;
        this.rtl = rtl;
        this.skipForLocale = skipForLocale;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isRtl() {
        return rtl;
    }
    
    /**
     * @return the dispName
     */
    public String getDispName() {
        return dispName;
    }

    /**
     * @return the skipForLocale
     */
    public boolean isSkipForLocale() {
        return skipForLocale;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Language other = (Language) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return value;
    }
    
}
