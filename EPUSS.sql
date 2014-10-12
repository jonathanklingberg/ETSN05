drop schema puss1403;
CREATE DATABASE puss1403;
USE puss1403;

CREATE TABLE Users(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userName VARCHAR(10) NOT NULL,
password VARCHAR(10) NOT NULL,
sessionId VARCHAR(255) ,
isActive tinyint(1) ,
PRIMARY KEY ( id ) ,
UNIQUE KEY (userName)
);

CREATE TABLE Roles(
role VARCHAR(20) NOT NULL,
PRIMARY KEY (role)
);

CREATE TABLE Types(
type INT UNSIGNED NOT NULL,
PRIMARY KEY (type)
);

CREATE TABLE ProjectGroups(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
groupName VARCHAR(20) NOT NULL,
PRIMARY KEY ( id ) ,
UNIQUE KEY (groupName)
);

CREATE TABLE RoleInGroup(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userId INT UNSIGNED,
groupId INT UNSIGNED NOT NULL,
role VARCHAR(20) NOT NULL DEFAULT 1,
isActiveInGroup TINYINT(1),
PRIMARY KEY ( id ) ,
FOREIGN KEY (groupId) REFERENCES ProjectGroups(id),
FOREIGN KEY (role) REFERENCES Roles(role)
);

CREATE TABLE TimeReports(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userId INT UNSIGNED NOT NULL,
groupId INT UNSIGNED NOT NULL,
date DATE NOT NULL,
duration INT UNSIGNED NOT NULL,
type INT UNSIGNED NOT NULL,
week INT UNSIGNED NOT NULL,
signed TINYINT(1) NOT NULL,
PRIMARY KEY ( id ) ,
FOREIGN KEY (groupId) REFERENCES ProjectGroups(id),
FOREIGN KEY (type) REFERENCES Types(type)
);

-- Create default users
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('admin', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('testuser', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('devuser', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('pmuser', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('testuser2', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('devuser2', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('pmuser2', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('testuser3', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('devuser3', 'passwd', NULL, 1);
INSERT INTO Users(userName, password, sessionId, isActive) VALUES('pmuser3', 'passwd', NULL, 1);
-- Create default Roles
INSERT INTO Roles VALUES('ProjectManager');
INSERT INTO Roles VALUES('Developer');
INSERT INTO Roles VALUES('SystemArchitect');
INSERT INTO Roles VALUES('Tester');
INSERT INTO Roles VALUES('Unspecified');
INSERT INTO Roles VALUES('Admin');
-- Create AdminGroup
INSERT INTO ProjectGroups (groupName) VALUES ('AdminGroup');
INSERT INTO ProjectGroups (groupName) VALUES ('Group2');
INSERT INTO ProjectGroups (groupName) VALUES ('Group3');
INSERT INTO ProjectGroups (groupName) VALUES ('Group4');
-- Add admin to RoleInGroup
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (1, 1, 'Admin', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (2, 2, 'Tester', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (3, 2, 'Developer', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (4, 2, 'ProjectManager', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (5, 3, 'Tester', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (6, 3, 'Developer', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (7, 3, 'ProjectManager', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (8, 4, 'Tester', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (9, 4, 'Developer', 1);
INSERT INTO RoleInGroup (userId, groupId, role, isActiveInGroup) VALUES (10, 4, 'ProjectManager', 1);
<<<<<<< HEAD
-- Create a timereport for testing
INSERT INTO TimeReports(userId, groupId, date, duration, type, week, signed) VALUES( 2, 2, '2010-02-01', 2, 21, 2, 0);
=======
>>>>>>> 0ad58244f0c8140ccd73d5846ecb51991a8d802a
-- Create default types
INSERT INTO Types VALUES(11);
INSERT INTO Types VALUES(12);
INSERT INTO Types VALUES(13);
INSERT INTO Types VALUES(14);
INSERT INTO Types VALUES(15);
INSERT INTO Types VALUES(16);
INSERT INTO Types VALUES(17);
INSERT INTO Types VALUES(18);
INSERT INTO Types VALUES(19);
INSERT INTO Types VALUES(21);
INSERT INTO Types VALUES(22);
INSERT INTO Types VALUES(23);
INSERT INTO Types VALUES(30);
INSERT INTO Types VALUES(41);
INSERT INTO Types VALUES(42);
INSERT INTO Types VALUES(43);
INSERT INTO Types VALUES(44);
INSERT INTO Types VALUES(100);