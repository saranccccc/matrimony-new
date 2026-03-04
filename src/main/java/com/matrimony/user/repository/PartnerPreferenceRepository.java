package com.matrimony.user.repository;

import com.matrimony.user.entity.PartnerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartnerPreferenceRepository extends JpaRepository<PartnerPreference, Long> {

    Optional<PartnerPreference> findByUserId(String userId);

    @Query("""
                select p.userId
                from PartnerPreference p
                join p.preferredReligions r
                where r = :religion
            """)
    List<String> findUserIdsByReligion(@Param("religion") String religion);

}
