# MySQL-Front 5.1  (Build 2.7)

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE */;
/*!40101 SET SQL_MODE='STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES */;
/*!40103 SET SQL_NOTES='ON' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;


# Host: localhost    Database: skyd
# ------------------------------------------------------
# Server version 5.1.56-community

DROP DATABASE IF EXISTS `skyd`;
CREATE DATABASE `skyd` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `skyd`;

#
# Source for table gc_card
#

DROP TABLE IF EXISTS `gc_card`;
CREATE TABLE `gc_card` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `description` varchar(20) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Dumping data for table gc_card
#
/*!40000 ALTER TABLE `gc_card` DISABLE KEYS */;

INSERT INTO `gc_card` VALUES (1,'骏卡','（卡密）','2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_card` VALUES (2,'盛大点卡','（卡密）','2014-09-28','2014-09-28 22:47:20');
INSERT INTO `gc_card` VALUES (3,'空中网一卡通','（卡密）','2014-09-28','2014-09-28');
INSERT INTO `gc_card` VALUES (4,'完美卡','（卡密）','2014-09-28','2014-09-28');
INSERT INTO `gc_card` VALUES (5,'久游卡','（卡密）','2014-09-28','2014-09-28');
/*!40000 ALTER TABLE `gc_card` ENABLE KEYS */;

#
# Source for table gc_card_price
#

DROP TABLE IF EXISTS `gc_card_price`;
CREATE TABLE `gc_card_price` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `cardId` int(11) NOT NULL,
  `priceId` int(11) NOT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

#
# Dumping data for table gc_card_price
#
/*!40000 ALTER TABLE `gc_card_price` DISABLE KEYS */;

INSERT INTO `gc_card_price` VALUES (16,1,19,'2014-09-28','2014-09-28');
INSERT INTO `gc_card_price` VALUES (17,1,21,'2014-09-28','2014-09-28');
INSERT INTO `gc_card_price` VALUES (18,4,21,'2014-09-28','2014-09-28');
INSERT INTO `gc_card_price` VALUES (19,2,19,'2014-09-28','2014-09-28');
INSERT INTO `gc_card_price` VALUES (20,2,21,'2014-09-28','2014-09-28');
INSERT INTO `gc_card_price` VALUES (21,5,21,'2014-09-28','2014-09-28');
INSERT INTO `gc_card_price` VALUES (22,3,20,'2014-09-28','2014-09-28');
/*!40000 ALTER TABLE `gc_card_price` ENABLE KEYS */;

#
# Source for table gc_ivrchannel
#

DROP TABLE IF EXISTS `gc_ivrchannel`;
CREATE TABLE `gc_ivrchannel` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `cardId` int(11) NOT NULL,
  `priceId` int(11) NOT NULL,
  `paytypeId` int(11) NOT NULL,
  `called` varchar(50) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `fee` varchar(255) DEFAULT NULL,
  `province` varchar(250) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

#
# Dumping data for table gc_ivrchannel
#
/*!40000 ALTER TABLE `gc_ivrchannel` DISABLE KEYS */;

INSERT INTO `gc_ivrchannel` VALUES (27,1,19,17,'125908758666','中国移动用户服务拨打125908758666后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共8分钟左右。','全国（除广东、上海、四川、辽宁、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报两次，请注意记录。\r\n2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (28,1,21,17,'12590511203','中国移动用户服务拨打12590511203后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共12分钟左右。','全国（除广东、四川、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报三次，请注意记录。\r\n2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (29,1,21,17,'12590511602','中国移动用户服务拨打12590511602后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共12分钟左右。','全国（除广东、四川、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报三次，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (30,1,21,17,'12590730693','中国移动用户服务拨打12590730693后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共12分钟左右。','全国（除广东、四川、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报两次，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (31,1,21,17,'12590730695','中国移动用户服务拨打12590730695后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共14分钟左右。','全国（除广东、四川、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报两次，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (32,1,21,17,'125908758502','中国移动用户服务拨打125908758502后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共12分钟左右。','全国（除广东、四川、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报两次，请注意记录。\r\n2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (33,1,21,17,'125908758585','中国移动用户服务拨打125908758585后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','2元/分钟（不含通信费）。\r\n一共8分钟左右。','全国（除广东、上海、四川、辽宁、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报两次，请注意记录。\r\n2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (34,1,21,17,'12590658602','中国移动用户服务拨打12590658602后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','2元/分钟（不含通信费）。\r\n一共8分钟左右。','全国（除广东、上海、四川、辽宁、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报两次，请注意记录。\r\n2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (35,1,21,17,'12590658604','中国移动用户服务拨打12590658604后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','2元/分钟（不含通信费）。\r\n一共8分钟左右。','全国（除广东、上海、四川、辽宁、天津外）','1． 点卡的帐号信息是通过语音提示播报，一共播报两次，请注意记录。\r\n2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (36,2,19,17,'1259087584','中国移动用户服务拨打1259087584后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共8分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (37,2,21,17,'12590511622','中国移动用户服务拨打12590511622按6号键后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共13分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (38,2,21,17,'12590730692','中国移动用户服务拨打12590730692后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共13分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (39,2,21,17,'12590730694','中国移动用户服务拨打12590730694后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共13分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (40,2,21,17,'1259087589','中国移动用户服务拨打1259087589后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共13分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (41,2,21,17,'12590658601','中国移动用户服务拨打12590658601后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','2元/分钟（不含通信费）。\r\n一共8分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (42,3,20,17,'12590803005','中国移动用户服务拨打12590803005后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共12分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (43,3,20,17,'12590730692','中国移动用户服务拨打12590730692后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共12分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (44,3,20,17,'12590511622','中国移动用户服务拨打12590511622按6号键后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','1元/分钟（不含通信费）。\r\n一共12分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
INSERT INTO `gc_ivrchannel` VALUES (45,3,20,17,'12590658603','中国移动用户服务拨打12590658603后，按提示音进入购买环节。用户在听完足够时长后即可获得点卡。','2元/分钟（不含通信费）。\r\n一共7分钟左右。','全国','1． 点卡的帐号信息是通过语音提示播报，请注意记录。 2． 此号码只支持中国移动用户。','2014-10-06','2014-10-06');
/*!40000 ALTER TABLE `gc_ivrchannel` ENABLE KEYS */;

#
# Source for table gc_paychannel
#

DROP TABLE IF EXISTS `gc_paychannel`;
CREATE TABLE `gc_paychannel` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `cardId` int(11) NOT NULL,
  `priceId` int(11) NOT NULL,
  `paytypeId` int(11) NOT NULL,
  `msg` varchar(50) DEFAULT NULL,
  `spnum` varchar(50) DEFAULT NULL,
  `fee` int(11) NOT NULL,
  `feetype` int(11) NOT NULL DEFAULT '1',
  `feecount` int(11) NOT NULL DEFAULT '1',
  `province` varchar(250) DEFAULT NULL,
  `callouturl` varchar(255) DEFAULT NULL,
  `sendcardurl` varchar(255) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `note1` varchar(250) DEFAULT NULL,
  `note2` varchar(250) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

#
# Dumping data for table gc_paychannel
#
/*!40000 ALTER TABLE `gc_paychannel` DISABLE KEYS */;

INSERT INTO `gc_paychannel` VALUES (22,1,19,16,'1','1066950088',2,2,5,'北京','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/','test','test1','','2014-08-26','2014-08-26');
INSERT INTO `gc_paychannel` VALUES (23,1,21,16,'80','106695006',2,2,8,'北京','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/','test2','test2','','2014-08-26','2014-08-26');
INSERT INTO `gc_paychannel` VALUES (24,4,21,16,'81','106695006',2,2,8,'北京','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/','test3','test3','','2014-08-26','2014-08-26');
INSERT INTO `gc_paychannel` VALUES (25,2,19,16,'21','106695006',2,2,5,'ALL','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/','test','test','','2014-08-26','2014-08-26');
INSERT INTO `gc_paychannel` VALUES (26,2,21,16,'25','106695006',2,2,8,'ALL','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/','test','test','','2014-08-26','2014-08-26');
INSERT INTO `gc_paychannel` VALUES (27,5,21,16,'1','1066151888',2,2,8,'ALL','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/','test','test','','2014-09-09','2014-09-09');
INSERT INTO `gc_paychannel` VALUES (28,3,20,16,'6','1066151888',2,2,8,'ALL','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/','test','test','','2014-09-09','2014-09-09');
INSERT INTO `gc_paychannel` VALUES (29,1,19,19,'1','1',1000,1,1,'北京',NULL,NULL,'test','test',NULL,'2014-11-22','2014-11-22');
/*!40000 ALTER TABLE `gc_paychannel` ENABLE KEYS */;

#
# Source for table gc_paytype
#

DROP TABLE IF EXISTS `gc_paytype`;
CREATE TABLE `gc_paytype` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `img` varchar(50) DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

#
# Dumping data for table gc_paytype
#
/*!40000 ALTER TABLE `gc_paytype` DISABLE KEYS */;

INSERT INTO `gc_paytype` VALUES (16,'移动短信支付','ico-chinamobile.png','<li>短信支付：即通过发送短信指令完成支付扣费；<br /></li>\r\n<li>支持中国移动全球通、动感地带、神州行手机用户，括号段：134~139、147、150~152、157~159、182、183、187、188；</li>\r\n<li>支持资费：2/5/10/15/20/25/30元。<br /></li>','2014-08-17 23:52:00','2014-08-17 23:52:00');
INSERT INTO `gc_paytype` VALUES (17,'移动IVR支付','ico-mobilecard.png','<li>移动IVR支付：即通过拨打移动IVR完成支付扣费；<br /></li>','2014-08-17 23:52:01','2014-08-17 23:52:01');
INSERT INTO `gc_paytype` VALUES (18,'联通手机支付','ico-chinaunicom.png','<li>短信支付：即通过发送短信指令完成支付扣费；<br /></li>\r\n<li>支持中国联通手机手机用户，包括号段：130、131、132、155、156、185、186。</li>\r\n<li>在支付过程中，如您选择的支付通道受限限制，请更换其它通道进行支付。</li>\r\n<li>请按页面和短信提示完成操作。</li>','2014-08-17 23:52:01','2014-08-17 23:52:01');
INSERT INTO `gc_paytype` VALUES (19,'移动话费支付','ico-gamecard.png','<li>话费支付：即通过输入移动验证码完成支付扣费；<br /></li>\r\n<li>支持中国移动手机手机用户，包括号段：134~139、147、150~152、157~159、182、183、187、188；</li>\r\n<li>在支付过程中，如您选择的支付通道受限限制，请更换其它通道进行支付。</li>\r\n<li>请按页面提示完成操作。</li>','2014-08-17 23:52:01','2014-08-17 23:52:01');
INSERT INTO `gc_paytype` VALUES (20,'手机充值卡类支付','ico-mobilecard.png',NULL,'2014-08-17 23:52:01','2014-08-17 23:52:01');
INSERT INTO `gc_paytype` VALUES (21,'游戏卡类支付','ico-gamecard.png',NULL,'2014-08-17 23:52:01','2014-08-17 23:52:01');
INSERT INTO `gc_paytype` VALUES (22,'电信手机支付','ico-chintelecom.png','<li>短信支付：即通过发送短信指令完成支付扣费；<br /></li>\r\n<li>支持中国电信天翼手机手机用户，包括号段：133、153、180、181、189。</li>\r\n<li>支持资费：1/2/5/10/15/20/25/30元。</li>','2014-08-17 23:52:01','2014-08-17 23:52:01');
/*!40000 ALTER TABLE `gc_paytype` ENABLE KEYS */;

#
# Source for table gc_price
#

DROP TABLE IF EXISTS `gc_price`;
CREATE TABLE `gc_price` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `price` int(11) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

#
# Dumping data for table gc_price
#
/*!40000 ALTER TABLE `gc_price` DISABLE KEYS */;

INSERT INTO `gc_price` VALUES (16,900,'900点','2014-08-17 23:49:00','2014-08-17 23:49:00');
INSERT INTO `gc_price` VALUES (17,650,'650点','2014-08-17 23:49:31','2014-08-17 23:49:31');
INSERT INTO `gc_price` VALUES (18,600,'600点','2014-08-17 23:49:39','2014-08-17 23:49:39');
INSERT INTO `gc_price` VALUES (19,300,'300点','2014-08-17 23:49:45','2014-08-17 23:49:45');
INSERT INTO `gc_price` VALUES (20,100,'100点(5元)','2014-09-28','2014-09-28');
INSERT INTO `gc_price` VALUES (21,500,'500点','2014-09-28','2014-09-28');
/*!40000 ALTER TABLE `gc_price` ENABLE KEYS */;

#
# Source for table gc_price_paytype
#

DROP TABLE IF EXISTS `gc_price_paytype`;
CREATE TABLE `gc_price_paytype` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `priceId` int(11) NOT NULL DEFAULT '0',
  `paytypeId` int(11) NOT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

#
# Dumping data for table gc_price_paytype
#
/*!40000 ALTER TABLE `gc_price_paytype` DISABLE KEYS */;

INSERT INTO `gc_price_paytype` VALUES (16,16,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (17,17,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (18,18,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (19,19,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (20,20,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (21,21,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (22,22,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (23,23,16,'2014-08-17 23:45:52','2014-08-17 23:45:52');
INSERT INTO `gc_price_paytype` VALUES (24,16,17,'2014-10-07','2014-10-07');
INSERT INTO `gc_price_paytype` VALUES (25,17,17,'2014-10-07','2014-10-07');
INSERT INTO `gc_price_paytype` VALUES (26,19,17,'2014-10-07','2014-10-07');
INSERT INTO `gc_price_paytype` VALUES (27,20,17,'2014-10-07','2014-10-07');
INSERT INTO `gc_price_paytype` VALUES (28,22,17,'2014-10-07','2014-10-07');
INSERT INTO `gc_price_paytype` VALUES (29,16,19,'2014-11-22','2014-11-22');
INSERT INTO `gc_price_paytype` VALUES (30,17,19,'2014-11-22','2014-11-22');
/*!40000 ALTER TABLE `gc_price_paytype` ENABLE KEYS */;

#
# Source for table gw_card_password
#

DROP TABLE IF EXISTS `gw_card_password`;
CREATE TABLE `gw_card_password` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `cardid` int(11) NOT NULL DEFAULT '0',
  `priceid` int(11) NOT NULL DEFAULT '0',
  `cardno` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `state` int(11) NOT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

#
# Dumping data for table gw_card_password
#
/*!40000 ALTER TABLE `gw_card_password` DISABLE KEYS */;

INSERT INTO `gw_card_password` VALUES (20,1,21,'123123123','dsfdfdsfsdf',0,'2014-09-08','2014-09-08 23:25:06');
INSERT INTO `gw_card_password` VALUES (21,1,21,'werwe','sdfsadf',0,'2014-09-08','2014-09-08 23:33:59');
INSERT INTO `gw_card_password` VALUES (22,1,19,'aaa','aaa',0,'2014-09-09','2014-09-22 15:44:00');
INSERT INTO `gw_card_password` VALUES (23,1,19,'bbb','bbb',0,'2014-09-09','2014-09-23 14:16:00');
INSERT INTO `gw_card_password` VALUES (24,1,19,'ccc','ccc',0,'2014-09-09','2014-09-19 14:40:00');
INSERT INTO `gw_card_password` VALUES (25,2,19,'aaa','aaa',0,'2014-09-09','2014-09-29 23:38:00');
INSERT INTO `gw_card_password` VALUES (26,2,19,'adf','asdf',0,'2014-09-09','2014-09-09');
INSERT INTO `gw_card_password` VALUES (27,2,21,'aaaf','asd',0,'2014-09-09','2014-09-09');
INSERT INTO `gw_card_password` VALUES (28,2,21,'sdf','sdfs',0,'2014-09-09','2014-09-09');
INSERT INTO `gw_card_password` VALUES (29,3,21,'dsf','asdfsf',0,'2014-09-09','2014-09-09');
INSERT INTO `gw_card_password` VALUES (30,3,21,'asss','sssa',0,'2014-09-09','2014-09-09');
INSERT INTO `gw_card_password` VALUES (31,4,21,'www','www',0,'2014-09-09','2014-09-09');
INSERT INTO `gw_card_password` VALUES (32,4,21,'qqq','qqq',0,'2014-09-09','2014-09-09');
INSERT INTO `gw_card_password` VALUES (33,1,19,'aaaaa','aaaaa',0,'2014-10-07 20:25:37','2014-10-07 20:25:37');
INSERT INTO `gw_card_password` VALUES (34,1,19,'bbbbb','bbbbb',0,'2014-10-07 20:25:37','2014-10-07 20:25:37');
INSERT INTO `gw_card_password` VALUES (35,1,19,'ccccc','ccccc',0,'2014-10-07 20:25:37','2014-10-07 20:25:37');
/*!40000 ALTER TABLE `gw_card_password` ENABLE KEYS */;

#
# Source for table mobilelocation
#

DROP TABLE IF EXISTS `mobilelocation`;
CREATE TABLE `mobilelocation` (
  `Id` int(11) NOT NULL,
  `code` varchar(20) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_mobil_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table mobilelocation
#
/*!40000 ALTER TABLE `mobilelocation` DISABLE KEYS */;

INSERT INTO `mobilelocation` VALUES (233664,'1868158','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233665,'1854785','内蒙古','巴彦淖尔',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233666,'1858724','云南','昆明',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233667,'1857522','广东','惠州',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233668,'1857455','湖南','怀化',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233669,'1562466','内蒙古','乌兰察布',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233670,'1850760','广东','中山',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233671,'1453882','云南','昆明',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233672,'1866173','山东','青岛',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233673,'1856180','山东','青岛',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233674,'1568932','山东','淄博',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233675,'1856220','山东','烟台',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233676,'1866135','山东','泰安',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233677,'1865492','山东','济南',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233678,'1858846','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233679,'1856951','湖南','长沙',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233680,'1857557','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233681,'1857351','湖南','郴州',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233682,'1858372','四川','成都',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233683,'1856383','山东','烟台',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233684,'1850647','山东','潍坊',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233685,'1569447','山东','青岛',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233686,'1856942','湖南','长沙',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233687,'1857593','广东','湛江',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233688,'1568856','山东','烟台',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233689,'1869263','湖南','株洲',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233690,'1856027','山东','淄博',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233691,'1861509','山东','潍坊',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233692,'1856668','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233693,'1855314','山东','济南',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233694,'1866939','山东','威海',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233695,'1868206','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233696,'1861702','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233697,'1568990','山东','青岛',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233698,'1865458','山东','济南',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233699,'1864850','内蒙古','通辽',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233700,'1857556','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233701,'1852102','上海','上海',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233702,'1868234','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233703,'1866964','山东','临沂',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233704,'1857662','广东','深圳',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233705,'1856049','山东','青岛',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233706,'1856255','山东','青岛',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233707,'1851621','上海','上海',NULL,NULL);
INSERT INTO `mobilelocation` VALUES (233708,'1355292','北京','北京',NULL,NULL);
/*!40000 ALTER TABLE `mobilelocation` ENABLE KEYS */;

#
# Source for table sms_bill_log
#

DROP TABLE IF EXISTS `sms_bill_log`;
CREATE TABLE `sms_bill_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `spnum` varchar(20) DEFAULT NULL,
  `msg` varchar(200) DEFAULT NULL,
  `linkid` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `btime` datetime DEFAULT NULL,
  `etime` datetime DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `fee` int(11) DEFAULT NULL,
  `sfid` int(11) DEFAULT NULL,
  `cpid` int(11) DEFAULT NULL,
  `channelid` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `syncurl` varchar(200) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_bill_btime` (`btime`),
  KEY `ind_bill_spnum` (`spnum`),
  KEY `ind_bill_mobile` (`mobile`),
  KEY `ind_bill_parentid` (`parentid`),
  KEY `ind_bill_province` (`province`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table sms_bill_log
#
/*!40000 ALTER TABLE `sms_bill_log` DISABLE KEYS */;

/*!40000 ALTER TABLE `sms_bill_log` ENABLE KEYS */;

#
# Source for table sms_bill_temp
#

DROP TABLE IF EXISTS `sms_bill_temp`;
CREATE TABLE `sms_bill_temp` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `spnum` varchar(20) DEFAULT NULL,
  `msg` varchar(200) DEFAULT NULL,
  `linkid` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `btime` datetime DEFAULT NULL,
  `etime` datetime DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `fee` int(11) DEFAULT NULL,
  `sfid` int(11) DEFAULT NULL,
  `cpid` int(11) DEFAULT NULL,
  `channelid` int(11) DEFAULT '0',
  `type` int(11) DEFAULT NULL,
  `sendnum` int(11) DEFAULT NULL,
  `syncurl` varchar(200) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  `flag` varchar(255) DEFAULT NULL COMMENT '1:上行 2:状态报告 3:计费成功 4:预约外呼 5:外呼中 6:外呼成功 7:下发成功',
  PRIMARY KEY (`Id`),
  KEY `ind_bill_btime` (`btime`),
  KEY `ind_bill_spnum` (`spnum`),
  KEY `ind_bill_mobile` (`mobile`),
  KEY `ind_bill_parentid` (`parentid`),
  KEY `ind_bill_province` (`province`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

#
# Dumping data for table sms_bill_temp
#
/*!40000 ALTER TABLE `sms_bill_temp` DISABLE KEYS */;

INSERT INTO `sms_bill_temp` VALUES (1,'13552922122','1066950010','C6F25sk','3630383136353700','DELIVRD','2014-09-08 00:18:15','2014-09-08 20:40:50','北京','北京',1000,1,2,22,0,0,'http://218.206.72.142:8080/CP_WEB_BILL/callout.jsp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (2,'13552922122','10661234','111','3630383136353701','DELIVRD','2014-09-09 22:52:28','2014-09-09 22:55:54','北京','北京',1000,1,2,25,0,0,'http://218.206.72.142:8080/CP_WEB_BILL/callout.jsp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (5,'13552922122','10661234','222','3630383136353702','DELIVRD','2014-09-09 23:39:27','2014-09-09 23:37:07','北京','北京',500,1,2,26,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (6,'13552922122','10661234','222','3630383136353703','DELIVRD','2014-09-09 23:40:51','2014-09-09 23:40:33','北京','北京',500,1,2,26,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (8,'15210946199','10661234','111','3630383136353711','DELIVRD','2014-09-11 22:46:36','2014-09-11 22:46:38','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (9,'15971808277','106695006','25','12430208855088442349',NULL,'2014-09-12 12:43:25','2014-09-12 12:50:31','其他','其他',100,999999999,0,0,0,3,NULL,0,'3');
INSERT INTO `sms_bill_temp` VALUES (10,'15206718732','106695006','25','19434917051896829905',NULL,'2014-09-12 19:44:12',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (11,'15206718732','106695006','25','19455417256136146340',NULL,'2014-09-12 19:45:20',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (12,'13552922122','10665110','1234','26881487','DELIVERED','2014-09-15 08:14:39','2014-09-15 08:14:44','北京','北京',100,2,0,0,0,3,NULL,0,'3');
INSERT INTO `sms_bill_temp` VALUES (13,NULL,NULL,NULL,NULL,NULL,NULL,'2014-09-15 08:17:28',NULL,NULL,0,0,0,0,0,0,NULL,0,'2');
INSERT INTO `sms_bill_temp` VALUES (14,'13552922122','1066950010','C6F25sk','3630383136353733','DELIVRD','2014-09-15 14:16:19','2014-09-15 14:16:21','北京','北京',1000,1,2,22,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (15,'15853376188','106695006','25','19420917257136780167',NULL,'2014-09-15 19:41:47',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (16,'15853376188','106695006','25','19440417051897190691',NULL,'2014-09-15 19:44:28',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (17,'15853376188','106695006','25','19444410533862145306',NULL,'2014-09-15 19:45:29',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (18,'15853376188','106695006','25','19450517051897190779',NULL,'2014-09-15 19:45:29',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (19,'13552922122','10665110','1234','268814871','DELIVERED','2014-09-16 00:55:15','2014-09-16 00:55:18','北京','北京',100,2,0,27,0,0,NULL,0,'4');
INSERT INTO `sms_bill_temp` VALUES (20,'13552922122','10665110','1234','268814872','DELIVERED','2014-09-16 01:04:10','2014-09-16 01:04:08','北京','北京',100,2,0,27,0,0,NULL,0,'4');
INSERT INTO `sms_bill_temp` VALUES (21,'13552922122','10665110','1234','268814873','DELIVERED','2014-09-16 01:04:22','2014-09-16 01:04:22','北京','北京',100,2,0,27,0,0,NULL,0,'4');
INSERT INTO `sms_bill_temp` VALUES (22,'13552922122','10665110','1234','268814874','DELIVERED','2014-09-16 01:04:40','2014-09-16 01:04:41','北京','北京',100,2,0,27,0,0,NULL,0,'4');
INSERT INTO `sms_bill_temp` VALUES (23,'13552922122','10665110','1234','268814875','DELIVERED','2014-09-16 01:04:47','2014-09-16 01:04:50','北京','北京',100,2,0,27,0,0,NULL,0,'4');
INSERT INTO `sms_bill_temp` VALUES (25,'15210946199','10661234','111','3630383136353333','DELIVRD','2014-09-18 18:23:38','2014-09-18 18:23:42','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (26,'15210946199','10661234','111','363038313','DELIVRD','2014-09-18 21:37:39','2014-09-18 21:37:43','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (27,'15210946199','10661234','111','363038314','DELIVRD','2014-09-18 23:09:58','2014-09-18 23:10:01','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (28,'15210946199','10661234','111','36303831362','DELIVRD','2014-09-19 14:37:38','2014-09-19 14:37:41','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (29,'13552922122','10661234','111','36303831363','DELIVRD','2014-09-19 14:38:36','2014-09-19 14:38:38','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (30,'13552922122','10661234','111','36303','DELIVRD','2014-09-22 15:41:30','2014-09-22 15:41:34','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (31,'13552922122','10661234','111','363','DELIVRD','2014-09-23 14:13:21','2014-09-23 14:13:35','北京','北京',1000,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (32,'15949855882','106695006','25','07164507159887432232',NULL,'2014-09-25 07:17:32',NULL,'山东','潍坊',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (33,NULL,NULL,NULL,NULL,NULL,NULL,'2014-09-26 16:53:38',NULL,NULL,0,0,0,0,0,0,NULL,0,'2');
INSERT INTO `sms_bill_temp` VALUES (34,'15853376188','106695006','25','12572817256139061604',NULL,'2014-09-27 12:56:42',NULL,'山东','淄博',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (35,'13623635973','106695006','80','09441511133034333551',NULL,'2014-09-28 09:44:43',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (36,'18275280871','1066151888','6','10282717956099637184',NULL,'2014-09-28 10:28:34',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (37,'18275280871','1066151888','6','10345617956099637594',NULL,'2014-09-28 10:35:15',NULL,'其他','其他',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (38,'15735081281','106695006','80','11104215176079735129',NULL,'2014-09-28 11:11:08','2014-09-28 11:14:47','其他','其他',100,999999999,0,0,0,3,NULL,0,'3');
INSERT INTO `sms_bill_temp` VALUES (39,'15735081281','106695006','25','11114717295042029858',NULL,'2014-09-28 11:11:51','2014-09-28 11:14:48','其他','其他',100,999999999,0,0,0,3,NULL,0,'3');
INSERT INTO `sms_bill_temp` VALUES (40,'13996122424','1066151888','6','11535927788069281942',NULL,'2014-09-28 11:54:48','2014-09-28 11:55:18','其他','其他',100,999999999,0,0,0,3,NULL,0,'3');
INSERT INTO `sms_bill_temp` VALUES (41,'13879024268','106695006','80','12015226921072454660',NULL,'2014-09-28 12:02:33','2014-09-28 12:05:25','其他','其他',100,999999999,0,0,0,3,NULL,0,'3');
INSERT INTO `sms_bill_temp` VALUES (42,'13753987906','106695006','21','07433707275028921346',NULL,'2014-09-29 07:43:58','2014-09-29 08:40:25','山西','运城',100,999999999,0,0,0,3,NULL,0,'3');
INSERT INTO `sms_bill_temp` VALUES (43,'13753987906','106695006','21','07443707275028921370',NULL,'2014-09-29 07:44:59',NULL,'山西','运城',100,999999999,0,0,0,0,NULL,0,'1');
INSERT INTO `sms_bill_temp` VALUES (44,NULL,NULL,NULL,NULL,NULL,NULL,'2014-09-29 14:05:30',NULL,NULL,0,0,0,0,0,0,NULL,0,'2');
INSERT INTO `sms_bill_temp` VALUES (45,'13753987906','106695006','21','07433707275028921347','DELIVRD','2014-09-29 23:07:42','2014-09-29 23:08:09','山西','运城',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (46,'13552922122','106695006','21','07433707275028921341','DELIVRD','2014-09-29 23:20:14','2014-09-29 23:20:18','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (47,'13552922122','106695006','21','07433707275028921342','DELIVRD','2014-09-29 23:20:46','2014-09-29 23:20:48','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (48,'13552922122','106695006','21','07433707275028921343','DELIVRD','2014-09-29 23:20:57','2014-09-29 23:20:59','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (49,'13552922122','106695006','21','07433707275028921344','DELIVRD','2014-09-29 23:21:05','2014-09-29 23:21:07','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (50,'13552922122','106695006','21','07433707275028921345','DELIVRD','2014-09-29 23:22:27','2014-09-29 23:22:29','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (51,'13552922122','106695006','21','07433707275028921348','DELIVRD','2014-09-29 23:22:43','2014-09-29 23:22:44','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (52,'13552922122','106695006','21','07433707275028921349','DELIVRD','2014-09-29 23:22:49','2014-09-29 23:22:50','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (53,'13552922122','106695006','21','0743370727502892131','DELIVRD','2014-09-29 23:23:44','2014-09-29 23:23:45','北京','北京',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (54,'15110540161','106695006','21','09093907275028961644','DELIVRD','2014-09-30 09:10:17','2014-10-06 20:06:00','山西','忻州',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (55,'15110540161','106695006','21','09103915176079808430','DELIVRD','2014-09-30 09:10:47','2014-10-06 20:06:03','山西','忻州',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (56,'15110540161','106695006','21','09113915176079808475','DELIVRD','2014-09-30 09:11:48','2014-10-06 20:06:02','山西','忻州',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (58,'18334893994','106695006','25','17505207006089821544','DELIVRD','2014-09-30 17:52:10','2014-10-06 20:06:07','其他','其他',200,1,2,26,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (59,'18334893994','106695006','25','17510417295042120281','DELIVRD','2014-09-30 17:52:15','2014-10-06 20:06:05','其他','其他',200,1,2,26,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (60,'18334893994','106695006','25','17512007275028978809',NULL,'2014-09-30 17:52:46',NULL,'其他','其他',200,1,2,26,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'1');
INSERT INTO `sms_bill_temp` VALUES (61,'15095867474','106695006','80','18221627789069321887',NULL,'2014-09-30 18:22:53',NULL,'重庆','重庆',200,1,2,23,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'1');
INSERT INTO `sms_bill_temp` VALUES (62,'14789013319','106695006','80','22424827804296162345',NULL,'2014-09-30 22:43:43',NULL,'西藏','拉萨',200,1,2,23,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'1');
INSERT INTO `sms_bill_temp` VALUES (63,'18289013196','1066950088','1','00560010164906819091','DELIVRD','2014-10-01 00:55:13','2014-10-06 20:05:51','西藏','拉萨',200,1,2,22,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (64,'15110540161','106695006','21','16253815176079893986',NULL,'2014-10-01 16:26:02',NULL,'山西','忻州',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'1');
INSERT INTO `sms_bill_temp` VALUES (65,'15110540161','106695006','21','16263911133034496123',NULL,'2014-10-01 16:26:44',NULL,'山西','忻州',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'1');
INSERT INTO `sms_bill_temp` VALUES (66,'15110540161','106695006','21','16273807275029047326',NULL,'2014-10-01 16:27:47',NULL,'山西','忻州',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'1');
INSERT INTO `sms_bill_temp` VALUES (67,'15984219039','106695006','25','19055807773188206069','DELIVRD','2014-10-01 19:08:00','2014-10-06 20:23:25','四川','内江',200,1,2,26,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (68,'15984219039','106695006','25','19065825704168722090','DELIVRD','2014-10-01 19:08:23','2014-10-06 20:25:26','四川','内江',200,1,2,26,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (69,'13623447298','106695006','21','08463115176079932010','DELIVRD','2014-10-02 08:46:44','2014-10-06 19:38:31','山西','运城',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (70,'13623447298','106695006','21','08483115176079932219','DELIVRD','2014-10-02 08:48:30','2014-10-06 19:39:13','山西','运城',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (71,'13623447298','106695006','21','08573511133034535239','DELIVRD','2014-10-02 08:57:41','2014-10-06 19:39:17','山西','运城',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (72,'13623447298','106695006','21','09043115176079933870','DELIVRD','2014-10-02 09:04:53','2014-10-06 19:39:18','山西','运城',200,1,2,25,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (75,NULL,NULL,NULL,NULL,NULL,NULL,'2014-10-08 10:47:42',NULL,NULL,0,0,0,0,0,0,NULL,0,'2');
INSERT INTO `sms_bill_temp` VALUES (76,'13799774153','106695006','80','11085024051214919115','DELIVRD','2014-10-08 11:09:06','2014-10-08 11:12:17','福建','厦门',200,1,2,23,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (77,'13996122424','106695006','80','11410707767199404054','DELIVRD','2014-10-08 11:39:24','2014-10-08 11:43:14','重庆','重庆',200,1,2,23,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'3');
INSERT INTO `sms_bill_temp` VALUES (78,'18389004712','1066950088','1','17323727805296213101','DELIVRD','2014-10-08 17:32:42','2014-10-08 17:40:50','西藏','拉萨',200,1,2,22,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (79,'18389004712','1066950088','1','17365327806296213127','DELIVRD','2014-10-08 17:36:58','2014-10-08 17:48:05','西藏','拉萨',200,1,2,22,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (80,'18389004712','1066950088','1','17405027806296213161','DELIVRD','2014-10-08 17:40:50','2014-10-08 17:48:05','西藏','拉萨',200,1,2,22,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (81,'18389004712','1066950088','1','17415410166906869850','DELIVRD','2014-10-08 17:41:20','2014-10-08 18:41:07','西藏','拉萨',200,1,2,22,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (82,'18389004712','1066950088','1','17415110166906869849','DELIVRD','2014-10-08 17:41:20','2014-10-08 18:41:07','西藏','拉萨',200,1,2,22,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'4');
INSERT INTO `sms_bill_temp` VALUES (83,'13869770526','106695006','80','18342317259131537969',NULL,'2014-10-08 18:33:33',NULL,'山东','菏泽',200,1,2,23,0,0,'http://114.255.71.150:8099/OutcallDK/Submit.asp',2,'1');
/*!40000 ALTER TABLE `sms_bill_temp` ENABLE KEYS */;

#
# Source for table tbl_bill_log
#

DROP TABLE IF EXISTS `tbl_bill_log`;
CREATE TABLE `tbl_bill_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `caller` varchar(20) DEFAULT NULL,
  `called` varchar(20) DEFAULT NULL,
  `btime` datetime DEFAULT NULL,
  `etime` datetime DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `time` int(11) DEFAULT NULL,
  `fee` int(11) DEFAULT NULL,
  `sfid` int(11) DEFAULT NULL,
  `cpid` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `syncurl` varchar(200) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_bill_btime` (`btime`),
  KEY `ind_bill_called` (`called`),
  KEY `ind_bill_spid` (`sfid`),
  KEY `ind_bill_parentid` (`parentid`),
  KEY `ind_bill_province` (`province`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_bill_log
#
/*!40000 ALTER TABLE `tbl_bill_log` DISABLE KEYS */;

/*!40000 ALTER TABLE `tbl_bill_log` ENABLE KEYS */;

#
# Source for table tbl_bill_temp
#

DROP TABLE IF EXISTS `tbl_bill_temp`;
CREATE TABLE `tbl_bill_temp` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `caller` varchar(20) DEFAULT NULL,
  `called` varchar(20) DEFAULT NULL,
  `btime` datetime DEFAULT NULL,
  `etime` datetime DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `time` int(11) DEFAULT NULL,
  `fee` int(11) DEFAULT NULL,
  `sfid` int(11) DEFAULT NULL,
  `cpid` int(11) DEFAULT NULL,
  `sendnum` int(11) DEFAULT NULL,
  `syncurl` varchar(255) DEFAULT NULL,
  `parentid` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_temp_sendnum` (`sendnum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_bill_temp
#
/*!40000 ALTER TABLE `tbl_bill_temp` DISABLE KEYS */;

/*!40000 ALTER TABLE `tbl_bill_temp` ENABLE KEYS */;

#
# Source for table tbl_cp_called
#

DROP TABLE IF EXISTS `tbl_cp_called`;
CREATE TABLE `tbl_cp_called` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `cpid` int(11) DEFAULT NULL,
  `called` varchar(20) DEFAULT NULL,
  `cpname` varchar(20) DEFAULT NULL,
  `daylimit` int(11) DEFAULT NULL,
  `monthlimit` int(11) DEFAULT NULL,
  `reduce` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  `blackinfo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_cp_called
#
/*!40000 ALTER TABLE `tbl_cp_called` DISABLE KEYS */;

INSERT INTO `tbl_cp_called` VALUES (1,2,'1066950088_1',NULL,0,0,0,'2014-09-08 00:10:20','2014-09-29 21:16:02','');
INSERT INTO `tbl_cp_called` VALUES (2,2,'106695006_80',NULL,0,0,0,'2014-09-09 22:44:55','2014-09-29 21:15:57','');
INSERT INTO `tbl_cp_called` VALUES (3,2,'106695006_81',NULL,0,0,0,'2014-09-09 22:45:01','2014-09-29 21:15:52','');
INSERT INTO `tbl_cp_called` VALUES (4,2,'106695006_25',NULL,0,0,0,'2014-09-09 22:45:05','2014-09-29 21:15:48','');
INSERT INTO `tbl_cp_called` VALUES (5,2,'106695006_21',NULL,0,0,0,'2014-09-09 22:45:15','2014-09-29 21:15:43','');
INSERT INTO `tbl_cp_called` VALUES (6,2,'1066151888_1',NULL,0,0,0,'2014-09-29 21:17:07','2014-09-29 21:17:07','');
INSERT INTO `tbl_cp_called` VALUES (7,2,'1066151888_6',NULL,0,0,0,'2014-09-29 21:17:12','2014-09-29 21:17:12','');
/*!40000 ALTER TABLE `tbl_cp_called` ENABLE KEYS */;

#
# Source for table tbl_menu
#

DROP TABLE IF EXISTS `tbl_menu`;
CREATE TABLE `tbl_menu` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `target` varchar(20) DEFAULT NULL,
  `icon` varchar(20) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_menu
#
/*!40000 ALTER TABLE `tbl_menu` DISABLE KEYS */;

INSERT INTO `tbl_menu` VALUES (1,'数据管理','','right','',0,1,'2013-04-04 11:41:46','2013-04-04 11:41:46');
INSERT INTO `tbl_menu` VALUES (2,'通道管理','manage/sp!list.action','right','',1,1,'2013-04-04 11:43:07','2013-04-04 11:43:07');
INSERT INTO `tbl_menu` VALUES (3,'外呼渠道管理','manage/cp!list.action','right','',1,3,'2013-04-04 11:47:18','2013-04-04 11:47:18');
INSERT INTO `tbl_menu` VALUES (4,'外呼分配指令','manage/cpnum!list.action','right','',1,4,'2013-04-04 11:50:18','2013-04-04 11:50:18');
INSERT INTO `tbl_menu` VALUES (5,'数据统计','','right','',0,1,'2013-04-04 11:54:27','2013-04-04 11:54:27');
INSERT INTO `tbl_menu` VALUES (6,'点卡销售统计','card/cardbill!list.action','right','',5,2,'2013-04-04 11:55:10','2013-04-04 11:55:10');
INSERT INTO `tbl_menu` VALUES (7,'收入统计','manage/smsbill!list.action','right','',5,1,'2013-04-04 11:55:37','2013-04-04 11:55:37');
INSERT INTO `tbl_menu` VALUES (8,'号码管理','manage/snum!list.action','right',NULL,1,2,'2013-05-25 21:10:12','2013-05-25 21:10:15');
INSERT INTO `tbl_menu` VALUES (9,'IVR统计','cp/cpbill!list.action','right',NULL,5,1,'2013-06-11 23:41:33','2013-06-11 23:41:33');
INSERT INTO `tbl_menu` VALUES (10,'剩余点卡','card/cardbill!cardCount.action','right',NULL,5,5,'2013-06-11 23:41:33','2013-06-11 23:41:33');
INSERT INTO `tbl_menu` VALUES (11,'IVR渠道统计','manage/bill!cplist.action','right',NULL,5,2,'2013-06-19 00:08:32','2013-06-19 00:08:32');
INSERT INTO `tbl_menu` VALUES (12,'SMS渠道统计','manage/smsbill!cplist.action','right',NULL,5,4,'2013-08-10 19:16:02','2013-08-10 19:16:05');
INSERT INTO `tbl_menu` VALUES (13,'用户记录','card/cardbill!userBill.action','right',NULL,5,3,'2013-08-21 22:50:49','2013-08-21 22:50:49');
INSERT INTO `tbl_menu` VALUES (14,'SMS统计','cp/cpsmsbill!list.action','right',NULL,5,2,'2013-08-28 23:30:47','2013-08-28 23:30:47');
INSERT INTO `tbl_menu` VALUES (15,'指令信息','cp/cpsmsbill!blackinfo.action','right',NULL,5,3,'2013-11-16','2013-11-16');
INSERT INTO `tbl_menu` VALUES (16,'点卡导入','card/upload.action','right',NULL,5,4,'2014-10-07','2014-10-07');
/*!40000 ALTER TABLE `tbl_menu` ENABLE KEYS */;

#
# Source for table tbl_month
#

DROP TABLE IF EXISTS `tbl_month`;
CREATE TABLE `tbl_month` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `spid` int(11) DEFAULT NULL,
  `mobileno` varchar(20) DEFAULT NULL,
  `requesttime` varchar(20) DEFAULT NULL,
  `proviecode` varchar(20) DEFAULT NULL,
  `ringno` varchar(20) DEFAULT NULL,
  `subchannelid` varchar(20) DEFAULT NULL,
  `ordertype` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_month
#
/*!40000 ALTER TABLE `tbl_month` DISABLE KEYS */;

/*!40000 ALTER TABLE `tbl_month` ENABLE KEYS */;

#
# Source for table tbl_noprovice_code
#

DROP TABLE IF EXISTS `tbl_noprovice_code`;
CREATE TABLE `tbl_noprovice_code` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `caller` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_noprovice_code
#
/*!40000 ALTER TABLE `tbl_noprovice_code` DISABLE KEYS */;

/*!40000 ALTER TABLE `tbl_noprovice_code` ENABLE KEYS */;

#
# Source for table tbl_province_reduce
#

DROP TABLE IF EXISTS `tbl_province_reduce`;
CREATE TABLE `tbl_province_reduce` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `called` varchar(20) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `reduce` int(11) DEFAULT NULL,
  `daylimit` int(11) DEFAULT NULL,
  `monthlimit` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_province_reduce
#
/*!40000 ALTER TABLE `tbl_province_reduce` DISABLE KEYS */;

/*!40000 ALTER TABLE `tbl_province_reduce` ENABLE KEYS */;

#
# Source for table tbl_role_menu
#

DROP TABLE IF EXISTS `tbl_role_menu`;
CREATE TABLE `tbl_role_menu` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `roleid` int(11) DEFAULT NULL,
  `menuid` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_role_menu
#
/*!40000 ALTER TABLE `tbl_role_menu` DISABLE KEYS */;

INSERT INTO `tbl_role_menu` VALUES (1,1,1,'2014-08-17 23:01:49','2014-08-17 23:01:49');
INSERT INTO `tbl_role_menu` VALUES (2,1,2,'2014-08-17 23:01:51','2014-08-17 23:01:51');
INSERT INTO `tbl_role_menu` VALUES (3,1,3,'2014-08-17 23:01:51','2014-08-17 23:01:51');
INSERT INTO `tbl_role_menu` VALUES (4,1,4,'2014-08-17 23:01:51','2014-08-17 23:01:51');
INSERT INTO `tbl_role_menu` VALUES (5,1,5,'2014-08-17 23:01:51','2014-08-17 23:01:51');
INSERT INTO `tbl_role_menu` VALUES (6,11,5,'2014-08-17 23:02:49','2014-08-17 23:02:49');
INSERT INTO `tbl_role_menu` VALUES (7,1,7,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (8,1,8,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (9,10,5,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (10,10,9,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (11,11,9,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (12,1,10,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (14,1,13,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (15,10,14,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (16,11,14,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (17,1,6,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (19,10,15,'2014-08-17 23:02:52','2014-08-17 23:02:52');
INSERT INTO `tbl_role_menu` VALUES (20,11,15,'2014-08-17 23:02:52','2014-08-17 23:02:52');
/*!40000 ALTER TABLE `tbl_role_menu` ENABLE KEYS */;

#
# Source for table tbl_servicenum
#

DROP TABLE IF EXISTS `tbl_servicenum`;
CREATE TABLE `tbl_servicenum` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `opid` int(11) DEFAULT NULL,
  `spid` int(11) DEFAULT NULL,
  `spname` varchar(20) DEFAULT NULL,
  `called` varchar(20) DEFAULT NULL,
  `fee` int(11) DEFAULT NULL,
  `daylimit` int(11) DEFAULT NULL,
  `monthlimit` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_servicenum
#
/*!40000 ALTER TABLE `tbl_servicenum` DISABLE KEYS */;

INSERT INTO `tbl_servicenum` VALUES (1,1,1,NULL,'1066950010_C6F25sk',1000,0,0,0,'none;;',1,'2014-09-08 00:08:29','2014-09-08 00:08:29');
INSERT INTO `tbl_servicenum` VALUES (2,1,1,NULL,'10661234_123',200,0,0,0,'',1,'2014-09-09 22:43:44','2014-09-09 22:43:44');
INSERT INTO `tbl_servicenum` VALUES (3,1,1,NULL,'10661234_333',100,0,0,0,'',1,'2014-09-09 22:44:04','2014-09-09 22:44:04');
INSERT INTO `tbl_servicenum` VALUES (4,1,1,NULL,'10661234_111',1000,0,0,0,'',1,'2014-09-09 22:44:20','2014-09-09 22:44:20');
INSERT INTO `tbl_servicenum` VALUES (5,1,1,NULL,'10661234_222',500,0,0,0,'',1,'2014-09-09 22:44:31','2014-09-09 22:44:31');
/*!40000 ALTER TABLE `tbl_servicenum` ENABLE KEYS */;

#
# Source for table tbl_spinfo
#

DROP TABLE IF EXISTS `tbl_spinfo`;
CREATE TABLE `tbl_spinfo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `opid` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `contact` varchar(20) DEFAULT NULL,
  `syncurl` varchar(255) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_spinfo
#
/*!40000 ALTER TABLE `tbl_spinfo` DISABLE KEYS */;

INSERT INTO `tbl_spinfo` VALUES (1,1,'hljw','SMS','','mobile=phone&spnum=spNumber&msg=msgContent&linkid=LinkID&status=FReportCode&return=OK','/hljwsms/1/res.sms',1,'2014-09-08 00:07:13','2014-10-06 19:31:45');
/*!40000 ALTER TABLE `tbl_spinfo` ENABLE KEYS */;

#
# Source for table tbl_user
#

DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `passwd` varchar(20) DEFAULT NULL,
  `showname` varchar(20) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `contactMan` varchar(20) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `bankAccount` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `syncurl` varchar(255) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_user_role` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_user
#
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;

INSERT INTO `tbl_user` VALUES (1,'admin','skydpwd','超级管理员',0,1,NULL,NULL,NULL,NULL,1,NULL,'2013-04-04 10:51:30','2013-04-04 10:51:30');
INSERT INTO `tbl_user` VALUES (2,'hljw','hljw','鸿联九五外呼',0,10,NULL,NULL,NULL,NULL,1,'http://114.255.71.150:8099/OutcallDK/Submit.asp','2014-09-08 00:09:58','2014-09-09 23:34:39');
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;

#
# Source for table tbl_user_card_log
#

DROP TABLE IF EXISTS `tbl_user_card_log`;
CREATE TABLE `tbl_user_card_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `channelid` varchar(20) DEFAULT NULL,
  `cardId` int(11) DEFAULT NULL,
  `priceId` int(11) DEFAULT NULL,
  `flag` int(11) DEFAULT NULL COMMENT '1:上行 2:状态报告 3:计费成功 4:预约外呼 5:外呼中 6:外呼成功 7:下发成功',
  `smsids` varchar(255) DEFAULT NULL,
  `callouturl` varchar(255) DEFAULT NULL,
  `sendcardurl` varchar(255) DEFAULT NULL,
  `sendnum` int(11) DEFAULT NULL,
  `cardno` varchar(100) DEFAULT NULL,
  `cardpwd` varchar(100) DEFAULT NULL,
  `btime` datetime DEFAULT NULL,
  `etime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_bill_btime` (`btime`),
  KEY `ind_bill_mobile` (`mobile`),
  KEY `ind_bill_province` (`province`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Dumping data for table tbl_user_card_log
#
/*!40000 ALTER TABLE `tbl_user_card_log` DISABLE KEYS */;

INSERT INTO `tbl_user_card_log` VALUES (1,'13552922122','北京','北京','22',1,19,7,'1','http://218.206.72.142:8080/CP_WEB_BILL/callout.jsp','http://218.206.72.142:8080/CP_WEB_BILL/sendcard.jsp',0,'werwe','sdfsadf','2014-09-08 21:01:00','2014-09-08 21:01:00');
INSERT INTO `tbl_user_card_log` VALUES (2,'13552922122','北京','北京','25',1,16,7,'2','http://218.206.72.142:8080/CP_WEB_BILL/callout.jsp','http://218.206.72.142:8080/CP_WEB_BILL/sendcard.jsp',0,'aaa','aaa','2014-09-09 19:56:00','2014-09-09 19:56:00');
INSERT INTO `tbl_user_card_log` VALUES (3,'13552922122','北京','北京','26',1,16,6,'5,6','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/',3,'aaa','aaa','2014-09-09 23:50:00','2014-09-09 23:50:00');
INSERT INTO `tbl_user_card_log` VALUES (10,'18389004712','其他','其他','22',1,19,4,'78,79,80,81,82','http://114.255.71.150:8099/OutcallDK/Submit.asp','http://114.255.71.158:8061/',3,NULL,NULL,'2014-10-09 20:45:08','2014-10-09 20:45:08');
/*!40000 ALTER TABLE `tbl_user_card_log` ENABLE KEYS */;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
CREATE TABLE `tbl_user_pc_card_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `channelid` varchar(20) DEFAULT NULL,
  `cardId` int(11) DEFAULT NULL,
  `priceId` int(11) DEFAULT NULL,
  `fee` int(11) DEFAULT NULL,
  `ext` varchar(255) DEFAULT NULL,
  `resultcode` varchar(5) DEFAULT NULL COMMENT '0:成功 其它失败',
  `resultmsg` varchar(100) DEFAULT NULL,
  `sid` varchar(100) DEFAULT NULL,
  `cardno` varchar(100) DEFAULT NULL,
  `cardpwd` varchar(100) DEFAULT NULL,
  `btime` datetime DEFAULT NULL,
  `etime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_bill_btime` (`btime`),
  KEY `ind_bill_mobile` (`mobile`),
  KEY `ind_bill_province` (`province`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

CREATE TABLE `tbl_user_step_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `businessId` varchar(20) DEFAULT NULL,
  `status` varchar(11) DEFAULT NULL COMMENT 'pass、reject',
  `step` varchar(20) DEFAULT NULL,
  `msgContent` varchar(100) DEFAULT NULL,
  `btime` datetime DEFAULT NULL,
  `etime` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `ind_step_mobile` (`mobile`),
  KEY `ind_step_province` (`province`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
