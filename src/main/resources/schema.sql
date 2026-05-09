CREATE TABLE IF NOT EXISTS bet (
                                   id        BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   user_id   BIGINT        NOT NULL,
                                   event_id  BIGINT        NOT NULL,
                                   market_id BIGINT        NOT NULL,
                                   winner_id BIGINT        NOT NULL,
                                   amount    DECIMAL(10,2) NOT NULL,
    status    VARCHAR(20)   NOT NULL DEFAULT 'PENDING'
    );

