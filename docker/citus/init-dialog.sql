CREATE TABLE IF NOT EXISTS dialog_messages (
    id UUID NOT NULL,
    dialog_id UUID NOT NULL,
    from_user_id UUID NOT NULL,
    to_user_id UUID NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (dialog_id, id)
);

SELECT create_distributed_table('dialog_messages', 'dialog_id');
