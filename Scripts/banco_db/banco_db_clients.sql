CREATE DATABASE  IF NOT EXISTS `banco_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `banco_db`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: banco_db
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clients` (
  `idCliente` bigint NOT NULL AUTO_INCREMENT,
  `celular` varchar(255) NOT NULL,
  `codigoCliente` varchar(255) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `estado` enum('ACTIVO','INACTIVO') DEFAULT NULL,
  `fechaIngreso` datetime(6) NOT NULL,
  `fechaNacimiento` datetime(6) NOT NULL,
  `numeroIdentificacion` varchar(255) NOT NULL,
  `primerApellido` varchar(255) DEFAULT NULL,
  `primerNombre` varchar(255) DEFAULT NULL,
  `segundoApellido` varchar(255) DEFAULT NULL,
  `segundoNombre` varchar(50) DEFAULT NULL,
  `tipoIdentificacion` enum('C','P') DEFAULT NULL,
  PRIMARY KEY (`idCliente`),
  UNIQUE KEY `UK_238c3ihjlsr1lx21pt0fbw1a2` (`codigoCliente`),
  UNIQUE KEY `UK_321lngldsbo7ouj3o95pul9g7` (`correo`),
  UNIQUE KEY `UK_xix6qgm3kcqs34wfho121jc2` (`numeroIdentificacion`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'0954887845','CLI-1768271712038','jaime123@gmail.com','ACTIVO','2026-01-13 02:35:12.038000','1990-01-15 05:03:00.000000','1725364578','García','Jaime','López','Fernando','C'),(2,'0954887847','CLI-1768271712324','jose123@gmail.com','ACTIVO','2026-01-13 02:35:12.324000','1985-01-22 05:07:00.000000','A123','Martínez','José','González','Luis','P');
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-12 21:43:50
