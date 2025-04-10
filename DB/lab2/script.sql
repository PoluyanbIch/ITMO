/*
1
Сделать запрос для получения атрибутов из указанных таблиц, применив фильтры по указанным условиям:
Н_ТИПЫ_ВЕДОМОСТЕЙ, Н_ВЕДОМОСТИ.
Вывести атрибуты: Н_ТИПЫ_ВЕДОМОСТЕЙ.ИД, Н_ВЕДОМОСТИ.ДАТА.
Фильтры (AND):
a) Н_ТИПЫ_ВЕДОМОСТЕЙ.ИД = 3.
b) Н_ВЕДОМОСТИ.ДАТА = 2010-06-18.
Вид соединения: LEFT JOIN.
*/
SELECT "Н_ТИПЫ_ВЕДОМОСТЕЙ"."ИД", "Н_ВЕДОМОСТИ"."ДАТА"
FROM "Н_ТИПЫ_ВЕДОМОСТЕЙ"
LEFT JOIN "Н_ВЕДОМОСТИ" ON "Н_ТИПЫ_ВЕДОМОСТЕЙ"."ИД" = "Н_ВЕДОМОСТИ"."ТВ_ИД"
WHERE "Н_ТИПЫ_ВЕДОМОСТЕЙ"."ИД" = 3
AND "Н_ВЕДОМОСТИ"."ДАТА" = '2010-06-18';


/*
2
Сделать запрос для получения атрибутов из указанных таблиц, применив фильтры по указанным условиям:
Таблицы: Н_ЛЮДИ, Н_ОБУЧЕНИЯ, Н_УЧЕНИКИ.
Вывести атрибуты: Н_ЛЮДИ.ОТЧЕСТВО, Н_ОБУЧЕНИЯ.ЧЛВК_ИД, Н_УЧЕНИКИ.ИД.
Фильтры: (AND)
a) Н_ЛЮДИ.ИМЯ > Ярослав.
b) Н_ОБУЧЕНИЯ.НЗК = 001000.
c) Н_УЧЕНИКИ.НАЧАЛО > 2011-11-21.
Вид соединения: RIGHT JOIN.
*/
SELECT "Н_ЛЮДИ"."ОТЧЕСТВО", "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД", "Н_УЧЕНИКИ"."ИД"
FROM "Н_ЛЮДИ"
RIGHT JOIN "Н_ОБУЧЕНИЯ" ON "Н_ЛЮДИ"."ИД" = "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД"
RIGHT JOIN "Н_УЧЕНИКИ" ON "Н_ОБУЧЕНИЯ"."ЧЛВК_ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
WHERE "Н_ЛЮДИ"."ИМЯ" > 'Ярослав'
AND "Н_ОБУЧЕНИЯ"."НЗК" = '001000'
AND "Н_УЧЕНИКИ"."НАЧАЛО" > '2011-11-21';


/*
3
Вывести число студентов ФКТИУ, которые без ИНН.
Ответ должен содержать только одно число.
*/
SELECT COUNT(*)
FROM "Н_ЛЮДИ"
JOIN "Н_УЧЕНИКИ" ON "Н_ЛЮДИ"."ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
JOIN "Н_ПЛАНЫ" ON "Н_УЧЕНИКИ"."ПЛАН_ИД" = "Н_ПЛАНЫ"."ИД"
JOIN "Н_ОТДЕЛЫ" ON "Н_ПЛАНЫ"."ОТД_ИД" = "Н_ОТДЕЛЫ"."ИД"
WHERE "Н_ОТДЕЛЫ"."КОРОТКОЕ_ИМЯ" = 'КТиУ'
AND "Н_ЛЮДИ"."ИНН" IS NULL;


/*
4
Выдать различные фамилии преподавателей и число людей с каждой из этих фамилий, ограничив список фамилиями, встречающимися более 50 раз на кафедре вычислительной техники.
Для реализации использовать соединение таблиц.
*/
SELECT "Н_ЛЮДИ"."ФАМИЛИЯ", COUNT(*) AS "КОЛИЧЕСТВО"
FROM "Н_ЛЮДИ"
JOIN "Н_УЧЕНИКИ" ON "Н_ЛЮДИ"."ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
JOIN "Н_ПЛАНЫ" ON "Н_УЧЕНИКИ"."ПЛАН_ИД" = "Н_ПЛАНЫ"."ИД"
JOIN "Н_ОТДЕЛЫ" ON "Н_ПЛАНЫ"."ОТД_ИД" = "Н_ОТДЕЛЫ"."ИД"
WHERE "Н_ОТДЕЛЫ"."КОРОТКОЕ_ИМЯ" = 'ВТ'
GROUP BY "Н_ЛЮДИ"."ФАМИЛИЯ"
HAVING COUNT(*) > 50;


/*
5
Выведите таблицу со средними оценками студентов группы 4100 (Номер, ФИО, Ср_оценка),
 у которых средняя оценка не меньше максимальной оценк(е|и) в группе 3100.
*/
SELECT "Н_УЧЕНИКИ"."ГРУППА", "Н_ЛЮДИ"."ФАМИЛИЯ", "Н_ЛЮДИ"."ИМЯ", "Н_ЛЮДИ"."ОТЧЕСТВО", AVG(CAST("ОЦЕНКА" AS integer)) AS "СР_ОЦЕНКА"
FROM "Н_ЛЮДИ"
JOIN "Н_УЧЕНИКИ" ON "Н_ЛЮДИ"."ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
JOIN "Н_ВЕДОМОСТИ" ON "Н_УЧЕНИКИ"."ЧЛВК_ИД" = "Н_ВЕДОМОСТИ"."ЧЛВК_ИД"
WHERE "Н_УЧЕНИКИ"."ГРУППА" = '4100' AND "Н_ВЕДОМОСТИ"."ОЦЕНКА" NOT IN ('зачет', 'осв', 'незач', 'неявка', '99')
GROUP BY "Н_УЧЕНИКИ"."ГРУППА", "Н_ЛЮДИ"."ФАМИЛИЯ", "Н_ЛЮДИ"."ИМЯ", "Н_ЛЮДИ"."ОТЧЕСТВО"
HAVING AVG(CAST("ОЦЕНКА" AS integer)) >= (
    SELECT AVG(CAST("ОЦЕНКА" AS integer))
    FROM "Н_ВЕДОМОСТИ"
    JOIN "Н_УЧЕНИКИ" ON "Н_ВЕДОМОСТИ"."ЧЛВК_ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
    WHERE "Н_УЧЕНИКИ"."ГРУППА" = '3100' AND "Н_ВЕДОМОСТИ"."ОЦЕНКА" NOT IN ('зачет', 'осв', 'незач', 'неявка', '99')
    GROUP BY "Н_УЧЕНИКИ"."ЧЛВК_ИД"
    ORDER BY AVG(CAST("ОЦЕНКА" AS integer)) DESC
    LIMIT 1
);


/*
6
Получить список студентов, отчисленных до первого сентября 2012 года с очной или заочной формы обучения. В результат включить:
номер группы;
номер, фамилию, имя и отчество студента;
номер пункта приказа;
Для реализации использовать подзапрос с IN.
*/
SELECT "Н_УЧЕНИКИ"."ГРУППА", "Н_ЛЮДИ"."ИД", "Н_ЛЮДИ"."ФАМИЛИЯ", "Н_ЛЮДИ"."ИМЯ", "Н_ЛЮДИ"."ОТЧЕСТВО", "Н_УЧЕНИКИ"."П_ПРКОК_ИД"
FROM "Н_ЛЮДИ"
         JOIN "Н_УЧЕНИКИ" ON "Н_ЛЮДИ"."ИД" = "Н_УЧЕНИКИ"."ЧЛВК_ИД"
WHERE "Н_УЧЕНИКИ"."ПРИЗНАК" = 'отчисл'
  AND "Н_УЧЕНИКИ"."КОНЕЦ" < '2012-09-01'
  AND "Н_УЧЕНИКИ"."ВИД_ОБУЧ_ИД" IN (
    SELECT "ИД" FROM "Н_ФОРМЫ_ОБУЧЕНИЯ"
    WHERE "НАИМЕНОВАНИЕ" IN ('Очная', 'Заочная')
);


/*
7
Вывести список студентов, имеющих одинаковые отчества, но не совпадающие даты рождения.
*/
SELECT "Н_ЛЮДИ"."ОТЧЕСТВО", "Н_ЛЮДИ"."ФАМИЛИЯ", "Н_ЛЮДИ"."ИМЯ", "Н_ЛЮДИ"."ДАТА_РОЖДЕНИЯ"
FROM "Н_ЛЮДИ"
WHERE "Н_ЛЮДИ"."ОТЧЕСТВО" IN (
    SELECT "ОТЧЕСТВО"
    FROM "Н_ЛЮДИ"
    GROUP BY "ОТЧЕСТВО"
    HAVING COUNT(*) > 1
)
  AND EXISTS (
    SELECT 1
    FROM "Н_ЛЮДИ" AS L2
    WHERE L2."ОТЧЕСТВО" = "Н_ЛЮДИ"."ОТЧЕСТВО" AND L2."ОТЧЕСТВО" <> '.'
      AND L2."ДАТА_РОЖДЕНИЯ" <> "Н_ЛЮДИ"."ДАТА_РОЖДЕНИЯ"
)
ORDER BY "Н_ЛЮДИ"."ОТЧЕСТВО";