CREATE TABLE Status (
    status_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Classification (
    classification_id SERIAL PRIMARY KEY,
    family VARCHAR(100),
    order_name VARCHAR(100)
);

CREATE TABLE Location (
    location_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE Organization (
    organization_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address TEXT
);

CREATE TABLE Species (
    species_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    status_id INT REFERENCES Status(status_id) ON DELETE SET NULL,
    extinct_since VARCHAR(50),
    classification_id INT REFERENCES Classification(classification_id) ON DELETE SET NULL
);

CREATE TABLE Fossil (
    fossil_id SERIAL PRIMARY KEY,
    species_id INT REFERENCES Species(species_id) ON DELETE CASCADE,
    description TEXT
);

CREATE TABLE Zoologist (
    zoologist_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    organization_id INT REFERENCES Organization(organization_id) ON DELETE SET NULL,
    contact_details TEXT CHECK (contact_details ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

CREATE TABLE Discovery (
    discovery_id SERIAL PRIMARY KEY,
    fossil_id INT REFERENCES Fossil(fossil_id) ON DELETE CASCADE,
    zoologist_id INT REFERENCES Zoologist(zoologist_id) ON DELETE CASCADE,
    location_id INT REFERENCES Location(location_id) ON DELETE SET NULL,
    discovery_date VARCHAR(50),
    age VARCHAR(50),
    circumstances TEXT
);

CREATE TABLE ZoologistFossil (
    zoologist_id INT REFERENCES Zoologist(zoologist_id) ON DELETE CASCADE,
    fossil_id INT REFERENCES Fossil(fossil_id) ON DELETE CASCADE,
    PRIMARY KEY (zoologist_id, fossil_id)
);




INSERT INTO Status (name) VALUES
('вымерший'),
('обнаружен живым'),
('существует');

INSERT INTO Classification (family, order_name) VALUES
('Latimeriidae', 'Coelacanthiformes'),
('Burramyidae', 'Diprotodontia'),
('Pteropodidae', 'Chiroptera'),
('Felidae', 'Carnivora'),
('Canidae', 'Carnivora'),
('Elephantidae', 'Proboscidea');

INSERT INTO Location (name, description) VALUES
('Океан', 'Место, где был выловлен целакант'),
('Мельбурн', 'Город, где был найден австралийский горный карликовый опоссум'),
('Почта', 'Место, куда был отправлен экземпляр фруктовой летучей мыши'),
('Саванна', 'Место обитания слонов'),
('Лес', 'Место обитания кошек и собак'),
('Пещера', 'Место обнаружения древних ископаемых');

INSERT INTO Organization (name, address) VALUES
('Национальный музей естественной истории', 'Париж, Франция'),
('Университет Мельбурна', 'Мельбурн, Австралия'),
('Институт зоологии', 'Москва, Россия'),
('Центр изучения млекопитающих', 'Нью-Йорк, США');

INSERT INTO Species (name, status_id, extinct_since, classification_id) VALUES
('Целакант', 2, '65 миллионов лет назад', 1),
('Австралийский горный карликовый опоссум', 2, 'известен как ископаемое', 2),
('Фруктовая летучая мышь', 2, '10 тысяч лет назад', 3),
('Лев', 3, NULL, 4),
('Волк', 3, NULL, 5),
('Мамонт', 1, '10 тысяч лет назад', 6);

INSERT INTO Fossil (species_id, description) VALUES
(1, 'кость длиной 30 см'),
(1, 'чешуя целаканта'),
(2, 'череп опоссума'),
(3, 'кость крыла летучей мыши'),
(4, 'клык льва'),
(5, 'челюсть волка'),
(6, 'бивень мамонта'),
(6, 'кость ноги мамонта');

INSERT INTO Zoologist (name, organization_id, contact_details) VALUES
('Иван Иванов', 1, 'ivan@museum.com'),
('Мария Петрова', 2, 'maria@university.edu'),
('Алексей Смирнов', 3, 'alexey@zoology.ru'),
('Джон Доу', 4, 'john@mammals.org');

INSERT INTO Discovery (fossil_id, zoologist_id, location_id, discovery_date, age, circumstances) VALUES
(1, 1, 1, '1938 год', '65 миллионов лет', 'выловлен в океане'),
(2, 2, 2, '2000 год', 'неизвестно', 'найден в мусорном баке'),
(3, 3, 3, '2020 год', '10 тысяч лет', 'получен по почте'),
(4, 4, 4, '2015 год', '1 миллион лет', 'найден в саванне'),
(5, 1, 5, '2010 год', '500 тысяч лет', 'найден в лесу'),
(6, 2, 6, '2005 год', '10 тысяч лет', 'найден в пещере'),
(7, 3, 4, '1995 год', '50 тысяч лет', 'найден в саванне'),
(8, 4, 5, '1985 год', '100 тысяч лет', 'найден в лесу');

INSERT INTO ZoologistFossil (zoologist_id, fossil_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(4, 5),
(1, 6),
(2, 7),
(3, 8);