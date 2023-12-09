INSERT INTO categories VALUES (0, now(), 'Все', 'url', 'all', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (1, now(), 'Для дома', 'url', 'home', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (2, now(), 'Книги', 'url', 'books', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (3, now(), 'Одежда', 'url', 'clothes', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (4, now(), 'Животные', 'url', 'pets', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (5, now(), 'Спорт', 'url', 'sport', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (6, now(), 'Техника', 'url', 'appliance', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (7, now(), 'Еда', 'url', 'food', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (8, now(), 'Машина', 'url', 'auto', now()) ON CONFLICT DO NOTHING;
INSERT INTO categories VALUES (9, now(), 'Разное', 'url', 'other', now()) ON CONFLICT DO NOTHING;


INSERT INTO subcategories VALUES (1, now(), 'subcat desc', 'url', 'subcat name', now()) ON CONFLICT DO NOTHING;
INSERT INTO cities VALUES (1, now(), 'city_2', now()) ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('dc94023b-8658-42e6-bcdc-2c810feb07af', 'i am user', now(), 'user@mail.com', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_USER', now(), 'username') ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('0bae6c74-23bf-4703-873c-82ac8ca89f80', 'i am admin', now(), 'admin@mail.com', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_ADMIN', now(), 'admin') ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('60747caf-d37d-424b-9daf-30096fa2c061', 'There are some info', now(), 'thereis@mail.com', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_ADMIN', now(), 'other_username') ON CONFLICT DO NOTHING;

INSERT INTO items VALUES ('b0d569d5-5a7f-41d2-af3a-1cfe4478a97f', 'adr1', now(), 'item1', '81111111111', 'desc1', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', 'adr2', now(), 'item2', '81111111111', 'desc2', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'where it was located the locatino', now(), 'Item with long long lon long long long name', '81111111111', 'The description of the name', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr3', now(), 'item3', '81111111111', 'desc3', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES (gen_random_uuid(), 'adr5', now(), 'item_item', 'sdfbsdfbs', 'therdescdesc', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;


INSERT INTO item_images VALUES ('71a2c9da-dca7-4044-986d-1504eaa1ed13', now(), false, now(),'114cce42-278f-47cf-b551-dfbb0a6bd1d5') ON CONFLICT DO NOTHING;
INSERT INTO item_images VALUES ('074abbf3-33cb-49dc-b022-764704d5a935', now(), false, now(),'114cce42-278f-47cf-b551-dfbb0a6bd1d5') ON CONFLICT DO NOTHING;

INSERT INTO item_images VALUES ('09070b0b-bf77-4438-a868-aae569e10e0c', now(), false, now(),'b0d569d5-5a7f-41d2-af3a-1cfe4478a97f') ON CONFLICT DO NOTHING;
INSERT INTO item_images VALUES ('45239f5c-dee9-4672-9132-15dfad3868a1', now(), false, now(),'b0d569d5-5a7f-41d2-af3a-1cfe4478a97f') ON CONFLICT DO NOTHING;

INSERT INTO user_images VALUES ('60747caf-d37d-424b-9daf-30096fa2c061', now(), false, now(),'60747caf-d37d-424b-9daf-30096fa2c061') ON CONFLICT DO NOTHING;
INSERT INTO user_images VALUES ('dc94023b-8658-42e6-bcdc-2c810feb07af', now(), false, now(),'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;

