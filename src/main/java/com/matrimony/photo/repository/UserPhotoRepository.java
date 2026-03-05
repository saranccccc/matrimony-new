package com.matrimony.photo.repository;

import com.matrimony.photo.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    List<UserPhoto> findByUserIdAndIsDeletedFalse(String userId);

    Optional<UserPhoto> findByUserIdAndIsPrimaryTrueAndIsDeletedFalse(String userId);

    @Query("select count(p) from UserPhoto p where p.userId = :userId")
    long countByUserId(String userId);

}
