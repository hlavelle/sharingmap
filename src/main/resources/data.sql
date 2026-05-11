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

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Электрический чайник', 'Отдам рабочий электрический чайник для кухни', 1, 1, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Блендер', 'Кухонный прибор для смузи и супов, работает нормально', 1, 1, '97671179-0000-4000-8000-000000000001', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Микроволновка', 'Ищу недорогую микроволновую печь или другой кухонный прибор', 1, 2, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('11111111-2222-4333-8444-555555555555', 'Настольная лампа', 'Отдам лампу для рабочего стола, светит ярко', 1, 1, '97671179-0000-4000-8000-000000000001', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('22222222-3333-4444-8555-666666666666', 'Детская коляска', 'Отдам детскую коляску после одного ребенка', 1, 1, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('33333333-4444-4555-8666-777777777777', 'Куртка зимняя', 'Ищу теплую зимнюю куртку размера M', 1, 2, '97671179-0000-4000-8000-000000000001', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('44444444-5555-4666-8777-888888888888', 'Поводок для собаки', 'Отдам крепкий поводок для небольшой собаки', 1, 1, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('55555555-6666-4777-8888-999999999999', 'Футбольный мяч', 'Отдам футбольный мяч для тренировок', 1, 1, '97671179-0000-4000-8000-000000000001', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('66666666-7777-4888-8999-aaaaaaaaaaaa', 'Велосипед', 'Ищу городской велосипед в рабочем состоянии', 1, 2, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('77777777-8888-4999-8aaa-bbbbbbbbbbbb', 'Пакет круп и макарон', 'Отдам продукты длительного хранения: гречка, рис, макароны', 1, 1, '97671179-0000-4000-8000-000000000001', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('88888888-9999-4aaa-8bbb-cccccccccccc', 'Смартфон Android', 'Ищу рабочий смартфон Android для связи и мессенджеров', 1, 2, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('99999999-aaaa-4bbb-8ccc-dddddddddddd', 'Конструктор LEGO', 'Отдам детский конструктор с деталями и фигурками', 1, 1, '97671179-0000-4000-8000-000000000001', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO items (id, item_name, text, city_id, subcategory_id, user_id, is_gifted_onsm, state, created_at, updated_at)
VALUES ('abababab-bbbb-4ccc-8ddd-eeeeeeeeeeee', 'Чемодан', 'Отдам большой чемодан на колесиках для поездок', 1, 1, '11111111-1111-1111-1111-111111111111', false, 'ACTIVE', now(), now())
ON CONFLICT DO NOTHING;

INSERT INTO item_category (item_id, category_id) VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 2) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc', 4) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('dddddddd-dddd-dddd-dddd-dddddddddddd', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('ffffffff-ffff-ffff-ffff-ffffffffffff', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('11111111-2222-4333-8444-555555555555', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('22222222-3333-4444-8555-666666666666', 5) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('33333333-4444-4555-8666-777777777777', 2) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('44444444-5555-4666-8777-888888888888', 6) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('55555555-6666-4777-8888-999999999999', 7) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('66666666-7777-4888-8999-aaaaaaaaaaaa', 9) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('77777777-8888-4999-8aaa-bbbbbbbbbbbb', 3) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('88888888-9999-4aaa-8bbb-cccccccccccc', 8) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('99999999-aaaa-4bbb-8ccc-dddddddddddd', 5) ON CONFLICT DO NOTHING;
INSERT INTO item_category (item_id, category_id) VALUES ('abababab-bbbb-4ccc-8ddd-eeeeeeeeeeee', 10) ON CONFLICT DO NOTHING;

INSERT INTO item_location (item_id, location_id) VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('dddddddd-dddd-dddd-dddd-dddddddddddd', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('ffffffff-ffff-ffff-ffff-ffffffffffff', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('11111111-2222-4333-8444-555555555555', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('22222222-3333-4444-8555-666666666666', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('33333333-4444-4555-8666-777777777777', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('44444444-5555-4666-8777-888888888888', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('55555555-6666-4777-8888-999999999999', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('66666666-7777-4888-8999-aaaaaaaaaaaa', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('77777777-8888-4999-8aaa-bbbbbbbbbbbb', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('88888888-9999-4aaa-8bbb-cccccccccccc', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('99999999-aaaa-4bbb-8ccc-dddddddddddd', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location (item_id, location_id) VALUES ('abababab-bbbb-4ccc-8ddd-eeeeeeeeeeee', 1) ON CONFLICT DO NOTHING;
