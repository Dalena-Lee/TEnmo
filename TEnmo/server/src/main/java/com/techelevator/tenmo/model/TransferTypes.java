package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TransferTypes {

    @NotNull
    private int typeID;
    @NotBlank
    private String typeDesignation;

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeDesignation() {
        return typeDesignation;
    }

    public void setTypeDesignation(String typeDesignation) {
        this.typeDesignation = typeDesignation;
    }
}
