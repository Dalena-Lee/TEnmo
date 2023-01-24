package com.techelevator.tenmo.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Transfer {

   // @Min(value = 3001)
    private int transferId;

  //  @NotNull
   // @Positive
    private int senderID;

   // @NotNull
    //@Positive
    private int recipientID;

    @NotNull (message = "Amount cannot be blank.")
    private double amount;

    @NotBlank
    private String username;

    //@NotNull
    //@Positive
    private int typeId;

  //  @NotNull
   // @Positive
    private int statusId;

    public Transfer(){
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(int recipientID) {
        this.recipientID = recipientID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
   public String getUsername() {
    return username;
}

    public void setUsername(String username) {
        this.username = username;
    }
}