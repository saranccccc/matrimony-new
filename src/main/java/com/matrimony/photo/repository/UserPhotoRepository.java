package com.matrimony.photo.repository;

import com.matrimony.photo.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    List<UserPhoto> findByUserIdAndIsDeletedFalse(String userId);

    Optional<UserPhoto> findByUserIdAndIsPrimaryTrueAndIsDeletedFalse(String userId);

}
