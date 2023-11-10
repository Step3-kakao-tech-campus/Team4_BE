package com.ktc.matgpt.domain.user.entity;

public enum Gender {
    MALE,
    FEMALE,
    UNKNOWN;

    public static Gender fromString(String genderStr) {
        if (genderStr == null) {
            return Gender.UNKNOWN;
        }

        switch (genderStr.toLowerCase()) {
            case "male":
                return Gender.MALE;
            case "female":
                return Gender.FEMALE;
            default:
                return Gender.UNKNOWN;
        }
    }
}
