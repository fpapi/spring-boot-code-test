CREATE TABLE IF NOT EXISTS leaderboards (
    uuid UUID DEFAULT RANDOM_UUID() NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

ALTER TABLE leaderboard ADD board_id UUID;

INSERT INTO leaderboards(name) VALUES ('DEFAULT');

UPDATE leaderboard SET board_id = (SELECT uuid FROM leaderboards WHERE name = 'DEFAULT');

ALTER TABLE leaderboard ALTER COLUMN board_id UUID NOT NULL;

ALTER TABLE leaderboard ADD UNIQUE(board_id, nick);