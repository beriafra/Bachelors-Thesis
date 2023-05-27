-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost:3306    Database: CO3102_CW2_2022
-- ------------------------------------------------------
-- Server version	5.7.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Customer`
--
-- DROP DATABASE IF EXISTS igse;
-- CREATE DATABASE igse;
-- CustomerCustomerUSE igse;

DROP TABLE IF EXISTS `Customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Customer` (
  `customer_id` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `password_hash` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `property_type` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  `bedroom_num` int(11) NOT NULL,
  `balance` float NOT NULL,
  `type` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Customer`
--

LOCK TABLES `Customer` WRITE;
/*!40000 ALTER TABLE `Customer` DISABLE KEYS */;
INSERT INTO `Customer` VALUES ('gse@shangrila.gov.un','7d03bdc673fe753d95ef4e1fe70cec3d794e26273a75f0f93002e9ab3f6ee5ec','123 University Road','Detached',5,200,'ADMIN'),('test@gmail.com','5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5','Flat 2, 10 Vcctoria park road','Flats',3,200,'USER');
/*!40000 ALTER TABLE `Customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Reading`
--

DROP TABLE IF EXISTS `Reading`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Reading` (
  `reading_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` varchar(256) COLLATE utf8_unicode_ci NOT NULL,
  `submission_date` date NOT NULL,
  `elec_readings_day` float NOT NULL,
  `elet_reading_night` float NOT NULL,
  `gas_reading` float NOT NULL,
  `status` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`reading_id`),
  UNIQUE KEY `ReadingID_UNIQUE` (`reading_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reading`
--

LOCK TABLES `Reading` WRITE;
/*!40000 ALTER TABLE `Reading` DISABLE KEYS */;
INSERT INTO `Reading` VALUES (1,'test@gmail.com','2022-11-20',100,250,800,'paid'),(2,'test@gmail.com','2022-12-20',200,500,1600,'paid'),(3,'test@gmail.com','2023-01-20',300,800,2500,'pending');
/*!40000 ALTER TABLE `Reading` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Taiff`
--

DROP TABLE IF EXISTS `Taiff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Taiff` (
  `taiff_type` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `rate` float DEFAULT NULL,
  PRIMARY KEY (`taiff_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Taiff`
--

LOCK TABLES `Taiff` WRITE;
/*!40000 ALTER TABLE `Taiff` DISABLE KEYS */;
INSERT INTO `Taiff` VALUES ('electricity_day',0.34),('electricity_night',0.2),('gas',0.1),('sanding_charge',0.74);
/*!40000 ALTER TABLE `Taiff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Voucher`
--

DROP TABLE IF EXISTS `Voucher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Voucher` (
  `EVC_code` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `used` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`EVC_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Voucher`
--

LOCK TABLES `Voucher` WRITE;
/*!40000 ALTER TABLE `Voucher` DISABLE KEYS */;
INSERT INTO `Voucher` VALUES ('DM8LEESR',0),('NDA7SY2V',0),('RVA7DZ2D',0),('XTX2GZAD',1);
/*!40000 ALTER TABLE `Voucher` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-10 21:43:29
