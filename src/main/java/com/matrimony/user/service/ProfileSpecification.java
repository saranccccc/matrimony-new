package com.matrimony.user.service;

import com.matrimony.user.dto.ProfileSearchRequest;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.entity.UserProfile;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProfileSpecification {

    public static Specification<UserProfile> search(ProfileSearchRequest request, String currentUserId) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Exclude self
            predicates.add(cb.notEqual(root.get("userId"), currentUserId));
            // Only approved/completed
            predicates.add(root.get("status").in(ProfileStatus.APPROVED));
            if (request.getGender() != null) {
                predicates.add(cb.equal(root.get("gender"), request.getGender()));
            }
            if (request.getMinAge() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("age"), request.getMinAge()));
            }
            if (request.getMaxAge() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("age"), request.getMaxAge()));
            }
            if (request.getReligion() != null) {
                predicates.add(cb.equal(root.get("religion"), request.getReligion()));
            }
            if (request.getCaste() != null) {
                predicates.add(cb.equal(root.get("caste"), request.getCaste()));
            }
            if (request.getCity() != null) {
                predicates.add(cb.equal(root.get("city"), request.getCity()));
            }
            if (request.getState() != null) {
                predicates.add(cb.equal(root.get("state"), request.getState()));
            }
            if (request.getCountry() != null) {
                predicates.add(cb.equal(root.get("country"), request.getCountry()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
