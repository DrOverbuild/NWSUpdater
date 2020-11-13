INSERT INTO user (email, phone, password)
VALUES
('jreddin1@cub.uca.edu', '+15015143557', AES_ENCRYPT('reallygoodpass!','r')),
('jasper@jasperreddin.com', '+15015143557', AES_ENCRYPT('reallygoodpass!','r')),
('lol@lol.com', '+15015143557', AES_ENCRYPT('reallygoodpass!','r'));