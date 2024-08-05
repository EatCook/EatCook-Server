CREATE INDEX user_email_idx ON itcook_user (email, user_state);
CREATE INDEX post_user_id_flag_created_idx ON post (user_id, post_flag, created_at desc);
CREATE INDEX post_flag_last_modified_idx ON post (post_flag, last_modified_at desc);
CREATE INDEX notification_user_id_idx on notification (user_id);