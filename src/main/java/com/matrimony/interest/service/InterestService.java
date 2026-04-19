package com.matrimony.interest.service;

import com.matrimony.auth.service.UserValidationService;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.interest.dto.InterestStatus;
import com.matrimony.interest.entity.Interest;
import com.matrimony.interest.repository.InterestRepository;
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
    private final UserValidationService userValidationService;
    @Transactional
    public void sendInterest(String senderId, String receiverId) {
        userValidationService.validateUser(senderId);
        userValidationService.validateUser(receiverId);

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

    @Transactional
    public void respondToInterest(Long interestId, String receiverId, InterestStatus status) {
        Interest interest = interestRepository.findById(interestId).orElseThrow(() -> new CustomException(ErrorCode.INTEREST_NOT_FOUND));
        if (!interest.getReceiverUserId().equals(receiverId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (interest.getStatus() != InterestStatus.PENDING) {
            // todo interest already accepted
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
