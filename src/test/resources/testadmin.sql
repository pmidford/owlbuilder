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
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_name` varchar(63) DEFAULT NULL,
  `given_names` varchar(63) DEFAULT NULL,
  `assigned_id` varchar(512) DEFAULT NULL,
  `generated_id` varchar(512) DEFAULT NULL,
  `merge_set` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `merge_set__idx` (`merge_set`),
  CONSTRAINT `author_ibfk_1` FOREIGN KEY (`merge_set`) REFERENCES `author_merge` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (100,'Abrenica-Adamat','Liza R.',NULL,NULL,NULL),(101,'Torres','Mark Anthony J.',NULL,NULL,NULL);
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `author_merge`
--

DROP TABLE IF EXISTS `author_merge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author_merge` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `preferred` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `preferred__idx` (`preferred`),
  CONSTRAINT `author_merge_ibfk_1` FOREIGN KEY (`preferred`) REFERENCES `author` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author_merge`
--

LOCK TABLES `author_merge` WRITE;
/*!40000 ALTER TABLE `author_merge` DISABLE KEYS */;
/*!40000 ALTER TABLE `author_merge` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES (1,'NCBI','',3),(2,'Marcus Chibucos','MChibucos@som.umaryland.edu',4),(3,'Martin Ramirez','ramirez@macn.gov.ar',2),(4,'George Gkoutos','geg18@aber.ac.uk',1),(5,'Gene Ontology Consortium','',5),(6,'Janna Hastings','',6),(7,'Chris Mungall','',8);
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `authorship`
--

DROP TABLE IF EXISTS `authorship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `authorship` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publication` int(11) DEFAULT NULL,
  `author` int(11) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `publication__idx` (`publication`),
  KEY `author__idx` (`author`),
  CONSTRAINT `authorship_ibfk_1` FOREIGN KEY (`publication`) REFERENCES `publication` (`id`) ON DELETE CASCADE,
  CONSTRAINT `authorship_ibfk_2` FOREIGN KEY (`author`) REFERENCES `author` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authorship`
--

LOCK TABLES `authorship` WRITE;
/*!40000 ALTER TABLE `authorship` DISABLE KEYS */;
/*!40000 ALTER TABLE `authorship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `claim`
--

DROP TABLE IF EXISTS `claim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `claim` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publication` int(11) DEFAULT NULL,
  `behavior_term` int(11) DEFAULT NULL,
  `evidence` int(11) DEFAULT NULL,
  `primary_participant` int(11) DEFAULT NULL,
  `generated_id` varchar(512) DEFAULT NULL,
  `publication_behavior` varchar(512) DEFAULT NULL,
  `narrative` int(11) DEFAULT NULL,
  `uidset` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `publication__idx` (`publication`),
  KEY `primary_participant__idx` (`primary_participant`),
  KEY `narrative__idx` (`narrative`),
  KEY `uidset__idx` (`uidset`),
  CONSTRAINT `claim_ibfk_1` FOREIGN KEY (`publication`) REFERENCES `publication` (`id`) ON DELETE CASCADE,
  CONSTRAINT `claim_ibfk_2` FOREIGN KEY (`primary_participant`) REFERENCES `participant` (`id`) ON DELETE CASCADE,
  CONSTRAINT `claim_ibfk_3` FOREIGN KEY (`narrative`) REFERENCES `narrative` (`id`) ON DELETE CASCADE,
  CONSTRAINT `claim_ibfk_4` FOREIGN KEY (`uidset`) REFERENCES `uidset` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `claim`
--

LOCK TABLES `claim` WRITE;
/*!40000 ALTER TABLE `claim` DISABLE KEYS */;
INSERT INTO `claim` VALUES (1,3,11398,0,NULL,'','Stick-like posture',NULL,3),(2,3,11132,0,NULL,'http://arachb.org/arachb/ARACHB_0000321','prey capture',NULL,4),(26,123,11052,0,NULL,'http://arachb.org/arachb/ARACHB_0000343','turn toward male',1,5);
/*!40000 ALTER TABLE `claim` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain`
--

LOCK TABLES `domain` WRITE;
/*!40000 ALTER TABLE `domain` DISABLE KEYS */;
INSERT INTO `domain` VALUES (1,'behavior'),(2,'anatomy'),(3,'taxonomy'),(4,'evidence'),(5,'Gene Products'),(6,'Chemistry'),(7,'Qualities'),(8,'Bilateralian Anatomy');
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
-- Table structure for table `individual`
--

DROP TABLE IF EXISTS `individual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `individual` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` varchar(512) DEFAULT NULL,
  `generated_id` varchar(512) DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `term` int(11) DEFAULT NULL,
  `uidset` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `term__idx` (`term`),
  KEY `uidset__idx` (`uidset`),
  CONSTRAINT `individual_ibfk_1` FOREIGN KEY (`term`) REFERENCES `term` (`id`) ON DELETE CASCADE,
  CONSTRAINT `individual_ibfk_2` FOREIGN KEY (`uidset`) REFERENCES `uidset` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `individual`
--

LOCK TABLES `individual` WRITE;
/*!40000 ALTER TABLE `individual` DISABLE KEYS */;
INSERT INTO `individual` VALUES (94,NULL,'http://arachb.org/arachb/ARACHB_0000001','female',111938,6),(95,NULL,'http://arachb.org/arachb/ARACHB_0000411','whole organism of female',10473,7);
/*!40000 ALTER TABLE `individual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `individual2narrative`
--

DROP TABLE IF EXISTS `individual2narrative`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `individual2narrative` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `individual` int(11) DEFAULT NULL,
  `narrative` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `individual__idx` (`individual`),
  KEY `narrative__idx` (`narrative`),
  CONSTRAINT `individual2narrative_ibfk_1` FOREIGN KEY (`individual`) REFERENCES `individual` (`id`) ON DELETE CASCADE,
  CONSTRAINT `individual2narrative_ibfk_2` FOREIGN KEY (`narrative`) REFERENCES `narrative` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `individual2narrative`
--

LOCK TABLES `individual2narrative` WRITE;
/*!40000 ALTER TABLE `individual2narrative` DISABLE KEYS */;
INSERT INTO `individual2narrative` VALUES (38,94,1),(39,95,1),(40,104,1),(41,105,1),(42,107,2);
/*!40000 ALTER TABLE `individual2narrative` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `narrative`
--

DROP TABLE IF EXISTS `narrative`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `narrative` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publication` int(11) DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `generated_id` varchar(512) DEFAULT NULL,
  `uidset` int(11) DEFAULT NULL,
  `behavior_annotation` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `publication__idx` (`publication`),
  KEY `uidset__idx` (`uidset`),
  KEY `behavior_annotation__idx` (`behavior_annotation`),
  CONSTRAINT `narrative_ibfk_1` FOREIGN KEY (`publication`) REFERENCES `publication` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `narrative_ibfk_2` FOREIGN KEY (`uidset`) REFERENCES `uidset` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `narrative_ibfk_3` FOREIGN KEY (`behavior_annotation`) REFERENCES `term` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `narrative`
--

LOCK TABLES `narrative` WRITE;
/*!40000 ALTER TABLE `narrative` DISABLE KEYS */;
INSERT INTO `narrative` VALUES (1,123,'courtship sequence','','http://arachb.org/arachb/ARACHB_0000424',8,11059),(2,123,'sperm induction','','http://arachb.org/arachb/ARACHB_0000425',9,11059);
/*!40000 ALTER TABLE `narrative` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ontology_source`
--

LOCK TABLES `ontology_source` WRITE;
/*!40000 ALTER TABLE `ontology_source` DISABLE KEYS */;
INSERT INTO `ontology_source` VALUES (1,'Evidence Codes (ECO)','http://purl.obolibrary.org/obo/eco.owl',1,'2013-11-08 17:28:59',4,2),(2,'NCBI Taxonomy','file:///Users/pmidford/temp/ncbitaxon.owl',3,'2013-11-08 17:39:13',3,1),(3,'Spider Anatomy','http://purl.obolibrary.org/obo/spd.owl',1,'2013-11-08 17:39:44',2,3),(4,'NeuroBehavior Ontology','http://behavior-ontology.googlecode.com/svn/trunk/behavior.owl',1,'2013-11-08 17:40:23',1,4),(5,'Gene Ontology','http://purl.obolibrary.org/obo/go.owl',1,'2013-12-07 11:22:12',5,5),(6,'Phenotype and Trait Ontology','http://purl.obolibrary.org/obo/pato.owl',1,'2013-12-07 11:22:12',7,3),(7,'CHEBI','http://purl.obolibrary.org/obo/chebi.owl',1,'2013-12-07 11:22:12',6,6),(8,'Uberon','http://purl.obolibrary.org/obo/uberon.owl',1,'2013-12-07 11:22:12',8,7);
/*!40000 ALTER TABLE `ontology_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant`
--

DROP TABLE IF EXISTS `participant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taxon` int(11) DEFAULT NULL,
  `substrate` int(11) DEFAULT NULL,
  `anatomy` int(11) DEFAULT NULL,
  `quantification` varchar(16) DEFAULT NULL,
  `generated_id` varchar(512) DEFAULT NULL,
  `publication_taxon` varchar(512) DEFAULT NULL,
  `label` varchar(512) DEFAULT NULL,
  `publication_anatomy` varchar(512) DEFAULT NULL,
  `publication_substrate` varchar(512) DEFAULT NULL,
  `publication_text` varchar(512) DEFAULT NULL,
  `participation_property` int(11) DEFAULT NULL,
  `head_element` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `taxon__idx` (`taxon`),
  KEY `substrate__idx` (`substrate`),
  KEY `anatomy__idx` (`anatomy`),
  KEY `participation_property__idx` (`participation_property`),
  KEY `head_element__idx` (`head_element`),
  CONSTRAINT `participant_ibfk_1` FOREIGN KEY (`taxon`) REFERENCES `term` (`id`) ON DELETE CASCADE,
  CONSTRAINT `participant_ibfk_2` FOREIGN KEY (`substrate`) REFERENCES `term` (`id`) ON DELETE CASCADE,
  CONSTRAINT `participant_ibfk_3` FOREIGN KEY (`anatomy`) REFERENCES `term` (`id`) ON DELETE CASCADE,
  CONSTRAINT `participant_ibfk_4` FOREIGN KEY (`participation_property`) REFERENCES `property` (`id`) ON DELETE CASCADE,
  CONSTRAINT `participant_ibfk_5` FOREIGN KEY (`head_element`) REFERENCES `participant_element` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant`
--

LOCK TABLES `participant` WRITE;
/*!40000 ALTER TABLE `participant` DISABLE KEYS */;
INSERT INTO `participant` VALUES (1,4838,NULL,10473,'some',NULL,'Tetragnatha straminea','','','',NULL,NULL,1),(2,4838,NULL,10473,'some',NULL,'Tetragnatha straminea','','','',NULL,NULL,51),(29,111938,NULL,10473,'individual','http://arachb.org/arachb/ARACHB_0000349','Leucauge mariana','female','female','male',NULL,NULL,62);
/*!40000 ALTER TABLE `participant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant2claim`
--

DROP TABLE IF EXISTS `participant2claim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant2claim` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `claim` int(11) DEFAULT NULL,
  `participant` int(11) DEFAULT NULL,
  `property` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `participant__idx` (`participant`),
  CONSTRAINT `participant2claim_ibfk_1` FOREIGN KEY (`participant`) REFERENCES `participant` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant2claim`
--

LOCK TABLES `participant2claim` WRITE;
/*!40000 ALTER TABLE `participant2claim` DISABLE KEYS */;
INSERT INTO `participant2claim` VALUES (1,1,1,306),(2,2,2,306),(3,3,3,306),(4,26,29,306);
/*!40000 ALTER TABLE `participant2claim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant_element`
--

DROP TABLE IF EXISTS `participant_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant_element` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL,
  `participant` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `type__idx` (`type`),
  KEY `participant__idx` (`participant`),
  CONSTRAINT `participant_element_ibfk_1` FOREIGN KEY (`type`) REFERENCES `participant_type` (`id`) ON DELETE CASCADE,
  CONSTRAINT `participant_element_ibfk_2` FOREIGN KEY (`participant`) REFERENCES `participant` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant_element`
--

LOCK TABLES `participant_element` WRITE;
/*!40000 ALTER TABLE `participant_element` DISABLE KEYS */;
INSERT INTO `participant_element` VALUES (1,1,1),(2,1,1),(3,1,2),(4,1,2),(61,3,29),(62,3,29),(63,1,30),(64,1,30);
/*!40000 ALTER TABLE `participant_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant_link`
--

DROP TABLE IF EXISTS `participant_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant_link` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `child` int(11) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  `property` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `child__idx` (`child`),
  KEY `parent__idx` (`parent`),
  KEY `property__idx` (`property`),
  CONSTRAINT `participant_link_ibfk_1` FOREIGN KEY (`child`) REFERENCES `participant_element` (`id`) ON DELETE SET NULL,
  CONSTRAINT `participant_link_ibfk_2` FOREIGN KEY (`parent`) REFERENCES `participant_element` (`id`) ON DELETE SET NULL,
  CONSTRAINT `participant_link_ibfk_3` FOREIGN KEY (`property`) REFERENCES `property` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=582 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant_link`
--

LOCK TABLES `participant_link` WRITE;
/*!40000 ALTER TABLE `participant_link` DISABLE KEYS */;
INSERT INTO `participant_link` VALUES (577,1,2,15),(578,3,4,15),(579,5,6,15),(580,7,8,15),(581,61,62,15);
/*!40000 ALTER TABLE `participant_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participant_type`
--

DROP TABLE IF EXISTS `participant_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `participant_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participant_type`
--

LOCK TABLES `participant_type` WRITE;
/*!40000 ALTER TABLE `participant_type` DISABLE KEYS */;
INSERT INTO `participant_type` VALUES (1,'some_term'),(2,'only_term'),(3,'individual'),(4,'intersection'),(5,'union');
/*!40000 ALTER TABLE `participant_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pelement2individual`
--

DROP TABLE IF EXISTS `pelement2individual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pelement2individual` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `element` int(11) DEFAULT NULL,
  `individual` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `element__idx` (`element`),
  KEY `individual__idx` (`individual`),
  CONSTRAINT `pelement2individual_ibfk_1` FOREIGN KEY (`element`) REFERENCES `participant_element` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `pelement2individual_ibfk_2` FOREIGN KEY (`individual`) REFERENCES `individual` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pelement2individual`
--

LOCK TABLES `pelement2individual` WRITE;
/*!40000 ALTER TABLE `pelement2individual` DISABLE KEYS */;
INSERT INTO `pelement2individual` VALUES (1,61,94),(2,62,95),(3,67,96),(4,68,97);
/*!40000 ALTER TABLE `pelement2individual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pelement2term`
--

DROP TABLE IF EXISTS `pelement2term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pelement2term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `element` int(11) DEFAULT NULL,
  `term` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `element__idx` (`element`),
  KEY `term__idx` (`term`),
  CONSTRAINT `pelement2term_ibfk_1` FOREIGN KEY (`element`) REFERENCES `participant_element` (`id`) ON DELETE SET NULL,
  CONSTRAINT `pelement2term_ibfk_2` FOREIGN KEY (`term`) REFERENCES `term` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pelement2term`
--

LOCK TABLES `pelement2term` WRITE;
/*!40000 ALTER TABLE `pelement2term` DISABLE KEYS */;
INSERT INTO `pelement2term` VALUES (1,1,4838),(2,2,10473),(3,3,4838),(4,4,10473);
/*!40000 ALTER TABLE `pelement2term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS `property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` varchar(256) DEFAULT NULL,
  `authority` int(11) DEFAULT NULL,
  `label` varchar(64) DEFAULT NULL,
  `generated_id` varchar(64) DEFAULT NULL,
  `comment` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `authority__idx` (`authority`),
  CONSTRAINT `property_ibfk_1` FOREIGN KEY (`authority`) REFERENCES `authority` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=514 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property`
--

LOCK TABLES `property` WRITE;
/*!40000 ALTER TABLE `property` DISABLE KEYS */;
INSERT INTO `property` VALUES (2,'http://purl.obolibrary.org/obo/ECO_9000000',NULL,'used_in',NULL,NULL),(3,'http://purl.obolibrary.org/obo/ECO_9000001',NULL,'uses',NULL,NULL),(4,'http://purl.obolibrary.org/obo/spd#part_of',NULL,'part of',NULL,NULL),(5,'http://purl.obolibrary.org/obo/inheres_in',NULL,'inheres_in',NULL,NULL),(6,'http://purl.obolibrary.org/obo/chebi#has_role',NULL,NULL,NULL,NULL),(7,'http://purl.obolibrary.org/obo/nbo#by_means',NULL,'by_means',NULL,NULL),(8,'http://purl.obolibrary.org/obo/nbo#has-input',NULL,'has-input',NULL,NULL),(9,'http://purl.obolibrary.org/obo/nbo#has_participant',NULL,'has_participant',NULL,NULL),(10,'http://purl.obolibrary.org/obo/nbo#in_response_to',NULL,'in_response_to',NULL,NULL),(11,'http://purl.obolibrary.org/obo/nbo#is_about',NULL,'is_about',NULL,NULL),(12,'http://purl.obolibrary.org/obo/nbo#qualifier',NULL,'qualifier',NULL,NULL),(13,'http://purl.obolibrary.org/obo/pato#towards',NULL,NULL,NULL,NULL),(14,'http://purl.obolibrary.org/obo/uberon#has_quality',NULL,NULL,NULL,NULL),(15,'http://purl.obolibrary.org/obo/BFO_0000050',NULL,'part_of',NULL,NULL),(16,'http://purl.obolibrary.org/obo/BFO_0000051',NULL,'has_part',NULL,NULL),(17,'http://purl.obolibrary.org/obo/BFO_0000066',NULL,'occurs in',NULL,NULL),(18,'http://purl.obolibrary.org/obo/RO_0002091',NULL,'starts_during',NULL,NULL),(19,'http://purl.obolibrary.org/obo/RO_0002092',NULL,'happens_during',NULL,NULL),(20,'http://purl.obolibrary.org/obo/RO_0002093',NULL,'ends_during',NULL,NULL),(21,'http://purl.obolibrary.org/obo/RO_0002211',NULL,'regulates',NULL,NULL),(22,'http://purl.obolibrary.org/obo/RO_0002212',NULL,'negatively regulates',NULL,NULL),(23,'http://purl.obolibrary.org/obo/RO_0002213',NULL,'positively regulates',NULL,NULL),(24,'http://purl.obolibrary.org/obo/BFO_00000050',NULL,'part_of',NULL,NULL),(25,'http://purl.obolibrary.org/obo/pato#correlates_with',NULL,'correlates_with',NULL,NULL),(26,'http://purl.obolibrary.org/obo/pato#decreased_in_magnitude_relative_to',NULL,'decreased_in_magnitude_relative_to',NULL,NULL),(27,'http://purl.obolibrary.org/obo/pato#different_in_magnitude_relative_to',NULL,'different_in_magnitude_relative_to',NULL,NULL),(28,'http://purl.obolibrary.org/obo/pato#directly_associated_with',NULL,'directly_associated_with',NULL,NULL),(29,'http://purl.obolibrary.org/obo/pato#has_cross_section',NULL,'has_cross_section',NULL,NULL),(30,'http://purl.obolibrary.org/obo/pato#has_dividend_entity',NULL,'has_dividend_entity',NULL,NULL),(31,'http://purl.obolibrary.org/obo/pato#has_dividend_quality',NULL,'has_dividend_quality',NULL,NULL),(32,'http://purl.obolibrary.org/obo/pato#has_divisor_entity',NULL,'has_divisor_entity',NULL,NULL),(33,'http://purl.obolibrary.org/obo/pato#has_divisor_quality',NULL,'has_divisor_quality',NULL,NULL),(34,'http://purl.obolibrary.org/obo/pato#has_ratio_quality',NULL,'has_ratio_quality',NULL,NULL),(35,'http://purl.obolibrary.org/obo/pato#has_relative_magnitude',NULL,'has_relative_magnitude',NULL,NULL),(36,'http://purl.obolibrary.org/obo/pato#increased_in_magnitude_relative_to',NULL,'increased_in_magnitude_relative_to',NULL,NULL),(37,'http://purl.obolibrary.org/obo/pato#inheres_in',NULL,'inheres_in',NULL,NULL),(38,'http://purl.obolibrary.org/obo/pato#inversely_associated_with',NULL,'inversely_associated_with',NULL,NULL),(39,'http://purl.obolibrary.org/obo/pato#is_magnitude_of',NULL,'is_magnitude_of',NULL,NULL),(40,'http://purl.obolibrary.org/obo/pato#is_measurement_of',NULL,'is_measurement_of',NULL,NULL),(41,'http://purl.obolibrary.org/obo/pato#is_unit_of',NULL,'is_unit_of',NULL,NULL),(42,'http://purl.obolibrary.org/obo/pato#realized_by',NULL,'realized_by',NULL,NULL),(43,'http://purl.obolibrary.org/obo/pato#reciprocal_of',NULL,'reciprocal_of',NULL,NULL),(44,'http://purl.obolibrary.org/obo/pato#similar_in_magnitude_relative_to',NULL,'similar_in_magnitude_relative_to',NULL,NULL),(45,'http://purl.obolibrary.org/obo/pato#singly_occurring_form_of',NULL,'singly_occurring_form_of',NULL,NULL),(46,'http://purl.obolibrary.org/obo/RO_0000087',NULL,'has role',NULL,NULL),(47,'http://purl.obolibrary.org/obo/chebi#BFO_0000051',NULL,'has part',NULL,NULL),(48,'http://purl.obolibrary.org/obo/chebi#has_functional_parent',NULL,'has functional parent',NULL,NULL),(49,'http://purl.obolibrary.org/obo/chebi#has_parent_hydride',NULL,'has parent hydride',NULL,NULL),(50,'http://purl.obolibrary.org/obo/chebi#is_conjugate_acid_of',NULL,'is conjugate acid of',NULL,NULL),(51,'http://purl.obolibrary.org/obo/chebi#is_conjugate_base_of',NULL,'is conjugate base of',NULL,NULL),(52,'http://purl.obolibrary.org/obo/chebi#is_enantiomer_of',NULL,'is enantiomer of',NULL,NULL),(53,'http://purl.obolibrary.org/obo/chebi#is_substituent_group_from',NULL,'is substituent group from',NULL,NULL),(54,'http://purl.obolibrary.org/obo/chebi#is_tautomer_of',NULL,'is tautomer of',NULL,NULL),(55,'http://purl.obolibrary.org/obo/BFO_0000056',NULL,'participates in',NULL,NULL),(56,'http://purl.obolibrary.org/obo/BFO_0000062',NULL,'preceded_by',NULL,NULL),(57,'http://purl.obolibrary.org/obo/BFO_0000063',NULL,'precedes',NULL,NULL),(58,'http://purl.obolibrary.org/obo/BFO_0000067',NULL,'contains process',NULL,NULL),(59,'http://purl.obolibrary.org/obo/BSPO_0000096',NULL,'anterior_to',NULL,NULL),(60,'http://purl.obolibrary.org/obo/BSPO_0000097',NULL,'distal_to',NULL,NULL),(61,'http://purl.obolibrary.org/obo/BSPO_0000098',NULL,'dorsal_to',NULL,NULL),(62,'http://purl.obolibrary.org/obo/BSPO_0000099',NULL,'posterior_to',NULL,NULL),(63,'http://purl.obolibrary.org/obo/BSPO_0000100',NULL,'proximal_to',NULL,NULL),(64,'http://purl.obolibrary.org/obo/BSPO_0000102',NULL,'ventral_to',NULL,NULL),(65,'http://purl.obolibrary.org/obo/BSPO_0000107',NULL,'deep_to',NULL,NULL),(66,'http://purl.obolibrary.org/obo/BSPO_0000108',NULL,'superficial_to',NULL,NULL),(67,'http://purl.obolibrary.org/obo/BSPO_0000120',NULL,'in_left_side_of',NULL,NULL),(68,'http://purl.obolibrary.org/obo/BSPO_0000121',NULL,'in_right_side_of',NULL,NULL),(69,'http://purl.obolibrary.org/obo/BSPO_0000122',NULL,'in_posterior_side_of',NULL,NULL),(70,'http://purl.obolibrary.org/obo/BSPO_0000123',NULL,'in_anterior_side_of',NULL,NULL),(71,'http://purl.obolibrary.org/obo/BSPO_0000124',NULL,'in_proximal_side_of',NULL,NULL),(72,'http://purl.obolibrary.org/obo/BSPO_0000125',NULL,'in_distal_side_of',NULL,NULL),(73,'http://purl.obolibrary.org/obo/BSPO_0000126',NULL,'in_lateral_side_of',NULL,NULL),(74,'http://purl.obolibrary.org/obo/BSPO_0001100',NULL,'in_superficial_part_of',NULL,NULL),(75,'http://purl.obolibrary.org/obo/BSPO_0001101',NULL,'in_deep_part_of',NULL,NULL),(76,'http://purl.obolibrary.org/obo/BSPO_0001106',NULL,'proximalmost_part_of',NULL,NULL),(77,'http://purl.obolibrary.org/obo/BSPO_0001107',NULL,'immediately_deep_to',NULL,NULL),(78,'http://purl.obolibrary.org/obo/BSPO_0001108',NULL,'distalmost_part_of',NULL,NULL),(79,'http://purl.obolibrary.org/obo/BSPO_0001113',NULL,'preaxialmost_part_of',NULL,NULL),(80,'http://purl.obolibrary.org/obo/BSPO_0001114',NULL,'postaxial_to',NULL,NULL),(81,'http://purl.obolibrary.org/obo/BSPO_0001115',NULL,'postaxialmost_part_of',NULL,NULL),(82,'http://purl.obolibrary.org/obo/BSPO_0005001',NULL,'intersects_midsagittal_plane_of',NULL,NULL),(83,'http://purl.obolibrary.org/obo/BSPO_0015014',NULL,'immediately_superficial_to',NULL,NULL),(84,'http://purl.obolibrary.org/obo/BSPO_0015101',NULL,'in_dorsal_side_of',NULL,NULL),(85,'http://purl.obolibrary.org/obo/BSPO_0015102',NULL,'in_ventral_side_of',NULL,NULL),(86,'http://purl.obolibrary.org/obo/BSPO_1000000',NULL,'preaxial_to',NULL,NULL),(87,'http://purl.obolibrary.org/obo/RO_0000053',NULL,'bearer of',NULL,NULL),(88,'http://purl.obolibrary.org/obo/RO_0000086',NULL,'has quality',NULL,NULL),(89,'http://purl.obolibrary.org/obo/RO_0001015',NULL,'location_of',NULL,NULL),(90,'http://purl.obolibrary.org/obo/RO_0001019',NULL,'contains',NULL,NULL),(91,'http://purl.obolibrary.org/obo/RO_0001025',NULL,'located_in',NULL,NULL),(92,'http://purl.obolibrary.org/obo/RO_0002002',NULL,'has boundary',NULL,NULL),(93,'http://purl.obolibrary.org/obo/RO_0002005',NULL,'innervated_by',NULL,NULL),(94,'http://purl.obolibrary.org/obo/RO_0002007',NULL,'bounding layer of',NULL,NULL),(95,'http://purl.obolibrary.org/obo/RO_0002082',NULL,'simultaneous_with',NULL,NULL),(96,'http://purl.obolibrary.org/obo/RO_0002087',NULL,'immediately_preceded_by',NULL,NULL),(97,'http://purl.obolibrary.org/obo/RO_0002103',NULL,'synapsed by',NULL,NULL),(98,'http://purl.obolibrary.org/obo/RO_0002131',NULL,'overlaps',NULL,NULL),(99,'http://purl.obolibrary.org/obo/RO_0002134',NULL,'innervates',NULL,NULL),(100,'http://purl.obolibrary.org/obo/RO_0002150',NULL,'continuous_with',NULL,NULL),(101,'http://purl.obolibrary.org/obo/RO_0002158',NULL,'homologous_to',NULL,NULL),(102,'http://purl.obolibrary.org/obo/RO_0002159',NULL,'serially_homologous_to',NULL,NULL),(103,'http://purl.obolibrary.org/obo/RO_0002160',NULL,'only_in_taxon',NULL,NULL),(104,'http://purl.obolibrary.org/obo/RO_0002162',NULL,'in taxon',NULL,NULL),(105,'http://purl.obolibrary.org/obo/RO_0002170',NULL,'connected to',NULL,NULL),(106,'http://purl.obolibrary.org/obo/RO_0002176',NULL,'connects',NULL,NULL),(107,'http://purl.obolibrary.org/obo/RO_0002177',NULL,'attaches_to_part_of',NULL,NULL),(108,'http://purl.obolibrary.org/obo/RO_0002178',NULL,'supplies',NULL,NULL),(109,'http://purl.obolibrary.org/obo/RO_0002179',NULL,'drains',NULL,NULL),(110,'http://purl.obolibrary.org/obo/RO_0002180',NULL,'has component',NULL,NULL),(111,'http://purl.obolibrary.org/obo/RO_0002202',NULL,'develops_from',NULL,NULL),(112,'http://purl.obolibrary.org/obo/RO_0002203',NULL,'develops_into',NULL,NULL),(113,'http://purl.obolibrary.org/obo/RO_0002207',NULL,'directly_develops_from',NULL,NULL),(114,'http://purl.obolibrary.org/obo/RO_0002215',NULL,'capable_of',NULL,NULL),(115,'http://purl.obolibrary.org/obo/RO_0002216',NULL,'capable_of_part_of',NULL,NULL),(116,'http://purl.obolibrary.org/obo/RO_0002219',NULL,'surrounded_by',NULL,NULL),(117,'http://purl.obolibrary.org/obo/RO_0002220',NULL,'adjacent_to',NULL,NULL),(118,'http://purl.obolibrary.org/obo/RO_0002221',NULL,'surrounds',NULL,NULL),(119,'http://purl.obolibrary.org/obo/RO_0002223',NULL,'starts',NULL,NULL),(120,'http://purl.obolibrary.org/obo/RO_0002224',NULL,'starts with',NULL,NULL),(121,'http://purl.obolibrary.org/obo/RO_0002225',NULL,'develops_from_part_of',NULL,NULL),(122,'http://purl.obolibrary.org/obo/RO_0002226',NULL,'develops_in',NULL,NULL),(123,'http://purl.obolibrary.org/obo/RO_0002229',NULL,'ends',NULL,NULL),(124,'http://purl.obolibrary.org/obo/RO_0002230',NULL,'ends with',NULL,NULL),(125,'http://purl.obolibrary.org/obo/RO_0002254',NULL,'has developmental contribution from',NULL,NULL),(126,'http://purl.obolibrary.org/obo/RO_0002255',NULL,'developmentally_contributes_to',NULL,NULL),(127,'http://purl.obolibrary.org/obo/RO_0002256',NULL,'developmentally_induced_by',NULL,NULL),(128,'http://purl.obolibrary.org/obo/RO_0002258',NULL,'developmentally preceded by',NULL,NULL),(129,'http://purl.obolibrary.org/obo/RO_0002285',NULL,'developmentally_replaces',NULL,NULL),(130,'http://purl.obolibrary.org/obo/RO_0002322',NULL,'confers_advantage_in',NULL,NULL),(131,'http://purl.obolibrary.org/obo/RO_0002328',NULL,'functionally related to',NULL,NULL),(132,'http://purl.obolibrary.org/obo/RO_0002329',NULL,'part of structure that is capable of',NULL,NULL),(133,'http://purl.obolibrary.org/obo/RO_0002350',NULL,'member of',NULL,NULL),(134,'http://purl.obolibrary.org/obo/RO_0002351',NULL,'has member',NULL,NULL),(135,'http://purl.obolibrary.org/obo/RO_0002352',NULL,'input of',NULL,NULL),(136,'http://purl.obolibrary.org/obo/RO_0002353',NULL,'output of',NULL,NULL),(137,'http://purl.obolibrary.org/obo/RO_0002371',NULL,'attaches_to',NULL,NULL),(138,'http://purl.obolibrary.org/obo/RO_0002372',NULL,'has_muscle_origin',NULL,NULL),(139,'http://purl.obolibrary.org/obo/RO_0002373',NULL,'has_muscle_insertion',NULL,NULL),(140,'http://purl.obolibrary.org/obo/RO_0002374',NULL,'has_fused_element',NULL,NULL),(141,'http://purl.obolibrary.org/obo/RO_0002376',NULL,'tributary_of',NULL,NULL),(142,'http://purl.obolibrary.org/obo/RO_0002380',NULL,'branching_part_of',NULL,NULL),(143,'http://purl.obolibrary.org/obo/RO_0002385',NULL,'has potential to developmentally contribute to',NULL,NULL),(144,'http://purl.obolibrary.org/obo/RO_0002387',NULL,'has potential to develop into',NULL,NULL),(145,'http://purl.obolibrary.org/obo/RO_0002388',NULL,'has potential to directly develop into',NULL,NULL),(146,'http://purl.obolibrary.org/obo/RO_0002433',NULL,'contributes to morphology of',NULL,NULL),(147,'http://purl.obolibrary.org/obo/RO_0002473',NULL,'composed_primarily_of',NULL,NULL),(148,'http://purl.obolibrary.org/obo/RO_0002476',NULL,'child nucleus of',NULL,NULL),(149,'http://purl.obolibrary.org/obo/RO_0002477',NULL,'child nucleus of in hermaphrodite',NULL,NULL),(150,'http://purl.obolibrary.org/obo/RO_0002478',NULL,'child nucleus of in male',NULL,NULL),(151,'http://purl.obolibrary.org/obo/RO_0002488',NULL,'existence starts during',NULL,NULL),(152,'http://purl.obolibrary.org/obo/RO_0002489',NULL,'existence starts with',NULL,NULL),(153,'http://purl.obolibrary.org/obo/RO_0002492',NULL,'existence ends during',NULL,NULL),(154,'http://purl.obolibrary.org/obo/RO_0002493',NULL,'existence ends with',NULL,NULL),(155,'http://purl.obolibrary.org/obo/RO_0002494',NULL,'transformation of',NULL,NULL),(156,'http://purl.obolibrary.org/obo/RO_0002495',NULL,'immediate transformation of',NULL,NULL),(157,'http://purl.obolibrary.org/obo/RO_0002496',NULL,'existence starts during or after',NULL,NULL),(158,'http://purl.obolibrary.org/obo/RO_0002497',NULL,'existence ends during or before',NULL,NULL),(159,'http://purl.obolibrary.org/obo/RO_0002507',NULL,'has material contribution from',NULL,NULL),(160,'http://purl.obolibrary.org/obo/RO_0003000',NULL,'produces',NULL,NULL),(161,'http://purl.obolibrary.org/obo/RO_0003001',NULL,'produced_by',NULL,NULL),(162,'http://purl.obolibrary.org/obo/UBPROP_0000004',NULL,'provenance_notes',NULL,NULL),(163,'http://purl.obolibrary.org/obo/core#connected_to',NULL,NULL,NULL,NULL),(164,'http://purl.obolibrary.org/obo/core#distally_connected_to',NULL,NULL,NULL,NULL),(165,'http://purl.obolibrary.org/obo/core#innervated_by',NULL,NULL,NULL,NULL),(166,'http://purl.obolibrary.org/obo/core#subdivision_of',NULL,NULL,NULL,NULL),(167,'http://purl.obolibrary.org/obo/uberon/core#anteriorly_connected_to',NULL,'anteriorly connected to',NULL,NULL),(168,'http://purl.obolibrary.org/obo/uberon/core#channel_for',NULL,'channel for',NULL,NULL),(169,'http://purl.obolibrary.org/obo/uberon/core#channels_from',NULL,'channels_from',NULL,NULL),(170,'http://purl.obolibrary.org/obo/uberon/core#channels_into',NULL,'channels_into',NULL,NULL),(171,'http://purl.obolibrary.org/obo/uberon/core#conduit_for',NULL,'conduit for',NULL,NULL),(172,'http://purl.obolibrary.org/obo/uberon/core#developmentally_succeeded_by',NULL,NULL,NULL,NULL),(173,'http://purl.obolibrary.org/obo/uberon/core#distally_connected_to',NULL,'distally connected to',NULL,NULL),(174,'http://purl.obolibrary.org/obo/uberon/core#enclosed_by',NULL,'enclosed_by',NULL,NULL),(175,'http://purl.obolibrary.org/obo/uberon/core#encloses',NULL,'encloses',NULL,NULL),(176,'http://purl.obolibrary.org/obo/uberon/core#evolved_from',NULL,'evolved_from',NULL,NULL),(177,'http://purl.obolibrary.org/obo/uberon/core#evolved_multiple_times_in',NULL,'evolved_multiple_times_in',NULL,NULL),(178,'http://purl.obolibrary.org/obo/uberon/core#existence_starts_and_ends_during',NULL,'existence starts and ends during',NULL,NULL),(179,'http://purl.obolibrary.org/obo/uberon/core#has_muscle_antagonist',NULL,'has_muscle_antagonist',NULL,NULL),(180,'http://purl.obolibrary.org/obo/uberon/core#has_skeleton',NULL,'has skeleton',NULL,NULL),(181,'http://purl.obolibrary.org/obo/uberon/core#has_start',NULL,'has_start',NULL,NULL),(182,'http://purl.obolibrary.org/obo/uberon/core#in_central_side_of',NULL,'in_central_side_of',NULL,NULL),(183,'http://purl.obolibrary.org/obo/uberon/core#occurs_in',NULL,NULL,NULL,NULL),(184,'http://purl.obolibrary.org/obo/uberon/core#posteriorly_connected_to',NULL,'posteriorly connected to',NULL,NULL),(185,'http://purl.obolibrary.org/obo/uberon/core#proximally_connected_to',NULL,'proximally connected to',NULL,NULL),(186,'http://purl.obolibrary.org/obo/uberon/core#site_of',NULL,'site_of',NULL,NULL),(187,'http://purl.obolibrary.org/obo/uberon/core#subdivision_of',NULL,'subdivision of',NULL,NULL),(188,'http://purl.obolibrary.org/obo/uberon/core#transitively_anteriorly_connected_to',NULL,'transitively anteriorly connected to',NULL,NULL),(189,'http://purl.obolibrary.org/obo/uberon/core#transitively_connected_to',NULL,'transitively_connected to',NULL,NULL),(190,'http://purl.obolibrary.org/obo/uberon/core#transitively_distally_connected_to',NULL,'transitively distally connected to',NULL,NULL),(191,'http://purl.obolibrary.org/obo/uberon/core#transitively_proximally_connected_to',NULL,'transitively proximally connected to',NULL,NULL),(192,'http://purl.obolibrary.org/obo/BFO_0000052',NULL,'inheres in at all times',NULL,NULL),(193,'http://purl.obolibrary.org/obo/BFO_0000053',NULL,'bearer of at some time',NULL,NULL),(194,'http://purl.obolibrary.org/obo/BFO_0000054',NULL,'realized in',NULL,NULL),(195,'http://purl.obolibrary.org/obo/BFO_0000055',NULL,'realizes',NULL,NULL),(196,'http://purl.obolibrary.org/obo/BFO_0000057',NULL,'has participant at some time',NULL,NULL),(197,'http://purl.obolibrary.org/obo/BFO_0000058',NULL,'concretized by at some time',NULL,NULL),(198,'http://purl.obolibrary.org/obo/BFO_0000059',NULL,'concretizes at some time',NULL,NULL),(199,'http://purl.obolibrary.org/obo/BFO_0000070',NULL,'specifically depends on at all times',NULL,NULL),(200,'http://purl.obolibrary.org/obo/BFO_0000079',NULL,'function of at all times',NULL,NULL),(201,'http://purl.obolibrary.org/obo/BFO_0000080',NULL,'quality of at all times',NULL,NULL),(202,'http://purl.obolibrary.org/obo/BFO_0000081',NULL,'role of at all times',NULL,NULL),(203,'http://purl.obolibrary.org/obo/BFO_0000082',NULL,'located in at all times',NULL,NULL),(204,'http://purl.obolibrary.org/obo/BFO_0000083',NULL,'occupies spatial region at some time',NULL,NULL),(205,'http://purl.obolibrary.org/obo/BFO_0000084',NULL,'generically depends on at some time',NULL,NULL),(206,'http://purl.obolibrary.org/obo/BFO_0000085',NULL,'has function at some time',NULL,NULL),(207,'http://purl.obolibrary.org/obo/BFO_0000086',NULL,'has quality at some time',NULL,NULL),(208,'http://purl.obolibrary.org/obo/BFO_0000087',NULL,'has role at some time',NULL,NULL),(209,'http://purl.obolibrary.org/obo/BFO_0000101',NULL,'has generic dependent at some time',NULL,NULL),(210,'http://purl.obolibrary.org/obo/BFO_0000107',NULL,'disposition of at all times',NULL,NULL),(211,'http://purl.obolibrary.org/obo/BFO_0000108',NULL,'exists at',NULL,NULL),(212,'http://purl.obolibrary.org/obo/BFO_0000110',NULL,'has continuant part at all times',NULL,NULL),(213,'http://purl.obolibrary.org/obo/BFO_0000111',NULL,'has proper continuant part at all times',NULL,NULL),(214,'http://purl.obolibrary.org/obo/BFO_0000112',NULL,'has disposition at some time',NULL,NULL),(215,'http://purl.obolibrary.org/obo/BFO_0000113',NULL,'has material basis at all times',NULL,NULL),(216,'http://purl.obolibrary.org/obo/BFO_0000115',NULL,'has member part at some time',NULL,NULL),(217,'http://purl.obolibrary.org/obo/BFO_0000117',NULL,'has occurrent part',NULL,NULL),(218,'http://purl.obolibrary.org/obo/BFO_0000118',NULL,'has proper occurrent part',NULL,NULL),(219,'http://purl.obolibrary.org/obo/BFO_0000119',NULL,'has profile',NULL,NULL),(220,'http://purl.obolibrary.org/obo/BFO_0000121',NULL,'has temporal part',NULL,NULL),(221,'http://purl.obolibrary.org/obo/BFO_0000123',NULL,'has spatial occupant at some time',NULL,NULL),(222,'http://purl.obolibrary.org/obo/BFO_0000124',NULL,'has location at some time',NULL,NULL),(223,'http://purl.obolibrary.org/obo/BFO_0000125',NULL,'has specific dependent at some time',NULL,NULL),(224,'http://purl.obolibrary.org/obo/BFO_0000126',NULL,'has spatiotemporal occupant',NULL,NULL),(225,'http://purl.obolibrary.org/obo/BFO_0000127',NULL,'material basis of at some time',NULL,NULL),(226,'http://purl.obolibrary.org/obo/BFO_0000129',NULL,'member part of at some time',NULL,NULL),(227,'http://purl.obolibrary.org/obo/BFO_0000130',NULL,'occupies spatiotemporal region',NULL,NULL),(228,'http://purl.obolibrary.org/obo/BFO_0000132',NULL,'part of occurrent',NULL,NULL),(229,'http://purl.obolibrary.org/obo/BFO_0000133',NULL,'process profile of',NULL,NULL),(230,'http://purl.obolibrary.org/obo/BFO_0000136',NULL,'proper temporal part of',NULL,NULL),(231,'http://purl.obolibrary.org/obo/BFO_0000137',NULL,'proper part of continuant at all times',NULL,NULL),(232,'http://purl.obolibrary.org/obo/BFO_0000138',NULL,'proper part of occurrent',NULL,NULL),(233,'http://purl.obolibrary.org/obo/BFO_0000139',NULL,'temporal part of',NULL,NULL),(234,'http://purl.obolibrary.org/obo/BFO_0000151',NULL,'projects onto spatial region at some time',NULL,NULL),(235,'http://purl.obolibrary.org/obo/BFO_0000152',NULL,'spatial projection of spatiotemporal at some time',NULL,NULL),(236,'http://purl.obolibrary.org/obo/BFO_0000153',NULL,'projects onto temporal region',NULL,NULL),(237,'http://purl.obolibrary.org/obo/BFO_0000154',NULL,'temporal projection of spatiotemporal',NULL,NULL),(238,'http://purl.obolibrary.org/obo/BFO_0000155',NULL,'occupies temporal region',NULL,NULL),(239,'http://purl.obolibrary.org/obo/BFO_0000156',NULL,'has temporal occupant',NULL,NULL),(240,'http://purl.obolibrary.org/obo/BFO_0000157',NULL,'during which exists',NULL,NULL),(241,'http://purl.obolibrary.org/obo/BFO_0000158',NULL,'bearer of at all times',NULL,NULL),(242,'http://purl.obolibrary.org/obo/BFO_0000159',NULL,'has quality at all times',NULL,NULL),(243,'http://purl.obolibrary.org/obo/BFO_0000160',NULL,'has function at all times',NULL,NULL),(244,'http://purl.obolibrary.org/obo/BFO_0000161',NULL,'has role at all times',NULL,NULL),(245,'http://purl.obolibrary.org/obo/BFO_0000162',NULL,'has disposition at all times',NULL,NULL),(246,'http://purl.obolibrary.org/obo/BFO_0000163',NULL,'material basis of at all times',NULL,NULL),(247,'http://purl.obolibrary.org/obo/BFO_0000164',NULL,'concretizes at all times',NULL,NULL),(248,'http://purl.obolibrary.org/obo/BFO_0000165',NULL,'concretized by at all times',NULL,NULL),(249,'http://purl.obolibrary.org/obo/BFO_0000166',NULL,'participates in at all times',NULL,NULL),(250,'http://purl.obolibrary.org/obo/BFO_0000167',NULL,'has participant at all times',NULL,NULL),(251,'http://purl.obolibrary.org/obo/BFO_0000168',NULL,'has specific dependent at all times',NULL,NULL),(252,'http://purl.obolibrary.org/obo/BFO_0000169',NULL,'specifically depends on at some time',NULL,NULL),(253,'http://purl.obolibrary.org/obo/BFO_0000170',NULL,'has location at all times',NULL,NULL),(254,'http://purl.obolibrary.org/obo/BFO_0000171',NULL,'located in at some time',NULL,NULL),(255,'http://purl.obolibrary.org/obo/BFO_0000172',NULL,'has member part at all times',NULL,NULL),(256,'http://purl.obolibrary.org/obo/BFO_0000173',NULL,'member part of at all times',NULL,NULL),(257,'http://purl.obolibrary.org/obo/BFO_0000174',NULL,'has proper continuant part at some time',NULL,NULL),(258,'http://purl.obolibrary.org/obo/BFO_0000175',NULL,'proper part of continuant at some time',NULL,NULL),(259,'http://purl.obolibrary.org/obo/BFO_0000176',NULL,'part of continuant at some time',NULL,NULL),(260,'http://purl.obolibrary.org/obo/BFO_0000177',NULL,'part of continuant at all times',NULL,NULL),(261,'http://purl.obolibrary.org/obo/BFO_0000178',NULL,'has continuant part at some time',NULL,NULL),(262,'http://purl.obolibrary.org/obo/BFO_0000181',NULL,'has proper temporal part',NULL,NULL),(263,'http://purl.obolibrary.org/obo/BFO_0000184',NULL,'history of',NULL,NULL),(264,'http://purl.obolibrary.org/obo/BFO_0000185',NULL,'has history',NULL,NULL),(265,'http://purl.obolibrary.org/obo/BFO_0000186',NULL,'part of continuant at all times that whole exists',NULL,NULL),(266,'http://purl.obolibrary.org/obo/BFO_0000187',NULL,'has continuant part at all times that part exists',NULL,NULL),(267,'http://purl.obolibrary.org/obo/RO_0000300',NULL,'in neural circuit with',NULL,NULL),(268,'http://purl.obolibrary.org/obo/RO_0000301',NULL,'upstream in neural circuit with',NULL,NULL),(269,'http://purl.obolibrary.org/obo/RO_0000302',NULL,'downstream in neural circuit with',NULL,NULL),(270,'http://purl.obolibrary.org/obo/RO_0002001',NULL,'aligned with',NULL,NULL),(271,'http://purl.obolibrary.org/obo/RO_0002003',NULL,'electrically_synapsed_to',NULL,NULL),(272,'http://purl.obolibrary.org/obo/RO_0002004',NULL,'tracheates',NULL,NULL),(273,'http://purl.obolibrary.org/obo/RO_0002006',NULL,'has synaptic terminal of',NULL,NULL),(274,'http://purl.obolibrary.org/obo/RO_0002008',NULL,'coincident with',NULL,NULL),(275,'http://purl.obolibrary.org/obo/RO_0002085',NULL,'encompasses',NULL,NULL),(276,'http://purl.obolibrary.org/obo/RO_0002100',NULL,'has soma location',NULL,NULL),(277,'http://purl.obolibrary.org/obo/RO_0002101',NULL,'fasciculates with',NULL,NULL),(278,'http://purl.obolibrary.org/obo/RO_0002102',NULL,'axon synapses in',NULL,NULL),(279,'http://purl.obolibrary.org/obo/RO_0002104',NULL,'has plasma membrane part',NULL,NULL),(280,'http://purl.obolibrary.org/obo/RO_0002105',NULL,'synapsed_via_type_Ib_bouton_to',NULL,NULL),(281,'http://purl.obolibrary.org/obo/RO_0002106',NULL,'synapsed_via_type_Is_bouton_to',NULL,NULL),(282,'http://purl.obolibrary.org/obo/RO_0002107',NULL,'synapsed_via_type_II_bouton_to',NULL,NULL),(283,'http://purl.obolibrary.org/obo/RO_0002108',NULL,'synapsed_by_via_type_II_bouton',NULL,NULL),(284,'http://purl.obolibrary.org/obo/RO_0002109',NULL,'synapsed_by_via_type_Ib_bouton',NULL,NULL),(285,'http://purl.obolibrary.org/obo/RO_0002110',NULL,'has postsynaptic terminal in',NULL,NULL),(286,'http://purl.obolibrary.org/obo/RO_0002111',NULL,'releases neurotransmitter',NULL,NULL),(287,'http://purl.obolibrary.org/obo/RO_0002112',NULL,'synapsed_by_via_type_Is_bouton',NULL,NULL),(288,'http://purl.obolibrary.org/obo/RO_0002113',NULL,'has presynaptic terminal in',NULL,NULL),(289,'http://purl.obolibrary.org/obo/RO_0002114',NULL,'synapsed_via_type_III_bouton_to',NULL,NULL),(290,'http://purl.obolibrary.org/obo/RO_0002115',NULL,'synapsed_by_via_type_III_bouton',NULL,NULL),(291,'http://purl.obolibrary.org/obo/RO_0002120',NULL,'synapsed to',NULL,NULL),(292,'http://purl.obolibrary.org/obo/RO_0002121',NULL,'dendrite synapsed in',NULL,NULL),(293,'http://purl.obolibrary.org/obo/RO_0002130',NULL,'has synaptic terminal in',NULL,NULL),(294,'http://purl.obolibrary.org/obo/RO_0002132',NULL,'has fasciculating neuron projection',NULL,NULL),(295,'http://purl.obolibrary.org/obo/RO_0002151',NULL,'proper overlaps',NULL,NULL),(296,'http://purl.obolibrary.org/obo/RO_0002156',NULL,'derived by descent from',NULL,NULL),(297,'http://purl.obolibrary.org/obo/RO_0002157',NULL,'has derived by descendant',NULL,NULL),(298,'http://purl.obolibrary.org/obo/RO_0002163',NULL,'spatially disjoint from',NULL,NULL),(299,'http://purl.obolibrary.org/obo/RO_0002200',NULL,'has phenotype',NULL,NULL),(300,'http://purl.obolibrary.org/obo/RO_0002201',NULL,'phenotype of',NULL,NULL),(301,'http://purl.obolibrary.org/obo/RO_0002204',NULL,'gene product of',NULL,NULL),(302,'http://purl.obolibrary.org/obo/RO_0002205',NULL,'has gene product',NULL,NULL),(303,'http://purl.obolibrary.org/obo/RO_0002206',NULL,'expressed in',NULL,NULL),(304,'http://purl.obolibrary.org/obo/RO_0002210',NULL,'directly develops into',NULL,NULL),(305,'http://purl.obolibrary.org/obo/RO_0002214',NULL,'has prototype',NULL,NULL),(306,'http://purl.obolibrary.org/obo/RO_0002217',NULL,'actively participates in',NULL,NULL),(307,'http://purl.obolibrary.org/obo/RO_0002218',NULL,'has active participant',NULL,NULL),(308,'http://purl.obolibrary.org/obo/RO_0002231',NULL,'has start location',NULL,NULL),(309,'http://purl.obolibrary.org/obo/RO_0002232',NULL,'has end location',NULL,NULL),(310,'http://purl.obolibrary.org/obo/RO_0002233',NULL,'has input',NULL,NULL),(311,'http://purl.obolibrary.org/obo/RO_0002234',NULL,'has output',NULL,NULL),(312,'http://purl.obolibrary.org/obo/RO_0002257',NULL,'developmentally induces',NULL,NULL),(313,'http://purl.obolibrary.org/obo/RO_0002286',NULL,'developmentally succeeded by',NULL,NULL),(314,'http://purl.obolibrary.org/obo/RO_0002287',NULL,'part of developmental precursor of',NULL,NULL),(315,'http://purl.obolibrary.org/obo/RO_0002291',NULL,'ubiquitously expressed in',NULL,NULL),(316,'http://purl.obolibrary.org/obo/RO_0002292',NULL,'expresses',NULL,NULL),(317,'http://purl.obolibrary.org/obo/RO_0002293',NULL,'ubiquitously expresses',NULL,NULL),(318,'http://purl.obolibrary.org/obo/RO_0002295',NULL,'results in developmental progression of',NULL,NULL),(319,'http://purl.obolibrary.org/obo/RO_0002296',NULL,'results in development of',NULL,NULL),(320,'http://purl.obolibrary.org/obo/RO_0002297',NULL,'results in formation of',NULL,NULL),(321,'http://purl.obolibrary.org/obo/RO_0002298',NULL,'results in morphogenesis of',NULL,NULL),(322,'http://purl.obolibrary.org/obo/RO_0002299',NULL,'results in maturation of',NULL,NULL),(323,'http://purl.obolibrary.org/obo/RO_0002300',NULL,'results in disappearance of',NULL,NULL),(324,'http://purl.obolibrary.org/obo/RO_0002301',NULL,'results in developmental regression of',NULL,NULL),(325,'http://purl.obolibrary.org/obo/RO_0002303',NULL,'has habitat',NULL,NULL),(326,'http://purl.obolibrary.org/obo/RO_0002312',NULL,'evolutionary variant of',NULL,NULL),(327,'http://purl.obolibrary.org/obo/RO_0002313',NULL,'transports or maintains localization of',NULL,NULL),(328,'http://purl.obolibrary.org/obo/RO_0002314',NULL,'inheres in part of',NULL,NULL),(329,'http://purl.obolibrary.org/obo/RO_0002315',NULL,'results in acquisition of features of',NULL,NULL),(330,'http://purl.obolibrary.org/obo/RO_0002320',NULL,'evolutionarily related to',NULL,NULL),(331,'http://purl.obolibrary.org/obo/RO_0002321',NULL,'ecologically related to',NULL,NULL),(332,'http://purl.obolibrary.org/obo/RO_0002323',NULL,'mereotopologically related to',NULL,NULL),(333,'http://purl.obolibrary.org/obo/RO_0002324',NULL,'developmentally related to',NULL,NULL),(334,'http://purl.obolibrary.org/obo/RO_0002325',NULL,'colocalizes_with',NULL,NULL),(335,'http://purl.obolibrary.org/obo/RO_0002326',NULL,'contributes_to',NULL,NULL),(336,'http://purl.obolibrary.org/obo/RO_0002327',NULL,'enables',NULL,NULL),(337,'http://purl.obolibrary.org/obo/RO_0002330',NULL,'genomically related to',NULL,NULL),(338,'http://purl.obolibrary.org/obo/RO_0002331',NULL,'involved in',NULL,NULL),(339,'http://purl.obolibrary.org/obo/RO_0002332',NULL,'regulates levels of',NULL,NULL),(340,'http://purl.obolibrary.org/obo/RO_0002333',NULL,'enabled_by',NULL,NULL),(341,'http://purl.obolibrary.org/obo/RO_0002334',NULL,'regulated by',NULL,NULL),(342,'http://purl.obolibrary.org/obo/RO_0002335',NULL,'negatively regulated by',NULL,NULL),(343,'http://purl.obolibrary.org/obo/RO_0002336',NULL,'positively regulated by',NULL,NULL),(344,'http://purl.obolibrary.org/obo/RO_0002337',NULL,'related via localization to',NULL,NULL),(345,'http://purl.obolibrary.org/obo/RO_0002338',NULL,'has target start location',NULL,NULL),(346,'http://purl.obolibrary.org/obo/RO_0002339',NULL,'has target end location',NULL,NULL),(347,'http://purl.obolibrary.org/obo/RO_0002340',NULL,'imports',NULL,NULL),(348,'http://purl.obolibrary.org/obo/RO_0002341',NULL,'results in transport along',NULL,NULL),(349,'http://purl.obolibrary.org/obo/RO_0002342',NULL,'results in transport across',NULL,NULL),(350,'http://purl.obolibrary.org/obo/RO_0002343',NULL,'results in growth of',NULL,NULL),(351,'http://purl.obolibrary.org/obo/RO_0002344',NULL,'results in transport to from or in',NULL,NULL),(352,'http://purl.obolibrary.org/obo/RO_0002345',NULL,'exports',NULL,NULL),(353,'http://purl.obolibrary.org/obo/RO_0002348',NULL,'results in commitment to',NULL,NULL),(354,'http://purl.obolibrary.org/obo/RO_0002349',NULL,'results in determination of',NULL,NULL),(355,'http://purl.obolibrary.org/obo/RO_0002354',NULL,'formed as result of',NULL,NULL),(356,'http://purl.obolibrary.org/obo/RO_0002355',NULL,'results in structural organization of',NULL,NULL),(357,'http://purl.obolibrary.org/obo/RO_0002356',NULL,'results in specification of',NULL,NULL),(358,'http://purl.obolibrary.org/obo/RO_0002357',NULL,'results in developmental induction of',NULL,NULL),(359,'http://purl.obolibrary.org/obo/RO_0002360',NULL,'has dendrite location',NULL,NULL),(360,'http://purl.obolibrary.org/obo/RO_0002375',NULL,'in branching relationship with',NULL,NULL),(361,'http://purl.obolibrary.org/obo/RO_0002377',NULL,'distributary of',NULL,NULL),(362,'http://purl.obolibrary.org/obo/RO_0002378',NULL,'anabranch of',NULL,NULL),(363,'http://purl.obolibrary.org/obo/RO_0002379',NULL,'spatially coextensive with',NULL,NULL),(364,'http://purl.obolibrary.org/obo/RO_0002381',NULL,'main stem of',NULL,NULL),(365,'http://purl.obolibrary.org/obo/RO_0002382',NULL,'proper distributary of',NULL,NULL),(366,'http://purl.obolibrary.org/obo/RO_0002383',NULL,'proper tributary of',NULL,NULL),(367,'http://purl.obolibrary.org/obo/RO_0002384',NULL,'has developmental potential involving',NULL,NULL),(368,'http://purl.obolibrary.org/obo/RO_0002386',NULL,'has potential to developmentally induce',NULL,NULL),(369,'http://purl.obolibrary.org/obo/RO_0002400',NULL,'has direct input',NULL,NULL),(370,'http://purl.obolibrary.org/obo/RO_0002401',NULL,'has indirect input',NULL,NULL),(371,'http://purl.obolibrary.org/obo/RO_0002402',NULL,'has direct output',NULL,NULL),(372,'http://purl.obolibrary.org/obo/RO_0002403',NULL,'has indirect output',NULL,NULL),(373,'http://purl.obolibrary.org/obo/RO_0002404',NULL,'causally downstream of',NULL,NULL),(374,'http://purl.obolibrary.org/obo/RO_0002405',NULL,'immediately causally downstream of',NULL,NULL),(375,'http://purl.obolibrary.org/obo/RO_0002406',NULL,'directly activates',NULL,NULL),(376,'http://purl.obolibrary.org/obo/RO_0002407',NULL,'indirectly activates',NULL,NULL),(377,'http://purl.obolibrary.org/obo/RO_0002408',NULL,'directly inhibits',NULL,NULL),(378,'http://purl.obolibrary.org/obo/RO_0002409',NULL,'indirectly inhibits',NULL,NULL),(379,'http://purl.obolibrary.org/obo/RO_0002410',NULL,'causally related to',NULL,NULL),(380,'http://purl.obolibrary.org/obo/RO_0002411',NULL,'causally upstream of',NULL,NULL),(381,'http://purl.obolibrary.org/obo/RO_0002412',NULL,'immediately causally upstream of',NULL,NULL),(382,'http://purl.obolibrary.org/obo/RO_0002413',NULL,'directly provides input for',NULL,NULL),(383,'http://purl.obolibrary.org/obo/RO_0002414',NULL,'provides input for',NULL,NULL),(384,'http://purl.obolibrary.org/obo/RO_0002418',NULL,'causally upstream of or within',NULL,NULL),(385,'http://purl.obolibrary.org/obo/RO_0002424',NULL,'differs in',NULL,NULL),(386,'http://purl.obolibrary.org/obo/RO_0002425',NULL,'differs in attribute of',NULL,NULL),(387,'http://purl.obolibrary.org/obo/RO_0002426',NULL,'differs in attribute',NULL,NULL),(388,'http://purl.obolibrary.org/obo/RO_0002427',NULL,'causally downstream of or within',NULL,NULL),(389,'http://purl.obolibrary.org/obo/RO_0002428',NULL,'involved in regulation of',NULL,NULL),(390,'http://purl.obolibrary.org/obo/RO_0002429',NULL,'involved in positive regulation of',NULL,NULL),(391,'http://purl.obolibrary.org/obo/RO_0002430',NULL,'involved in negative regulation of',NULL,NULL),(392,'http://purl.obolibrary.org/obo/RO_0002431',NULL,'involved in or regulates',NULL,NULL),(393,'http://purl.obolibrary.org/obo/RO_0002432',NULL,'enables activity in',NULL,NULL),(394,'http://purl.obolibrary.org/obo/RO_0002434',NULL,'interacts with',NULL,NULL),(395,'http://purl.obolibrary.org/obo/RO_0002435',NULL,'genetically interacts with',NULL,NULL),(396,'http://purl.obolibrary.org/obo/RO_0002436',NULL,'molecularly interacts with',NULL,NULL),(397,'http://purl.obolibrary.org/obo/RO_0002437',NULL,'biotically interacts with',NULL,NULL),(398,'http://purl.obolibrary.org/obo/RO_0002438',NULL,'trophically interacts with',NULL,NULL),(399,'http://purl.obolibrary.org/obo/RO_0002439',NULL,'preys on',NULL,NULL),(400,'http://purl.obolibrary.org/obo/RO_0002440',NULL,'symbiotically interacts with',NULL,NULL),(401,'http://purl.obolibrary.org/obo/RO_0002441',NULL,'commensually interacts with',NULL,NULL),(402,'http://purl.obolibrary.org/obo/RO_0002442',NULL,'mutualistically interacts with',NULL,NULL),(403,'http://purl.obolibrary.org/obo/RO_0002443',NULL,'interacts with via parasite-host interaction',NULL,NULL),(404,'http://purl.obolibrary.org/obo/RO_0002444',NULL,'parasite of',NULL,NULL),(405,'http://purl.obolibrary.org/obo/RO_0002445',NULL,'parasitized by',NULL,NULL),(406,'http://purl.obolibrary.org/obo/RO_0002446',NULL,'semibiotically interacts with',NULL,NULL),(407,'http://purl.obolibrary.org/obo/RO_0002447',NULL,'phosphorylates',NULL,NULL),(408,'http://purl.obolibrary.org/obo/RO_0002448',NULL,'molecularly controls',NULL,NULL),(409,'http://purl.obolibrary.org/obo/RO_0002449',NULL,'molecularly decreases activity of',NULL,NULL),(410,'http://purl.obolibrary.org/obo/RO_0002450',NULL,'molecularly increases activity of',NULL,NULL),(411,'http://purl.obolibrary.org/obo/RO_0002451',NULL,'transmitted by',NULL,NULL),(412,'http://purl.obolibrary.org/obo/RO_0002452',NULL,'has symptom',NULL,NULL),(413,'http://purl.obolibrary.org/obo/RO_0002453',NULL,'host of',NULL,NULL),(414,'http://purl.obolibrary.org/obo/RO_0002454',NULL,'has host',NULL,NULL),(415,'http://purl.obolibrary.org/obo/RO_0002455',NULL,'pollinates',NULL,NULL),(416,'http://purl.obolibrary.org/obo/RO_0002456',NULL,'pollinated by',NULL,NULL),(417,'http://purl.obolibrary.org/obo/RO_0002457',NULL,'acquires nutrients from',NULL,NULL),(418,'http://purl.obolibrary.org/obo/RO_0002458',NULL,'preyed upon by',NULL,NULL),(419,'http://purl.obolibrary.org/obo/RO_0002459',NULL,'is vector for',NULL,NULL),(420,'http://purl.obolibrary.org/obo/RO_0002460',NULL,'has vector',NULL,NULL),(421,'http://purl.obolibrary.org/obo/RO_0002461',NULL,'partner in',NULL,NULL),(422,'http://purl.obolibrary.org/obo/RO_0002462',NULL,'subject participant in',NULL,NULL),(423,'http://purl.obolibrary.org/obo/RO_0002463',NULL,'target participant in',NULL,NULL),(424,'http://purl.obolibrary.org/obo/RO_0002464',NULL,'helper property',NULL,NULL),(425,'http://purl.obolibrary.org/obo/RO_0002465',NULL,'is symbiosis',NULL,NULL),(426,'http://purl.obolibrary.org/obo/RO_0002466',NULL,'is commensalism',NULL,NULL),(427,'http://purl.obolibrary.org/obo/RO_0002467',NULL,'is mutualism',NULL,NULL),(428,'http://purl.obolibrary.org/obo/RO_0002468',NULL,'is parasitism',NULL,NULL),(429,'http://purl.obolibrary.org/obo/RO_0002469',NULL,'provides nutrients for',NULL,NULL),(430,'http://purl.obolibrary.org/obo/RO_0002470',NULL,'eats',NULL,NULL),(431,'http://purl.obolibrary.org/obo/RO_0002471',NULL,'is eaten by',NULL,NULL),(432,'http://purl.obolibrary.org/obo/RO_0002472',NULL,'is evidence for',NULL,NULL),(433,'http://purl.obolibrary.org/obo/RO_0002479',NULL,'has part that occurs in',NULL,NULL),(434,'http://purl.obolibrary.org/obo/RO_0002480',NULL,'ubiquitinates',NULL,NULL),(435,'http://purl.obolibrary.org/obo/RO_0002481',NULL,'is kinase activity',NULL,NULL),(436,'http://purl.obolibrary.org/obo/RO_0002482',NULL,'is ubiquitination',NULL,NULL),(437,'http://purl.obolibrary.org/obo/RO_0002485',NULL,'receives input from',NULL,NULL),(438,'http://purl.obolibrary.org/obo/RO_0002486',NULL,'sends output to',NULL,NULL),(439,'http://purl.obolibrary.org/obo/RO_0002487',NULL,'relation between structure and stage',NULL,NULL),(440,'http://purl.obolibrary.org/obo/RO_0002490',NULL,'existence overlaps',NULL,NULL),(441,'http://purl.obolibrary.org/obo/RO_0002491',NULL,'existence starts and ends during',NULL,NULL),(442,'http://purl.obolibrary.org/obo/RO_0002502',NULL,'depends on',NULL,NULL),(443,'http://purl.obolibrary.org/obo/RO_0002503',NULL,'towards',NULL,NULL),(444,'http://purl.obolibrary.org/obo/RO_0002505',NULL,'has intermediate',NULL,NULL),(445,'http://purl.obolibrary.org/obo/RO_0002510',NULL,'transcribed from',NULL,NULL),(446,'http://purl.obolibrary.org/obo/RO_0002511',NULL,'transcribed to',NULL,NULL),(447,'http://purl.obolibrary.org/obo/RO_0002512',NULL,'ribosomal translation of',NULL,NULL),(448,'http://purl.obolibrary.org/obo/RO_0002513',NULL,'ribosomally translates to',NULL,NULL),(449,'http://purl.obolibrary.org/obo/RO_0002514',NULL,'sequentially related to',NULL,NULL),(450,'http://purl.obolibrary.org/obo/RO_0002515',NULL,'sequentially adjacent to',NULL,NULL),(451,'http://purl.obolibrary.org/obo/RO_0002516',NULL,'has start sequence',NULL,NULL),(452,'http://purl.obolibrary.org/obo/RO_0002517',NULL,'is start sequence of',NULL,NULL),(453,'http://purl.obolibrary.org/obo/RO_0002518',NULL,'has end sequence',NULL,NULL),(454,'http://purl.obolibrary.org/obo/RO_0002519',NULL,'is end sequence of',NULL,NULL),(455,'http://purl.obolibrary.org/obo/RO_0002520',NULL,'is consecutive sequence of',NULL,NULL),(456,'http://purl.obolibrary.org/obo/RO_0002521',NULL,'is sequentially aligned with',NULL,NULL),(457,'http://purl.obolibrary.org/obo/RO_0002522',NULL,'bounds sequence of',NULL,NULL),(458,'http://purl.obolibrary.org/obo/RO_0002523',NULL,'is bound by sequence of',NULL,NULL),(459,'http://purl.obolibrary.org/obo/RO_0002524',NULL,'has subsequence',NULL,NULL),(460,'http://purl.obolibrary.org/obo/RO_0002525',NULL,'is subsequence of',NULL,NULL),(461,'http://purl.obolibrary.org/obo/RO_0002526',NULL,'overlaps sequence of',NULL,NULL),(462,'http://purl.obolibrary.org/obo/RO_0002527',NULL,'does not overlap sequence of',NULL,NULL),(463,'http://purl.obolibrary.org/obo/RO_0002528',NULL,'is upstream of sequence of',NULL,NULL),(464,'http://purl.obolibrary.org/obo/RO_0002529',NULL,'is downstream of sequence of',NULL,NULL),(465,'http://purl.obolibrary.org/obo/RO_0002530',NULL,'is immediately downstream of sequence of',NULL,NULL),(466,'http://purl.obolibrary.org/obo/RO_0002531',NULL,'is immediately upstream of sequence of',NULL,NULL),(467,'http://purl.obolibrary.org/obo/RO_0002551',NULL,'has skeleton',NULL,NULL),(468,'http://purl.obolibrary.org/obo/RO_0002552',NULL,'results in ending of',NULL,NULL),(469,'http://purl.obolibrary.org/obo/RO_0002553',NULL,'hyperparasitoid of',NULL,NULL),(470,'http://purl.obolibrary.org/obo/RO_0002554',NULL,'hyperparasitoidized by',NULL,NULL),(471,'http://purl.obolibrary.org/obo/RO_0002555',NULL,'allelopath of',NULL,NULL),(472,'http://purl.obolibrary.org/obo/RO_0002556',NULL,'pathogen of',NULL,NULL),(473,'http://purl.obolibrary.org/obo/RO_0002557',NULL,'has pathogen',NULL,NULL),(474,'http://purl.obolibrary.org/obo/RO_0002558',NULL,'has evidence',NULL,NULL),(475,'http://purl.obolibrary.org/obo/RO_0003002',NULL,'represses expression of',NULL,NULL),(476,'http://purl.obolibrary.org/obo/RO_0003003',NULL,'increases expression of',NULL,NULL),(477,'http://www.w3.org/2002/07/owl#topObjectProperty',NULL,NULL,NULL,NULL),(478,'http://purl.obolibrary.org/obo/IAO_0000581',NULL,'has time stamp',NULL,NULL),(479,'http://purl.obolibrary.org/obo/IAO_0000583',NULL,'has measurement datum',NULL,NULL),(480,'http://purl.obolibrary.org/obo/IAO_0000039',NULL,'has measurement unit label',NULL,NULL),(481,'http://purl.obolibrary.org/obo/IAO_0000136',NULL,'is about',NULL,NULL),(482,'http://purl.obolibrary.org/obo/IAO_0000142',NULL,'mentions',NULL,NULL),(483,'http://purl.obolibrary.org/obo/IAO_0000219',NULL,'denotes',NULL,NULL),(484,'http://purl.obolibrary.org/obo/IAO_0000221',NULL,'is quality measurement of',NULL,NULL),(485,'http://purl.obolibrary.org/obo/IAO_0000407',NULL,'has coordinate unit label',NULL,NULL),(486,'http://purl.obolibrary.org/obo/IAO_0000413',NULL,'is duration of',NULL,NULL),(487,'http://purl.obolibrary.org/obo/IAO_0000417',NULL,'is quality measured as',NULL,NULL),(488,'http://purl.obolibrary.org/obo/IAO_0000418',NULL,'is quality specification of',NULL,NULL),(489,'http://purl.obolibrary.org/obo/IAO_0000419',NULL,'quality is specified as',NULL,NULL),(490,'http://purl.obolibrary.org/obo/OBI_0000293',NULL,NULL,NULL,NULL),(491,'http://purl.obolibrary.org/obo/OBI_0000294',NULL,'is_concretization_of',NULL,NULL),(492,'http://purl.obolibrary.org/obo/OBI_0000297',NULL,'is_concretized_as',NULL,NULL),(493,'http://purl.obolibrary.org/obo/OBI_0000299',NULL,NULL,NULL,NULL),(494,'http://purl.org/obo/owl/OBO_REL#bearer_of',NULL,NULL,NULL,NULL),(495,'http://www.obofoundry.org/ro/ro.owl#has_part',NULL,NULL,NULL,NULL),(496,'http://www.obofoundry.org/ro/ro.owl#part_of',NULL,NULL,NULL,NULL),(497,'http://purl.obolibrary.org/obo/RO_0001000',NULL,'derives_from',NULL,NULL),(498,'http://purl.obolibrary.org/obo/envo#has_condition',NULL,'has_condition',NULL,NULL),(499,'http://purl.obolibrary.org/obo/envo#has_increased_levels_of',NULL,'has_increased_levels_of',NULL,NULL),(500,'http://purl.obolibrary.org/obo/envo#has_quality',NULL,'has_quality',NULL,NULL),(501,'http://purl.obolibrary.org/obo/RO_0000052',NULL,'inheres in',NULL,NULL),(502,'http://purl.obolibrary.org/obo/RO_0000056',NULL,'participates in',NULL,NULL),(503,'http://purl.obolibrary.org/obo/RO_0000057',NULL,'has participant',NULL,NULL),(504,'http://purl.obolibrary.org/obo/RO_0001001',NULL,'derives into',NULL,NULL),(505,'http://purl.obolibrary.org/obo/RO_0001018',NULL,'contained in',NULL,NULL),(506,'http://purl.obolibrary.org/obo/RO_0002081',NULL,'before_or_simultaneous_with',NULL,NULL),(507,'http://purl.obolibrary.org/obo/RO_0002083',NULL,'before',NULL,NULL),(508,'http://purl.obolibrary.org/obo/RO_0002084',NULL,'during_which_ends',NULL,NULL),(509,'http://purl.obolibrary.org/obo/RO_0002086',NULL,'ends_after',NULL,NULL),(510,'http://purl.obolibrary.org/obo/RO_0002088',NULL,'during_which_starts',NULL,NULL),(511,'http://purl.obolibrary.org/obo/RO_0002089',NULL,'starts_before',NULL,NULL),(512,'http://purl.obolibrary.org/obo/RO_0002090',NULL,'immediately_precedes',NULL,NULL),(513,'http://purl.obolibrary.org/obo/RO_0002222',NULL,'temporally related to',NULL,NULL);
/*!40000 ALTER TABLE `property` ENABLE KEYS */;
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
  `volume` int(11) DEFAULT NULL,
  `curation_status` int(11) DEFAULT NULL,
  `curation_update` datetime DEFAULT NULL,
  `doi` varchar(512) DEFAULT NULL,
  `publication_year` varchar(512) DEFAULT NULL,
  `source_publication` varchar(512) DEFAULT NULL,
  `generated_id` varchar(512) DEFAULT NULL,
  `publication_date` varchar(512) DEFAULT NULL,
  `issue` varchar(512) DEFAULT NULL,
  `page_range` varchar(512) DEFAULT NULL,
  `serial_identifier` varchar(512) DEFAULT NULL,
  `uidset` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `curation_status__idx` (`curation_status`),
  KEY `pubauthoridx` (`author_list`(32)),
  KEY `uidset__idx` (`uidset`),
  CONSTRAINT `publication_ibfk_1` FOREIGN KEY (`curation_status`) REFERENCES `publication_curation` (`id`) ON DELETE CASCADE,
  CONSTRAINT `publication_ibfk_2` FOREIGN KEY (`uidset`) REFERENCES `uidset` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publication`
--

LOCK TABLES `publication` WRITE;
/*!40000 ALTER TABLE `publication` DISABLE KEYS */;
INSERT INTO `publication` VALUES (1,'Journal','Not Found',NULL,NULL,'The influence of prey on size, capture area and mesh height of the orb-web of the garden spider, Argiope aemula (Walckenaer, 1841) (Araneaea: Araneidae).\r\n','','Abrenica-Adamat, Liza R.; Torres, Mark Anthony J.; Barrion, Adelina A.; Dupo, Aimee Lynn B.; Demayo, Cesar G.\r\n','',1,2,NULL,NULL,'2009','Egyptian Academic Journal of Biological Sciences B Zoology ','http://arachb.org/arachb/ARACHB_0000099','','1','65-71 ','',10),(2,'Journal','Not Found',NULL,NULL,'New species of Anelosimus (Araneae: Theridiidae) from Africa and Southeast Asia, with notes on sociality and color polymorphism.\r\n','','Agnarsson, Ingi; Zhang, Jun-Xia\r\n','',1147,2,NULL,NULL,'2006','Zootaxa','http://arachb.org/arachb/ARACHB_0000285','10-Mar','','1-34','',11),(3,'Journal','Downloaded','2011-04-18','2011-04-23','Habitat distribution, life history and behavior of Tetragnatha spider species in the Great Smoky Mountains National Park.\r\n','','Aiken, Marie; Coyle, Frederick A.','',28,4,NULL,'http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2','2000','Journal of Arachnology',NULL,'','1','97-106','',12),(123,'Journal','Downloaded','2011-05-11',NULL,'Courtship, copulation, and sperm transfer in Leucauge mariana (Araneae, Tetragnathidae) with implications for higher classification','','Eberhard, William G.; Huber, Bernhard A.','',26,4,NULL,NULL,'1998','Journal of Arachnology','http://arachb.org/arachb/ARACHB_0000311','','3','342-368','',13);
/*!40000 ALTER TABLE `publication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publication_curation`
--

DROP TABLE IF EXISTS `publication_curation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publication_curation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(31) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publication_curation`
--

LOCK TABLES `publication_curation` WRITE;
/*!40000 ALTER TABLE `publication_curation` DISABLE KEYS */;
INSERT INTO `publication_curation` VALUES (1,'Unavailable'),(2,'Not Found'),(3,'Have Hardcopy'),(4,'Have PDF'),(5,'Have Other Electronic'),(6,'Initial Review');
/*!40000 ALTER TABLE `publication_curation` ENABLE KEYS */;
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
  `external_id` varchar(64) DEFAULT NULL,
  `generated_id` varchar(255) DEFAULT NULL,
  `parent_term` int(11) DEFAULT NULL,
  `merged` char(1) DEFAULT NULL,
  `merge_status` varchar(64) DEFAULT NULL,
  `uidset` int(11) DEFAULT NULL,
  `authority` int(11) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_term__idx` (`parent_term`),
  KEY `uidset__idx` (`uidset`),
  KEY `authority__idx` (`authority`),
  KEY `parent__idx` (`parent`),
  CONSTRAINT `taxon_ibfk_1` FOREIGN KEY (`parent_term`) REFERENCES `term` (`id`) ON DELETE CASCADE,
  CONSTRAINT `taxon_ibfk_2` FOREIGN KEY (`uidset`) REFERENCES `uidset` (`id`) ON DELETE CASCADE,
  CONSTRAINT `taxon_ibfk_3` FOREIGN KEY (`authority`) REFERENCES `authority` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `taxon_ibfk_4` FOREIGN KEY (`parent`) REFERENCES `taxon` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taxon`
--

LOCK TABLES `taxon` WRITE;
/*!40000 ALTER TABLE `taxon` DISABLE KEYS */;
INSERT INTO `taxon` VALUES (1,'Leucauge mariana',NULL,NULL,'Emerton','1884','urn:lsid:amnh.org:spidersp:013764',NULL,NULL,NULL,NULL,14,NULL,NULL);
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
  `uidset` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `domain__idx` (`domain`),
  KEY `authority__idx` (`authority`),
  KEY `uidset__idx` (`uidset`),
  CONSTRAINT `term_ibfk_1` FOREIGN KEY (`domain`) REFERENCES `domain` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `term_ibfk_2` FOREIGN KEY (`authority`) REFERENCES `authority` (`id`) ON DELETE NO ACTION,
  CONSTRAINT `term_ibfk_3` FOREIGN KEY (`uidset`) REFERENCES `uidset` (`id`) ON DELETE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=111939 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `term`
--

LOCK TABLES `term` WRITE;
/*!40000 ALTER TABLE `term` DISABLE KEYS */;
INSERT INTO `term` VALUES (4838,'http://purl.obolibrary.org/obo/NCBITaxon_336608',3,NULL,'Tetragnatha straminea','http://arachb.org/arachb/TEST_0000001',NULL,1),(10473,'http://purl.obolibrary.org/obo/SPD_0000001',2,NULL,'whole organism','http://purl.obolibrary.org/obo/SPD_0000001',NULL,2),(11052,'http://purl.obolibrary.org/obo/NBO_0000002',1,4,'whole body movement',NULL,NULL,15),(11059,'http://purl.obolibrary.org/obo/NBO_0000010',1,4,'reproductive behavior',NULL,NULL,17),(11398,'http://purl.obolibrary.org/obo/NBO_0000358',1,4,'resting posture',NULL,'\"Intentionally or habitually assumed arrangement of the body and its limbs in inactivity.\" [NBO:GVG]',16),(111938,'urn:lsid:amnh.org:spidersp:013764',3,10,'Leucauge mariana',NULL,NULL,14);
/*!40000 ALTER TABLE `term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uidset`
--

DROP TABLE IF EXISTS `uidset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uidset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` varchar(256) DEFAULT NULL,
  `generated_id` varchar(64) DEFAULT NULL,
  `ref_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `generated_id` (`generated_id`),
  UNIQUE KEY `ref_id` (`ref_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uidset`
--

LOCK TABLES `uidset` WRITE;
/*!40000 ALTER TABLE `uidset` DISABLE KEYS */;
INSERT INTO `uidset` VALUES (1,'http://purl.obolibrary.org/obo/NCBITaxon_336608',NULL,'http://purl.obolibrary.org/obo/NCBITaxon_336608'),(2,'http://purl.obolibrary.org/obo/SPD_0000001',NULL,'http://purl.obolibrary.org/obo/SPD_0000001'),(3,NULL,'http://arachb.org/arachb/ARACHB_0000322','http://arachb.org/arachb/ARACHB_0000322'),(4,NULL,'http://arachb.org/arachb/ARACHB_0000321','http://arachb.org/arachb/ARACHB_0000321'),(5,NULL,'http://arachb.org/arachb/ARACHB_0000343','http://arachb.org/arachb/ARACHB_0000343'),(6,NULL,'http://arachb.org/arachb/ARACHB_0000410','http://arachb.org/arachb/ARACHB_0000410'),(7,NULL,'http://arachb.org/arachb/ARACHB_0000411','http://arachb.org/arachb/ARACHB_0000411'),(8,NULL,'http://arachb.org/arachb/ARACHB_0000424','http://arachb.org/arachb/ARACHB_0000424'),(9,NULL,'http://arachb.org/arachb/ARACHB_0000425','http://arachb.org/arachb/ARACHB_0000425'),(10,NULL,'http://arachb.org/arachb/ARACHB_0000099','http://arachb.org/arachb/ARACHB_0000099'),(11,NULL,'http://arachb.org/arachb/ARACHB_0000285','http://arachb.org/arachb/ARACHB_0000285'),(12,'http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;2',NULL,'http://dx.doi.org/10.1636/0161-8202(2000)028[0097:HDLHAB]2.0.CO;'),(13,NULL,'http://arachb.org/arachb/ARACHB_0000311','http://arachb.org/arachb/ARACHB_0000311'),(14,'urn:lsid:amnh.org:spidersp:013764',NULL,'urn:lsid:amnh.org:spidersp:013764'),(15,'http://purl.obolibrary.org/obo/NBO_0000002',NULL,'http://purl.obolibrary.org/obo/NBO_0000002'),(16,'http://purl.obolibrary.org/obo/NBO_0000358',NULL,'http://purl.obolibrary.org/obo/NBO_0000358'),(17,'http://purl.obolibrary.org/obo/NB0_0000010',NULL,'http://purl.obolibrary.org/obo/NB0_0000010');
/*!40000 ALTER TABLE `uidset` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-08-05 10:19:44
