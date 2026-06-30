-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: orders
-- ------------------------------------------------------
-- Server version	9.5.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '24179b67-44be-11f1-813e-cc28aa199c1d:1-245';

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `idBill` int NOT NULL AUTO_INCREMENT,
  `orderID` int DEFAULT NULL,
  `clientName` varchar(45) DEFAULT NULL,
  `productName` varchar(45) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `totalPrice` double DEFAULT NULL,
  `orderDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idBill`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (1,0,'Marius','asgr',1,123,'2026-05-02 14:52:23'),(2,0,'Marius','asgr',3,369,'2026-05-02 14:52:48'),(3,8,'Ion Popescu','Laptop Dell XPS',1,4500,'2026-05-02 19:33:54'),(4,10,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:34:04'),(5,12,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:35:58'),(6,14,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:36:02'),(7,16,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:36:02'),(8,18,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:36:03'),(9,20,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:36:03'),(10,22,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:38:54'),(11,23,'Ion Popescu','Laptop Dell XPS',1,4500,'2026-05-02 19:40:09'),(12,24,'Maria Ionescu','Mouse Logitech',2,300,'2026-05-02 19:40:17'),(13,25,'Maria Ionescu','Mouse Logitech',21,3150,'2026-05-02 19:40:24'),(14,26,'Ion Popescu','Laptop Dell XPS',1,4500,'2026-05-04 09:42:09'),(15,27,'Ion Popescu','Laptop Dell XPS',1,4500,'2026-05-11 18:45:29'),(16,27,'Ion Popescu','Laptop Dell XPS',1,4500,'2026-05-11 18:45:29'),(17,28,'Ion Popescu','Mouse Logitech',1,150,'2026-05-11 18:51:14'),(18,28,'Ion Popescu','Mouse Logitech',1,150,'2026-05-11 18:51:14'),(19,29,'Ion Popescu','Mouse Logitech',1,150,'2026-05-11 18:52:26'),(20,29,'Ion Popescu','Mouse Logitech',1,150,'2026-05-11 18:52:26'),(21,30,'Ion Popescu','Mouse Logitech',1,150,'2026-05-11 18:52:39'),(22,30,'Ion Popescu','Mouse Logitech',1,150,'2026-05-11 18:52:39'),(23,31,'Ion Popescu','Mouse Logitech',1,150,'2026-05-11 19:10:39'),(24,34,'Ion Popescu','Mouse Logitech',1,150,'2026-05-12 11:46:09'),(25,35,'Alexandru Popa','Tastatura Mecanica',2,600,'2026-05-12 12:01:16'),(26,36,'Elena Muresan','Headset Sony',1,450,'2026-05-12 12:01:27'),(27,37,'Alexandru Popa','Monitor Samsung 27\"',2,2400,'2026-05-12 12:01:52'),(28,38,'Maria Ionescu','Laptop Dell XPS',1,4500,'2026-05-15 19:17:45');
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `idClient` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`idClient`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (8,'Ion Popescu','Str. Mihai Eminescu 10, Cluj-Napoca','ion.popescu@gmail.com','0722123456'),(9,'Maria Ionescu','Str. Avram Iancu 5, Cluj-Napoca','maria.ionescu@gmail.com','0733234567'),(10,'Alexandru Popa','Bd. 21 Decembrie 15, Cluj-Napoca','alex.popa@gmail.com','0744345678'),(11,'Elena Muresan','Str. Horea 20, Cluj-Napoca','elena.muresan@gmail.com','0755456789'),(38,'Marius Furdui','Str. Unu 2, Turda','marius.furdui@gmail.com','0747123457');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `idOrder` int NOT NULL AUTO_INCREMENT,
  `clientID` int DEFAULT NULL,
  `productID` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`idOrder`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (23,8,5,1),(24,9,6,2),(27,8,5,1),(29,8,6,1),(35,10,7,2),(36,11,9,1),(37,10,8,2);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `stock` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (5,'Laptop Dell XPS',4500,5),(6,'Mouse Logitech',150,6),(7,'Tastatura Mecanica',300,28),(8,'Monitor Samsung 27\"',1200,13),(9,'Headset Sony',450,24);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-15 22:37:26
