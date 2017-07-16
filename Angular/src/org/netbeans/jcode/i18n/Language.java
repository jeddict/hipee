/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.jcode.i18n;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author jGauravGupta
 */
public class Language implements Serializable {

    private final String name, value;
    private boolean rtl;

    public Language(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Language(String name, String value, boolean rtl) {
        this.name = name;
        this.value = value;
        this.rtl = rtl;
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

}
