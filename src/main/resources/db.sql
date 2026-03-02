use test;
CREATE TABLE `users` (
 `user_id` BINARY(26) NOT NULL,
  `profile_id` varchar(20) NOT NULL,
   `first_name` varchar(255)  NOT NULL,
      `last_name` varchar(255) DEFAULT NULL,
     `email` varchar(255) NOT NULL,
  `mobile_no` varchar(255) NOT NULL,
    `password_hash` varchar(255) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ,
    `created_by` varchar(255) DEFAULT NULL,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` varchar(255) DEFAULT NULL,
  `user_status` enum('ACTIVE','BLOCKED','OTP_PENDING') DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `profile_id_UNIQUE` (`profile_id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `mobile_no_UNIQUE` (`mobile_no`)
);



CREATE TABLE `otp_verification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` BINARY(26) DEFAULT NULL,
  `channel` enum('BOTH','EMAIL','SMS') DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `status` enum('ACTIVE','USED','EXPIRED','BLOCKED') DEFAULT NULL,
  `destination` varchar(255) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `attempt_count` bigint NOT NULL,
  'resend_count' bigint NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
   PRIMARY KEY (`id`),
     CONSTRAINT fk_otp_user FOREIGN KEY (user_id)  REFERENCES users(user_id)   ON DELETE CASCADE
);


CREATE TABLE refresh_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(100) NOT NULL UNIQUE,
	user_id VARCHAR(100) NOT NULL UNIQUE,
	revoked BOOLEAN DEFAULT FALSE,
	expiryDate TIMESTAMP NOT NULL ,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_refresh_token_user_id ON refresh_token(user_id);

CREATE TABLE `profile_id_sequence` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);


CREATE TABLE plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    validity_days INT NOT NULL,
    contact_view_limit INT NOT NULL,
    message_limit INT NOT NULL,
    chat_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    profile_boost_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);


ALTER TABLE plans
ADD CONSTRAINT chk_price_positive CHECK (price >= 0),
ADD CONSTRAINT chk_validity_positive CHECK (validity_days > 0),
ADD CONSTRAINT chk_contact_limit_positive CHECK (contact_view_limit >= 0),
ADD CONSTRAINT chk_message_limit_positive CHECK (message_limit >= 0);


CREATE TABLE subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL,
    plan_id BIGINT NOT NULL,
    plan_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    total_contact_limit INT NOT NULL,
    remaining_contact_views INT NOT NULL,
    total_message_limit INT NOT NULL,
    remaining_messages INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL
);

CREATE INDEX idx_subscriptions_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscriptions_status ON subscriptions(status);
CREATE INDEX idx_subscriptions_expiry ON subscriptions(expiry_date);


CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL,
    plan_id BIGINT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_id VARCHAR(255),
    payment_gateway VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    purpose VARCHAR(50) NOT NULL,
    payment_date TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL
);

CREATE INDEX idx_payments_user_id ON payments(user_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payments_transaction_id ON payments(transaction_id);

CREATE TABLE wallets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL UNIQUE,
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_wallet_user_id ON wallets(user_id);


CREATE TABLE wallet_transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    reference_type VARCHAR(50),
    reference_id VARCHAR(100),
    balance_after_transaction DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_wallet_tx_user_id ON wallet_transactions(user_id);
CREATE INDEX idx_wallet_tx_reference ON wallet_transactions(reference_id);


CREATE TABLE contact_unlocks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    requester_user_id VARCHAR(100) NOT NULL,
    target_user_id VARCHAR(100) NOT NULL,
    amount_charged DECIMAL(12,2) NOT NULL,
    payment_source VARCHAR(50) NOT NULL,
    subscription_id BIGINT NULL,
    wallet_transaction_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_contact_unlock_requester  ON contact_unlocks(requester_user_id);
CREATE INDEX idx_contact_unlock_target ON contact_unlocks(target_user_id);
CREATE UNIQUE INDEX uk_contact_unlock_unique ON contact_unlocks(requester_user_id, target_user_id);

CREATE TABLE user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    age INT,
    gender VARCHAR(20),
    religion VARCHAR(100),
    caste VARCHAR(100),
    sub_caste VARCHAR(100),
    education VARCHAR(200),
    profession VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    about_me TEXT,
    status VARCHAR(30),
    photo_visibility VARCHAR(30),
    contact_visibility VARCHAR(30),
    profile_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_profile_user_id ON user_profiles(user_id);
CREATE INDEX idx_profile_religion ON user_profiles(religion);
CREATE INDEX idx_profile_caste ON user_profiles(caste);
CREATE INDEX idx_profile_status ON user_profiles(status);


CREATE TABLE partner_preferences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL UNIQUE,
    min_age INT,
    max_age INT,
    religion_no_bar BOOLEAN DEFAULT FALSE,
    caste_no_bar BOOLEAN DEFAULT FALSE,
    preferred_religions VARCHAR(500),
    preferred_castes VARCHAR(500),
    preferred_country VARCHAR(100),
    preferred_state VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_pref_user_id ON partner_preferences(user_id);

CREATE TABLE interests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_user_id CHAR(26) NOT NULL,
    receiver_user_id CHAR(26) NOT NULL,
    status VARCHAR(30) NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL,
    CONSTRAINT uk_interest UNIQUE(sender_user_id, receiver_user_id)
);

CREATE INDEX idx_interest_sender ON interests(sender_user_id);
CREATE INDEX idx_interest_receiver ON interests(receiver_user_id);


CREATE TABLE user_photos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id CHAR(26) NOT NULL,
    photo_url VARCHAR(500) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    status VARCHAR(30) NOT NULL,
    visibility VARCHAR(30) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_photo_user ON user_photos(user_id);
CREATE INDEX idx_photo_status ON user_photos(status);
CREATE INDEX idx_photo_visibility ON user_photos(visibility);

CREATE TABLE access_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    requester_user_id CHAR(26) NOT NULL,
    owner_user_id CHAR(26) NOT NULL,
    type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL,
    CONSTRAINT uk_access_request
    UNIQUE(requester_user_id, owner_user_id, type)
);

CREATE INDEX idx_access_owner ON access_requests(owner_user_id);
CREATE INDEX idx_access_requester ON access_requests(requester_user_id);
CREATE INDEX idx_access_status ON access_requests(status);


CREATE TABLE conversations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user1_id CHAR(26) NOT NULL,
    user2_id CHAR(26) NOT NULL,
    is_blocked BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL,
    CONSTRAINT uk_conversation UNIQUE(user1_id, user2_id)
);

CREATE INDEX idx_conv_user1 ON conversations(user1_id);
CREATE INDEX idx_conv_user2 ON conversations(user2_id);


CREATE TABLE messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    conversation_id BIGINT NOT NULL,
    sender_user_id CHAR(26) NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
  	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_msg_conversation ON messages(conversation_id);
CREATE INDEX idx_msg_sender ON messages(sender_user_id);


CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id CHAR(26) NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255),
    message TEXT,
    reference_id VARCHAR(50),
    is_read BOOLEAN DEFAULT FALSE,
  	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_notification_user ON notifications(user_id);
CREATE INDEX idx_notification_read ON notifications(user_id, is_read);
CREATE INDEX idx_notification_user_created ON notifications(user_id, created_at DESC);

CREATE TABLE moderation_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    action VARCHAR(50) NOT NULL,
    target_user_id CHAR(26),
    target_photo_id BIGINT,
    status VARCHAR(50) NOT NULL,
    requested_by CHAR(26) NOT NULL,
    approved_by CHAR(26),
    remarks TEXT,
  	created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
    created_by varchar(255) DEFAULT NULL,
	updated_by varchar(255) DEFAULT NULL
);

CREATE INDEX idx_moderation_status ON moderation_requests(status);

