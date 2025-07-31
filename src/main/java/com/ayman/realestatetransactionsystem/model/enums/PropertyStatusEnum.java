package com.ayman.realestatetransactionsystem.model.enums;

public enum PropertyStatusEnum {
    AVAILABLE, LOCKED, SOLD, HIDDEN;

    public static PropertyStatusEnum getCode(String status) {
        return switch (status.toUpperCase()) {
            case "AVAILABLE" -> AVAILABLE;
            case "LOCKED" -> LOCKED;
            case "SOLD" -> SOLD;
            default -> HIDDEN;
        };
    }
}
