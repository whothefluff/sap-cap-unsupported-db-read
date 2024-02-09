CREATE DATABASE  IF NOT EXISTS `some_schema` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `some_schema`;
-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: some_schema
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `sites`
--

DROP TABLE IF EXISTS `sites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sites` (
  `SITE` varchar(100) NOT NULL,
  `DS` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `CAT1` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `CAT2` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `ID_COUNTRY` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `LONGITUDE_WGS84` decimal(38,8) DEFAULT NULL,
  `LATITUDE_WGS84` decimal(38,8) DEFAULT NULL,
  PRIMARY KEY (`SITE`),
  KEY `idx_external_poi_ID_COUNTRY` (`ID_COUNTRY`),
  KEY `idx_external_poi_DS_CAT_L1` (`CAT1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sites`
--

LOCK TABLES `sites` WRITE;
/*!40000 ALTER TABLE `sites` DISABLE KEYS */;
INSERT INTO `sites` VALUES ('EX1','example 1','CA',NULL,'GE',7.72106800,46.97566200),('EX10','example 10','Library','Public','DE',13.40495400,52.52000700),('EX11','example 11','Park','City','US',-74.00597400,40.71277600),('EX12','example 12','Museum','Art','FR',2.35222200,48.85661400),('EX13','example 13','Restaurant','French','IT',12.49636600,41.90278300),('EX14','example 14','Beach','Secluded','AU',151.20929600,-33.86882000),('EX15','example 15','Monument','Historic','US',-77.00900300,38.88993100),('EX16','example 16','Mall','Luxury','JP',139.69170600,35.68948700),('EX17','example 17','Theater','Performing Arts','GB',-0.12775800,51.50735100),('EX18','example 18','University','Science','DE',13.40495400,52.52000700),('EX19','example 19','Library','Research','FR',2.35222200,48.85661400),('EX2','example 2','Museum','History','US',-77.00900300,38.88993100),('EX20','example 20','Park','National','CA',-112.50085400,59.90020900),('EX3','example 3','Park','National','CA',-106.34677100,56.13036600),('EX4','example 4','Restaurant','Italian','IT',12.49636600,41.90278300),('EX5','example 5','Beach',NULL,'AU',133.77513600,-25.27439800),('EX6','example 6','Monument','Memorial','FR',2.35222200,48.85661400),('EX7','example 7','Mall','Shopping','JP',139.69170600,35.68948700),('EX8','example 8','Theater','Cinema','IN',77.20902100,28.61393900),('EX9','example 9','University','Education','GB',-0.12775800,51.50735100);
/*!40000 ALTER TABLE `sites` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-09 20:49:57
