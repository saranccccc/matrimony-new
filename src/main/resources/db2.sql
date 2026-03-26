CREATE TABLE `access_requests`
(
    `id`                bigint      NOT NULL AUTO_INCREMENT,
    `requester_user_id` char(26)    NOT NULL,
    `owner_user_id`     char(26)    NOT NULL,
    `type`              varchar(30) NOT NULL,
    `status`            varchar(30) NOT NULL,
    `created_at`        timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        timestamp   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`        varchar(255)         DEFAULT NULL,
    `updated_by`        varchar(255)         DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_access_request` (`requester_user_id`, `owner_user_id`, `type`),
    KEY `idx_access_owner` (`owner_user_id`),
    KEY `idx_access_requester` (`requester_user_id`),
    KEY `idx_access_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE `contact_unlocks`
(
    `id`                    bigint         NOT NULL AUTO_INCREMENT,
    `requester_user_id`     varchar(100)   NOT NULL,
    `target_user_id`        varchar(100)   NOT NULL,
    `amount_charged`        decimal(12, 2) NOT NULL,
    `payment_source`        varchar(50)    NOT NULL,
    `subscription_id`       bigint                  DEFAULT NULL,
    `wallet_transaction_id` bigint                  DEFAULT NULL,
    `created_at`            timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`            timestamp      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`            varchar(255)            DEFAULT NULL,
    `updated_by`            varchar(255)            DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_contact_unlock_unique` (`requester_user_id`, `target_user_id`),
    KEY `idx_contact_unlock_requester` (`requester_user_id`),
    KEY `idx_contact_unlock_target` (`target_user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE `conversations`
(
    `id`         bigint    NOT NULL AUTO_INCREMENT,
    `user1_id`   char(26)  NOT NULL,
    `user2_id`   char(26)  NOT NULL,
    `is_blocked` tinyint(1)         DEFAULT '0',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by` varchar(255)       DEFAULT NULL,
    `updated_by` varchar(255)       DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_conversation` (`user1_id`, `user2_id`),
    KEY `idx_conv_user1` (`user1_id`),
    KEY `idx_conv_user2` (`user2_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `interests`
(
    `id`               bigint      NOT NULL AUTO_INCREMENT,
    `sender_user_id`   char(26)    NOT NULL,
    `receiver_user_id` char(26)    NOT NULL,
    `status`           varchar(30) NOT NULL,
    `created_at`       timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       timestamp   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`       varchar(255)         DEFAULT NULL,
    `updated_by`       varchar(255)         DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_interest` (`sender_user_id`, `receiver_user_id`),
    KEY `idx_interest_sender` (`sender_user_id`),
    KEY `idx_interest_receiver` (`receiver_user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `messages`
(
    `id`              bigint    NOT NULL AUTO_INCREMENT,
    `conversation_id` bigint    NOT NULL,
    `sender_user_id`  char(26)  NOT NULL,
    `content`         text      NOT NULL,
    `is_read`         tinyint(1)         DEFAULT '0',
    `created_at`      timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      timestamp NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`      varchar(255)       DEFAULT NULL,
    `updated_by`      varchar(255)       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_msg_conversation` (`conversation_id`),
    KEY `idx_msg_sender` (`sender_user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


CREATE TABLE `moderation_requests`
(
    `id`              bigint      NOT NULL AUTO_INCREMENT,
    `action`          varchar(50) NOT NULL,
    `target_user_id`  char(26)             DEFAULT NULL,
    `target_photo_id` bigint               DEFAULT NULL,
    `status`          varchar(50) NOT NULL,
    `requested_by`    char(26)    NOT NULL,
    `approved_by`     char(26)             DEFAULT NULL,
    `remarks`         text,
    `created_at`      timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      timestamp   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`      varchar(255)         DEFAULT NULL,
    `updated_by`      varchar(255)         DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_moderation_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;



CREATE TABLE `notifications`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT,
    `user_id`      char(26)    NOT NULL,
    `type`         varchar(50) NOT NULL,
    `title`        varchar(255)         DEFAULT NULL,
    `message`      text,
    `reference_id` varchar(50)          DEFAULT NULL,
    `is_read`      tinyint(1)           DEFAULT '0',
    `created_at`   timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   timestamp   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`   varchar(255)         DEFAULT NULL,
    `updated_by`   varchar(255)         DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_notification_user` (`user_id`),
    KEY `idx_notification_read` (`user_id`, `is_read`),
    KEY `idx_notification_user_created` (`user_id`, `created_at` DESC)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `otp_verification`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `user_id`       binary(26)                                 DEFAULT NULL,
    `channel`       enum ('BOTH','EMAIL','SMS')                DEFAULT NULL,
    `otp`           varchar(255)                               DEFAULT NULL,
    `status`        enum ('ACTIVE','USED','EXPIRED','BLOCKED') DEFAULT NULL,
    `destination`   varchar(255)                               DEFAULT NULL,
    `expires_at`    datetime(6)                                DEFAULT NULL,
    `attempt_count` bigint NOT NULL,
    `created_at`    datetime(6)                                DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_otp_user` (`user_id`),
    CONSTRAINT `fk_otp_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `partner_preferences`
(
    `id`                  bigint       NOT NULL AUTO_INCREMENT,
    `user_id`             varchar(100) NOT NULL,
    `min_age`             int                   DEFAULT NULL,
    `max_age`             int                   DEFAULT NULL,
    `religion_no_bar`     tinyint(1)            DEFAULT '0',
    `caste_no_bar`        tinyint(1)            DEFAULT '0',
    `preferred_religions` varchar(500)          DEFAULT NULL,
    `preferred_castes`    varchar(500)          DEFAULT NULL,
    `preferred_country`   varchar(100)          DEFAULT NULL,
    `preferred_state`     varchar(100)          DEFAULT NULL,
    `created_at`          timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`          varchar(255)          DEFAULT NULL,
    `updated_by`          varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_id` (`user_id`),
    KEY `idx_pref_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;



CREATE TABLE `payments`
(
    `id`              bigint         NOT NULL AUTO_INCREMENT,
    `user_id`         varchar(100)   NOT NULL,
    `plan_id`         bigint              DEFAULT NULL,
    `amount`          decimal(10, 2) NOT NULL,
    `transaction_id`  varchar(255)        DEFAULT NULL,
    `payment_gateway` varchar(100)        DEFAULT NULL,
    `status`          varchar(20)    NOT NULL,
    `purpose`         varchar(50)    NOT NULL,
    `payment_date`    timestamp      NULL DEFAULT NULL,
    `created_at`      timestamp      NOT NULL,
    `updated_at`      timestamp      NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_payments_user_id` (`user_id`),
    KEY `idx_payments_status` (`status`),
    KEY `idx_payments_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `profile_id_sequence`
(
    `id` bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `refresh_token`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `token`       varchar(100) NOT NULL,
    `user_id`     varchar(100) NOT NULL,
    `revoked`     tinyint(1)            DEFAULT '0',
    `expiry_date` timestamp    NOT NULL,
    `created_at`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`  varchar(255)          DEFAULT NULL,
    `updated_by`  varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `token` (`token`),
    UNIQUE KEY `user_id` (`user_id`),
    KEY `idx_refresh_token_user_id` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `subscriptions`
(
    `id`                      bigint         NOT NULL AUTO_INCREMENT,
    `user_id`                 varchar(100)   NOT NULL,
    `plan_id`                 bigint         NOT NULL,
    `plan_name`               varchar(100)   NOT NULL,
    `price`                   decimal(10, 2) NOT NULL,
    `start_date`              timestamp      NOT NULL,
    `expiry_date`             timestamp      NOT NULL,
    `total_contact_limit`     int            NOT NULL,
    `remaining_contact_views` int            NOT NULL,
    `total_message_limit`     int            NOT NULL,
    `remaining_messages`      int            NOT NULL,
    `status`                  varchar(20)    NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_subscriptions_user_id` (`user_id`),
    KEY `idx_subscriptions_status` (`status`),
    KEY `idx_subscriptions_expiry` (`expiry_date`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `user_photos`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `user_id`    char(26)     NOT NULL,
    `photo_url`  varchar(500) NOT NULL,
    `is_primary` tinyint(1)            DEFAULT '0',
    `status`     varchar(30)  NOT NULL,
    `visibility` varchar(30)  NOT NULL,
    `is_deleted` tinyint(1)            DEFAULT '0',
    `created_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by` varchar(255)          DEFAULT NULL,
    `updated_by` varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_photo_user` (`user_id`),
    KEY `idx_photo_status` (`status`),
    KEY `idx_photo_visibility` (`visibility`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `user_profiles`
(
    `id`                 bigint       NOT NULL AUTO_INCREMENT,
    `user_id`            varchar(100) NOT NULL,
    `first_name`         varchar(100)          DEFAULT NULL,
    `last_name`          varchar(100)          DEFAULT NULL,
    `age`                int                   DEFAULT NULL,
    `gender`             varchar(20)           DEFAULT NULL,
    `religion`           varchar(100)          DEFAULT NULL,
    `caste`              varchar(100)          DEFAULT NULL,
    `sub_caste`          varchar(100)          DEFAULT NULL,
    `education`          varchar(200)          DEFAULT NULL,
    `profession`         varchar(200)          DEFAULT NULL,
    `city`               varchar(100)          DEFAULT NULL,
    `state`              varchar(100)          DEFAULT NULL,
    `country`            varchar(100)          DEFAULT NULL,
    `about_me`           text,
    `status`             varchar(30)           DEFAULT NULL,
    `photo_visibility`   varchar(30)           DEFAULT NULL,
    `contact_visibility` varchar(30)           DEFAULT NULL,
    `profile_completed`  tinyint(1)            DEFAULT '0',
    `isBoosted`          tinyint(1)            DEFAULT NULL,
    `boostExpiry`        timestamp    NULL     DEFAULT NULL,
    `created_at`         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         timestamp    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`         varchar(255)          DEFAULT NULL,
    `updated_by`         varchar(255)          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_id` (`user_id`),
    KEY `idx_profile_user_id` (`user_id`),
    KEY `idx_profile_religion` (`religion`),
    KEY `idx_profile_caste` (`caste`),
    KEY `idx_profile_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `users`
(
    `user_id`        binary(26)   NOT NULL,
    `profile_id`     varchar(20)  NOT NULL,
    `first_name`     varchar(255) NOT NULL,
    `last_name`      varchar(255)                                                    DEFAULT NULL,
    `email`          varchar(255) NOT NULL,
    `mobile_no`      varchar(255) NOT NULL,
    `password_hash`  varchar(255) NOT NULL,
    `user_status`    enum ('ACTIVE','BLOCKED','OTP_PENDING')                         DEFAULT NULL,
    `role`           enum ('USER','ADMIN_REQUESTER','ADMIN_APPROVER')                DEFAULT NULL,
    `created_at`     timestamp    NULL                                               DEFAULT CURRENT_TIMESTAMP,
    `created_by`     varchar(255)                                                    DEFAULT NULL,
    `updated_at`     timestamp    NULL                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by`     varchar(255)                                                    DEFAULT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `profile_id_UNIQUE` (`profile_id`),
    UNIQUE KEY `email_UNIQUE` (`email`),
    UNIQUE KEY `mobile_no_UNIQUE` (`mobile_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `wallet_transactions`
(
    `id`                        bigint         NOT NULL AUTO_INCREMENT,
    `user_id`                   varchar(100)   NOT NULL,
    `amount`                    decimal(12, 2) NOT NULL,
    `type`                      varchar(20)    NOT NULL,
    `reference_type`            varchar(50)             DEFAULT NULL,
    `reference_id`              varchar(100)            DEFAULT NULL,
    `balance_after_transaction` decimal(12, 2) NOT NULL,
    `created_at`                timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`                timestamp      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by`                varchar(255)            DEFAULT NULL,
    `updated_by`                varchar(255)            DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_wallet_tx_user_id` (`user_id`),
    KEY `idx_wallet_tx_reference` (`reference_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
CREATE TABLE `wallets`
(
    `id`         bigint         NOT NULL AUTO_INCREMENT,
    `user_id`    varchar(100)   NOT NULL,
    `balance`    decimal(12, 2) NOT NULL DEFAULT '0.00',
    `created_at` timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` timestamp      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `created_by` varchar(255)            DEFAULT NULL,
    `updated_by` varchar(255)            DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `user_id` (`user_id`),
    KEY `idx_wallet_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;






























