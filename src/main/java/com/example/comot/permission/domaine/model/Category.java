package com.example.comot.permission.domaine.model;

public enum Category {
    CREATE_EVENT,
    MANAGE_SCHEDULE,
    VIEW_SALES,
    VALIDATE_TICKET,
    SELLING_ON_SITE,
    SUPER_ADMIN;

    public static Category formString(String category) {
        return switch (category) {
            case "CREATE_EVENT" -> CREATE_EVENT;
            case "MANAGE_SCHEDULE" -> MANAGE_SCHEDULE;
            case "VIEW_SALES" -> VIEW_SALES;
            case "VALIDATE_TICKET" -> VALIDATE_TICKET;
            case "SELLING_ON_SITE" -> SELLING_ON_SITE;
            case "SUPER_ADMIN" -> SUPER_ADMIN;
            default -> throw new IllegalArgumentException("Invalid category " + category);
        };
    }

    public String toString(Category category) {
        return switch (this) {
            case CREATE_EVENT -> "CREATE_EVENT";
            case MANAGE_SCHEDULE -> "MANAGE_SCHEDULE";
            case VIEW_SALES -> "VIEW_SALES";
            case VALIDATE_TICKET -> "VALIDATE_TICKET";
            case SELLING_ON_SITE -> "SELLING_ON_SITE";
            case SUPER_ADMIN -> "SUPER_ADMIN";
        };
    }

}
