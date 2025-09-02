-- Если для одного и того же вида был записан один и тот же момент
-- открытия в одном и том же месте дважды.

CREATE OR REPLACE FUNCTION check_unique_discovery()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM discovery
               WHERE species_id = NEW.species_id
               AND location_id = NEW.location_id
               AND discovery_date = NEW.discovery_date) THEN
        RAISE EXCEPTION 'Такое открытие уже существует для этого вида в этом месте';
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER unique_discovery_trigger
    BEFORE INSERT ON discovery
    FOR EACH ROW
    EXECUTE FUNCTION check_unique_discovery();
