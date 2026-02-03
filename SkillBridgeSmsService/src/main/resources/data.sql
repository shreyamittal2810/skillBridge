-- This script runs after JPA schema update
-- If student_id column still exists in applications table, this will fail silently on restart
-- The column needs to be manually dropped via MySQL client
-- ALTER TABLE applications DROP COLUMN student_id;

-- No-op statement to prevent Spring Boot from treating this as empty
SELECT 1;
