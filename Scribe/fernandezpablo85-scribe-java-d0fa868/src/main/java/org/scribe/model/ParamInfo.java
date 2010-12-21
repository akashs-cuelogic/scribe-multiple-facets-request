/*
 Copyright 2010 Wissen System Pvt. Ltd. All rights reserved.
 Author: Varpe Sagar  on  16-Dec-2010 6:03:31 PM
 */
package org.scribe.model;

import java.io.Serializable;

/**
 * @author Varpe Sagar
 * 
 *         Create Date : 16-Dec-2010 6:03:31 PM
 */
@SuppressWarnings("serial")
public class ParamInfo implements Serializable {

    private String  key;

    private String  value;

    private Boolean isUsed = false;

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
