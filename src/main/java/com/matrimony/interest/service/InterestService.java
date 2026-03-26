package com.matrimony.interest.service;

import com.matrimony.admin.service.UserService;
import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.interest.dto.InterestStatus;
import com.matrimony.interest.entity.Interest;
import com.matrimony.interest.repository.InterestRepository;
import com.matrimony.user.dto.ProfileStatus;
import com.matrimony.user.dto.UserProfileResponse;
import com.matrimony.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final UserService userService;
    private final UserProfileService userProfileService;

    @Transactional
    public void sendInterest(String senderId, String receiverId) {
        validateUser(senderId);
        validateUser(receiverId);
        if (senderId.equals(receiverId)) {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }

        Optional<Interest> existing = interestRepository.findBySenderUserIdAndReceiverUserId(senderId, receiverId);

        if (existing.isPresent()) {
            throw new CustomException(ErrorCode.INTEREST_ALREADY_SENT);
        }

        Interest interest = Interest.builder().senderUserId(senderId).receiverUserId(receiverId).status(InterestStatus.PENDING).build();

        interestRepository.save(interest);
    }

    private void validateUser(String senderId) {
        User user = userService.getUser(senderId);
        if (UserStatus.ACTIVE != user.getUserStatus()) {
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }
        UserProfileResponse userProfileResponse = userProfileService.getProfile(senderId);
        if (ProfileStatus.APPROVED != userProfileResponse.getStatus()) {
            throw new CustomException(ErrorCode.PROFILE_NOT_APPROVED);
        }
    }

    @Transactional
    public void respondToInterest(Long interestId, String receiverId, InterestStatus status) {
        Interest interest = interestRepository.findById(interestId).orElseThrow(() -> new CustomException(ErrorCode.INTEREST_NOT_FOUND));
        if (!interest.getReceiverUserId().equals(receiverId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (interest.getStatus() != InterestStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }

        if (status == InterestStatus.ACCEPTED || status == InterestStatus.REJECTED) {

            interest.setStatus(status);

        } else {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }
        interestRepository.save(interest);
    }

    public Page<Interest> getReceivedInterests(String userId, Pageable pageable) {
        return interestRepository.findByReceiverUserIdAndStatus(userId, InterestStatus.PENDING, pageable);
    }

    public Page<Interest> getSentInterests(String userId, Pageable pageable) {
        return interestRepository.findBySenderUserId(userId, pageable);
    }
}
