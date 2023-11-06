package com.ktc.matgpt.user.entity;


public enum AgeGroup {
    ZERO_TO_NINE,
    TEN_TO_NINETEEN,
    TWENTY_TO_TWENTYNINE,
    THIRTY_TO_THIRTYNINE,
    FORTY_TO_FORTYNINE,
    FIFTY_TO_FIFTYNINE,
    SIXTY_ABOVE;


    public static AgeGroup of(String ageRange, String delimiter) {

        Integer startAge;
        Integer endAge;

        if (ageRange.endsWith(delimiter)) {
            startAge = Integer.parseInt(ageRange.replace(delimiter, ""));
            endAge = null;
        } else {
            String[] arr = ageRange.split(delimiter);
            if (arr.length != 2) {
                throw new IllegalArgumentException("Invalid age range format: " + ageRange);
            }

            try {
                startAge = Integer.parseInt(arr[0]);
                endAge = Integer.parseInt(arr[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid age range format: " + ageRange, e);
            }
        }

        return convertToAgeRange(startAge, endAge);
    }

    private static AgeGroup convertToAgeRange(Integer startAge, Integer endAge) {
        // 시작 나이를 기준으로 적절한 Enum 값을 반환합니다.
        if (startAge >= 0 && (endAge == null || endAge <= 9)) {
            return AgeGroup.ZERO_TO_NINE;
        } else if (startAge >= 10 && (endAge == null || endAge <= 19)) {
            return AgeGroup.TEN_TO_NINETEEN;
        } else if (startAge >= 20 && (endAge == null || endAge <= 29)) {
            return AgeGroup.TWENTY_TO_TWENTYNINE;
        } else if (startAge >= 30 && (endAge == null || endAge <= 39)) {
            return AgeGroup.THIRTY_TO_THIRTYNINE;
        } else if (startAge >= 40 && (endAge == null || endAge <= 49)) {
            return AgeGroup.FORTY_TO_FORTYNINE;
        } else if (startAge >= 50 && (endAge == null || endAge <= 59)) {
            return AgeGroup.FIFTY_TO_FIFTYNINE;
        } else {
            return AgeGroup.SIXTY_ABOVE;
        }
    }
}