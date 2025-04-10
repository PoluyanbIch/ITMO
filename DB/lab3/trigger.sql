-- Функция
CREATE FUNCTION update_extinct_since()
RETURNS TRIGGER AS $$
BEGIN
UPDATE Species
SET extinct_since = 'discovered recently'
WHERE species_id = NEW.species_id AND extinct_since IS NULL;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Триггер
CREATE TRIGGER trg_update_extinct_since
    AFTER INSERT ON Fossil
    FOR EACH ROW
    EXECUTE FUNCTION update_extinct_since();
