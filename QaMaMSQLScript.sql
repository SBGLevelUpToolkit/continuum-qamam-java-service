CREATE DATABASE  IF NOT EXISTS `ContinuumAssessment` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ContinuumAssessment`;
-- MIGRATION SCRIPT FOR DATABASE
--
-- 
-- ------------------------------------------------------
-- 

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
-- Table structure for table `ContinuumAssessmentResults`
--

DROP TABLE IF EXISTS `ContinuumAssessmentResults`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ContinuumAssessmentResults` (
  `teamName` varchar(100) DEFAULT NULL,
  `standards` int(11) DEFAULT NULL,
  `metrics` int(11) DEFAULT NULL,
  `integration` int(11) DEFAULT NULL,
  `stakeholder` int(11) DEFAULT NULL,
  `teamManagement` int(11) DEFAULT NULL,
  `documentation` int(11) DEFAULT NULL,
  `assessmentProcess` int(11) DEFAULT NULL,
  `research` int(11) DEFAULT NULL,
  `involvement` int(11) DEFAULT NULL,
  `quality` int(11) DEFAULT NULL,
  `execution` int(11) DEFAULT NULL,
  `featureteams` int(11) DEFAULT NULL,
  `dateassessed` varchar(100) DEFAULT NULL,
  `portfolio` varchar(150) DEFAULT NULL,
  `rawdata` longtext,
  UNIQUE KEY `my_unique_key` (`teamName`,`dateassessed`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

--
