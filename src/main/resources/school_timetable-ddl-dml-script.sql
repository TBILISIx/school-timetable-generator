SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema school_timetable
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `school_timetable` DEFAULT CHARACTER SET utf8;
USE `school_timetable`;

-- -----------------------------------------------------
-- Table `subjects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `school_timetable`.`subjects` (
  `id`    INT          NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(100) NOT NULL,
  `is_pe` TINYINT(1)   NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `teachers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `school_timetable`.`teachers` (
  `id`   INT          NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `classrooms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `school_timetable`.`classrooms` (
  `id`   INT          NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `time_slots`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `school_timetable`.`time_slots` (
  `id`            INT         NOT NULL AUTO_INCREMENT,
  `day_of_week`   VARCHAR(15) NOT NULL,
  `period_number` INT         NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `timetables`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `school_timetable`.`timetables` (
  `id`               INT      NOT NULL AUTO_INCREMENT,
  `created_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `subjects_per_day` INT      NOT NULL,
  `fitness_score`    INT      NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `timetable_entries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `school_timetable`.`timetable_entries` (
  `id`           INT NOT NULL AUTO_INCREMENT,
  `timetable_id` INT NOT NULL,
  `subject_id`   INT NOT NULL,
  `time_slot_id` INT NOT NULL,
  `teacher_id`   INT NOT NULL,
  `classroom_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_timetable_entries_timetables_idx`  (`timetable_id` ASC),
  INDEX `fk_timetable_entries_subjects_idx`    (`subject_id`   ASC),
  INDEX `fk_timetable_entries_time_slots_idx`  (`time_slot_id` ASC),
  INDEX `fk_timetable_entries_teachers_idx`    (`teacher_id`   ASC),
  INDEX `fk_timetable_entries_classrooms_idx`  (`classroom_id` ASC),
  CONSTRAINT `fk_timetable_entries_timetables`
    FOREIGN KEY (`timetable_id`) REFERENCES `school_timetable`.`timetables` (`id`)
    ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_timetable_entries_subjects`
    FOREIGN KEY (`subject_id`)   REFERENCES `school_timetable`.`subjects`   (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_timetable_entries_time_slots`
    FOREIGN KEY (`time_slot_id`) REFERENCES `school_timetable`.`time_slots` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_timetable_entries_teachers`
    FOREIGN KEY (`teacher_id`)   REFERENCES `school_timetable`.`teachers`   (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_timetable_entries_classrooms`
    FOREIGN KEY (`classroom_id`) REFERENCES `school_timetable`.`classrooms` (`id`)
    ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `teacher_subjects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `school_timetable`.`teacher_subjects` (
  `teacher_id` INT NOT NULL,
  `subject_id` INT NOT NULL,
  PRIMARY KEY (`teacher_id`, `subject_id`),
  CONSTRAINT `fk_teacher_subjects_teachers`
    FOREIGN KEY (`teacher_id`) REFERENCES `school_timetable`.`teachers` (`id`)
    ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_teacher_subjects_subjects`
    FOREIGN KEY (`subject_id`) REFERENCES `school_timetable`.`subjects` (`id`)
    ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Seed Data
-- -----------------------------------------------------
INSERT INTO `school_timetable`.`subjects` (`name`, `is_pe`) VALUES
('Mathematics',        0),
('Physics',            0),
('Chemistry',          0),
('Biology',            0),
('English',            0),
('History',            0),
('Geography',          0),
('Computer Science',   0),
('Physical Education', 1);

INSERT INTO `school_timetable`.`teachers` (`name`) VALUES
('Mr. Davit'),
('Ms. Nino'),
('Mr. Domenti'),
('Ms. Mariam'),
('Mr. Lasha');

INSERT INTO `school_timetable`.`classrooms` (`name`) VALUES
('Room 101'),
('Room 102'),
('Room 103'),
('Gym');

INSERT INTO `school_timetable`.`teacher_subjects` (`teacher_id`, `subject_id`) VALUES
(1, 1), (1, 2),
(2, 3), (2, 4),
(3, 5), (3, 6),
(4, 7), (4, 8),
(5, 9);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
