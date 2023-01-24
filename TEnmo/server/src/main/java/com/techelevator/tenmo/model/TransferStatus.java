package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TransferStatus {

    @NotNull
    @Min(value = 1)
    private int statusId;
    @NotBlank
    private String statusDesignation;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusDesignation() {
        return statusDesignation;
    }

    public void setStatusDesignation(String statusDesignation) {
        this.statusDesignation = statusDesignation;
    }

}
