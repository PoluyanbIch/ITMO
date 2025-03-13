--- Найти все виды с их статусами
SELECT s.name AS species_name, st.name AS status
FROM Species s
JOIN Status st ON s.status_id = st.status_id;

---Найти все обнаружения с указанием вида, статуса и зоолога
SELECT s.name AS species_name, st.name AS status, z.name AS zoologist_name, d.discovery_date
FROM Discovery d
JOIN Fossil f ON d.fossil_id = f.fossil_id
JOIN Species s ON f.species_id = s.species_id
JOIN Status st ON s.status_id = st.status_id
JOIN Zoologist z ON d.zoologist_id = z.zoologist_id;


---Найти все ископаемые, относящиеся к вымершим видам
SELECT f.description, s.name AS species_name
FROM Fossil f
JOIN Species s ON f.species_id = s.species_id
JOIN Status st ON s.status_id = st.status_id
WHERE st.name = 'вымерший';