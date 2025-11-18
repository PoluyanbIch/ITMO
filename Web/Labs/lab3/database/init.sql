CREATE TABLE hits (
      id SERIAL PRIMARY KEY,
      x NUMERIC NOT NULL,
      y NUMERIC NOT NULL,
      r NUMERIC NOT NULL,
      is_hit BOOLEAN NOT NULL,
      execution_time DOUBLE PRECISION NOT NULL,
      current_datetime TIMESTAMP DEFAULT NOW() NOT NULL
);
