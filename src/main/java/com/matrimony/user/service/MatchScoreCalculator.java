package com.matrimony.user.service;

import com.matrimony.user.entity.PartnerPreference;
import com.matrimony.user.entity.UserProfile;

public class MatchScoreCalculator {

    public static int calculate(UserProfile user, UserProfile candidate, PartnerPreference preference) {

        int score = 0;

        // AGE
        if (candidate.getAge() >= preference.getMinAge() && candidate.getAge() <= preference.getMaxAge()) {
            score += 20;
        }

        // RELIGION
        if (Boolean.TRUE.equals(preference.getReligionNoBar()) || candidate.getReligion().equals(user.getReligion())) {
            score += 25;
        }

        // CASTE
        if (Boolean.TRUE.equals(preference.getCasteNoBar()) || candidate.getCaste().equals(user.getCaste())) {
            score += 20;
        }

        // LOCATION
        if (candidate.getCountry().equals(user.getCountry())) {
            score += 15;
        }

        // EDUCATION
        if (candidate.getEducation().equals(user.getEducation())) {
            score += 10;
        }

        // BOOST (future)
        if (Boolean.TRUE.equals(candidate.getIsBoosted())) {
            score += 10;
        }

        return score;
    }
}
