
INSERT INTO oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, authorities, access_token_validity, refresh_token_validity, autoapprove)
VALUES ('bct_test_client', 'api-resource', '{bcrypt}$2a$10$qOcZXTToYEANwSaa9VRZX.Pjr4nFT.F2aCCWHg0J2hYqD2G67pML.', 'read,write', 'password,refresh_token', '', '43200', '2592000', 'false');

