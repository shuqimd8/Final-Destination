-- MySQL dump 10.13  Distrib 8.0.43, for macos15 (x86_64)
--
-- Host: 127.0.0.1    Database: final_destination
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `buckets`
--

DROP TABLE IF EXISTS `buckets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buckets` (
  `bucket_ID` int NOT NULL,
  `bucket_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`bucket_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buckets`
--

LOCK TABLES `buckets` WRITE;
/*!40000 ALTER TABLE `buckets` DISABLE KEYS */;
/*!40000 ALTER TABLE `buckets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food_words`
--

DROP TABLE IF EXISTS `food_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food_words` (
  `word_ID` int NOT NULL AUTO_INCREMENT,
  `word_string` varchar(45) DEFAULT NULL,
  `bucket` int DEFAULT NULL,
  `category` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`word_ID`),
  KEY `bucket_idx` (`bucket`),
  CONSTRAINT `bucket` FOREIGN KEY (`bucket`) REFERENCES `buckets` (`bucket_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food_words`
--

LOCK TABLES `food_words` WRITE;
/*!40000 ALTER TABLE `food_words` DISABLE KEYS */;
/*!40000 ALTER TABLE `food_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `game_ID` int NOT NULL AUTO_INCREMENT,
  `student` varchar(45) NOT NULL,
  `correct_answers` int DEFAULT '0',
  `incorrect_answers` int DEFAULT '0',
  `total_rounds` int DEFAULT '0',
  `score` int DEFAULT '0',
  `avg_answer_time` int DEFAULT '0',
  PRIMARY KEY (`game_ID`),
  KEY `student_idx` (`student`),
  CONSTRAINT `student` FOREIGN KEY (`student`) REFERENCES `Students` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `levels`
--

DROP TABLE IF EXISTS `levels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `levels` (
  `level_name` varchar(20) NOT NULL,
  `min_score` int DEFAULT NULL,
  `max_score` int DEFAULT NULL,
  `image` longtext,
  PRIMARY KEY (`level_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `levels`
--

LOCK TABLES `levels` WRITE;
/*!40000 ALTER TABLE `levels` DISABLE KEYS */;
/*!40000 ALTER TABLE `levels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rounds`
--

DROP TABLE IF EXISTS `rounds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rounds` (
  `round_ID` int NOT NULL AUTO_INCREMENT,
  `round_number` int DEFAULT NULL,
  `game_ID` int DEFAULT NULL,
  `correct?` tinyint DEFAULT '0',
  `answer_time` int DEFAULT NULL,
  PRIMARY KEY (`round_ID`),
  UNIQUE KEY `round_ID_UNIQUE` (`round_ID`),
  KEY `game_idx` (`game_ID`),
  CONSTRAINT `game` FOREIGN KEY (`game_ID`) REFERENCES `games` (`game_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rounds`
--

LOCK TABLES `rounds` WRITE;
/*!40000 ALTER TABLE `rounds` DISABLE KEYS */;
/*!40000 ALTER TABLE `rounds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_statistics`
--

DROP TABLE IF EXISTS `student_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_statistics` (
  `stats_ID` int NOT NULL,
  `correct_answers` int DEFAULT NULL,
  `incorrect_answers` int DEFAULT NULL,
  `average_correct_answers` float DEFAULT NULL,
  `average_incorrect_answers` float DEFAULT NULL,
  `total_games` int DEFAULT NULL,
  `total_words` int DEFAULT NULL,
  `average_answer_speed` float DEFAULT NULL,
  `average_score` float DEFAULT NULL,
  `student_user` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`stats_ID`),
  KEY `student_user_idx` (`student_user`),
  CONSTRAINT `student_user` FOREIGN KEY (`student_user`) REFERENCES `Students` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_statistics`
--

LOCK TABLES `student_statistics` WRITE;
/*!40000 ALTER TABLE `student_statistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `student_statistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Students`
--

DROP TABLE IF EXISTS `Students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Students` (
  `username` varchar(30) NOT NULL,
  `hashed_password` varchar(45) DEFAULT NULL,
  `f_name` varchar(45) DEFAULT NULL,
  `overall_score` int DEFAULT NULL,
  `high_score` int DEFAULT NULL,
  `level` varchar(45) DEFAULT NULL,
  `teacher` int DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `level_idx` (`level`),
  KEY `teacher_idx` (`teacher`),
  CONSTRAINT `level` FOREIGN KEY (`level`) REFERENCES `levels` (`level_name`),
  CONSTRAINT `teacher` FOREIGN KEY (`teacher`) REFERENCES `teachers` (`teacher_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Students`
--

LOCK TABLES `Students` WRITE;
/*!40000 ALTER TABLE `Students` DISABLE KEYS */;
INSERT INTO `Students` VALUES ('Test01','TestPass25','Tester',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `Students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teachers`
--

DROP TABLE IF EXISTS `teachers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teachers` (
  `teacher_ID` int NOT NULL,
  `hashed_password` varchar(45) DEFAULT NULL,
  `f_name` varchar(45) DEFAULT NULL,
  `l_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`teacher_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachers`
--

LOCK TABLES `teachers` WRITE;
/*!40000 ALTER TABLE `teachers` DISABLE KEYS */;
INSERT INTO `teachers` VALUES (1,'TestPass20','Mister','Tester');
/*!40000 ALTER TABLE `teachers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'final_destination'
--

--
-- Dumping routines for database 'final_destination'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-30 10:59:46
