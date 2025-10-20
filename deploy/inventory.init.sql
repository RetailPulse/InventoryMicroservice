-- MySQL dump 10.13  Distrib 9.3.0, for Linux (x86_64)
--
-- Host: localhost    Database: RPInventoryDB
-- ------------------------------------------------------
-- Server version	9.3.0

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
-- Table structure for table `inventory`
--
CREATE DATABASE IF NOT EXISTS RPInventoryDB;

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `business_entity_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `total_cost_price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=201 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (116,3,23,60,0),(117,3,2,30,0),(118,3,22,60,0),(119,3,24,60,0),(120,3,3,30,0),(121,3,1,30,0),(122,3,26,60,0),(123,3,27,30,0),(124,3,30,30,0),(125,3,29,60,0),(126,3,25,60,0),(127,3,28,30,0),(128,3,33,70,0),(129,3,31,30,0),(130,3,34,70,0),(131,3,35,70,0),(132,3,32,70,0),(133,3,36,70,0),(134,3,4,30,0),(135,3,6,30,0),(136,3,7,30,0),(137,3,5,30,0),(138,3,8,30,0),(139,3,12,70,0),(140,3,15,70,0),(141,3,14,70,0),(142,3,9,30,0),(143,3,10,30,0),(144,3,13,70,0),(145,3,16,70,0),(146,3,20,60,0),(147,3,17,60,0),(148,3,18,60,0),(149,3,21,60,0),(150,3,19,60,0),(151,1,1,30,0),(152,1,5,30,0),(153,1,4,30,0),(154,1,7,30,0),(155,1,3,30,0),(156,1,2,30,0),(157,1,8,30,0),(158,1,10,30,0),(159,1,9,30,0),(160,1,6,30,0),(161,1,14,30,0),(162,1,12,30,0),(163,1,15,30,0),(164,1,16,30,0),(165,1,29,30,0),(166,1,28,30,0),(167,1,27,30,0),(168,1,13,30,0),(169,1,30,30,0),(170,1,31,30,0),(171,1,32,30,0),(172,1,34,30,0),(173,1,33,30,0),(174,1,36,30,0),(175,1,35,30,0),(176,2,1,40,0),(177,2,5,40,0),(178,2,3,40,0),(179,2,4,40,0),(180,2,2,40,0),(181,2,6,40,0),(182,2,17,40,0),(183,2,9,40,0),(184,2,18,40,0),(185,2,7,40,0),(186,2,8,40,0),(187,2,10,40,0),(188,2,19,40,0),(189,2,20,40,0),(190,2,22,40,0),(191,2,21,40,0),(192,2,23,40,0),(193,2,24,40,0),(194,2,27,40,0),(195,2,26,40,0),(196,2,25,40,0),(197,2,28,40,0),(198,2,29,40,0),(199,2,30,40,0),(200,2,31,40,0);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transaction`
--

DROP TABLE IF EXISTS `inventory_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transaction` (
  `id` binary(16) NOT NULL,
  `cost_price_per_unit` double NOT NULL,
  `destination` bigint NOT NULL,
  `inserted_at` datetime(6) NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `source` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transaction`
--

LOCK TABLES `inventory_transaction` WRITE;
/*!40000 ALTER TABLE `inventory_transaction` DISABLE KEYS */;
INSERT INTO `inventory_transaction` VALUES (_binary '��FO�Jv�z.�J\�q\�',0,2,'2025-04-27 14:02:23.038659',22,40,3),(_binary 'Yy^\Z\�IH�#9\�\�~J',0,1,'2025-04-27 14:01:01.614377',29,30,3),(_binary '\')\�9�H3�\'\Z]�k�\�',0,1,'2025-04-27 14:01:01.524336',4,30,3),(_binary '��\�tiCC�;쇠h�',0,3,'2025-04-27 13:59:52.223978',24,100,4),(_binary '|\�4�G����\�R;�#',0,2,'2025-04-27 14:02:22.952121',7,40,3),(_binary '��\�0\�I��T�W�9',0,2,'2025-04-27 14:02:22.868488',4,40,3),(_binary '\�r�\�wC�\�\�\�\�*i',0,3,'2025-04-27 13:59:52.316686',33,100,4),(_binary '$@[ؘ\�C��*6\"�7K',0,1,'2025-04-27 14:01:01.602539',15,30,3),(_binary '$\�~ג�Hٯx�;\�\�',0,3,'2025-04-27 13:59:52.324148',32,100,4),(_binary '(a�o�KT�,\�w{m\0',0,3,'2025-04-27 13:59:52.462371',16,100,4),(_binary '.6\"\�[M�,e�fN',0,1,'2025-04-27 14:01:01.686575',36,30,3),(_binary '/\03ഞEߙr.\�\�*6\�',0,3,'2025-04-27 13:59:52.223979',23,100,4),(_binary '1�= ūEj�\�&+�H�',0,3,'2025-04-27 13:59:52.223979',1,100,4),(_binary '3{\�J5\�B��\�\�VƇ_',0,3,'2025-04-27 13:59:52.512616',21,100,4),(_binary '3�\\u_\0F��͠\�&@\\\�',0,2,'2025-04-27 14:02:23.126141',28,40,3),(_binary '6���\�M_�\�c�Ŗ�\�',0,1,'2025-04-27 14:01:01.653598',32,30,3),(_binary '>\�\�\�\�pDp���\�@',0,3,'2025-04-27 13:59:52.491236',18,100,4),(_binary '@\�\'\�@@��\�j��&�^',0,3,'2025-04-27 13:59:52.324148',36,100,4),(_binary '@\�\"M\�SJa�a7\�@\�J',0,3,'2025-04-27 13:59:52.321054',34,100,4),(_binary 'A�{\�m\�A��\�h�\�q',0,1,'2025-04-27 14:01:01.639469',13,30,3),(_binary 'H�U]T\�F\"�W5,�\�',0,3,'2025-04-27 13:59:52.320948',31,100,4),(_binary 'Ky����M���n\n\�\0�',0,3,'2025-04-27 13:59:52.224011',3,100,4),(_binary 'SX�IA\��֙aӲc,',0,1,'2025-04-27 14:01:01.614346',27,30,3),(_binary 'S�\�\r�H\�:�?2\�\Z\�',0,1,'2025-04-27 14:01:01.571087',14,30,3),(_binary 'Z\�YSfKۖ`�\�h�lb',0,1,'2025-04-27 14:01:01.614422',28,30,3),(_binary 'c/�i��A�\�\�\�(U',0,2,'2025-04-27 14:02:23.126144',30,40,3),(_binary 'c]��\�CKk��\�\�x\�v',0,3,'2025-04-27 13:59:52.278667',29,100,4),(_binary 'f\�ћ�N\0��,��x�\�',0,2,'2025-04-27 14:02:22.862977',1,40,3),(_binary 'i�>���Gn�^\�\�ʯ��',0,1,'2025-04-27 14:01:01.607851',16,30,3),(_binary 'j;QH\�yC槾lF\�w\�',0,2,'2025-04-27 14:02:22.952095',18,40,3),(_binary 'k�8YJ;�j�(��k\�',0,2,'2025-04-27 14:02:23.113466',27,40,3),(_binary 'q.�\�\�OP��1\�\�',0,3,'2025-04-27 13:59:52.364871',7,100,4),(_binary 'r\�\�\�@<�\�E/H(K\�',0,3,'2025-04-27 13:59:52.510271',19,100,4),(_binary 'r٫Ȭ�L^�\�]\�v��W',0,1,'2025-04-27 14:01:01.571211',6,30,3),(_binary 't�)�\�@;�\�3o>-',0,1,'2025-04-27 14:01:01.659813',33,30,3),(_binary 'tLunJ櫔\�\�\�x�',0,3,'2025-04-27 13:59:52.278803',25,100,4),(_binary 'u�ir(|E3�\�\�	3�\�',0,2,'2025-04-27 14:02:22.952095',9,40,3),(_binary 'u��/\�Mb�<�n!�',0,3,'2025-04-27 13:59:52.321070',35,100,4),(_binary 'v��햦CВf0�@&\�\�',0,1,'2025-04-27 14:01:01.648589',31,30,3),(_binary 'xj\��\�N���h\�L�;\0',0,2,'2025-04-27 14:02:22.870981',6,40,3),(_binary '{�<�b@E���\�$\�\�',0,3,'2025-04-27 13:59:52.362862',8,100,4),(_binary '��)}bDA9��K:vQ*',0,3,'2025-04-27 13:59:52.276180',30,100,4),(_binary '�;\�\�ecNO�\'\�Qs�\�',0,3,'2025-04-27 13:59:52.484967',17,100,4),(_binary '�V̝DE��p#\�3\�',0,1,'2025-04-27 14:01:01.717355',35,30,3),(_binary '�\�&��M\��~�\�\�',0,2,'2025-04-27 14:02:23.038658',24,40,3),(_binary '��\���N�?�\�״e\�',0,2,'2025-04-27 14:02:23.179290',31,40,3),(_binary '�|��T8C �N}w+\�Ft',0,1,'2025-04-27 14:01:01.571045',9,30,3),(_binary '�� ?\�wH1�%\�J�,@',0,2,'2025-04-27 14:02:23.115586',26,40,3),(_binary '�\�\�>�Lm�\�_{\"B$',0,1,'2025-04-27 14:01:01.571146',12,30,3),(_binary '�`�uD���a\�\�\�tv',0,2,'2025-04-27 14:02:22.870981',5,40,3),(_binary '�.y#\�I���d$<\�~9',0,2,'2025-04-27 14:02:23.036260',19,40,3),(_binary '�pK\�\�#Gj�?p��-d',0,2,'2025-04-27 14:02:23.038649',20,40,3),(_binary '�\��خ[H��\�O\�\�\nA`',0,3,'2025-04-27 13:59:52.224104',22,100,4),(_binary '�l�rj\�NA�m9��1�n',0,3,'2025-04-27 13:59:52.364871',6,100,4),(_binary '�\�-qjF�y+���\rb',0,3,'2025-04-27 13:59:52.357023',4,100,4),(_binary '�>��+3L��z44��p�',0,1,'2025-04-27 14:01:01.562462',10,30,3),(_binary '�\�o\�Z\�K��	��\��',0,1,'2025-04-27 14:01:01.659845',34,30,3),(_binary '��\\\�\�K��\�u8e�3�',0,3,'2025-04-27 13:59:52.403952',14,100,4),(_binary '�\��全I�������rr',0,2,'2025-04-27 14:02:22.871046',3,40,3),(_binary '�BQ�}aFg�\'�ܖ',0,1,'2025-04-27 14:01:01.524225',2,30,3),(_binary '�F<\n��DH��\�5\�=i',0,1,'2025-04-27 14:01:01.645939',30,30,3),(_binary '�$n\�]N٨D\Z�\n�',0,2,'2025-04-27 14:02:22.946961',17,40,3),(_binary '�\�RuB�C~�u��\rj)',0,2,'2025-04-27 14:02:23.115564',25,40,3),(_binary '\�x3%3eH��>�\�a\�Uc',0,1,'2025-04-27 14:01:01.558980',8,30,3),(_binary 'ô�`\n�E��\����,',0,2,'2025-04-27 14:02:23.038665',23,40,3),(_binary '\�@\�K>�H3��o\�\�`ѵ',0,3,'2025-04-27 13:59:52.441390',13,100,4),(_binary '\�0\�`��@:�P�oEV��',0,3,'2025-04-27 13:59:52.281086',28,100,4),(_binary 'Ӿ\�\�T�H\��-�\�T��s',0,2,'2025-04-27 14:02:22.949942',8,40,3),(_binary '\��c�!.B:���u�vD�',0,1,'2025-04-27 14:01:01.524237',7,30,3),(_binary 'Է6Ώ�EH��,\��\r�_',0,2,'2025-04-27 14:02:23.126141',29,40,3),(_binary '\�%.?\�@̬y\n�\��5',0,3,'2025-04-27 13:59:52.481669',20,100,4),(_binary '\��֥LC��)\�\��+X�',0,3,'2025-04-27 13:59:52.392958',12,100,4),(_binary '\�\�\�\�Kdy\�v؞',0,3,'2025-04-27 13:59:52.223979',2,100,4),(_binary '\�\�&�Lk�2WI��b',0,3,'2025-04-27 13:59:52.364878',5,100,4),(_binary '\�\�p?(C@ɵ\�@�I\�7\�',0,3,'2025-04-27 13:59:52.414529',9,100,4),(_binary '\�#���N/�\�\�\�B\�Y\�',0,1,'2025-04-27 14:01:01.524241',3,30,3),(_binary '\�t\�\�WHI�<p\�L#�',0,3,'2025-04-27 13:59:52.276170',26,100,4),(_binary '\�\�J��C��w\�\�ꨏ',0,1,'2025-04-27 14:01:01.521589',5,30,3),(_binary '\�G\�u\�VC��?)�ņ�',0,2,'2025-04-27 14:02:23.038939',21,40,3),(_binary '\�@\�ҔD@�KAX\�\�pC',0,3,'2025-04-27 13:59:52.403903',15,100,4),(_binary '��M\�W\�DA�p�@�x�k',0,3,'2025-04-27 13:59:52.278904',27,100,4),(_binary '�\�s\Z\�MJ·$��U�d\�',0,1,'2025-04-27 14:01:01.521589',1,30,3),(_binary '�\�~\�\�M����\�\��X�',0,2,'2025-04-27 14:02:22.871113',2,40,3),(_binary '�\�\�\�M��\�\�y\�G',0,3,'2025-04-27 13:59:52.440660',10,100,4),(_binary '��\'\�4,Ce��\�~�ء�',0,2,'2025-04-27 14:02:22.949939',10,40,3);
/*!40000 ALTER TABLE `inventory_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `barcode` varchar(255) DEFAULT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `origin` varchar(255) DEFAULT NULL,
  `rrp` double NOT NULL,
  `sku` varchar(255) NOT NULL,
  `subcategory` varchar(255) DEFAULT NULL,
  `uom` varchar(255) DEFAULT NULL,
  `vendor_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq1mafxn973ldq80m1irp3mpvq` (`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,_binary '','LEV-M-0001','LEVIS','Men','Authentic Button-Down Shirt','USA',99.9,'RP1','Top','Piece','LEV'),(2,_binary '','LEV-M-0002','LEVIS','Men','Sunset Pocket Standard Fit Shirt','USA',94.9,'RP2','Top','Piece','LEV'),(3,_binary '','LEV-M-0003','LEVIS','Men','Classic Pocket Standard Fit Shirt','USA',66.4,'RP3','Top','Piece','LEV'),(4,_binary '','LEV-M-0004','LEVIS','Men','Short-Sleeve Banded Collar Shirt','USA',84.9,'RP4','Top','Piece','LEV'),(5,_binary '','LEV-M-0005','LEVIS','Men','Classic Western Standard Fit Shirt','USA',104.9,'RP5','Top','Piece','LEV'),(6,_binary '','LEV-M-1001','LEVIS','Men','502 Taper Jeans','USA',109.9,'RP6','Jeans','Piece','LEV'),(7,_binary '','LEV-M-1002','LEVIS','Men','501 Original Lightweight Jeans','USA',129.9,'RP7','Jeans','Piece','LEV'),(8,_binary '','LEV-M-1003','LEVIS','Men','55 Relaxed Straight Lightweight Jeans','USA',159.9,'RP8','Jeans','Piece','LEV'),(9,_binary '','LEV-M-1004','LEVIS','Men','Blue Tab Men\'s 511 Slim Jeans','USA',349.9,'RP9','Jeans','Piece','LEV'),(10,_binary '','LEV-M-1005','LEVIS','Men','555 Relaxed Straight Jeans','USA',89.9,'RP10','Jeans','Piece','LEV'),(12,_binary '','LEV-F-0001','LEVIS','Women','Harlie Boyfriend Shirt','USA',89.9,'RP11','Top','Piece','LEV'),(13,_binary '','LEV-F-0002','LEVIS','Women','Mandy Long-Sleeve Blouse','USA',89.9,'RP12','Top','Piece','LEV'),(14,_binary '','LEV-F-0003','LEVIS','Women','Winona Shirt','USA',99.9,'RP13','Top','Piece','LEV'),(15,_binary '','LEV-F-0004','LEVIS','Women','Blue Tab Tuxedo Shirt','USA',199.9,'RP14','Top','Piece','LEV'),(16,_binary '','LEV-F-0005','LEVIS','Women','Darlene Utility Shirt','USA',89.9,'RP15','Top','Piece','LEV'),(17,_binary '','LEV-F-1001','LEVIS','Women','Ribcage Wide Leg Jeans','USA',149.9,'RP16','Jeans','Piece','LEV'),(18,_binary '','LEV-F-1002','LEVIS','Women','Blue Tab Plank Straight Jeans','USA',299.9,'RP17','Jeans','Piece','LEV'),(19,_binary '','LEV-F-1003','LEVIS','Women','Blue Tab Column Jeans','USA',349.9,'RP18','Jeans','Piece','LEV'),(20,_binary '','LEV-F-2001','LEVIS','Women','Icon Skirt','USA',79.9,'RP19','Skirt','Piece','LEV'),(21,_binary '','LEV-F-2002','LEVIS','Women','High-Rise Slit Skirt','USA',94.9,'RP20','Skirt','Piece','LEV'),(22,_binary '','LEV-F-8001','LEVIS','Women','Heritage Tote-All Bag','USA',59.9,'RP21','Bag','Piece','LEV'),(23,_binary '','LEV-F-8002','LEVIS','Women','Brooklyn XL Shoulder Bag','USA',89.9,'RP22','Bag','Piece','LEV'),(24,_binary '','LEV-F-8003','LEVIS','Women','Devon Crossbody Bag','USA',34.9,'RP23','Bag','Piece','LEV'),(25,_binary '','LEV-M-8001','LEVIS','Men','Icon Tote','USA',59.9,'RP24','Bag','Piece','LEV'),(26,_binary '','LEV-M-8002','LEVIS','Men','Heritage Messenger Bag','USA',54.9,'RP25','Bag','Piece','LEV'),(27,_binary '','DIE-M-1001','DIESEL','Men','1986 Larkee-Beex 009ZS','Italy',300,'RP26','Jeans','Piece','DIE'),(28,_binary '','DIE-M-1002','DIESEL','Men','2023 D-Finitive 09l23','Italy',480,'RP27','Jeans','Piece','DIE'),(29,_binary '','DIE-M-1003','DIESEL','Men','Regular 2030 D-Krooley Joggjeans 0670m','Italy',590,'RP28','Jeans','Piece','DIE'),(30,_binary '','DIE-U-0001','DIESEL','Unisex','S-Leon: Fluid crinkled shirt with logo collar','Italy',590,'RP29','Top','Piece','DIE'),(31,_binary '','DIE-M-0001','DIESEL','Men','S-Elly: Faded bowling shirt with logo prints','Italy',480,'RP30','Top','Piece','DIE'),(32,_binary '','DIE-M-0002','DIESEL','Men','S-Simply-Dnm: Shirt in cotton poplin and denim','Italy',535,'RP31','Top','Piece','DIE'),(33,_binary '','LEV-U-0002','DIESEL','Unisex','D-Giri-S Track: Zipped hoodie in dirt-effect Track Denim','Italy',630,'RP32','Top','Piece','DIE'),(34,_binary '','DIE-F-0001','DIESEL','Women','F-Slimmy-Hood-Od','Italy',480,'RP33','Top','Piece','DIE'),(35,_binary '','DIE-F-0002','DIESEL','Women','T-Ele: Ribbed T-shirt with watercolour heart D','Italy',143,'RP34','Top','Piece','DIE'),(36,_binary '','DIE-F-0003','DIESEL','Women','T-Uncuties-Short-Q2: T-shirt with Diesel Core logo','Italy',180,'RP35','Top','Piece','DIE');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

-- Dump completed on 2025-04-27 14:03:41
