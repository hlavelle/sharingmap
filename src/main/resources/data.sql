INSERT INTO categories VALUES (0, now(), 'Все', 'all', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (1, now(), 'Для дома', 'home', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (2, now(), 'Одежда', 'clothes', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (3, now(), 'Еда', 'food', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (4, now(), 'Книги', 'books', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (5, now(), 'Детям', 'child', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (6, now(), 'Питомцам', 'pets', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (7, now(), 'Спорт', 'sport', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (8, now(), 'Техника', 'appliance', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (9, now(), 'Машина', 'auto', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (10, now(), 'Разное', 'other', now()) ON CONFLICT DO NOTHING;

INSERT INTO subcategories VALUES (1, now(), 'Отдам', 'url', 'give', now()) ON CONFLICT DO NOTHING;
