-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 13, 2019 at 04:36 PM
-- Server version: 10.1.31-MariaDB
-- PHP Version: 7.2.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smartguide`
--
CREATE DATABASE IF NOT EXISTS `smartguide` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `smartguide`;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `beacon`
--

DROP TABLE IF EXISTS `beacon`;
CREATE TABLE `beacon` (
  `id` int(11) NOT NULL,
  `RoomId` int(11) DEFAULT NULL,
  `MACaddress` varchar(255) DEFAULT NULL,
  `RSSIvalue` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `officetimings`
--

DROP TABLE IF EXISTS `officetimings`;
CREATE TABLE `officetimings` (
  `tmid` int(255) NOT NULL,
  `tmday` text NOT NULL,
  `tmopen` time NOT NULL,
  `tmclose` time NOT NULL,
  `stfmemName` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pictures`
--

DROP TABLE IF EXISTS `pictures`;
CREATE TABLE `pictures` (
  `pid` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `image_text` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `roominfo`
--

DROP TABLE IF EXISTS `roominfo`;
CREATE TABLE `roominfo` (
  `id` int(11) NOT NULL,
  `Name` varchar(255) DEFAULT NULL,
  `RoomNo` text NOT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `audioInfo` varchar(255) DEFAULT NULL,
  `LeftR_id` text NOT NULL,
  `RightR_id` text,
  `FrontR_id` text,
  `BackR_id` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `roommembers`
--

DROP TABLE IF EXISTS `roommembers`;
CREATE TABLE `roommembers` (
  `rmid` int(11) NOT NULL,
  `rmroomno` text NOT NULL,
  `rmperson` text NOT NULL,
  `rmdesignation` text NOT NULL,
  `rmexperts` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `beacon`
--
ALTER TABLE `beacon`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `officetimings`
--
ALTER TABLE `officetimings`
  ADD PRIMARY KEY (`tmid`);

--
-- Indexes for table `pictures`
--
ALTER TABLE `pictures`
  ADD PRIMARY KEY (`pid`);

--
-- Indexes for table `roominfo`
--
ALTER TABLE `roominfo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `roommembers`
--
ALTER TABLE `roommembers`
  ADD PRIMARY KEY (`rmid`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `officetimings`
--
ALTER TABLE `officetimings`
  MODIFY `tmid` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `pictures`
--
ALTER TABLE `pictures`
  MODIFY `pid` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `roominfo`
--
ALTER TABLE `roominfo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `roommembers`
--
ALTER TABLE `roommembers`
  MODIFY `rmid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
