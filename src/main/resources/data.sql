INSERT INTO categories VALUES (0, now(), 'Все', 'all', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (1, now(), 'Для дома', 'home', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (2, now(), 'Книги', 'books', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (3, now(), 'Одежда', 'clothes', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (4, now(), 'Животные', 'pets', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (5, now(), 'Спорт', 'sport', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (6, now(), 'Техника', 'appliance', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (7, now(), 'Еда', 'food', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (8, now(), 'Машина', 'auto', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (9, now(), 'Разное', 'other', now()) ON CONFLICT DO NOTHING;


INSERT INTO subcategories VALUES (1, now(), 'subcat desc', 'url', 'subcat name', now()) ON CONFLICT DO NOTHING;

INSERT INTO cities VALUES (1, now(), 'Москва', now()) ON CONFLICT DO NOTHING;
INSERT INTO cities VALUES (2, now(), 'Санкт-Петербург', now()) ON CONFLICT DO NOTHING;

INSERT INTO users VALUES ('dc94023b-8658-42e6-bcdc-2c810feb07af', 'i am user', now(), 'user@mail.com', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_USER', now(), 'username') ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('0bae6c74-23bf-4703-873c-82ac8ca89f80', 'i am admin', now(), 'admin@mail.com', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_ADMIN', now(), 'admin') ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('60747caf-d37d-424b-9daf-30096fa2c061', 'There are some info', now(), 'thereis@mail.com', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_ADMIN', now(), 'other_username') ON CONFLICT DO NOTHING;

INSERT INTO contacts VALUES (gen_random_uuid(), '89111111111', now(), 'TELEGRAM', now(), 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO contacts VALUES (gen_random_uuid(), '89222222222', now(), 'TELEGRAM', now(), 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO contacts VALUES (gen_random_uuid(), '89111111111', now(), 'PHONE', now(), '60747caf-d37d-424b-9daf-30096fa2c061') ON CONFLICT DO NOTHING;

INSERT INTO locations VALUES (1, now(), 'Шаболовская', 'METRO', now(), 1) ON CONFLICT DO NOTHING;
INSERT INTO locations VALUES (2, now(), 'Беляево', 'METRO', now(), 1) ON CONFLICT DO NOTHING;
INSERT INTO locations VALUES (3, now(), 'Площадь Восстания', 'METRO', now(), 2) ON CONFLICT DO NOTHING;
INSERT INTO locations VALUES (4, now(), 'Невский Проспект', 'METRO', now(), 2) ON CONFLICT DO NOTHING;

INSERT INTO items VALUES ('b0d569d5-5a7f-41d2-af3a-1cfe4478a97f', now(), 'item1', 'desc1', now(), 1, 1, 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', now(), 'item2', 'desc2', now(), 1, 1, 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'Item with long long lon long long long name', 'The description of the name', now(), 1, 1, 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item3', 'desc3', now(), 1, 1, 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item4', 'desc4', now(), 1, 1, 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item4', 'desc4', now(), 1, 1, 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item4', 'desc4', now(), 1, 1, 'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item4', 'desc4', now(), 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item4', 'desc4', now(), 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item4', 'desc4', now(), 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item4', 'desc4', now(), 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), now(), 'item_item', 'therdescdesc', now(), 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;


INSERT INTO item_images VALUES ('71a2c9da-dca7-4044-986d-1504eaa1ed13', now(), false, now(),'114cce42-278f-47cf-b551-dfbb0a6bd1d5') ON CONFLICT DO NOTHING;
INSERT INTO item_images VALUES ('074abbf3-33cb-49dc-b022-764704d5a935', now(), false, now(),'114cce42-278f-47cf-b551-dfbb0a6bd1d5') ON CONFLICT DO NOTHING;

INSERT INTO item_images VALUES ('09070b0b-bf77-4438-a868-aae569e10e0c', now(), false, now(),'b0d569d5-5a7f-41d2-af3a-1cfe4478a97f') ON CONFLICT DO NOTHING;
INSERT INTO item_images VALUES ('45239f5c-dee9-4672-9132-15dfad3868a1', now(), false, now(),'b0d569d5-5a7f-41d2-af3a-1cfe4478a97f') ON CONFLICT DO NOTHING;

INSERT INTO user_images VALUES ('60747caf-d37d-424b-9daf-30096fa2c061', now(), false, now(),'60747caf-d37d-424b-9daf-30096fa2c061') ON CONFLICT DO NOTHING;
INSERT INTO user_images VALUES ('dc94023b-8658-42e6-bcdc-2c810feb07af', now(), false, now(),'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;

INSERT INTO item_category VALUES ('b0d569d5-5a7f-41d2-af3a-1cfe4478a97f', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_category VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', 5) ON CONFLICT DO NOTHING;
INSERT INTO item_category VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', 6) ON CONFLICT DO NOTHING;
INSERT INTO item_category VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', 2) ON CONFLICT DO NOTHING;

INSERT INTO item_location VALUES ('b0d569d5-5a7f-41d2-af3a-1cfe4478a97f', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', 1) ON CONFLICT DO NOTHING;
INSERT INTO item_location VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', 2) ON CONFLICT DO NOTHING;
