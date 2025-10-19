CREATE DATABASE  IF NOT EXISTS `pos_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pos_db`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: pos_db
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Table structure for table `invoice_items`
--

DROP TABLE IF EXISTS `invoice_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` int DEFAULT NULL,
  `item_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `invoice_id` (`invoice_id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `invoice_items_ibfk_1` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
  CONSTRAINT `invoice_items_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `items` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_items`
--

LOCK TABLES `invoice_items` WRITE;
/*!40000 ALTER TABLE `invoice_items` DISABLE KEYS */;
INSERT INTO `invoice_items` VALUES (3,2,5,100,20.00,2000.00),(4,2,4,50,130.00,6500.00),(5,2,5,100,20.00,2000.00),(6,2,4,50,130.00,6500.00),(7,3,6,50,60.00,3000.00),(8,3,5,50,20.00,1000.00),(9,3,6,50,60.00,3000.00),(10,3,5,50,20.00,1000.00),(11,4,6,100,60.00,6000.00),(12,4,7,100,35.00,3500.00),(13,4,6,100,60.00,6000.00),(14,4,7,100,35.00,3500.00),(15,5,6,1000,60.00,60000.00),(16,5,4,50,130.00,6500.00),(17,5,5,100,20.00,2000.00),(18,5,6,1000,60.00,60000.00),(19,5,4,50,130.00,6500.00),(20,5,5,100,20.00,2000.00);
/*!40000 ALTER TABLE `invoice_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_number` varchar(50) NOT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `billing_type` varchar(20) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Active',
  PRIMARY KEY (`id`),
  UNIQUE KEY `invoice_number` (`invoice_number`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,'INV-e79fb717','2025-10-19 18:23:04','Retail',8500.00,0.00,8500.00,'Cancelled'),(2,'INV-5edbdc84','2025-10-19 18:39:01','Retail',8500.00,0.00,8500.00,'Cancelled'),(3,'INV-463c0332','2025-10-19 20:40:37','Retail',4000.00,1000.00,3000.00,'Cancelled'),(4,'INV-1a78db73','2025-10-19 22:10:10','Retail',9500.00,5000.00,4500.00,'Cancelled'),(5,'INV-04ff6df1','2025-10-19 22:24:10','Retail',68500.00,5000.00,63500.00,'Active');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_code` varchar(50) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL,
  `wholesale_price` decimal(10,2) DEFAULT NULL,
  `retail_price` decimal(10,2) DEFAULT NULL,
  `label_price` decimal(10,2) DEFAULT NULL,
  `credit_price` decimal(10,2) DEFAULT NULL,
  `status` varchar(10) DEFAULT 'Active',
  `image_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_code` (`item_code`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (4,'Book001','Atlas CR 120pgs','Books',150.00,0.00,130.00,0.00,0.00,'Active',NULL),(5,'Pen002','Atlas Chooty','Pen',25.00,15.00,20.00,25.00,22.00,'Active',NULL),(6,'Book002','Promate CR single rule 80pg','Books',80.00,75.00,60.00,100.00,70.00,'Active',NULL),(7,'Pen001','Atlas GEL','Pen',45.00,30.00,35.00,45.00,40.00,'Active',NULL);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','123456');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-19 23:53:55
