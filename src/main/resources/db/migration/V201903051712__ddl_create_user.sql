CREATE TABLE IF NOT EXISTS auth_user
(
  id      BIGSERIAL PRIMARY KEY,
  name    VARCHAR(50),
  role_id BIGINT NULL
)
