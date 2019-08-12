insert into coupon
(id, discount_amount, is_used)
values
  (1, 1000, false),
  (2, 1000, false),
  (3, 1000, false),
  (4, 1000, false);

insert into orders
(id, price, coupon_id)
values
  (1, 10000, 1),
  (2, 20000, 2),
  (3, 30000, 3),
  (4, 40000, 4);

insert into account
  (address, detail_address, zip_code, created_at, update_at, email, first_name, last_name, password_expiration_date, password_failed_count, password_time_to_live, password, id)
values
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:02', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test002@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:01:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 2),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:01', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test001@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 1),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:03', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test003@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:02:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 3),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:04', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test004@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:03:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 4),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:05', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test005@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:04:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 5),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:06', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test006@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:05:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 6),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:07', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test007@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:06:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 7),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:08', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test008@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:07:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 8),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:09', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test009@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:08:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 9),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:10', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test010@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:09:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 10),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:11', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test011@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:10:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 11),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:12', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test012@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:11:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 12),
  ('address1', 'address2', '002', STR_TO_DATE('2019-01-20 00:00:13', '%Y-%m-%d %H:%i:%s'), STR_TO_DATE('2019-01-20 00:00:00', '%Y-%m-%d %H:%i:%s'), 'test013@test.com', 'first', 'last', STR_TO_DATE('2020-01-20 00:12:00', '%Y-%m-%d %H:%i:%s'), 0, 1209604, '$2a$10$tI3Y.nhgC.73LYCszoCaLu3nNEIM4QgeACiNseWlvr1zjrV5NCCs6', 13);