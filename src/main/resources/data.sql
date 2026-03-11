INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (0, now(), 'Все', 'all', now(), 0) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (1, now(), 'Для дома', 'home', now(), 1) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (2, now(), 'Одежда', 'clothes', now(), 2) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (3, now(), 'Еда', 'food', now(), 3) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (4, now(), 'Книги', 'books', now(), 4) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (5, now(), 'Детям', 'child', now(), 5) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (6, now(), 'Питомцам', 'pets', now(), 6) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (7, now(), 'Спорт', 'sport', now(), 7) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (8, now(), 'Техника', 'appliance', now(), 8) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (9, now(), 'Машина', 'auto', now(), 9) ON CONFLICT DO NOTHING;
INSERT INTO categories (id, created_at, category_name, description, updated_at, position) VALUES (10, now(), 'Разное', 'other', now(), 10) ON CONFLICT DO NOTHING;

INSERT INTO subcategories (id, category_name, description, image_url, created_at, updated_at) VALUES (1, 'Отдам', 'give', 'url', now(), now()) ON CONFLICT DO NOTHING;
INSERT INTO subcategories (id, category_name, description, image_url, created_at, updated_at) VALUES (2, 'Ищут', 'get', 'url', now(), now()) ON CONFLICT DO NOTHING;

INSERT INTO cities (id, created_at, city_name, updated_at) VALUES (1, now(), 'Москва', now()) ON CONFLICT DO NOTHING;
INSERT INTO cities (id, created_at, city_name, updated_at) VALUES (2, now(), 'Санкт-Петербург', now()) ON CONFLICT DO NOTHING;

INSERT INTO locations (id, created_at, location_name, type, updated_at, city_id) VALUES (1, now(), 'Центр', 'METRO', now(), 1) ON CONFLICT DO NOTHING;
INSERT INTO locations (id, created_at, location_name, type, updated_at, city_id) VALUES (2, now(), 'Невский проспект', 'METRO_SPB', now(), 2) ON CONFLICT DO NOTHING;

INSERT INTO users (id, username, email, role, password, enabled, locked, gifted_items, created_at, updated_at)
VALUES ('11111111-1111-1111-1111-111111111111', 'demo', 'demo@local', 'ROLE_USER', 'demo', true, false, 0, now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO users (id, username, email, role, password, enabled, locked, gifted_items, created_at, updated_at)
VALUES (
    '97671179-0000-4000-8000-000000000001',
    'local_tester',
    'z97671179@gmail.com',
    'ROLE_USER',
    '$2a$06$yTMekhJPSVy9diPOW4pPXeGqw8M72O.FLCVMFWemXqh2/BmndtETy',
    true,
    false,
    0,
    now(),
    now()
)
ON CONFLICT (email) DO UPDATE
SET username = EXCLUDED.username,
    password = EXCLUDED.password,
    enabled = true,
    locked = false,
    role = 'ROLE_USER',
    updated_at = now();

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Тостер', 'Рабочий тостер, отдам бесплатно', 1, 1, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Кроссовки 42', 'Ищу кроссовки 42 размера в хорошем состоянии', 1, 2, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Книга по Kotlin', 'Отдам книгу по Kotlin в хорошем состоянии', 1, 1, '97671179-0000-4000-8000-000000000001', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO item_category (item_id, category_id) VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 2) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc', 4) ON CONFLICT DO NOTHING;

INSERT INTO item_location (item_id, location_id) VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc', 1) ON CONFLICT DO NOTHING;
