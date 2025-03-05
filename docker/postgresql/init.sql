DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'course_db') THEN
      CREATE DATABASE course_db;
   END IF;
END $$;
