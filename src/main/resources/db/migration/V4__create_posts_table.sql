CREATE TABLE posts (
    id UUID PRIMARY KEY,
    author_user_id UUID NOT NULL REFERENCES users(id),
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_posts_author_user_id ON posts(author_user_id);
CREATE INDEX idx_posts_created_at ON posts(created_at DESC);
