CREATE TABLE friends (
    user_id UUID NOT NULL REFERENCES users(id),
    friend_user_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, friend_user_id)
);

CREATE INDEX idx_friends_user_id ON friends(user_id);
CREATE INDEX idx_friends_friend_user_id ON friends(friend_user_id);
