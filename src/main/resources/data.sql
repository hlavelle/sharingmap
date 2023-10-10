INSERT INTO categories VALUES (1, now(), 'cat desc', 'url', 'cat name', now()) ON CONFLICT DO NOTHING;
INSERT INTO subcategories VALUES (1, now(), 'subcat desc', 'url', 'subcat name', now()) ON CONFLICT DO NOTHING;
INSERT INTO cities VALUES (1, now(), 'city_2', now()) ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('dc94023b-8658-42e6-bcdc-2c810feb07af', 'i am user', now(), 'mail', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_USER', now(), 'username') ON CONFLICT DO NOTHING;
INSERT INTO users VALUES ('0bae6c74-23bf-4703-873c-82ac8ca89f80', 'i am admin', now(), 'admin', true, false, '$2a$12$X3Ci24kRUjPtChLrk6KTaOR02PooC2FnIe76QdhwRCggaXSvci1YO', 'ROLE_ADMIN', now(), 'admin') ON CONFLICT DO NOTHING;

INSERT INTO items VALUES ('bda028eb-20d1-48b2-81d5-8d4e789e0caf', 'adr1', now(), 'item1', '81111111111', 'desc1', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES ('114cce42-278f-47cf-b551-dfbb0a6bd1d5', 'adr2', now(), 'item2', '81111111111', 'desc2', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES ('f0cdca24-ad16-40da-9d9a-cee8a118cbb8', 'adr3', now(), 'item3', '81111111111', 'desc3', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES ('bee95e93-a62c-4e43-a7a9-47c2132656af', 'adr4', now(), 'item4', '81111111111', 'desc4', now(), 1, 1, 1,'dc94023b-8658-42e6-bcdc-2c810feb07af') ON CONFLICT DO NOTHING;

INSERT INTO items VALUES ('f03b8b91-4fae-443f-b556-8c8b748a5e2c', 'adr5', now(), 'item5', '81111111111', 'desc5', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;
INSERT INTO items VALUES ('b539cb1a-a1b7-4ec3-a02b-2bcbdbff1040', 'adr6', now(), 'item6', '81111111111', 'desc6', now(), 1, 1, 1,'0bae6c74-23bf-4703-873c-82ac8ca89f80') ON CONFLICT DO NOTHING;