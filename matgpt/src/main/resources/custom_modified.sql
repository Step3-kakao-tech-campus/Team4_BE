drop table if exists category_tb cascade;
drop table if exists coin_earning_history_tb cascade;
drop table if exists coin_tb cascade;
drop table if exists coin_usage_history_tb cascade;
drop table if exists food_tb cascade;
drop table if exists gpt_guidance_tb cascade;
drop table if exists gpt_review_tb cascade;
drop table if exists image_tb cascade;
drop table if exists likereview_tb cascade;
drop table if exists likestore_tb cascade;
drop table if exists refresh_token cascade;
drop table if exists review_tb cascade;
drop table if exists store_tb cascade;
drop table if exists sub_category_tb cascade;
drop table if exists tag_tb cascade;
drop table if exists user_tb cascade;

create table category_tb (
                             id bigint generated by default as identity,
                             name varchar(255) not null unique,
                             primary key (id)
);
create table coin_earning_history_tb (
                                         amount integer not null,
                                         balance integer not null,
                                         coin_id bigint,
                                         earned_at timestamp(6),
                                         id bigint generated by default as identity,
                                         primary key (id)
);
create table coin_tb (
                         balance integer not null,
                         id bigint generated by default as identity,
                         user_id bigint unique,
                         primary key (id)
);
create table coin_usage_history_tb (
                                       amount integer not null,
                                       balance integer not null,
                                       coin_id bigint,
                                       id bigint generated by default as identity,
                                       used_at timestamp(6),
                                       primary key (id)
);
create table food_tb (
                         nums_of_review integer,
                         avg_rating float(53),
                         created_at timestamp(6) not null,
                         id bigint generated by default as identity,
                         store_id bigint,
                         updated_at timestamp(6) not null,
                         food_name varchar(255) not null,
                         created_by varchar(255),
                         updated_by varchar(255),
                         primary key (id)
);
create table gpt_guidance_tb (
                                 created_at timestamp(6) not null,
                                 id bigint generated by default as identity,
                                 user_id bigint not null,
                                 content varchar(1000) not null,
                                 primary key (id)
);
create table gpt_review_tb (
                               last_nums_of_review integer not null,
                               created_at timestamp(6),
                               id bigint generated by default as identity,
                               store_id bigint not null,
                               updated_at timestamp(6),
                               content varchar(1000) not null,
                               summary_type varchar(255),
                               primary key (id)
);
create table image_tb (
                          id bigint generated by default as identity,
                          review_id bigint,
                          url varchar(255) not null,
                          primary key (id)
);
create table likereview_tb (
                               id bigint generated by default as identity,
                               review_id bigint,
                               user_id bigint,
                               primary key (id)
);
create table likestore_tb (
                              id bigint generated by default as identity,
                              store_id bigint,
                              user_id bigint,
                              primary key (id)
);
create table refresh_token (
                               refresh_key varchar(255) not null,
                               refresh_value varchar(255),
                               primary key (refresh_key)
);
create table review_tb (
                           cost_per_person integer,
                           people_count integer,
                           rating integer,
                           recommend_count integer,
                           total_price integer,
                           created_at timestamp(6) not null,
                           id bigint generated by default as identity,
                           store_id bigint,
                           updated_at timestamp(6) not null,
                           user_id bigint not null,
                           content varchar(1000) not null,
                           review_uuid varchar(255) not null unique,
                           created_by varchar(255),
                           updated_by varchar(255),
                           primary key (id)
);
create table store_tb (
                          avg_cost_per_person integer,
                          avg_visit_count float(53),
                          latitude float(53) not null,
                          longitude float(53) not null,
                          nums_of_review integer not null,
                          avg_rating float(53) not null,
                          id bigint generated by default as identity,
                          sub_category_id bigint,
                          address varchar(255) not null,
                          business_hours varchar(255) not null,
                          name varchar(255) not null,
                          phone_number varchar(255) not null,
                          store_image_url varchar(255),
                          primary key (id)
);
create table sub_category_tb (
                                 category_id bigint,
                                 id bigint generated by default as identity,
                                 name varchar(255),
                                 primary key (id)
);
create table tag_tb (
                        locationx float(53),
                        locationy float(53),
                        menu_rating integer,
                        tag_name varchar(255),
                        food_id bigint,
                        id bigint generated by default as identity,
                        image_id bigint,
                        primary key (id)
);
create table user_tb (
                         age_group tinyint check (age_group between 0 and 6),
                         id bigint generated by default as identity,
                         email varchar(255) not null,
                         gender varchar(255) check (gender in ('MALE','FEMALE','UNKNOWN')),
                         locale varchar(255) check (locale in ('CHINESE','SIMPLIFIED_CHINESE','TRADITIONAL_CHINESE','FRANCE','GERMANY','ITALY','JAPAN','KOREA','UK','US','CANADA','CANADA_FRENCH')),
                         name varchar(255),
                         password varchar(255) not null,
                         profile_image_url varchar(255),
                         primary key (id)
);

create index IDXfps10sr0ew01vss6ernhg3xrl
    on sub_category_tb (category_id);

alter table if exists coin_earning_history_tb
    add constraint FKe3ro8rydi9csy1d4w4yn4m9g3
    foreign key (coin_id)
    references coin_tb;

alter table if exists coin_tb
    add constraint FKnk3tam03d88410kxx2n1kwbnr
    foreign key (user_id)
    references user_tb;

alter table if exists coin_usage_history_tb
    add constraint FKlceug82c102saky2j3nn1fs0h
    foreign key (coin_id)
    references coin_tb;

alter table if exists food_tb
    add constraint FKb9gwt0he3maew2mbkrp01kik4
    foreign key (store_id)
    references store_tb;


alter table if exists gpt_guidance_tb
    add constraint FKr991god6vkcxr2en3taemdiat
    foreign key (user_id)
    references user_tb;

alter table if exists gpt_review_tb
    add constraint FK1bwgd17car7kt7ecir5iojnkn
    foreign key (store_id)
    references store_tb;

alter table if exists image_tb
    add constraint FKi6tnyyjksl98uleo4c1nlqc98
    foreign key (review_id)
    references review_tb;

alter table if exists likereview_tb
    add constraint FKhqqqk7vf0rbx8lqluspc1npif
    foreign key (review_id)
    references review_tb;

alter table if exists likereview_tb
    add constraint FKr3vsccpdc0kjf1f6bm639f8oj
    foreign key (user_id)
    references user_tb;

alter table if exists likestore_tb
    add constraint FKj2k1efhi83nmsfdl9nip28wj3
    foreign key (store_id)
    references store_tb;

alter table if exists likestore_tb
    add constraint FK6cjo4wqumevkpbwbdfek1lecy
    foreign key (user_id)
    references user_tb;

alter table if exists review_tb
    add constraint FKo2d6eltlplc02ckofk164kmq9
    foreign key (store_id)
    references store_tb;

alter table if exists review_tb
    add constraint FK3po4getvgqcrdfofwpq8ar8cb
    foreign key (user_id)
    references user_tb;

alter table if exists store_tb
    add constraint FK91imrex8mafierjwiax0qrx4h
    foreign key (sub_category_id)
    references sub_category_tb;

alter table if exists sub_category_tb
    add constraint FKlmkixhn6ewux6ju2mny12pgom
    foreign key (category_id)
    references category_tb;

alter table if exists tag_tb
    add constraint FKtnb5i40unye1d3gd6rwee049r
    foreign key (food_id)
    references food_tb;

alter table if exists tag_tb
    add constraint FKiw1bam0yky4pui77q7p1u6h2w
    foreign key (image_id)
    references image_tb;

-- Category Table Initalization
INSERT INTO category_tb (name) VALUES ('KOREAN');
INSERT INTO category_tb (name) VALUES ('CHINESE');
INSERT INTO category_tb (name) VALUES ('DESSERT');
INSERT INTO category_tb (name) VALUES ('JAPANESE');


-- User Table Initalization
INSERT INTO user_tb (AGE_GROUP, ID, EMAIL, profile_image_url, GENDER, NAME, LOCALE, PASSWORD)
VALUES  (2, 1, 'nstgic@gmail.com', '','FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 2, 'female@gmail.com', '','FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd2','FRANCE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2 ,3,  'sk980919@kakao.com','','MALE', '364ea4bc-65b6-4f27-8682-61ce58896898', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 4, 'user4@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 5, 'user5@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 6, 'user6@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 7, 'user7@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 8, 'user8@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 9, 'user9@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 10, 'user10@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7'),
        (2, 11, 'user110@gmail.com', '', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7');


-- DetailedCategory Table Initialization
INSERT INTO sub_category_tb (id, name, category_id) VALUES
                                                        (1, '설렁탕/곰탕', 1),
                                                        (2, '중식당', 2),
                                                        (3, '디저트/카페', 3),
                                                        (4, '칼국수/만두', 1),
                                                        (5, '전', 1),
                                                        (6, '돼지고기', 1),
                                                        (7, '칼국수', 1),
                                                        (8, '국밥', 1),
                                                        (9, '퓨전', 1),
                                                        (10, '짜장면', 2),
                                                        (11, '스시', 4);


INSERT INTO store_tb
(id, name, PHONE_NUMBER, address, business_Hours, latitude, longitude, avg_Cost_Per_Person, avg_Visit_Count, nums_Of_Review, avg_Rating, sub_category_id)
VALUES
    (1, '미성옥', '02-776-8929', '서울 중구 명동길 25-11', '06:00 - 21:00', 37.5640065, 126.983556, 11000, 3, 16, 3.5,1),
    (2, '딘타이펑 명동점', '02-3789-2778', '서울 중구 명동7길 13 명동증권빌딩', '11:00 - 21:00', 37.5643309, 126.984133, 18000, 4, 8, 3.8,2),
    (3, '어반플랜트 명동', '0507-1480-0154', '서울 중구 명동8나길 38 1층', '09:00 - 21:00', 37.5614752, 126.982830, 7000, 4, 12, 4.2,3),
    (4, '명동교자', '0507-1443-3525', '서울 중구 명동10길 10 명동교자', '10:30 - 21:00', 37.5634232, 126.9850928, 10000, 3, 12, 4.0,4),
    (5, '서울지짐이', '02-3789-2778', '서울 중구 명동9가길10 1, 2층', '11:00 - 21:00', 37.5650588, 126.9840605, 18000, 4, 81, 3.0,5),
    (6, '흑돈가 명동점', '02-3789-2778', '서울 중구 명동7길 21', '11:00 - 21:00', 37.56471, 126.9838683, 18000, 4, 18, 4.4,6),
    (7, '박대감닭한마리', '02-3789-2778', '서울 중구 명동7길 21', '11:00 - 21:00', 37.56471, 126.9812345, 18000, 4, 28, 3.3,7),
    (8, '음식점8', '02-3789-2778', '서울 중구 명동', '11:00 - 21:00', 37.568876, 126.9823532, 18000, 4, 38, 3.9,8),
    (9, '음식점9', '02-3789-2778', '서울 중구 명동7길', '11:00 - 21:00', 37.56645, 126.988566, 18000, 4, 28, 2.7,9),
    (10, '음식점10', '02-3789-2778', '서울 중구 명동7길 13', '11:00 - 21:00', 37.566005, 126.9824525, 18000, 4, 548, 2.0,10),
    (11, '음식점11', '02-3789-2778', '서울 중구', '11:00 - 21:00', 37.56313, 126.980006, 18000, 4, 23, 4.8,11);



-- LikeStore Table Initalization
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (1,1,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (2,2,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (3,3,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (4,7,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (5,1,2);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (6,2,2);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (7,5,2);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (8,6,2);


-- Review Table Initalization

-- Store1에 User 1~10이 작성한 리뷰 11개 (reviewId: 1~11)
INSERT INTO review_tb
(store_id, user_id, content, rating, review_uuid, recommend_count, total_price, cost_per_person, people_count, created_at, updated_at, created_by, updated_by)
VALUES
    (1, 1, '집에서 한우로 끓여먹는 곰탕과도 비슷한 색과 국물입니다. 고기 한입 베어물자마자 한우에서만 느껴지는 고소한 육즙과 풍미가 느껴져서 설렁탕을 먹는내내 우와 ㅜㅜ 하면서 먹었습니다. 더불어 나오는 깍두기도 직접 담그신것 같았는데, 화룡정점이었습니다. 아시안 게임 국대분들 귀국하고 메달 목에 거시고 단체 회식 하고 계셨습니다^^ 엄청난 영양 보신 한우 설렁탕!! 명동 가면 꼭 가볼만합니다!', 5,'uuiduuiduuiduuid001', 2, 50000, 25000, 2, now(), now(), 1, 1),
    (1, 2, '참말로 맛있네용', 4,'uuiduuiduuiduuid002', 3, 30000, 150000, 2, now(), now(), 2, 2),
    (1, 3, '단출한 메뉴에 집중해서 좋아요. 늘 혼자 가서 수육은 먹어보지 못해 아쉽네요.', 5,'uuiduuiduuiduuid003', 1, 50000, 25000, 2, now(), now(), 3, 3),
    (1, 4, '1966년부터 이어온 전통있는곳이지만 숨겨진 히든 플레이스 같은 곳이라 명동에서도 숨은 골목으로 찾아 들어가야한다. 우리나라에도 이런 전통 있는 국내산 한우로만 만든 설렁탕 집이 유지되고 있어서 감사할정도! 고기나 특유의 잡내 조차 느껴지지 않게 맑은 국물 거기에 말도 안되는 배추김치와 깍두기의 맛은 한국 김치가 원조이자 세계 제일임을 느끼게 해 줄정도다. 일본인들이 정말 많이 찾아온다 그들도 정통을 아는거지... 근데 진짜 너무 맛있음 여긴.... 동네라면 매일 아침 먹고 출근하고 싶을 정도임.... 무려 새벽 6시에 오픈하는 곳!!!!', 5,'uuiduuiduuiduuid004', 0, 50000, 25000, 2, now(), now(), 4, 4),
    (1, 5, '설렁탕과 수육 맛집 인정^^
명동에 나갔다가 맛있다길래 찾아갔다.
큰길에서 벗어나 뒷골목에 있는데도
어떻게들 알고 찾아오는지.. ㅎ
입구보다 매장안에 들어서니 꽤 넓다.
완전 오래된 노포는 아니고 1966년에 오픈한 가게라고~~~
2명인데 양이 많을것 같아서
설렁탕과 수육하나를 주문했더니,
국수넣은 국물한그릇을 덤으로 더 주시는..
인심까지 훈훈~~~♡
수육은 소고기의 부위별로 나오는데,
살코기가 팍팍하지않고 맛있다.
기름도 쫄깃한게...
이런맛 처음~~~♡♡♡
설렁탕 국물도 단백하니 맛이 좋았다.
오랜만에 든든한 점심을 먹었더니
기분이 좋다 ㅎㅎ', 5,'uuiduuiduuiduuid005', 0, 50000, 25000, 2, now(), now(), 5, 5),
    (1, 6, '처음 왔는데 무조건 특으로 맛납니다^^', 5,'uuiduuiduuiduuid006', 0, 50000, 25000, 2, now(), now(), 6, 6),
    (1, 7, '잡내없고 깔끔해서 먹고나도 개운해요!', 5,'uuiduuiduuiduuid007', 1, 50000, 25000, 2, now(), now(), 7, 7),
    (1, 8, '수육 진짜 최고에요,,, 꼭 드세요ㅠㅠ', 5,'uuiduuiduuiduuid008', 0, 50000, 25000, 2, now(), now(), 8, 8),
    (1, 9, '주말 아침에 방문했는데일본관광객들이 많이 오네요~^^
아침 든든하게 잘해결했어요~', 5,'uuiduuiduuiduuid009', 3, 50000, 25000, 2, now(), now(), 9, 9),
    (1, 10, '아주 맛집이에요 명동에 또 온다면 다시 오고싶어요
수육 첫점부터 맛있었어요', 5, 'uuiduuiduuiduuid010', 0, 50000, 25000, 2, now(), now(), 10, 10),
    (1, 1, '단출한 메뉴에 집중해서 좋아요. 늘 혼자 가서 수육은 먹어보지 못해 아쉽네요.', 5, 'uuiduuiduuiduuid011', 5, 50000, 25000, 2, now(), now(), 11, 11);


-- User1이 Store2~9에 등록한 리뷰들 8개 (reviewId: 12~19)   - 위 Store1에는 리뷰 2개 등록(reviewId: 1, 11)
INSERT INTO review_tb
(store_id, user_id, content, rating, review_uuid, recommend_count, total_price, cost_per_person, people_count, created_at, updated_at, created_by, updated_by)
VALUES
    (2, 1, '맛있는데 좀 짜요', 4, 'uuiduuiduuiduuid012', 5, 50000, 25000, 2, now(), now(), 12, 12),
    (3, 1, '이것이 맛있나요?', 3, 'uuiduuiduuiduuid013', 4, 50000, 25000, 2, now(), now(), 13, 13),
    (4, 1, '사람이 많아 한참 기다렸네요ㅠ', 2, 'uuiduuiduuiduuid014', 0, 50000, 25000, 2, now(), now(), 14, 14),
    (5, 1, '음식이 너무 자극적이고 양이 너무 부족해요 배부르게 먹으려면 4만원치는 먹어야 할듯', 1, 'uuiduuiduuiduuid015', 3, 50000, 25000, 2, now(), now(), 15, 15),
    (6, 1, '.', 2, 'uuiduuiduuiduuid016', 0, 50000, 25000, 2, now(), now(), 16, 16),
    (7, 1, '너무 멋진 공간이에요! 카페 인테리어도 플랜테리어 대박인데 안쪽에 전시 공간도 있고 커피도 맛있어요! 프레츨 장식도 귀엽고! 담에 가면 프레츨 꼭 먹으려고요. 분위기나 공간에 비해 금액도 합리적이고 조용하고 넓고! 직원분들도 다 넘넘 친절하세요. 북적이지 않고 아늑해서 정말 맘에 드는 카페입니다! 이제 명동 근처 가면 무조건 요기 가려구요!!', 5, 'uuiduuiduuiduuid017', 1, 50000, 25000, 2, now(), now(), 17, 17),
    (8, 1, '너티라떼 고소하고 진해서 맛나요😊 프레첼은 맛이 다양하고 예뻤습니다💕 입구에서부터 너무 예쁘게 잘 꾸며져 있었습니다. 매장에서 꽃향기 나요🙂 포토스팟도 많고, 사진찍는거 좋아하시는 분들이라면 방문하시면 넘 좋을 것 같아요❤️ 직원분들도 친절하세요👍🏻', 5, 'uuiduuiduuiduuid018', 1, 50000, 25000, 2, now(), now(), 18, 18),
    (9, 1, '매장이 독특해요
입구부터 출구까지 보는 재미가 있는 카페예요
규모도 꽤 넓고 천장도 높은편이라 시끄럽지도 않고 대화하기 좋아요
쪽파 프레즐 맛있었고 음료랑 커피맛도 좋았습니다
명동에 카페가 많으면서도 갈만한 카페는 그리 많지않은데 요기 딱 좋네요
단골해야겠어요:)', 4, 'uuiduuiduuiduuid019', 0, 50000, 25000, 2, now(), now(), 19, 19);


-- LikeReview Table Initalization
-- (리뷰 id, 등록된 좋아요수) : (1,2) (2,3) (3,1) (7,1) (9,3) (11,5) (12,5) (13,4) (15,3) (17,1) (18,1) -> total: 29
-- (회원 id, 누른 좋아요수) : (1,4) (2,3) (3,2) (4,3) (5,2) (7,2) (8,3) (9,4) (10,4) (11,2) -> total: 229
INSERT INTO likereview_tb (review_id, user_id) VALUES (1, 1);
INSERT INTO likereview_tb (review_id, user_id) VALUES (2, 1);
INSERT INTO likereview_tb (review_id, user_id) VALUES (7, 1);
INSERT INTO likereview_tb (review_id, user_id) VALUES (3, 1);
INSERT INTO likereview_tb (review_id, user_id) VALUES (1, 2);
INSERT INTO likereview_tb (review_id, user_id) VALUES (2, 2);
INSERT INTO likereview_tb (review_id, user_id) VALUES (7, 2);
INSERT INTO likereview_tb (review_id, user_id) VALUES (7, 2);
INSERT INTO likereview_tb (review_id, user_id) VALUES (9, 2);
INSERT INTO likereview_tb (review_id, user_id) VALUES (2, 3);
INSERT INTO likereview_tb (review_id, user_id) VALUES (9, 3);
INSERT INTO likereview_tb (review_id, user_id) VALUES (9, 4);
INSERT INTO likereview_tb (review_id, user_id) VALUES (11, 4);
INSERT INTO likereview_tb (review_id, user_id) VALUES (12, 4);
INSERT INTO likereview_tb (review_id, user_id) VALUES (11, 5);
INSERT INTO likereview_tb (review_id, user_id) VALUES (12, 5);
INSERT INTO likereview_tb (review_id, user_id) VALUES (11, 7);
INSERT INTO likereview_tb (review_id, user_id) VALUES (12, 7);
INSERT INTO likereview_tb (review_id, user_id) VALUES (11, 8);
INSERT INTO likereview_tb (review_id, user_id) VALUES (12, 8);
INSERT INTO likereview_tb (review_id, user_id) VALUES (13, 8);
INSERT INTO likereview_tb (review_id, user_id) VALUES (11, 9);
INSERT INTO likereview_tb (review_id, user_id) VALUES (12, 9);
INSERT INTO likereview_tb (review_id, user_id) VALUES (13, 9);
INSERT INTO likereview_tb (review_id, user_id) VALUES (15, 9);
INSERT INTO likereview_tb (review_id, user_id) VALUES (13, 10);
INSERT INTO likereview_tb (review_id, user_id) VALUES (15, 10);
INSERT INTO likereview_tb (review_id, user_id) VALUES (17, 10);
INSERT INTO likereview_tb (review_id, user_id) VALUES (18, 10);
INSERT INTO likereview_tb (review_id, user_id) VALUES (13, 11);
INSERT INTO likereview_tb (review_id, user_id) VALUES (15, 11);


INSERT INTO image_tb (review_id, url) VALUES (1, 'image1.png');
-- reviewId: 8에 image 1~3 등록
INSERT INTO image_tb (review_id, url) VALUES (8, 'image1_review8.png');
INSERT INTO image_tb (review_id, url) VALUES (8, 'image2_review8.png');
INSERT INTO image_tb (review_id, url) VALUES (8, 'image3_review8.png');
-- reviewId: 9에 image 1~2 등록
INSERT INTO image_tb (review_id, url) VALUES (9, 'image1_review9.png');
INSERT INTO image_tb (review_id, url) VALUES (9, 'image2_review9.png');



INSERT INTO food_tb (store_id, food_name, nums_of_review, avg_rating, created_at, updated_at) VALUES (1, 'food1', 3, 2.5, now(), now());



INSERT INTO tag_tb (image_id, food_id, tag_name, menu_rating, locationx, locationy) VALUES (1, 1, 'food1', 3, 25.08, 36.74);
INSERT INTO tag_tb (image_id, food_id, tag_name, menu_rating, locationx, locationy) VALUES (2, 1, 'food1', 3, 25.08, 36.74);
INSERT INTO tag_tb (image_id, food_id, tag_name, menu_rating, locationx, locationy) VALUES (3, 1, 'food1', 3, 25.08, 36.74);
INSERT INTO tag_tb (image_id, food_id, tag_name, menu_rating, locationx, locationy) VALUES (4, 1, 'food1', 3, 25.08, 36.74);
INSERT INTO tag_tb (image_id, food_id, tag_name, menu_rating, locationx, locationy) VALUES (5, 1, 'food1', 3, 25.08, 36.74);
INSERT INTO tag_tb (image_id, food_id, tag_name, menu_rating, locationx, locationy) VALUES (6, 1, 'food1', 3, 25.08, 36.74);
