-- MySQL dump 10.13  Distrib 5.5.32, for osx10.6 (i386)
--
-- Host: localhost    Database: testadmin
-- ------------------------------------------------------
-- Server version	5.5.32

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
-- Table structure for table `actor2assertion`
--

DROP TABLE IF EXISTS `actor2assertion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actor2assertion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `actorID` varchar(255) DEFAULT NULL,
  `assertion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `assertion__idx` (`assertion`),
  CONSTRAINT `actor2assertion_ibfk_1` FOREIGN KEY (`assertion`) REFERENCES `assertion` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actor2assertion`
--

LOCK TABLES `actor2assertion` WRITE;
/*!40000 ALTER TABLE `actor2assertion` DISABLE KEYS */;
/*!40000 ALTER TABLE `actor2assertion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anatomy2assertion`
--

DROP TABLE IF EXISTS `anatomy2assertion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anatomy2assertion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `anatomy_term` int(11) DEFAULT NULL,
  `assertion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `anatomy_term__idx` (`anatomy_term`),
  KEY `assertion__idx` (`assertion`),
  CONSTRAINT `anatomy2assertion_ibfk_1` FOREIGN KEY (`anatomy_term`) REFERENCES `anatomy_term` (`id`) ON DELETE CASCADE,
  CONSTRAINT `anatomy2assertion_ibfk_2` FOREIGN KEY (`assertion`) REFERENCES `assertion` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anatomy2assertion`
--

LOCK TABLES `anatomy2assertion` WRITE;
/*!40000 ALTER TABLE `anatomy2assertion` DISABLE KEYS */;
/*!40000 ALTER TABLE `anatomy2assertion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anatomy_term`
--

DROP TABLE IF EXISTS `anatomy_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `anatomy_term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `spd_id` varchar(255) DEFAULT NULL,
  `obo_id` varchar(255) DEFAULT NULL,
  `generated_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anatomy_term`
--

LOCK TABLES `anatomy_term` WRITE;
/*!40000 ALTER TABLE `anatomy_term` DISABLE KEYS */;
INSERT INTO `anatomy_term` VALUES (1,'leg 1','0000021','','');
/*!40000 ALTER TABLE `anatomy_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assertion`
--

DROP TABLE IF EXISTS `assertion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assertion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publication` int(11) DEFAULT NULL,
  `publication_behavior` varchar(255) DEFAULT NULL,
  `behavior_term` int(11) DEFAULT NULL,
  `publication_taxon` varchar(255) DEFAULT NULL,
  `taxon` int(11) DEFAULT NULL,
  `publication_anatomy` varchar(255) DEFAULT NULL,
  `evidence` int(11) DEFAULT NULL,
  `generated_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `publication__idx` (`publication`),
  KEY `taxon__idx` (`taxon`),
  CONSTRAINT `assertion_ibfk_1` FOREIGN KEY (`publication`) REFERENCES `publication` (`id`) ON DELETE CASCADE,
  CONSTRAINT `assertion_ibfk_2` FOREIGN KEY (`taxon`) REFERENCES `taxon` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assertion`
--

LOCK TABLES `assertion` WRITE;
/*!40000 ALTER TABLE `assertion` DISABLE KEYS */;
INSERT INTO `assertion` VALUES (1,1,'',NULL,'Tetragnatha straminea',1,'',NULL,'');
/*!40000 ALTER TABLE `assertion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authority` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `domain` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `domain__idx` (`domain`),
  CONSTRAINT `authority_ibfk_1` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES (1,'NCBI','',3),(2,'Marcus Chibucos','MChibucos@som.umaryland.edu',4),(3,'Martin Ramirez','ramirez@macn.gov.ar',2),(4,'George Gkoutos','geg18@aber.ac.uk',1);
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `behavior2assertion`
--

DROP TABLE IF EXISTS `behavior2assertion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `behavior2assertion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `behavior` int(11) DEFAULT NULL,
  `assertion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `behavior__idx` (`behavior`),
  KEY `assertion__idx` (`assertion`),
  CONSTRAINT `behavior2assertion_ibfk_1` FOREIGN KEY (`behavior`) REFERENCES `behavior_term` (`id`) ON DELETE CASCADE,
  CONSTRAINT `behavior2assertion_ibfk_2` FOREIGN KEY (`assertion`) REFERENCES `assertion` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `behavior2assertion`
--

LOCK TABLES `behavior2assertion` WRITE;
/*!40000 ALTER TABLE `behavior2assertion` DISABLE KEYS */;
/*!40000 ALTER TABLE `behavior2assertion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `behavior_synonym`
--

DROP TABLE IF EXISTS `behavior_synonym`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `behavior_synonym` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `primary_term` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `primary_term__idx` (`primary_term`),
  CONSTRAINT `behavior_synonym_ibfk_1` FOREIGN KEY (`primary_term`) REFERENCES `behavior_term` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `behavior_synonym`
--

LOCK TABLES `behavior_synonym` WRITE;
/*!40000 ALTER TABLE `behavior_synonym` DISABLE KEYS */;
/*!40000 ALTER TABLE `behavior_synonym` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `behavior_term`
--

DROP TABLE IF EXISTS `behavior_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `behavior_term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `nbo_id` varchar(255) DEFAULT NULL,
  `obo_id` varchar(255) DEFAULT NULL,
  `abo_id` varchar(255) DEFAULT NULL,
  `generated_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `behavior_term`
--

LOCK TABLES `behavior_term` WRITE;
/*!40000 ALTER TABLE `behavior_term` DISABLE KEYS */;
INSERT INTO `behavior_term` VALUES (1,'courtship behavior','NBO:0000077','','','');
/*!40000 ALTER TABLE `behavior_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain`
--

DROP TABLE IF EXISTS `domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `domain` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain`
--

LOCK TABLES `domain` WRITE;
/*!40000 ALTER TABLE `domain` DISABLE KEYS */;
INSERT INTO `domain` VALUES (1,'behavior'),(2,'anatomy'),(3,'taxonomy'),(4,'evidence');
/*!40000 ALTER TABLE `domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evidence_code`
--

DROP TABLE IF EXISTS `evidence_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evidence_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `long_name` varchar(255) DEFAULT NULL,
  `obo_id` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evidence_code`
--

LOCK TABLES `evidence_code` WRITE;
/*!40000 ALTER TABLE `evidence_code` DISABLE KEYS */;
INSERT INTO `evidence_code` VALUES (1,'traceable author statement','ECO:0000033','TAS');
/*!40000 ALTER TABLE `evidence_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ontology_processing`
--

DROP TABLE IF EXISTS `ontology_processing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ontology_processing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ontology_processing`
--

LOCK TABLES `ontology_processing` WRITE;
/*!40000 ALTER TABLE `ontology_processing` DISABLE KEYS */;
INSERT INTO `ontology_processing` VALUES (1,'OWL ontology'),(2,'OBO ontology'),(3,'NCBI taxonomy');
/*!40000 ALTER TABLE `ontology_processing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ontology_source`
--

DROP TABLE IF EXISTS `ontology_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ontology_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `source_url` varchar(255) DEFAULT NULL,
  `processing` int(11) DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  `domain` int(11) DEFAULT NULL,
  `authority` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `processing__idx` (`processing`),
  KEY `domain__idx` (`domain`),
  KEY `authority__idx` (`authority`),
  CONSTRAINT `ontology_source_ibfk_1` FOREIGN KEY (`processing`) REFERENCES `ontology_processing` (`id`) ON DELETE CASCADE,
  CONSTRAINT `ontology_source_ibfk_2` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `ontology_source_ibfk_3` FOREIGN KEY (`authority`) REFERENCES `authority` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ontology_source`
--

LOCK TABLES `ontology_source` WRITE;
/*!40000 ALTER TABLE `ontology_source` DISABLE KEYS */;
INSERT INTO `ontology_source` VALUES (1,'Evidence Codes (ECO)','http://purl.obolibrary.org/obo/eco.owl',1,'2013-11-08 17:28:59',4,2),(2,'NCBI Taxonomy','file:///Users/pmidford/temp/ncbitaxon.owl',3,'2013-11-08 17:39:13',3,1),(3,'Spider Anatomy','http://purl.obolibrary.org/obo/spd.owl',1,'2013-11-08 17:39:44',2,3),(4,'NeuroBehavior Ontology','http://behavior-ontology.googlecode.com/svn/trunk/behavior.owl',1,'2013-11-08 17:40:23',1,4);
/*!40000 ALTER TABLE `ontology_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant2assertion`
--

DROP TABLE IF EXISTS `participant2assertion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant2assertion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `participantclass` int(11) DEFAULT NULL,
  `participantID` varchar(255) DEFAULT NULL,
  `assertion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `participantclass__idx` (`participantclass`),
  KEY `assertion__idx` (`assertion`),
  CONSTRAINT `participant2assertion_ibfk_1` FOREIGN KEY (`participantclass`) REFERENCES `taxon` (`id`) ON DELETE CASCADE,
  CONSTRAINT `participant2assertion_ibfk_2` FOREIGN KEY (`assertion`) REFERENCES `assertion` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant2assertion`
--

LOCK TABLES `participant2assertion` WRITE;
/*!40000 ALTER TABLE `participant2assertion` DISABLE KEYS */;
/*!40000 ALTER TABLE `participant2assertion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publication`
--

DROP TABLE IF EXISTS `publication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publication` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publication_type` varchar(31) DEFAULT NULL,
  `dispensation` varchar(31) DEFAULT NULL,
  `downloaded` date DEFAULT NULL,
  `reviewed` date DEFAULT NULL,
  `title` longtext,
  `alternate_title` longtext,
  `author_list` longtext,
  `editor_list` longtext,
  `source_publication` varchar(255) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `issue` varchar(255) DEFAULT NULL,
  `serial_identifier` varchar(255) DEFAULT NULL,
  `page_range` varchar(255) DEFAULT NULL,
  `publication_date` varchar(255) DEFAULT NULL,
  `publication_year` varchar(255) DEFAULT NULL,
  `doi` varchar(255) DEFAULT NULL,
  `generated_id` varchar(255) DEFAULT NULL,
  `curation_status` int(11) DEFAULT NULL,
  `curation_update` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publication`
--

LOCK TABLES `publication` WRITE;
/*!40000 ALTER TABLE `publication` DISABLE KEYS */;
INSERT INTO `publication` VALUES (1,'Journal','Downloaded','2011-04-18','2011-04-23','Habitat distribution, life history and behavior of Tetragnatha spider species in the Great Smoky Mountains National Park.','','Aiken, Marie; Coyle, Frederick A.','','Journal of Arachnology',28,'1','','97-106','','2000','http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2','',4,'2013-12-02 08:35:12');
/*!40000 ALTER TABLE `publication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxon`
--

DROP TABLE IF EXISTS `taxon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `ncbi_id` varchar(255) DEFAULT NULL,
  `ottol_id` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `generated_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxon`
--

LOCK TABLES `taxon` WRITE;
/*!40000 ALTER TABLE `taxon` DISABLE KEYS */;
INSERT INTO `taxon` VALUES (1,'Tetragnatha straminea','336608','','Emerton','1884','');
/*!40000 ALTER TABLE `taxon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taxon_synonym`
--

DROP TABLE IF EXISTS `taxon_synonym`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `taxon_synonym` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `valid_name` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `valid_name__idx` (`valid_name`),
  CONSTRAINT `taxon_synonym_ibfk_1` FOREIGN KEY (`valid_name`) REFERENCES `taxon` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxon_synonym`
--

LOCK TABLES `taxon_synonym` WRITE;
/*!40000 ALTER TABLE `taxon_synonym` DISABLE KEYS */;
/*!40000 ALTER TABLE `taxon_synonym` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `term`
--

DROP TABLE IF EXISTS `term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` varchar(255) DEFAULT NULL,
  `domain` int(11) DEFAULT NULL,
  `authority` int(11) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `generated_id` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `domain__idx` (`domain`),
  KEY `authority__idx` (`authority`),
  CONSTRAINT `term_ibfk_1` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `term_ibfk_2` FOREIGN KEY (`authority`) REFERENCES `authority` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=58149 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `term`
--

LOCK TABLES `term` WRITE;
/*!40000 ALTER TABLE `term` DISABLE KEYS */;
INSERT INTO `term` VALUES (1,'http://purl.obolibrary.org/obo/NCBITaxon_336608',3,NULL,'Tetragnatha straminea','',NULL);
/*!40000 ALTER TABLE `term` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-12-02 17:14:55
