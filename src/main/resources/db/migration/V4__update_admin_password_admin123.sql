-- Update admin password to admin123
UPDATE sys_user 
SET password = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' 
WHERE username = 'admin';

-- Verify the update
SELECT username, password FROM sys_user WHERE username = 'admin';
