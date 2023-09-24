INSERT INTO categories VALUES (1, now(), 'cat desc', 'url', 'cat name', now()) ON CONFLICT DO NOTHING;
INSERT INTO subcategories VALUES (1, now(), 'subcat desc', 'url', 'subcat name', now()) ON CONFLICT DO NOTHING;
INSERT INTO cities VALUES (1, now(), 'city_2', now()) ON CONFLICT DO NOTHING;
INSERT INTO users VALUES (1, 'i am user', now(), 'mail', 'username', now()) ON CONFLICT DO NOTHING;
