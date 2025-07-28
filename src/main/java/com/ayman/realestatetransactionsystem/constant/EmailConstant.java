package com.ayman.realestatetransactionsystem.constant;



public class EmailConstant {
    public static final String PAYMENT_EMAIL_SUBJECT = "Property Transaction Completed";
    public static final String REQUEST_EMAIL_SUBJECT = "Request For %s";
    public static final String REQUEST_APPROVAL_EMAIL_SUBJECT = "Request Approved";
    public static final String REQUEST_DENIED_EMAIL_SUBJECT = "Request Denied";

    public static final String BUYER_APPROVAL_MESSAGE = "Your request to the property %s has been approved by %s";
    public static final String BUYER_DENIAL_MESSAGE = "Your request to the property %s has been denied by %s \n%s";
    public static final String BUYER_PAYMENT_MESSAGE = "Your payment succeeded for the property %s";
    public static final String SELLER_PAYMENT_MESSAGE = "%s has completed the payment successfully for the property %s";
    public static final String BROKER_PAYMENT_MESSAGE = "Commission for the property %s with the amount %.2f has been transferred successfully to your bank account";
    public static final String SELLER_REQUEST_MESSAGE = "You received a new request from %s to the property %s";
    public static final String BUYER_REQUEST_MESSAGE = "Your request to the property %s have been delivered successfully";
    public static final String BROKER_REQUEST_MESSAGE = "New request from %s to the property %s";
    public static final double BROKER_COMMISSION_RATE = 0.049375;
}
