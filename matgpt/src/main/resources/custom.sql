-- Category Table Initialization
INSERT INTO category_tb (id, name) VALUES
                                       (1, 'KOREAN'),
                                       (2, 'CHINESE'),
                                       (3, 'DESSERT'),
                                       (4, 'JAPANESE');
-- User Table Initalization
INSERT INTO user_tb (AGE_GROUP, ID, EMAIL, GENDER, NAME, LOCALE, PASSWORD)
VALUES (2, 1, 'nstgic@gmail.com', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7');
INSERT INTO user_tb (AGE_GROUP, ID, EMAIL, GENDER, NAME, LOCALE, PASSWORD)
VALUES (2, 2, 'female@gmail.com', 'FEMALE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd2','FRANCE', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7');
INSERT INTO user_tb (AGE_GROUP, ID, EMAIL, GENDER, NAME, LOCALE, PASSWORD)
VALUES (2 ,3,  'sk980919@kakao.com','MALE', '364ea4bc-65b6-4f27-8682-61ce58896898', 'KOREA', 'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7');


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
    (1, '미성옥', '02-776-8929', '서울 중구 명동길 25-11', '06:00 - 21:00', 37.5640065, 126.983556, 11000, 3, 15, 3.5,1),
    (2, '딘타이펑 명동점', '02-3789-2778', '서울 중구 명동7길 13 명동증권빌딩', '11:00 - 21:00', 37.5643309, 126.984133, 18000, 4, 0, 3.8,2),
    (3, '어반플랜트 명동', '0507-1480-0154', '서울 중구 명동8나길 38 1층', '09:00 - 21:00', 37.5614752, 126.982830, 7000, 4, 20, 4.2,3),
    (4, '명동교자', '0507-1443-3525', '서울 중구 명동10길 10 명동교자', '10:30 - 21:00', 37.5634232, 126.9850928, 10000, 3, 4, 4.0,4),
    (5, '서울지짐이', '02-3789-2778', '서울 중구 명동9가길10 1, 2층', '11:00 - 21:00', 37.5650588, 126.9840605, 18000, 4, 0, 3.0,5),
    (6, '흑돈가 명동점', '02-3789-2778', '서울 중구 명동7길 21', '11:00 - 21:00', 37.56471, 126.9838683, 18000, 4, 0, 4.4,6),
    (7, '박대감닭한마리', '02-3789-2778', '서울 중구 명동7길 21', '11:00 - 21:00', 37.56471, 126.9812345, 18000, 4, 0, 3.3,7),
    (8, '음식점8', '02-3789-2778', '서울 중구 명동', '11:00 - 21:00', 37.568876, 126.9823532, 18000, 4, 0, 3.9,8),
    (9, '음식점9', '02-3789-2778', '서울 중구 명동7길', '11:00 - 21:00', 37.56645, 126.988566, 18000, 4, 0, 2.7,9),
    (10, '음식점10', '02-3789-2778', '서울 중구 명동7길 13', '11:00 - 21:00', 37.566005, 126.9824525, 18000, 4, 0, 2.0,10),
    (11, '음식점11', '02-3789-2778', '서울 중구', '11:00 - 21:00', 37.56313, 126.980006, 18000, 4, 0, 4.8,11);



-- LikeStore Table Initalization
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (1,1,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (2,2,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (3,3,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (4,7,1);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (5,1,2);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (6,2,2);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (7,5,2);
INSERT INTO likestore_tb(id,store_id,user_id)VALUES (8,6,2);


INSERT INTO review_tb
(id, store_id, user_id, content, rating, review_uuid, recommend_count, total_price, cost_per_person, people_count, created_at, updated_at)
VALUES
    (2, 1, 2, '참말로 맛있네용', 4,'uuiduuiduuiduuid001', 2, 30000, 150000, 2, now(), now()),
    (3, 1, 2, '단출한 메뉴에 집중해서 좋아요. 늘 혼자 가서 수육은 먹어보지 못해 아쉽네요.', 5,'uuiduuiduuiduuid003', 1, 50000, 25000, 2, now(), now()),
    (4, 1, 2, '1966년부터 이어온 전통있는곳이지만 숨겨진 히든 플레이스 같은 곳이라 명동에서도 숨은 골목으로 찾아 들어가야한다. 우리나라에도 이런 전통 있는 국내산 한우로만 만든 설렁탕 집이 유지되고 있어서 감사할정도! 고기나 특유의 잡내 조차 느껴지지 않게 맑은 국물 거기에 말도 안되는 배추김치와 깍두기의 맛은 한국 김치가 원조이자 세계 제일임을 느끼게 해 줄정도다. 일본인들이 정말 많이 찾아온다 그들도 정통을 아는거지... 근데 진짜 너무 맛있음 여긴.... 동네라면 매일 아침 먹고 출근하고 싶을 정도임.... 무려 새벽 6시에 오픈하는 곳!!!!', 5,'uuiduuiduuiduuid004', 1, 50000, 25000, 2, now(), now()),
    (5, 1, 2, '설렁탕과 수육 맛집 인정^^
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
기분이 좋다 ㅎㅎ', 5,'uuiduuiduuiduuid005', 1, 50000, 25000, 2, now(), now()),
    (6, 1, 2, '처음 왔는데 무조건 특으로 맛납니다^^', 5,'uuiduuiduuiduuid006', 1, 50000, 25000, 2, now(), now()),
    (7, 1, 2, '잡내없고 깔끔해서 먹고나도 개운해요!', 3,'uuiduuiduuiduuid007', 1, 50000, 25000, 2, now(), now()),
    (8, 1, 2, '수육 진짜 최고에요,,, 꼭 드세요ㅠㅠ', 2,'uuiduuiduuiduuid008', 1, 50000, 25000, 2, now(), now()),
    (9, 1, 2, '주말 아침에 방문했는데일본관광객들이 많이 오네요~^^
아침 든든하게 잘해결했어요~', 5,'uuiduuiduuiduuid009', 1, 50000, 25000, 2, now(), now()),
    (10, 1, 2, '아주 맛집이에요 명동에 또 온다면 다시 오고싶어요 수육 첫점부터 맛있었어요', 5,'uuiduuiduuiduuid010', 1, 50000, 25000, 2, now(), now()),
    (11, 1, 2, '단출한 메뉴에 집중해서 좋아요. 늘 혼자 가서 수육은 먹어보지 못해 아쉽네요.', 5, 'uuiduuiduuiduuid011',1, 50000, 25000, 2, now(), now()),
    (12, 1, 2, '맛있는데 좀 짜요', 4,'uuiduuiduuiduuid012', 1, 50000, 25000, 2, now(), now()),
    (13, 1, 2, '이것이 맛있나요?', 3,'uuiduuiduuiduuid013', 1, 50000, 25000, 2, now(), now()),
    (14, 1, 2, '사람이 많아 한참 기다렸네요ㅠ', 2,'uuiduuiduuiduuid014', 1, 50000, 25000, 2, now(), now()),
    (15, 1, 2, '음식이 너무 자극적이고 양이 너무 부족해요 배부르게 먹으려면 4만원치는 먹어야 할듯', 1,'uuiduuiduuiduuid015', 1, 50000, 25000, 2, now(), now()),
    (16, 1, 2, '.', 2, 'uuiduuiduuiduuid016', 1, 50000, 25000, 2, now(), now()),
    (17, 3, 2, '너무 멋진 공간이에요! 카페 인테리어도 플랜테리어 대박인데 안쪽에 전시 공간도 있고 커피도 맛있어요! 프레츨 장식도 귀엽고! 담에 가면 프레츨 꼭 먹으려고요. 분위기나 공간에 비해 금액도 합리적이고 조용하고 넓고! 직원분들도 다 넘넘 친절하세요. 북적이지 않고 아늑해서 정말 맘에 드는 카페입니다! 이제 명동 근처 가면 무조건 요기 가려구요!!', 5, 'uuiduuiduuiduuid017', 1, 50000, 25000, 2, now(), now()),
    (18, 3, 2, '너티라떼 고소하고 진해서 맛나요😊 프레첼은 맛이 다양하고 예뻤습니다💕 입구에서부터 너무 예쁘게 잘 꾸며져 있었습니다. 매장에서 꽃향기 나요🙂 포토스팟도 많고, 사진찍는거 좋아하시는 분들이라면 방문하시면 넘 좋을 것 같아요❤️ 직원분들도 친절하세요👍🏻', 5, 'uuiduuiduuiduuid018', 1, 50000, 25000, 2, now(), now()),
    (19, 3, 2, '매장이 독특해요 입구부터 출구까지 보는 재미가 있는 카페예요 규모도 꽤 넓고 천장도 높은편이라 시끄럽지도 않고 대화하기 좋아요 쪽파 프레즐 맛있었고 음료랑 커피맛도 좋았습니다 명동에 카페가 많으면서도 갈만한 카페는 그리 많지않은데 요기 딱 좋네요 단골해야겠어요:)', 4, 'uuiduuiduuiduuid019', 1, 50000, 25000, 2, now(), now()),
    (20, 3, 2, '입구부터 프렛첼이 매달려있어서 프렛첼을 먹지 않을 수 없었는데, 먹지 않았으면 크게 후회할 뻔. 쪽파크림치즈와 아몬드 크림치즈도 맛있었으나, 빵 자체가 맛있었음! 홍대 어반플랜트에 만족했기에 이곳도 인테리어와 분위기 기대하고 들렀는데, 왠걸, 홍대의 습한 분위기와도 또 다르고 디저트에 대만족하고 옴 🤩', 5, 'uuiduuiduuiduuid020', 1, 50000, 25000, 2, now(), now()),
    (21, 3, 2, '이런곳이 있는줄 몰랐네요. 넘 신기해요. 커피는 막 맛있다 맛없다 말하긴 그렇지만… 분위기가 먹고 들어갑니다. 상당히 많은 나무들이 입구부터 반겨주고~ 진자 특별한 프레즐이 있어 디저트 먹기 좋은 곳 같아요. 들어갈 수는 없었지만… 꽃밭도 너무 아름답고 기분 좋아지는 곳 입니다 ^^', 5, 'uuiduuiduuiduuid021', 1, 50000, 25000, 2, now(), now()),
    (22, 3, 2, '✅ 맛있는 디저트가 있는 곳 ✅ 믿고 먹을 수 있는 라떼 ✅ 넓은 카페, 테이블 간 간격 적당👍', 3, 'uuiduuiduuiduuid022', 1, 50000, 25000, 2, now(), now()),
    (23, 3, 2, '인테리어예뻐요 음식은 그럭저럭', 3, 'uuiduuiduuiduuid023', 1, 50000, 25000, 2, now(), now()),
    (24, 3, 2, '이 카페는 정말 아름답고 편안한 분위기를 가지고 있어요. 커피 맛도 훌륭하고, 프레즐이나 다른 디저트도 매우 맛있었어요. 내부 인테리어가 참신하고 고급스러워서, 특별한 날이나 친구들과의 모임에도 좋을 것 같아요.', 5, 'uuiduuiduuiduuid024', 1, 50000, 25000, 2, now(), now()),
    (25, 3, 2, '인테리어도 예쁘고 디저트도 맛있는 카페에요. 직원들도 친절해서 기분 좋게 이용할 수 있었습니다.', 5, 'uuiduuiduuiduuid025', 1, 50000, 25000, 2, now(), now()),
    (26, 3, 2, '입구부터 꽃과 나무로 가득해서 정말 예뻐요! 카페 안쪽에도 식물들이 많아서 분위기가 너무 좋아요. 커피랑 디저트도 맛있어서 다음에 또 방문하고 싶은 곳이에요!', 5, 'uuiduuiduuiduuid026', 1, 50000, 25000, 2, now(), now()),
    (27, 3, 2, '프레첼이 맛있어요! 인테리어도 너무 예쁘고 분위기도 좋아요. 특히 직원분들이 친절해서 다음에 또 방문하고 싶어요.', 5, 'uuiduuiduuiduuid027', 1, 50000, 25000, 2, now(), now()),
    (28, 3, 2, '카페 분위기도 좋고, 직원분들도 친절해서 기분 좋게 이용했습니다. 프레첼이랑 커피도 맛있었어요!', 5, 'uuiduuiduuiduuid028', 1, 50000, 25000, 2, now(), now()),
    (29, 3, 2, '커피가 맛있어요! 카페 분위기도 좋고, 직원분들도 친절하셔서 자주 이용하게 될 것 같아요.', 4, 'uuiduuiduuiduuid029', 1, 50000, 25000, 2, now(), now()),
    (30, 3, 2, '분위기도 좋고, 음료도 맛있어요. 직원분들도 친절해서 기분 좋게 이용했습니다.', 4, 'uuiduuiduuiduuid030', 1, 50000, 25000, 2, now(), now()),
    (31, 3, 2, '카페 분위기가 정말 좋아요! 커피랑 디저트도 맛있고, 직원분들도 친절해서 자주 방문하고 싶은 곳입니다.', 5, 'uuiduuiduuiduuid031', 1, 50000, 25000, 2, now(), now()),
    (32, 3, 2, '카페 인테리어가 예쁘고, 커피랑 디저트도 맛있어요. 직원분들도 친절해서 좋은 경험이었습니다.', 5, 'uuiduuiduuiduuid032', 1, 50000, 25000, 2, now(), now()),
    (33, 3, 2, '맛있는 커피와 디저트, 그리고 멋진 인테리어까지, 이 카페는 정말 매력적인 곳입니다.', 5, 'uuiduuiduuiduuid033', 1, 50000, 25000, 2, now(), now()),
    (34, 3, 2, '카페 분위기가 너무 좋고, 커피랑 디저트도 맛있어요. 직원분들도 친절해서 편안하게 즐길 수 있는 곳입니다.', 5, 'uuiduuiduuiduuid034', 1, 50000, 25000, 2, now(), now()),
    (35, 3, 2, '카페 인테리어가 예쁘고, 커피 맛도 좋아요. 직원분들도 친절해서 기분 좋은 시간을 보낼 수 있었습니다.', 5, 'uuiduuiduuiduuid035', 1, 50000, 25000, 2, now(), now()),
    (36, 3, 2, '카페가 아름답고 분위기도 좋아요. 커피와 디저트도 맛있고, 직원분들도 친절해서 좋은 경험이었습니다.', 5, 'uuiduuiduuiduuid036', 1, 50000, 25000, 2, now(), now()),
    (37, 4, 2, '서비스도 좋고, 음식 맛도 최고!', 5, 'uuiduuiduuiduuid037', 5, 50000, 25000, 2, now(), now()),
    (38, 4, 2, '완전 추천합니다!', 5, 'uuiduuiduuiduuid038', 5, 50000, 25000, 2, now(), now()),
    (39, 4, 2, '최고의 음식, 최고의 서비스!', 5, 'uuiduuiduuiduuid039', 5, 50000, 25000, 2, now(), now()),
    (40, 4, 2, '자주 방문하려고요. 정말 맛있어요!', 5, 'uuiduuiduuiduuid040', 5, 50000, 25000, 2, now(), now());

-- LikeStore Table Initalization
INSERT INTO likereview_tb(review_id, user_id)VALUES (2, 1);
INSERT INTO likereview_tb(review_id, user_id)VALUES (3, 1);
INSERT INTO likereview_tb(review_id, user_id)VALUES (7, 1);
INSERT INTO likereview_tb(review_id, user_id)VALUES (2, 2);
INSERT INTO likereview_tb(review_id, user_id)VALUES (7, 2);
INSERT INTO likereview_tb(review_id, user_id)VALUES (7, 2);

INSERT INTO coin_tb(id, user_id, balance) VALUES (1, 1, 10);
INSERT INTO coin_tb(id, user_id, balance) VALUES (2, 2, 10);
INSERT INTO coin_tb(id, user_id, balance) VALUES (3, 3, 10);