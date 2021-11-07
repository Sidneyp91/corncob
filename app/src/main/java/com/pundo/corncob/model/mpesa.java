package com.pundo.corncob.model;

import android.util.Base64;

import com.bdhobare.mpesa.utils.Config;
import com.bdhobare.mpesa.utils.TimeUtil;

public class mpesa {
    private String businessShortCode;
    private String password;
    private String timestamp;
    private String transactionType;
    private String amount;
    private String partyA;
    private String partyB;
    private String phoneNumber;
    private String callBackURL;
    private String accountReference;
    private String transactionDesc;

    private mpesa(Builder builder) {
        this.businessShortCode = builder.businessShortCode;
        this.timestamp = TimeUtil.getTimestamp();
        this.password = getPassword(builder.businessShortCode, builder.passkey, this.timestamp);
        this.transactionType = builder.transactionType;
        this.amount = String.valueOf(builder.amount);
        this.partyA = builder.partyA;
        this.partyB = builder.partyB;
        this.phoneNumber = builder.phoneNumber;
        this.accountReference = builder.accountReference;
        this.transactionDesc = builder.transactionDesc;
        this.callBackURL = builder.callBackURL;
        if (!builder.firebaseRegID.isEmpty()){
            if (builder.callBackURL.endsWith("/")){
                this.callBackURL = builder.callBackURL + builder.firebaseRegID;
            }else {
                this.callBackURL = builder.callBackURL + "/" + builder.firebaseRegID;
            }
        }

    }
    private String getPassword(String businessShortCode, String passkey, String timestamp){
        String str = businessShortCode + passkey + timestamp;
        return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
    }
    public static class Builder {
        private String businessShortCode;
        private String passkey;
        private String transactionType = Config.DEFAULT_TRANSACTION_TYPE;
        private int amount;
        private String partyA;
        private String partyB;
        private String phoneNumber;
        private String callBackURL = "http://mycallbackurl.com/checkout.php";
        private String accountReference;
        private String transactionDesc;
        private String firebaseRegID = "";
    }

    public mpesa() {
    }

    public mpesa(String businessShortCode, String password, String timestamp, String transactionType, String amount, String partyA, String partyB, String phoneNumber, String callBackURL, String accountReference, String transactionDesc) {
        this.businessShortCode = businessShortCode;
        this.password = password;
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.amount = amount;
        this.partyA = partyA;
        this.partyB = partyB;
        this.phoneNumber = phoneNumber;
        this.callBackURL = callBackURL;
        this.accountReference = accountReference;
        this.transactionDesc = transactionDesc;
    }

    public String getBusinessShortCode() {
        return businessShortCode;
    }

    public void setBusinessShortCode(String businessShortCode) {
        this.businessShortCode = businessShortCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPartyA() {
        return partyA;
    }

    public void setPartyA(String partyA) {
        this.partyA = partyA;
    }

    public String getPartyB() {
        return partyB;
    }

    public void setPartyB(String partyB) {
        this.partyB = partyB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCallBackURL() {
        return callBackURL;
    }

    public void setCallBackURL(String callBackURL) {
        this.callBackURL = callBackURL;
    }

    public String getAccountReference() {
        return accountReference;
    }

    public void setAccountReference(String accountReference) {
        this.accountReference = accountReference;
    }

    public String getTransactionDesc() {
        return transactionDesc;
    }

    public void setTransactionDesc(String transactionDesc) {
        this.transactionDesc = transactionDesc;
    }
}
