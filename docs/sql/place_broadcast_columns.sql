ALTER TABLE place
    ADD COLUMN featured_live_info_tv TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN featured_life_master TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN featured_baekban_trip TINYINT(1) NOT NULL DEFAULT 0;
