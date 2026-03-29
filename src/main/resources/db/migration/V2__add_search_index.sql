CREATE INDEX idx_users_first_name_second_name ON users (first_name text_pattern_ops, second_name text_pattern_ops);
