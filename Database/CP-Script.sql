-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `registration_date` DATETIME NOT NULL,
  UNIQUE INDEX `emial_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `phone_number_UNIQUE` (`phone_number` ASC) VISIBLE,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1
KEY_BLOCK_SIZE = 8;


-- -----------------------------------------------------
-- Table `mydb`.`Cats`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Cats` (
  `cat_id` INT NOT NULL AUTO_INCREMENT,
  `cat_name` VARCHAR(100) NOT NULL,
  `age` INT NOT NULL,
  `notes` TEXT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`cat_id`),
  INDEX `fk_Cats_Users_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Cats_Users`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Rooms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Rooms` (
  `room_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `type` ENUM('Pokoj pro kočku', 'Pokoj pro majitele a kočku') NOT NULL,
  PRIMARY KEY (`room_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Reservations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Reservations` (
  `reservation_id` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `status` ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') NOT NULL,
  `user_id` INT NOT NULL,
  `room_id` INT NOT NULL,
  PRIMARY KEY (`reservation_id`),
  INDEX `fk_Reservations_Users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_Reservations_Rooms1_idx` (`room_id` ASC) VISIBLE,
  CONSTRAINT `fk_Reservations_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Reservations_Rooms1`
    FOREIGN KEY (`room_id`)
    REFERENCES `mydb`.`Rooms` (`room_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Cat_Reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Cat_Reservation` (
  `reservation_id` INT NOT NULL,
  `cat_id` INT NOT NULL,
  INDEX `fk_Cat_Reservation_Reservations1_idx` (`reservation_id` ASC) VISIBLE,
  INDEX `fk_Cat_Reservation_Cats1_idx` (`cat_id` ASC) VISIBLE,
  CONSTRAINT `fk_Cat_Reservation_Reservations1`
    FOREIGN KEY (`reservation_id`)
    REFERENCES `mydb`.`Reservations` (`reservation_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cat_Reservation_Cats1`
    FOREIGN KEY (`cat_id`)
    REFERENCES `mydb`.`Cats` (`cat_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
