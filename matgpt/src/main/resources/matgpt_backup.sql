-- MySQL dump 10.13  Distrib 8.0.35, for Linux (x86_64)
--
-- Host: localhost    Database: matgpt_db
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category_tb`
--

DROP TABLE IF EXISTS `category_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_tb` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `name` varchar(255) NOT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_tb`
--

LOCK TABLES `category_tb` WRITE;
/*!40000 ALTER TABLE `category_tb` DISABLE KEYS */;
INSERT INTO `category_tb` VALUES (2,'CHINESE'),(3,'DESSERT'),(4,'JAPANESE'),(1,'KOREAN');
/*!40000 ALTER TABLE `category_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coin_earning_history_tb`
--

DROP TABLE IF EXISTS `coin_earning_history_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coin_earning_history_tb` (
                                           `amount` int NOT NULL,
                                           `balance` int NOT NULL,
                                           `coin_id` bigint DEFAULT NULL,
                                           `earned_at` datetime(6) DEFAULT NULL,
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           PRIMARY KEY (`id`),
                                           KEY `FK_coin_earning_history_tb_coin_id` (`coin_id`),
                                           CONSTRAINT `FK_coin_earning_history_tb_coin_id` FOREIGN KEY (`coin_id`) REFERENCES `coin_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coin_earning_history_tb`
--

LOCK TABLES `coin_earning_history_tb` WRITE;
/*!40000 ALTER TABLE `coin_earning_history_tb` DISABLE KEYS */;
INSERT INTO `coin_earning_history_tb` VALUES (2,2,1,'2023-11-09 23:41:33.520261',1),(0,1,1,'2023-11-09 23:41:39.697674',2),(12,13,1,'2023-11-10 03:02:29.476361',3),(-1,12,1,'2023-11-10 03:12:03.310895',4),(-1,11,1,'2023-11-10 03:12:05.565927',5),(-1,10,1,'2023-11-10 03:12:07.195126',6);
/*!40000 ALTER TABLE `coin_earning_history_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coin_tb`
--

DROP TABLE IF EXISTS `coin_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coin_tb` (
                           `balance` int NOT NULL,
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `user_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK_coin_tb_user_id` (`user_id`),
                           CONSTRAINT `FK_coin_tb_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coin_tb`
--

LOCK TABLES `coin_tb` WRITE;
/*!40000 ALTER TABLE `coin_tb` DISABLE KEYS */;
INSERT INTO `coin_tb` VALUES (2,1,12);
/*!40000 ALTER TABLE `coin_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coin_usage_history_tb`
--

DROP TABLE IF EXISTS `coin_usage_history_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coin_usage_history_tb` (
                                         `amount` int NOT NULL,
                                         `balance` int NOT NULL,
                                         `coin_id` bigint DEFAULT NULL,
                                         `id` bigint NOT NULL AUTO_INCREMENT,
                                         `used_at` datetime(6) DEFAULT NULL,
                                         PRIMARY KEY (`id`),
                                         KEY `FK_coin_usage_history_tb_coin_id` (`coin_id`),
                                         CONSTRAINT `FK_coin_usage_history_tb_coin_id` FOREIGN KEY (`coin_id`) REFERENCES `coin_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coin_usage_history_tb`
--

LOCK TABLES `coin_usage_history_tb` WRITE;
/*!40000 ALTER TABLE `coin_usage_history_tb` DISABLE KEYS */;
INSERT INTO `coin_usage_history_tb` VALUES (1,1,1,1,'2023-11-09 23:41:39.372145'),(0,1,1,2,'2023-11-09 23:41:50.261769'),(0,10,1,3,'2023-11-10 03:13:59.409698'),(0,10,1,4,'2023-11-10 03:14:01.302510'),(8,2,1,5,'2023-11-10 03:14:02.764321');
/*!40000 ALTER TABLE `coin_usage_history_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food_tb`
--

DROP TABLE IF EXISTS `food_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_tb` (
                           `avg_rating` double DEFAULT NULL,
                           `nums_of_review` int DEFAULT NULL,
                           `created_at` datetime(6) NOT NULL,
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `store_id` bigint DEFAULT NULL,
                           `updated_at` datetime(6) NOT NULL,
                           `created_by` varchar(255) DEFAULT NULL,
                           `food_description` varchar(255) DEFAULT NULL,
                           `food_name` varchar(255) NOT NULL,
                           `updated_by` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK_food_tb_store_id` (`store_id`),
                           CONSTRAINT `FK_food_tb_store_id` FOREIGN KEY (`store_id`) REFERENCES `store_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_tb`
--

LOCK TABLES `food_tb` WRITE;
/*!40000 ALTER TABLE `food_tb` DISABLE KEYS */;
INSERT INTO `food_tb` VALUES (0,3,'2023-11-10 01:49:28.731193',1,1,'2023-11-10 23:30:35.844617','12',NULL,'string','12');
/*!40000 ALTER TABLE `food_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gpt_guidance_tb`
--

DROP TABLE IF EXISTS `gpt_guidance_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gpt_guidance_tb` (
                                   `created_at` datetime(6) NOT NULL,
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `user_id` bigint DEFAULT NULL,
                                   `content` varchar(1000) NOT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `FK_gpt_guidance_tb_user_id` (`user_id`),
                                   CONSTRAINT `FK_gpt_guidance_tb_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gpt_guidance_tb`
--

LOCK TABLES `gpt_guidance_tb` WRITE;
/*!40000 ALTER TABLE `gpt_guidance_tb` DISABLE KEYS */;
/*!40000 ALTER TABLE `gpt_guidance_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gpt_review_tb`
--

DROP TABLE IF EXISTS `gpt_review_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gpt_review_tb` (
                                 `last_nums_of_review` int NOT NULL,
                                 `created_at` datetime(6) DEFAULT NULL,
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `store_id` bigint DEFAULT NULL,
                                 `updated_at` datetime(6) DEFAULT NULL,
                                 `content` varchar(1000) NOT NULL,
                                 `summary_type` varchar(255) DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `FK_gpt_review_tb_store_id` (`store_id`),
                                 CONSTRAINT `FK_gpt_review_tb_store_id` FOREIGN KEY (`store_id`) REFERENCES `store_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gpt_review_tb`
--

LOCK TABLES `gpt_review_tb` WRITE;
/*!40000 ALTER TABLE `gpt_review_tb` DISABLE KEYS */;
INSERT INTO `gpt_review_tb` VALUES (16,'2023-11-10 00:16:08.000000',1,1,'2023-11-10 12:21:53.000000','음식점은 메뉴가 단축되어 있고, 수육을 먹을 수 없어서 아쉽지만 맛집으로 여러 곳에 가고 싶어요. 명동에 다시 갔을 때는 다시 오고 싶어요. 주말 아침에는 일본 관광객들이 많이 옵니다. 아침에 먹기에 괜찮았어요. 맛이 좋은 것은 물론 가게도 꽤 넓어요. 설렁탕과 수','best'),(16,'2023-11-10 00:16:08.000000',2,1,'2023-11-10 12:21:53.000000','음식의 자극적인 맛과 양이 부족하며 가격이 비싸다는 평가, 수육이 최고로 맛있고 깔끔하며 기다림이 긴 것에 대한 불만, 맛있고 개운한 맛이지만 약간 짜다는 의견이 있었다.','worst');
/*!40000 ALTER TABLE `gpt_review_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_tb`
--

DROP TABLE IF EXISTS `image_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_tb` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `review_id` bigint DEFAULT NULL,
                            `url` varchar(255) NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FK_image_tb_review_id` (`review_id`),
                            CONSTRAINT `FK_image_tb_review_id` FOREIGN KEY (`review_id`) REFERENCES `review_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_tb`
--

LOCK TABLES `image_tb` WRITE;
/*!40000 ALTER TABLE `image_tb` DISABLE KEYS */;
INSERT INTO `image_tb` VALUES (1,42,'string'),(2,44,'string'),(3,71,'string');
/*!40000 ALTER TABLE `image_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likereview_tb`
--

DROP TABLE IF EXISTS `likereview_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likereview_tb` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `review_id` bigint DEFAULT NULL,
                                 `user_id` bigint DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `FK_likereview_tb_review_id` (`review_id`),
                                 KEY `FK_likereview_tb_user_id` (`user_id`),
                                 CONSTRAINT `FK_likereview_tb_review_id` FOREIGN KEY (`review_id`) REFERENCES `review_tb` (`id`),
                                 CONSTRAINT `FK_likereview_tb_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likereview_tb`
--

LOCK TABLES `likereview_tb` WRITE;
/*!40000 ALTER TABLE `likereview_tb` DISABLE KEYS */;
INSERT INTO `likereview_tb` VALUES (1,16,12),(12,3,12),(13,4,12),(14,5,12),(15,6,12),(16,7,12),(17,8,12),(18,9,12),(19,10,12),(20,11,12),(22,13,12),(23,2,12),(24,12,12);
/*!40000 ALTER TABLE `likereview_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likestore_tb`
--

DROP TABLE IF EXISTS `likestore_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likestore_tb` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `store_id` bigint DEFAULT NULL,
                                `user_id` bigint DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                KEY `FK_likestore_tb_store_id` (`store_id`),
                                KEY `FK_likestore_tb_user_id` (`user_id`),
                                CONSTRAINT `FK_likestore_tb_store_id` FOREIGN KEY (`store_id`) REFERENCES `store_tb` (`id`),
                                CONSTRAINT `FK_likestore_tb_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likestore_tb`
--

LOCK TABLES `likestore_tb` WRITE;
/*!40000 ALTER TABLE `likestore_tb` DISABLE KEYS */;
INSERT INTO `likestore_tb` VALUES (12,3,12),(13,4,12),(14,5,12),(15,6,12),(17,8,12),(18,9,12),(20,11,12),(26,1,12),(27,7,12);
/*!40000 ALTER TABLE `likestore_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_token` (
                                 `refresh_key` varchar(255) NOT NULL,
                                 `refresh_value` varchar(255) DEFAULT NULL,
                                 PRIMARY KEY (`refresh_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review_tb`
--

DROP TABLE IF EXISTS `review_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review_tb` (
                             `cost_per_person` int DEFAULT NULL,
                             `people_count` int DEFAULT NULL,
                             `rating` int DEFAULT NULL,
                             `recommend_count` int DEFAULT NULL,
                             `total_price` int DEFAULT NULL,
                             `created_at` datetime(6) NOT NULL,
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `store_id` bigint DEFAULT NULL,
                             `updated_at` datetime(6) NOT NULL,
                             `user_id` bigint NOT NULL,
                             `content` varchar(1000) NOT NULL,
                             `created_by` varchar(255) DEFAULT NULL,
                             `review_uuid` varchar(255) NOT NULL,
                             `updated_by` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `review_uuid` (`review_uuid`),
                             KEY `FK_review_tb_store_id` (`store_id`),
                             KEY `FK_review_tb_user_id` (`user_id`),
                             CONSTRAINT `FK_review_tb_store_id` FOREIGN KEY (`store_id`) REFERENCES `store_tb` (`id`),
                             CONSTRAINT `FK_review_tb_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review_tb`
--

LOCK TABLES `review_tb` WRITE;
/*!40000 ALTER TABLE `review_tb` DISABLE KEYS */;
INSERT INTO `review_tb` VALUES (150000,2,4,3,30000,'2023-11-07 23:21:25.000000',2,1,'2023-11-10 12:44:23.419179',2,'참말로 맛있네용','2','uuiduuiduuiduuid001','12'),(25000,2,5,2,50000,'2023-11-07 23:21:25.000000',3,1,'2023-11-10 04:30:12.656160',3,'단출한 메뉴에 집중해서 좋아요. 늘 혼자 가서 수육은 먹어보지 못해 아쉽네요.','3','uuiduuiduuiduuid003','12'),(25000,2,5,2,50000,'2023-11-07 23:21:25.000000',4,1,'2023-11-10 04:30:14.098454',4,'1966년부터 이어온 전통있는곳이지만 숨겨진 히든 플레이스 같은 곳이라 명동에서도 숨은 골목으로 찾아 들어가야한다. 우리나라에도 이런 전통 있는 국내산 한우로만 만든 설렁탕 집이 유지되고 있어서 감사할정도! 고기나 특유의 잡내 조차 느껴지지 않게 맑은 국물 거기에 말도 안되는 배추김치와 깍두기의 맛은 한국 김치가 원조이자 세계 제일임을 느끼게 해 줄정도다. 일본인들이 정말 많이 찾아온다 그들도 정통을 아는거지... 근데 진짜 너무 맛있음 여긴.... 동네라면 매일 아침 먹고 출근하고 싶을 정도임.... 무려 새벽 6시에 오픈하는 곳!!!!','4','uuiduuiduuiduuid004','12'),(25000,2,5,2,50000,'2023-11-07 23:21:25.000000',5,1,'2023-11-10 04:30:16.450672',5,'설렁탕과 수육 맛집 인정^^\n명동에 나갔다가 맛있다길래 찾아갔다.\n큰길에서 벗어나 뒷골목에 있는데도\n어떻게들 알고 찾아오는지.. ㅎ\n입구보다 매장안에 들어서니 꽤 넓다.\n완전 오래된 노포는 아니고 1966년에 오픈한 가게라고~~~\n2명인데 양이 많을것 같아서\n설렁탕과 수육하나를 주문했더니,\n국수넣은 국물한그릇을 덤으로 더 주시는..\n인심까지 훈훈~~~♡\n수육은 소고기의 부위별로 나오는데,\n살코기가 팍팍하지않고 맛있다.\n기름도 쫄깃한게...\n이런맛 처음~~~♡♡♡\n설렁탕 국물도 단백하니 맛이 좋았다.\n오랜만에 든든한 점심을 먹었더니\n기분이 좋다 ㅎㅎ','5','uuiduuiduuiduuid005','12'),(25000,2,5,2,50000,'2023-11-07 23:21:25.000000',6,1,'2023-11-10 04:30:17.781829',6,'처음 왔는데 무조건 특으로 맛납니다^^','6','uuiduuiduuiduuid006','12'),(25000,2,3,2,50000,'2023-11-07 23:21:25.000000',7,1,'2023-11-10 04:30:18.880146',7,'잡내없고 깔끔해서 먹고나도 개운해요!','7','uuiduuiduuiduuid007','12'),(25000,2,2,2,50000,'2023-11-07 23:21:25.000000',8,1,'2023-11-10 04:30:21.772942',8,'수육 진짜 최고에요,,, 꼭 드세요ㅠㅠ','8','uuiduuiduuiduuid008','12'),(25000,2,5,2,50000,'2023-11-07 23:21:25.000000',9,1,'2023-11-10 04:30:29.325512',9,'주말 아침에 방문했는데일본관광객들이 많이 오네요~^^\n아침 든든하게 잘해결했어요~','9','uuiduuiduuiduuid009','12'),(25000,2,5,2,50000,'2023-11-07 23:21:25.000000',10,1,'2023-11-10 04:30:31.984075',10,'아주 맛집이에요 명동에 또 온다면 다시 오고싶어요 수육 첫점부터 맛있었어요','10','uuiduuiduuiduuid010','12'),(25000,2,5,2,50000,'2023-11-07 23:21:25.000000',11,1,'2023-11-10 04:30:33.627061',1,'단출한 메뉴에 집중해서 좋아요. 늘 혼자 가서 수육은 먹어보지 못해 아쉽네요.','1','uuiduuiduuiduuid011','12'),(25000,2,4,2,50000,'2023-11-07 23:21:25.000000',12,1,'2023-11-10 15:40:08.454779',2,'맛있는데 좀 짜요','2','uuiduuiduuiduuid012','12'),(25000,2,3,2,50000,'2023-11-07 23:21:25.000000',13,1,'2023-11-10 04:30:36.254666',3,'이것이 맛있나요?','3','uuiduuiduuiduuid013','12'),(25000,2,2,1,50000,'2023-11-07 23:21:25.000000',14,1,'2023-11-07 23:21:25.000000',4,'사람이 많아 한참 기다렸네요ㅠ','4','uuiduuiduuiduuid014','4'),(25000,2,1,1,50000,'2023-11-07 23:21:25.000000',15,1,'2023-11-07 23:21:25.000000',5,'음식이 너무 자극적이고 양이 너무 부족해요 배부르게 먹으려면 4만원치는 먹어야 할듯','5','uuiduuiduuiduuid015','5'),(25000,2,2,2,50000,'2023-11-07 23:21:25.000000',16,1,'2023-11-09 23:40:23.896502',6,'.','6','uuiduuiduuiduuid016','12'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',17,3,'2023-11-07 23:21:25.000000',7,'너무 멋진 공간이에요! 카페 인테리어도 플랜테리어 대박인데 안쪽에 전시 공간도 있고 커피도 맛있어요! 프레츨 장식도 귀엽고! 담에 가면 프레츨 꼭 먹으려고요. 분위기나 공간에 비해 금액도 합리적이고 조용하고 넓고! 직원분들도 다 넘넘 친절하세요. 북적이지 않고 아늑해서 정말 맘에 드는 카페입니다! 이제 명동 근처 가면 무조건 요기 가려구요!!','7','uuiduuiduuiduuid017','7'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',18,3,'2023-11-07 23:21:25.000000',8,'너티라떼 고소하고 진해서 맛나요😊 프레첼은 맛이 다양하고 예뻤습니다💕 입구에서부터 너무 예쁘게 잘 꾸며져 있었습니다. 매장에서 꽃향기 나요🙂 포토스팟도 많고, 사진찍는거 좋아하시는 분들이라면 방문하시면 넘 좋을 것 같아요❤️ 직원분들도 친절하세요👍🏻','8','uuiduuiduuiduuid018','8'),(25000,2,4,1,50000,'2023-11-07 23:21:25.000000',19,3,'2023-11-07 23:21:25.000000',9,'매장이 독특해요 입구부터 출구까지 보는 재미가 있는 카페예요 규모도 꽤 넓고 천장도 높은편이라 시끄럽지도 않고 대화하기 좋아요 쪽파 프레즐 맛있었고 음료랑 커피맛도 좋았습니다 명동에 카페가 많으면서도 갈만한 카페는 그리 많지않은데 요기 딱 좋네요 단골해야겠어요:)','9','uuiduuiduuiduuid019','9'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',20,3,'2023-11-07 23:21:25.000000',10,'입구부터 프렛첼이 매달려있어서 프렛첼을 먹지 않을 수 없었는데, 먹지 않았으면 크게 후회할 뻔. 쪽파크림치즈와 아몬드 크림치즈도 맛있었으나, 빵 자체가 맛있었음! 홍대 어반플랜트에 만족했기에 이곳도 인테리어와 분위기 기대하고 들렀는데, 왠걸, 홍대의 습한 분위기와도 또 다르고 디저트에 대만족하고 옴 🤩','10','uuiduuiduuiduuid020','10'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',21,3,'2023-11-07 23:21:25.000000',1,'이런곳이 있는줄 몰랐네요. 넘 신기해요. 커피는 막 맛있다 맛없다 말하긴 그렇지만… 분위기가 먹고 들어갑니다. 상당히 많은 나무들이 입구부터 반겨주고~ 진자 특별한 프레즐이 있어 디저트 먹기 좋은 곳 같아요. 들어갈 수는 없었지만… 꽃밭도 너무 아름답고 기분 좋아지는 곳 입니다 ^^','1','uuiduuiduuiduuid021','1'),(25000,2,3,1,50000,'2023-11-07 23:21:25.000000',22,3,'2023-11-07 23:21:25.000000',2,'✅ 맛있는 디저트가 있는 곳 ✅ 믿고 먹을 수 있는 라떼 ✅ 넓은 카페, 테이블 간 간격 적당👍','2','uuiduuiduuiduuid022','2'),(25000,2,3,1,50000,'2023-11-07 23:21:25.000000',23,3,'2023-11-07 23:21:25.000000',3,'인테리어예뻐요 음식은 그럭저럭','3','uuiduuiduuiduuid023','3'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',24,3,'2023-11-07 23:21:25.000000',4,'이 카페는 정말 아름답고 편안한 분위기를 가지고 있어요. 커피 맛도 훌륭하고, 프레즐이나 다른 디저트도 매우 맛있었어요. 내부 인테리어가 참신하고 고급스러워서, 특별한 날이나 친구들과의 모임에도 좋을 것 같아요.','4','uuiduuiduuiduuid024','4'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',25,3,'2023-11-07 23:21:25.000000',5,'인테리어도 예쁘고 디저트도 맛있는 카페에요. 직원들도 친절해서 기분 좋게 이용할 수 있었습니다.','5','uuiduuiduuiduuid025','5'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',26,3,'2023-11-07 23:21:25.000000',6,'입구부터 꽃과 나무로 가득해서 정말 예뻐요! 카페 안쪽에도 식물들이 많아서 분위기가 너무 좋아요. 커피랑 디저트도 맛있어서 다음에 또 방문하고 싶은 곳이에요!','6','uuiduuiduuiduuid026','6'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',27,3,'2023-11-07 23:21:25.000000',7,'프레첼이 맛있어요! 인테리어도 너무 예쁘고 분위기도 좋아요. 특히 직원분들이 친절해서 다음에 또 방문하고 싶어요.','7','uuiduuiduuiduuid027','7'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',28,3,'2023-11-07 23:21:25.000000',8,'카페 분위기도 좋고, 직원분들도 친절해서 기분 좋게 이용했습니다. 프레첼이랑 커피도 맛있었어요!','8','uuiduuiduuiduuid028','8'),(25000,2,4,1,50000,'2023-11-07 23:21:25.000000',29,3,'2023-11-07 23:21:25.000000',9,'커피가 맛있어요! 카페 분위기도 좋고, 직원분들도 친절하셔서 자주 이용하게 될 것 같아요.','9','uuiduuiduuiduuid029','9'),(25000,2,4,1,50000,'2023-11-07 23:21:25.000000',30,3,'2023-11-07 23:21:25.000000',10,'분위기도 좋고, 음료도 맛있어요. 직원분들도 친절해서 기분 좋게 이용했습니다.','10','uuiduuiduuiduuid030','10'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',31,3,'2023-11-07 23:21:25.000000',1,'카페 분위기가 정말 좋아요! 커피랑 디저트도 맛있고, 직원분들도 친절해서 자주 방문하고 싶은 곳입니다.','1','uuiduuiduuiduuid031','1'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',32,3,'2023-11-07 23:21:25.000000',2,'카페 인테리어가 예쁘고, 커피랑 디저트도 맛있어요. 직원분들도 친절해서 좋은 경험이었습니다.','2','uuiduuiduuiduuid032','2'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',33,3,'2023-11-07 23:21:25.000000',3,'맛있는 커피와 디저트, 그리고 멋진 인테리어까지, 이 카페는 정말 매력적인 곳입니다.','3','uuiduuiduuiduuid033','3'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',34,3,'2023-11-07 23:21:25.000000',4,'카페 분위기가 너무 좋고, 커피랑 디저트도 맛있어요. 직원분들도 친절해서 편안하게 즐길 수 있는 곳입니다.','4','uuiduuiduuiduuid034','4'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',35,3,'2023-11-07 23:21:25.000000',5,'카페 인테리어가 예쁘고, 커피 맛도 좋아요. 직원분들도 친절해서 기분 좋은 시간을 보낼 수 있었습니다.','5','uuiduuiduuiduuid035','5'),(25000,2,5,1,50000,'2023-11-07 23:21:25.000000',36,3,'2023-11-07 23:21:25.000000',6,'카페가 아름답고 분위기도 좋아요. 커피와 디저트도 맛있고, 직원분들도 친절해서 좋은 경험이었습니다.','6','uuiduuiduuiduuid036','6'),(25000,2,5,5,50000,'2023-11-07 23:21:25.000000',37,4,'2023-11-07 23:21:25.000000',7,'서비스도 좋고, 음식 맛도 최고!','7','uuiduuiduuiduuid037','7'),(25000,2,5,5,50000,'2023-11-07 23:21:25.000000',38,4,'2023-11-07 23:21:25.000000',8,'완전 추천합니다!','8','uuiduuiduuiduuid038','8'),(25000,2,5,5,50000,'2023-11-07 23:21:25.000000',39,4,'2023-11-07 23:21:25.000000',9,'최고의 음식, 최고의 서비스!','9','uuiduuiduuiduuid039','9'),(25000,2,5,5,50000,'2023-11-07 23:21:25.000000',40,4,'2023-11-07 23:21:25.000000',10,'자주 방문하려고요. 정말 맛있어요!','10','uuiduuiduuiduuid040','10'),(0,4,0,0,0,'2023-11-10 01:49:04.646302',41,1,'2023-11-10 01:49:04.646302',12,'aaaxaczcxz','12','677b5716-89ac-4fc5-94b8-d22a1cc79785','12'),(0,4,0,0,0,'2023-11-10 01:49:09.085686',42,1,'2023-11-10 01:49:09.085686',12,'aaaxaczcxz','12','4f183642-7c99-4219-b2e2-652bb331bf28','12'),(0,1,0,0,0,'2023-11-10 16:35:49.922877',43,1,'2023-11-10 16:35:49.922877',12,'string','12','466dba83-6e87-4f12-b4dd-84d8c6f6887c','12'),(0,1,0,0,0,'2023-11-10 16:39:42.485964',44,1,'2023-11-10 16:39:42.485964',12,'string','12','0ee4ed1f-eca6-44d8-a00f-c64bdb97293a','12'),(0,1,0,0,0,'2023-11-10 16:40:30.001732',45,1,'2023-11-10 16:40:30.001732',12,'string','12','2e7e86cf-5c81-4961-a62b-64be06e8e84b','12'),(0,100,2,0,30,'2023-11-10 17:04:11.539781',46,1,'2023-11-10 17:04:11.539781',12,'추천합니다.\n탕수육은 조금 눅눅합니다.','12','50209413-278c-440b-a696-cf8100021612','12'),(0,100,2,0,30,'2023-11-10 17:11:45.382644',47,1,'2023-11-10 17:11:45.382644',12,'추천합니다.\n탕수육은 조금 눅눅합니다.','12','b1ed50b0-a3f1-4982-9882-9f03d061ae45','12'),(0,100,2,0,30,'2023-11-10 17:27:25.890790',49,1,'2023-11-10 17:27:25.890790',12,'추천합니다.\n탕수육은 조금 눅눅합니다.','12','6981bde8-339c-455c-a856-edc46e06440e','12'),(20000,1,5,0,20000,'2023-11-10 18:22:38.508370',50,1,'2023-11-10 18:22:38.508370',12,'삼겹살 맛있어요','12','184b4818-7312-470c-b575-732720b06299','12'),(0,100,2,0,30,'2023-11-10 18:24:56.802405',51,1,'2023-11-10 18:24:56.802405',12,'추천합니다.\n탕수육은 조금 눅눅합니다.','12','c57146df-f4e9-4cc7-9778-9c999d3bb319','12'),(100,1,1,0,100,'2023-11-10 19:24:05.580841',52,1,'2023-11-10 19:24:05.580841',12,'string','12','7c3519a0-3597-4846-ba5d-ed6ca698dcd6','12'),(100,1,1,0,100,'2023-11-10 20:27:06.441862',53,1,'2023-11-10 20:27:06.441862',12,'string','12','b7359fb1-5a4f-4e98-8d8d-815a03fc48b1','12'),(100,1,1,0,100,'2023-11-10 20:27:41.127105',54,1,'2023-11-10 20:27:41.127105',12,'string','12','b2a3bdd2-3f75-4b65-8653-a29903007958','12'),(10000,1,1,0,10000,'2023-11-10 20:33:45.109219',55,1,'2023-11-10 20:33:45.109219',12,'string','12','d2f3f4ec-33bf-40ad-9a5d-c45fb9dc99ca','12'),(20000,1,5,0,20000,'2023-11-10 20:44:15.523084',56,1,'2023-11-10 20:44:15.523084',12,'삼겹살 좋아요','12','0a079c6f-3a9e-4338-a76a-e8009e287a15','12'),(0,1,0,0,0,'2023-11-10 21:00:22.321216',57,1,'2023-11-10 21:00:22.321216',12,'string','12','ae36c49f-b06c-432d-90f3-f7da1a60559d','12'),(0,1,0,0,0,'2023-11-10 21:01:10.647551',58,1,'2023-11-10 21:01:10.647551',12,'string','12','68f058ff-5949-4447-8cd5-1f6597d2dc45','12'),(0,1,0,0,0,'2023-11-10 21:01:41.336849',59,1,'2023-11-10 21:01:41.336849',12,'string','12','6b92ed4f-8ca4-482d-aeec-7e6f306e539c','12'),(0,1,0,0,0,'2023-11-10 21:02:05.644942',60,1,'2023-11-10 21:02:05.644942',12,'string','12','986b4c34-7968-4ca7-9cbc-b2618ee4fa45','12'),(10000,1,1,0,10000,'2023-11-10 21:02:27.915793',61,1,'2023-11-10 21:02:27.915793',12,'sss','12','2662a3a7-52a8-41a7-8de9-fc8de2674ec9','12'),(0,1,0,0,0,'2023-11-10 21:03:45.663498',62,1,'2023-11-10 21:03:45.663498',12,'string','12','1b5434d5-9614-4eb5-be1c-0975adf73f0b','12'),(10000,1,5,0,10000,'2023-11-10 21:03:54.362800',63,1,'2023-11-10 21:03:54.362800',12,'삼겹살 맛있어요','12','7b376e4e-824f-4337-b55b-77fc5762451c','12'),(10000,1,5,0,10000,'2023-11-10 21:19:26.443865',64,1,'2023-11-10 21:19:26.443865',12,'삼겹살 맛있어요','12','7739fec5-df09-4e40-97e9-3c0182ee5f7a','12'),(10000,1,5,0,10000,'2023-11-10 21:47:38.126687',65,1,'2023-11-10 21:47:38.126687',12,'삼겹살 맛있어요','12','dc0e2ce6-c9ab-4567-988a-c5f546b3ee88','12'),(10000,1,5,0,10000,'2023-11-10 21:48:03.574827',66,1,'2023-11-10 21:48:03.574827',12,'삼겹살 맛있어요','12','845d2b57-47b5-46d8-9655-cf0a4a56084d','12'),(10000,1,5,0,10000,'2023-11-10 21:48:45.593781',67,1,'2023-11-10 21:48:45.593781',12,'삼겹살 맛있어요','12','d25ce23e-ad70-49f2-91c4-cd78bc6a4ce0','12'),(10000,1,5,0,10000,'2023-11-10 23:17:33.675291',68,1,'2023-11-10 23:17:33.675291',12,'삼겹살 맛있다','12','22c551ad-bc7a-4cd1-b0af-cd1594cecc36','12'),(10000,1,5,0,10000,'2023-11-10 23:17:52.712903',69,1,'2023-11-10 23:17:52.712903',12,'삼겹살 맛있다','12','9a9d9c39-a8c1-4a89-bbab-36e54543f15c','12'),(10000,1,5,0,10000,'2023-11-10 23:25:15.918274',70,1,'2023-11-10 23:25:15.918274',12,'삼겹살맛있다','12','e0dc3f08-28cd-458a-80a0-cd7d23181372','12'),(10000,1,5,0,10000,'2023-11-10 23:25:42.221050',71,1,'2023-11-10 23:25:42.221050',12,'삼겹살맛있다','12','44237cb8-4cad-45ff-854d-2c355eb18113','12'),(10000,1,5,0,10000,'2023-11-10 23:28:52.384630',72,1,'2023-11-10 23:28:52.384630',12,'삼겹살맛있다','12','bf5975e5-aee1-421f-9be7-62ed5748d8f5','12'),(5100,2,5,0,10200,'2023-11-10 23:35:32.759473',73,1,'2023-11-10 23:35:32.759473',12,'삼겹살맛있어','12','13cce21e-aa18-45d6-ad7c-adf422f3c928','12'),(10000,2,5,0,20000,'2023-11-10 23:36:51.262656',74,1,'2023-11-10 23:36:51.262656',12,'육즙이넘쳐요','12','3d61b186-5b86-413b-8ee9-1f2d9a5b2594','12'),(10000,2,5,0,20000,'2023-11-10 23:37:55.718453',75,1,'2023-11-10 23:37:55.718453',12,'맛있음','12','1ca0beeb-1999-42f1-9e1d-ffb7508f0c59','12'),(10000,2,5,0,20000,'2023-11-10 23:41:23.717679',76,1,'2023-11-10 23:41:23.717679',12,'올라가라 제발','12','5d300fad-41c3-419c-ae0f-bc515fc46133','12'),(5000,2,5,0,10000,'2023-11-10 23:54:31.876942',77,4,'2023-11-10 23:54:31.876942',12,'올라가','12','c17de2c3-8665-499e-853e-4c51b3cf9797','12'),(10000,2,5,0,20000,'2023-11-11 00:01:16.571631',78,4,'2023-11-11 00:01:16.571631',12,'맛있어요 냉면 서비스도 좋았어요','12','a61ab846-65a2-44d4-8413-1493c1ed5fc0','12'),(10000,3,5,0,30000,'2023-11-11 00:14:52.264277',79,4,'2023-11-11 00:14:52.264277',12,'삼겹살','12','917ea587-37af-4488-901e-fc6558ead4fe','12'),(10000,2,5,0,20000,'2023-11-11 00:17:04.739386',80,4,'2023-11-11 00:17:04.739386',12,'삼겹살과냉면','12','0c8afe47-ae20-4803-ab4a-2f942a643329','12'),(10000,2,5,0,20000,'2023-11-11 00:19:09.033968',81,4,'2023-11-11 00:19:09.033968',12,'테스트','12','45bff815-4744-4d77-bb92-d059a6cd2eb6','12'),(10000,2,5,0,20000,'2023-11-11 00:19:27.622602',82,4,'2023-11-11 00:19:27.622602',12,'테스트','12','9b8ef285-d72c-4308-a2ec-ad234ff5b837','12'),(10000,2,5,0,20000,'2023-11-11 00:19:40.485924',83,4,'2023-11-11 00:19:40.485924',12,'테스트','12','b00fc202-4b6d-44ae-9b0e-3c18c013db27','12'),(10000,2,5,0,20000,'2023-11-11 00:20:03.053995',84,4,'2023-11-11 00:20:03.053995',12,'테스트','12','cdbccdce-6bad-4e78-9027-fd2ff3e26b1e','12'),(500,2,5,0,1000,'2023-11-11 00:21:50.928280',85,4,'2023-11-11 00:21:50.928280',12,'테스트','12','0007e9c8-eb57-4dc8-86ed-e16ebe31fcfa','12'),(500,2,5,0,1000,'2023-11-11 00:24:41.069456',86,4,'2023-11-11 00:24:41.069456',12,'테스트','12','0af4930c-fd92-48c2-bfb3-30fdddb1f828','12'),(20000,2,5,0,40000,'2023-11-11 00:27:09.067175',87,4,'2023-11-11 00:27:09.067175',12,'너무 맛있었어요 고기가 정말 신선하고 밑반찬들도 훌륭했어요!','12','68e1435a-5a4c-4fec-bd5d-65551a32d060','12'),(13333,3,5,0,40000,'2023-11-11 00:30:34.470064',88,4,'2023-11-11 00:30:34.470064',12,'너무 맛있었어요! 고기도 정말 신선했고 밑반찬도 다양하게 잘 나왔어요','12','15b2cccf-28dd-4426-94cb-420550476f30','12'),(10000,4,5,0,40000,'2023-11-11 00:34:25.476885',89,4,'2023-11-11 00:34:25.476885',12,'너무 맛있었어요! 고기가 정말 신선했고 밑반찬도 다양하게 잘 나왔어요','12','d8e03264-a8e4-498e-9949-cd5ddebb2837','12'),(10000,4,5,0,40000,'2023-11-11 00:38:52.178286',90,4,'2023-11-11 00:38:52.178286',12,'너무 맛있었어요! 고기가 정말 신선했고 밑반찬도 다양하게 잘 나왔어요','12','a69dd2e1-41b6-4c57-b82e-078adec4c908','12'),(10000,4,5,0,40000,'2023-11-11 00:39:20.415500',91,4,'2023-11-11 00:39:20.415500',12,'너무 맛있었어요! 고기가 정말 신선했고 밑반찬도 다양하게 잘 나왔어요','12','ba330014-2625-4749-bd99-d6b1b516e1be','12');
/*!40000 ALTER TABLE `review_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_tb`
--

DROP TABLE IF EXISTS `store_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `store_tb` (
                            `avg_cost_per_person` int DEFAULT NULL,
                            `avg_rating` double NOT NULL,
                            `avg_visit_count` double DEFAULT NULL,
                            `latitude` double NOT NULL,
                            `longitude` double NOT NULL,
                            `nums_of_review` int NOT NULL,
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `sub_category_id` bigint DEFAULT NULL,
                            `address` varchar(255) NOT NULL,
                            `business_hours` varchar(255) NOT NULL,
                            `name` varchar(255) NOT NULL,
                            `phone_number` varchar(255) NOT NULL,
                            `store_image_url` varchar(255) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `FK_store_tb_sub_category_id` (`sub_category_id`),
                            CONSTRAINT `FK_store_tb_sub_category_id` FOREIGN KEY (`sub_category_id`) REFERENCES `sub_category_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_tb`
--

LOCK TABLES `store_tb` WRITE;
/*!40000 ALTER TABLE `store_tb` DISABLE KEYS */;
INSERT INTO `store_tb` VALUES (7568,3.058309037900874,9.81632653061225,37.5640065,126.983556,49,1,1,'서울 중구 명동길 25-11','06:00 - 21:00','미성옥','02-776-8929',NULL),(18000,3.8,4,37.5643309,126.984133,8,2,2,'서울 중구 명동7길 13 명동증권빌딩','11:00 - 21:00','딘타이펑 명동점','02-3789-2778',NULL),(7000,4.2,4,37.5614752,126.98283,12,3,3,'서울 중구 명동8나길 38 1층','09:00 - 21:00','어반플랜트 명동','0507-1480-0154',NULL),(9599,4.5555555555555545,2.740740740740741,37.5634232,126.9850928,27,4,4,'서울 중구 명동10길 10 명동교자','10:30 - 21:00','명동교자','0507-1443-3525',NULL),(18000,3,4,37.5650588,126.9840605,81,5,5,'서울 중구 명동9가길10 1, 2층','11:00 - 21:00','서울지짐이','02-3789-2778',NULL),(18000,4.4,4,37.56471,126.9838683,18,6,6,'서울 중구 명동7길 21','11:00 - 21:00','흑돈가 명동점','02-3789-2778',NULL),(18000,3.3,4,37.56471,126.9812345,28,7,7,'서울 중구 명동7길 21','11:00 - 21:00','박대감닭한마리','02-3789-2778',NULL),(18000,3.9,4,37.568876,126.9823532,38,8,8,'서울 중구 명동','11:00 - 21:00','음식점8','02-3789-2778',NULL),(18000,2.7,4,37.56645,126.988566,28,9,9,'서울 중구 명동7길','11:00 - 21:00','음식점9','02-3789-2778',NULL),(18000,2,4,37.566005,126.9824525,548,10,10,'서울 중구 명동7길 13','11:00 - 21:00','음식점10','02-3789-2778',NULL),(18000,4.8,4,37.56313,126.980006,23,11,11,'서울 중구','11:00 - 21:00','음식점11','02-3789-2778',NULL);
/*!40000 ALTER TABLE `store_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sub_category_tb`
--

DROP TABLE IF EXISTS `sub_category_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sub_category_tb` (
                                   `category_id` bigint DEFAULT NULL,
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `name` varchar(255) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `IDX_sub_category_tb_category_id` (`category_id`),
                                   CONSTRAINT `FK_sub_category_tb_category_id` FOREIGN KEY (`category_id`) REFERENCES `category_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sub_category_tb`
--

LOCK TABLES `sub_category_tb` WRITE;
/*!40000 ALTER TABLE `sub_category_tb` DISABLE KEYS */;
INSERT INTO `sub_category_tb` VALUES (1,1,'설렁탕/곰탕'),(2,2,'중식당'),(3,3,'디저트/카페'),(1,4,'칼국수/만두'),(1,5,'전'),(1,6,'돼지고기'),(1,7,'칼국수'),(1,8,'국밥'),(1,9,'퓨전'),(2,10,'짜장면'),(4,11,'스시');
/*!40000 ALTER TABLE `sub_category_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag_tb`
--

DROP TABLE IF EXISTS `tag_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag_tb` (
                          `locationx` double DEFAULT NULL,
                          `locationy` double DEFAULT NULL,
                          `menu_rating` int DEFAULT NULL,
                          `food_id` bigint DEFAULT NULL,
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `image_id` bigint DEFAULT NULL,
                          `tag_name` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          KEY `FK_tag_tb_food_id` (`food_id`),
                          KEY `FK_tag_tb_image_id` (`image_id`),
                          CONSTRAINT `FK_tag_tb_food_id` FOREIGN KEY (`food_id`) REFERENCES `food_tb` (`id`),
                          CONSTRAINT `FK_tag_tb_image_id` FOREIGN KEY (`image_id`) REFERENCES `image_tb` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag_tb`
--

LOCK TABLES `tag_tb` WRITE;
/*!40000 ALTER TABLE `tag_tb` DISABLE KEYS */;
INSERT INTO `tag_tb` VALUES (0,0,0,1,1,1,'string'),(0,0,0,1,2,2,'string'),(0,0,0,1,3,3,'string');
/*!40000 ALTER TABLE `tag_tb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_tb`
--

DROP TABLE IF EXISTS `user_tb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_tb` (
                           `age_group` tinyint DEFAULT NULL,
                           `email_verified` bit(1) DEFAULT NULL,
                           `is_first_login` bit(1) NOT NULL,
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `email` varchar(255) NOT NULL,
                           `gender` enum('FEMALE','MALE','UNKNOWN') DEFAULT NULL,
                           `locale` varchar(255) DEFAULT NULL,
                           `name` varchar(255) NOT NULL,
                           `profile_image_url` varchar(255) DEFAULT NULL,
                           `provider` enum('GOOGLE','KAKAO') NOT NULL,
                           `provider_id` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           CONSTRAINT `user_tb_chk_1` CHECK ((`age_group` between 0 and 6))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_tb`
--

LOCK TABLES `user_tb` WRITE;
/*!40000 ALTER TABLE `user_tb` DISABLE KEYS */;
INSERT INTO `user_tb` VALUES (2,NULL,_binary '\0',1,'nstgic3@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038773712'),(2,NULL,_binary '\0',2,'female@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',3,'user3@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',4,'user4@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',5,'user5@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',6,'user6@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',7,'user7@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',8,'user8@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',9,'user9@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',10,'user10@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',11,'user110@gmail.com','FEMALE',NULL,'ac98bef6-79c0-4a7b-b9b4-9c3e397dbbd7',NULL,'KAKAO','3038733712'),(2,NULL,_binary '\0',12,'sk980919@kakao.com','MALE',NULL,'125e1f58-b422-4dcd-ac64-c184674732ed',NULL,'KAKAO','3111484287');
/*!40000 ALTER TABLE `user_tb` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-11  0:51:07
