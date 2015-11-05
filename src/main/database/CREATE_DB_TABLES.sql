SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`type` (
  `typ_id` INT(6) NOT NULL ,
  `type` VARCHAR(32) NULL COMMENT 'Description of the type' ,
  `maintain_type` CHAR(1) NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  PRIMARY KEY (`typ_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`code_book`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`code_book` (
  `cob_id` INT(6) NOT NULL ,
  `value` VARCHAR(32) NULL COMMENT 'Description of the code book entry, as used within Lennon.' ,
  `external_representation` VARCHAR(32) NULL COMMENT 'Description of the code book entry, as used outside of Lennon, if needed for interfaces.' ,
  `maintain_value` CHAR(1) NULL COMMENT 'Indicates whether or not the value column is maintainable through the TP client screens.' ,
  `maintain_ext_rep` CHAR(1) NULL COMMENT 'Indicates whether or not the external_representation column is maintainable through the TP client screens.' ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `maintain_parent_code_book` CHAR(1) NULL COMMENT 'Indicates whether or not the cob_id_fk column is maintainable through the TP client screens.' ,
  `code_book_cob_id` INT(6) NOT NULL ,
  `typ_id` INT(6) NOT NULL ,
  PRIMARY KEY (`cob_id`, `code_book_cob_id`) ,
  UNIQUE INDEX `cob_id_UNIQUE` (`cob_id` ASC) ,
  INDEX `fk_code_book_code_book1_idx` (`code_book_cob_id` ASC) ,
  INDEX `fk_code_book_type1_idx` (`typ_id` ASC) ,
  CONSTRAINT `fk_code_book_code_book1`
    FOREIGN KEY (`code_book_cob_id` )
    REFERENCES `mydb`.`code_book` (`cob_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_code_book_type1`
    FOREIGN KEY (`typ_id` )
    REFERENCES `mydb`.`type` (`typ_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`business`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`business` (
  `bus_id` INT(6) NOT NULL ,
  `bus_id_tp` INT(6) NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `bus_description` VARCHAR(32) NULL ,
  `bus_name` VARCHAR(6) NULL ,
  `bus_code` CHAR(3) NULL ,
  `scans_substitute_bus_code` CHAR(3) NULL ,
  `businesscol` VARCHAR(45) NULL ,
  `atoc_substitute_bus_code` CHAR(3) NULL ,
  `bus_id_tp_parent` INT(6) NULL ,
  `tp_par_bus_description` VARCHAR(32) NULL COMMENT 'Description of parent business (see bus_id_tp_parent above)' ,
  `tp_par_bus_name` VARCHAR(6) NULL ,
  `tp_par_bus_code` CHAR(3) NULL ,
  `bus_id_hoc` INT(6) NULL COMMENT 'bus_id (IS primary key) of the business at level 2 in the hierarchy, that is, one with a business type of Holding Company or Third Party Group.' ,
  `hoc_bus_description` VARCHAR(32) NULL ,
  `hoc_bus_name` VARCHAR(6) NULL ,
  `hoc_bus_code` CHAR(3) NULL ,
  `bus_id_prc` INT(6) NULL COMMENT 'bus_id (IS primary key) of the business at level 3 in the hierarchy, that is, one with a business type of TOC, Profit Centre, Retailer, Retailer (Sapphire), or Third Party' ,
  `prc_bus_description` VARCHAR(32) NULL ,
  `prc_bus_name` VARCHAR(6) NULL ,
  `prc_bus_code` CHAR(3) NULL ,
  `bus_id_isd` INT(6) NULL ,
  `isd_bus_description` VARCHAR(32) NULL ,
  `isd_bus_name` VARCHAR(6) NULL ,
  `isd_bus_code` CHAR(3) NULL ,
  `bus_id_esd` INT(6) NULL ,
  `esd_bus_description` VARCHAR(32) NULL ,
  `esd_bus_name` VARCHAR(6) NULL ,
  `esd_bus_code` CHAR(3) NULL ,
  `bus_id_but` INT(6) NULL ,
  `but_bus_description` VARCHAR(32) NULL ,
  `but_bus_name` VARCHAR(6) NULL ,
  `but_bus_code` CHAR(3) NULL ,
  `ftf_site_identity` VARCHAR(4) NULL ,
  `euro_conversion_date` DATE NULL ,
  PRIMARY KEY (`bus_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`year`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`year` (
  `year` INT(4) NOT NULL COMMENT 'The four digit code of the fiscal year. This is when the year ends, for example 2000 for 01/04/1999 to 31/03/2000' ,
  `start_date` DATE NULL COMMENT 'year start date, usually 1 April' ,
  `end_date` DATE NULL COMMENT 'year end date, usually 31 March' ,
  PRIMARY KEY (`year`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`period`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`period` (
  `per_id` INT NOT NULL ,
  `year` INT(4) NOT NULL COMMENT 'fiscal year in which the period falls' ,
  `period` INT(2) NULL COMMENT 'period number within the fiscal year' ,
  `start_date` DATE NULL COMMENT 'first day of the period' ,
  `end_date` DATE NULL COMMENT 'last day of the period' ,
  `status` VARCHAR(6) NULL COMMENT 'indicates whether the period is open or closed' ,
  `period_mask` VARCHAR(8) NULL COMMENT 'Format for periods, as used in the IS, for example 2000/P04 for period 4 of the financial year 1999/2000.' ,
  PRIMARY KEY (`per_id`) ,
  INDEX `fk_period_year1_idx` (`year` ASC) ,
  CONSTRAINT `fk_period_year1`
    FOREIGN KEY (`year` )
    REFERENCES `mydb`.`year` (`year` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`week`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`week` (
  `week_id` INT(6) NOT NULL ,
  `week` INT(2) NULL COMMENT 'week number within the fiscal year' ,
  `start_date` DATE NULL COMMENT 'first date of the week' ,
  `end_date` DATE NULL COMMENT 'last date of the week' ,
  `week_mask` VARCHAR(8) NULL COMMENT 'Format for weeks, as used in the IS, for example 2000/W04 for week 4 of the financial year 1999/2000' ,
  `year` INT NOT NULL ,
  `period` INT NOT NULL ,
  PRIMARY KEY (`week_id`) ,
  INDEX `fk_week_year1_idx` (`year` ASC) ,
  INDEX `fk_week_period1_idx` (`period` ASC) ,
  CONSTRAINT `fk_week_year1`
    FOREIGN KEY (`year` )
    REFERENCES `mydb`.`year` (`year` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_week_period1`
    FOREIGN KEY (`period` )
    REFERENCES `mydb`.`period` (`per_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`calendar`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`calendar` (
  `cal_id` INT NOT NULL ,
  `calendar_date` DATE NULL ,
  `day_of_week` VARCHAR(32) NULL COMMENT 'the day of the week' ,
  `period` INT NOT NULL ,
  `year` INT(4) NOT NULL ,
  `week` INT(6) NOT NULL ,
  PRIMARY KEY (`cal_id`) ,
  INDEX `fk_calendar_period1_idx` (`period` ASC) ,
  INDEX `fk_calendar_year1_idx` (`year` ASC) ,
  INDEX `fk_calendar_week1_idx` (`week` ASC) ,
  CONSTRAINT `fk_calendar_period1`
    FOREIGN KEY (`period` )
    REFERENCES `mydb`.`period` (`per_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_calendar_year1`
    FOREIGN KEY (`year` )
    REFERENCES `mydb`.`year` (`year` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_calendar_week1`
    FOREIGN KEY (`week` )
    REFERENCES `mydb`.`week` (`week_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ticket_Status`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`ticket_Status` (
  `tis_id` INT(6) NOT NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `ticket_status_code` VARCHAR(3) NULL ,
  `ticket_status_description` VARCHAR(32) NULL ,
  `percent_discount` DECIMAL(5,2) NULL ,
  `rat_railcard_type_desc` VARCHAR(32) NULL ,
  `ticket_status_type_desc` VARCHAR(32) NULL ,
  PRIMARY KEY (`tis_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ticket_Status_Link`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`ticket_Status_Link` (
  `tsl_id` INT(6) NOT NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `tis_id_links` INT(6) NOT NULL COMMENT 'ID of the parent ticket status' ,
  `tis_id_linked_by` INT(6) NOT NULL COMMENT 'ID of the child ticket status' ,
  PRIMARY KEY (`tsl_id`) ,
  INDEX `fk_ticket_Status_Link_ticket_Status1_idx` (`tis_id_links` ASC) ,
  INDEX `fk_ticket_Status_Link_ticket_Status2_idx` (`tis_id_linked_by` ASC) ,
  CONSTRAINT `fk_ticket_Status_Link_ticket_Status1`
    FOREIGN KEY (`tis_id_links` )
    REFERENCES `mydb`.`ticket_Status` (`tis_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ticket_Status_Link_ticket_Status2`
    FOREIGN KEY (`tis_id_linked_by` )
    REFERENCES `mydb`.`ticket_Status` (`tis_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`record_field`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`record_field` (
  `ref_id` INT(6) NOT NULL ,
  `ref_id_tp` INT(6) NULL ,
  `name_of_source_field` VARCHAR(32) NULL ,
  `field_number` INT(4) NULL ,
  `position` INT(4) NULL ,
  `length` INT(4) NULL ,
  `record_type` CHAR(2) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `expired` CHAR(1) NULL ,
  PRIMARY KEY (`ref_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`product_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`product_type` (
  `prt_id` INT(6) NOT NULL ,
  `product_type_description` VARCHAR(32) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `expired` CHAR(1) NULL ,
  PRIMARY KEY (`prt_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ticket_class`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`ticket_class` (
  `tic_id` INT(6) NOT NULL ,
  `ticket_class_description` VARCHAR(32) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `expired` CHAR(1) NULL ,
  PRIMARY KEY (`tic_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`ticket_allocation_code`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`ticket_allocation_code` (
  `tac_id` INT(6) NOT NULL ,
  `tac_code` VARCHAR(1) NULL ,
  `tac_description` VARCHAR(32) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `expired` CHAR(1) NULL ,
  PRIMARY KEY (`tac_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`product`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`product` (
  `pro_id` INT(6) NOT NULL ,
  `product_code` VARCHAR(5) NULL ,
  `product_description` VARCHAR(32) NULL ,
  `jof_journey_factor` DECIMAL(5,2) NULL ,
  `pro_id_group_1` INT(6) NULL COMMENT 'ID of the linked parent product which has a product type of Primary Product Group (PPG)' ,
  `pro_group_1_code` VARCHAR(5) NULL ,
  `pro_group_1_desc` VARCHAR(32) NULL ,
  `pro_id_group_2` INT(6) NULL ,
  `pro_group_2_code` VARCHAR(5) NULL ,
  `pro_group_2_desc` VARCHAR(32) NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `bus_id_corrective` INT(6) NULL ,
  `exclude_from_ngt_sundries` VARCHAR(1) NULL ,
  `business` INT(6) NOT NULL ,
  `prt_id` INT(6) NOT NULL ,
  `cob_id_debit_credit_type` INT(6) NOT NULL ,
  `cob_id_railcard_type` INT(6) NOT NULL ,
  `cob_id_suspendable_ind` INT(6) NOT NULL ,
  `tic_id` INT(6) NOT NULL ,
  `tac_id` INT(6) NOT NULL ,
  PRIMARY KEY (`pro_id`) ,
  INDEX `fk_product_business1_idx` (`business` ASC) ,
  INDEX `fk_product_product_type1_idx` (`prt_id` ASC) ,
  INDEX `fk_product_code_book1_idx` (`cob_id_debit_credit_type` ASC) ,
  INDEX `fk_product_code_book2_idx` (`cob_id_railcard_type` ASC) ,
  INDEX `fk_product_code_book3_idx` (`cob_id_suspendable_ind` ASC) ,
  INDEX `fk_product_ticket_class1_idx` (`tic_id` ASC) ,
  INDEX `fk_product_ticket_allocation_code1_idx` (`tac_id` ASC) ,
  CONSTRAINT `fk_product_business1`
    FOREIGN KEY (`business` )
    REFERENCES `mydb`.`business` (`bus_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_product_type1`
    FOREIGN KEY (`prt_id` )
    REFERENCES `mydb`.`product_type` (`prt_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_code_book1`
    FOREIGN KEY (`cob_id_debit_credit_type` )
    REFERENCES `mydb`.`code_book` (`code_book_cob_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_code_book2`
    FOREIGN KEY (`cob_id_railcard_type` )
    REFERENCES `mydb`.`code_book` (`code_book_cob_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_code_book3`
    FOREIGN KEY (`cob_id_suspendable_ind` )
    REFERENCES `mydb`.`code_book` (`code_book_cob_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_ticket_class1`
    FOREIGN KEY (`tic_id` )
    REFERENCES `mydb`.`ticket_class` (`tic_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_ticket_allocation_code1`
    FOREIGN KEY (`tac_id` )
    REFERENCES `mydb`.`ticket_allocation_code` (`tac_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`product_Link`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`product_Link` (
  `pro_id` INT(6) NOT NULL ,
  `pro_id_parent` INT(6) NOT NULL ,
  `prl_id` INT(6) NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  INDEX `fk_product_Link_product1_idx` (`pro_id` ASC) ,
  INDEX `fk_product_Link_product2_idx` (`pro_id_parent` ASC) ,
  CONSTRAINT `fk_product_Link_product1`
    FOREIGN KEY (`pro_id` )
    REFERENCES `mydb`.`product` (`pro_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_Link_product2`
    FOREIGN KEY (`pro_id_parent` )
    REFERENCES `mydb`.`product` (`pro_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`route`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`route` (
  `rou_id` INT(6) NOT NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `route_code` VARCHAR(5) NULL ,
  `route_description` VARCHAR(35) NULL ,
  `tac_id` INT(6) NOT NULL ,
  `cob_id_route_type` INT(6) NOT NULL ,
  PRIMARY KEY (`rou_id`) ,
  INDEX `fk_route_ticket_allocation_code1_idx` (`tac_id` ASC) ,
  INDEX `fk_route_code_book1_idx` (`cob_id_route_type` ASC) ,
  CONSTRAINT `fk_route_ticket_allocation_code1`
    FOREIGN KEY (`tac_id` )
    REFERENCES `mydb`.`ticket_allocation_code` (`tac_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_route_code_book1`
    FOREIGN KEY (`cob_id_route_type` )
    REFERENCES `mydb`.`code_book` (`code_book_cob_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`route_link`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`route_link` (
  `rol_id` INT(6) NOT NULL ,
  `rou_id_child` INT(6) NOT NULL ,
  `rou_id_parent` INT(6) NOT NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  PRIMARY KEY (`rol_id`) ,
  INDEX `fk_route_link_route1_idx` (`rou_id_child` ASC) ,
  INDEX `fk_route_link_route2_idx` (`rou_id_parent` ASC) ,
  CONSTRAINT `fk_route_link_route1`
    FOREIGN KEY (`rou_id_child` )
    REFERENCES `mydb`.`route` (`rou_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_route_link_route2`
    FOREIGN KEY (`rou_id_parent` )
    REFERENCES `mydb`.`route` (`rou_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`location_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`location_type` (
  `lot_id` INT(6) NOT NULL ,
  `location_type_desc` VARCHAR(32) NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  PRIMARY KEY (`lot_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`location`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`location` (
  `loc_id` INT(6) NOT NULL ,
  `location` VARCHAR(8) NULL ,
  `location_description` VARCHAR(32) NULL ,
  `lot_id` INT(6) NOT NULL ,
  `address` VARCHAR(220) NULL ,
  `postcode` VARCHAR(8) NULL ,
  `telephone` VARCHAR(15) NULL ,
  `loc_id_primary_zone` INT(6) NULL ,
  `primary_zone_code` VARCHAR(8) NULL ,
  `primary_zone_desc` VARCHAR(32) NULL ,
  `loc_id_secondary_zone` INT(6) NULL ,
  `secondary_zone_code` VARCHAR(8) NULL ,
  `secondary_zone_desc` VARCHAR(32) NULL ,
  `loc_id_tertiary_zone` INT(6) NULL ,
  `tertiary_zone_code` VARCHAR(8) NULL ,
  `tertiary_zone_desc` VARCHAR(32) NULL ,
  `loc_id_tv_station` INT(6) NULL ,
  `tv_station_code` VARCHAR(8) NULL ,
  `tv_station_name` VARCHAR(32) NULL ,
  `loc_id_local_authority` INT(6) NULL ,
  `local_authority_code` VARCHAR(8) NULL ,
  `local_authority_name` VARCHAR(32) NULL ,
  `loc_id_station_group` INT(6) NULL ,
  `station_group` VARCHAR(8) NULL ,
  `station_group_desc` VARCHAR(32) NULL ,
  `loc_id_london_station_grp` INT(6) NULL ,
  `london_station_grp_code` VARCHAR(8) NULL ,
  `london_station_grp_desc` VARCHAR(32) NULL ,
  `os_grid_reference` VARCHAR(8) NULL ,
  `postal_region` VARCHAR(2) NULL ,
  `nalco_12char_description` VARCHAR(12) NULL ,
  `nalco_16char_description` VARCHAR(16) NULL ,
  `nalco_26char_description` VARCHAR(26) NULL ,
  `nalco_tiploc` VARCHAR(7) NULL ,
  `nalco_3char_code` VARCHAR(3) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `expired` CHAR(1) NULL ,
  `loc_id_master_account` INT(6) NULL ,
  `master_account_code` VARCHAR(8) NULL ,
  `master_account_name` VARCHAR(32) NULL ,
  `cob_id_group_type` INT(6) NOT NULL ,
  PRIMARY KEY (`loc_id`) ,
  INDEX `fk_location_location_type1_idx` (`lot_id` ASC) ,
  INDEX `fk_location_code_book1_idx` (`cob_id_group_type` ASC) ,
  CONSTRAINT `fk_location_location_type1`
    FOREIGN KEY (`lot_id` )
    REFERENCES `mydb`.`location_type` (`lot_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_location_code_book1`
    FOREIGN KEY (`cob_id_group_type` )
    REFERENCES `mydb`.`code_book` (`code_book_cob_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`group_function_type`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`group_function_type` (
  `gft_id` INT(6) NOT NULL ,
  `group_function_type_desc` VARCHAR(32) NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  PRIMARY KEY (`gft_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`location_link`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`location_link` (
  `lol_id` INT(6) NOT NULL ,
  `expired` CHAR(1) NULL ,
  `effective_from` DATE NULL ,
  `effective_to` DATE NULL ,
  `loc_id_child` INT(6) NOT NULL ,
  `loc_id_parent` INT(6) NOT NULL ,
  `gft_id` INT(6) NOT NULL ,
  PRIMARY KEY (`lol_id`) ,
  INDEX `fk_location_link_location1_idx` (`loc_id_child` ASC) ,
  INDEX `fk_location_link_location2_idx` (`loc_id_parent` ASC) ,
  INDEX `fk_location_link_group_function_type1_idx` (`gft_id` ASC) ,
  CONSTRAINT `fk_location_link_location1`
    FOREIGN KEY (`loc_id_child` )
    REFERENCES `mydb`.`location` (`loc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_location_link_location2`
    FOREIGN KEY (`loc_id_parent` )
    REFERENCES `mydb`.`location` (`loc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_location_link_group_function_type1`
    FOREIGN KEY (`gft_id` )
    REFERENCES `mydb`.`group_function_type` (`gft_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`calendar_currency`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mydb`.`calendar_currency` (
  `currency_rate` DECIMAL(10,2) NOT NULL ,
  `conversion_rate` INT NOT NULL ,
  `currency_date` DATETIME NOT NULL )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`fare`
-- -----------------------------------------------------

CREATE  TABLE IF NOT EXISTS `mydb`.`fare` (
  `origin_location` varchar(32) DEFAULT NULL,
  `destination_location` varchar(32) DEFAULT NULL,
  `selling_location` varchar(32) DEFAULT NULL,
  `route` varchar(6) NOT NULL,
  `product` varchar(6) NOT NULL,
  `ticketStatus` int(6) NOT NULL,
  `fare` decimal(12,2) DEFAULT NULL,
  `value` int(8) DEFAULT NULL,
  `with_effect_from` datetime DEFAULT NULL,
  `with_effect_until` datetime DEFAULT NULL,
  `expired` char(1) DEFAULT NULL,
  `effective_to` date DEFAULT NULL,
  `effective_from` date DEFAULT NULL
) ENGINE=InnoDB 

USE `mydb` ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Table `mydb`.`applicationparameters`
-- -----------------------------------------------------

CREATE  TABLE IF NOT EXISTS `mydb`.`applicationparameters` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `integer_value` int(11) NOT NULL,
  `expired` varchar(45) DEFAULT NULL,
  `effective_from` date DEFAULT NULL,
  `effective_to` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB 
