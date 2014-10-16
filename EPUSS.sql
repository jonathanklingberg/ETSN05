drop schema puss1403;
CREATE DATABASE puss1403;
USE puss1403;

CREATE TABLE Users(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userName VARCHAR(10) NOT NULL,
password VARCHAR(10) NOT NULL,
sessionId VARCHAR(255) ,
PRIMARY KEY ( id ) ,
UNIQUE KEY (userName)
);

CREATE TABLE Roles(
role VARCHAR(20) NOT NULL,
PRIMARY KEY (role)
);

CREATE TABLE Types(
type VARCHAR(1) NOT NULL,
PRIMARY KEY (type)
);

CREATE TABLE Numbers(
number INT UNSIGNED NOT NULL,
PRIMARY KEY (number)
);

CREATE TABLE ProjectGroups(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
groupName VARCHAR(20) NOT NULL,
PRIMARY KEY (id) ,
UNIQUE KEY (groupName)
);

CREATE TABLE RoleInGroup(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userId INT UNSIGNED,
groupId INT UNSIGNED NOT NULL,
role VARCHAR(20) NOT NULL DEFAULT 1,
isActiveInGroup TINYINT(1),
PRIMARY KEY (id) ,
FOREIGN KEY (groupId) REFERENCES ProjectGroups(id),
FOREIGN KEY (role) REFERENCES Roles(role)
);

CREATE TABLE TimeReports(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userId INT UNSIGNED NOT NULL,
groupId INT UNSIGNED NOT NULL,
date DATE NOT NULL,
duration INT UNSIGNED NOT NULL,
type VARCHAR(1) NOT NULL,
number INT UNSIGNED NOT NULL,
week INT UNSIGNED NOT NULL,
signed TINYINT(1) NOT NULL,
PRIMARY KEY (id) ,
FOREIGN KEY (groupId) REFERENCES ProjectGroups(id),
FOREIGN KEY (type) REFERENCES Types(type),
FOREIGN KEY (number) REFERENCES Numbers(number)
);

-- Create default users
INSERT INTO Users(userName, password, sessionId) VALUES('admin', 'adminpw', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('testuser', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('devuser', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('pmuser', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('testuser2', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('devuser2', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('pmuser2', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('testuser3', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('devuser3', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('pmuser3', 'passwd', NULL);
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

INSERT INTO Numbers(number) VALUES(11);
INSERT INTO Numbers(number) VALUES(12);
INSERT INTO Numbers(number) VALUES(13);
INSERT INTO Numbers(number) VALUES(14);
INSERT INTO Numbers(number) VALUES(15);
INSERT INTO Numbers(number) VALUES(16);
INSERT INTO Numbers(number) VALUES(17);
INSERT INTO Numbers(number) VALUES(18);
INSERT INTO Numbers(number) VALUES(19);
INSERT INTO Numbers(number) VALUES(21);
INSERT INTO Numbers(number) VALUES(22);
INSERT INTO Numbers(number) VALUES(23);
INSERT INTO Numbers(number) VALUES(30);
INSERT INTO Numbers(number) VALUES(41);
INSERT INTO Numbers(number) VALUES(42);
INSERT INTO Numbers(number) VALUES(43);
INSERT INTO Numbers(number) VALUES(44);
INSERT INTO Numbers(number) VALUES(100);

-- Create default types
INSERT INTO Types VALUES("D");
INSERT INTO Types VALUES("I");
INSERT INTO Types VALUES("F");
INSERT INTO Types VALUES("R");

-- Create timereport
INSERT INTO TimeReports(userId, groupId, date, duration, type, number, week, signed) VALUES(3, 3, 2014-10-12, 60, 'I', 100, 1, 1);
drop schema puss1403;
CREATE DATABASE puss1403;
USE puss1403;

CREATE TABLE Users(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userName VARCHAR(10) NOT NULL,
password VARCHAR(10) NOT NULL,
sessionId VARCHAR(255) ,
PRIMARY KEY ( id ) ,
UNIQUE KEY (userName)
);

CREATE TABLE Roles(
role VARCHAR(20) NOT NULL,
PRIMARY KEY (role)
);

CREATE TABLE Types(
type VARCHAR(1) NOT NULL,
PRIMARY KEY (type)
);

CREATE TABLE Numbers(
number INT UNSIGNED NOT NULL,
PRIMARY KEY (number)
);

CREATE TABLE ProjectGroups(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
groupName VARCHAR(20) NOT NULL,
PRIMARY KEY (id) ,
UNIQUE KEY (groupName)
);

CREATE TABLE RoleInGroup(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userId INT UNSIGNED,
groupId INT UNSIGNED NOT NULL,
role VARCHAR(20) NOT NULL DEFAULT 1,
isActiveInGroup TINYINT(1),
PRIMARY KEY (id) ,
FOREIGN KEY (groupId) REFERENCES ProjectGroups(id),
FOREIGN KEY (role) REFERENCES Roles(role)
);

CREATE TABLE TimeReports(
id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
userId INT UNSIGNED NOT NULL,
groupId INT UNSIGNED NOT NULL,
date DATE NOT NULL,
duration INT UNSIGNED NOT NULL,
type VARCHAR(1) NOT NULL,
number INT UNSIGNED NOT NULL,
week INT UNSIGNED NOT NULL,
signed TINYINT(1) NOT NULL,
PRIMARY KEY (id) ,
FOREIGN KEY (groupId) REFERENCES ProjectGroups(id),
FOREIGN KEY (type) REFERENCES Types(type),
FOREIGN KEY (number) REFERENCES Numbers(number)
);

-- Create default users
INSERT INTO Users(userName, password, sessionId) VALUES('admin', 'adminpw', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('testuser', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('devuser', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('pmuser', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('testuser2', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('devuser2', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('pmuser2', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('testuser3', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('devuser3', 'passwd', NULL);
INSERT INTO Users(userName, password, sessionId) VALUES('pmuser3', 'passwd', NULL);
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

INSERT INTO Numbers(number) VALUES(11);
INSERT INTO Numbers(number) VALUES(12);
INSERT INTO Numbers(number) VALUES(13);
INSERT INTO Numbers(number) VALUES(14);
INSERT INTO Numbers(number) VALUES(15);
INSERT INTO Numbers(number) VALUES(16);
INSERT INTO Numbers(number) VALUES(17);
INSERT INTO Numbers(number) VALUES(18);
INSERT INTO Numbers(number) VALUES(19);
INSERT INTO Numbers(number) VALUES(21);
INSERT INTO Numbers(number) VALUES(22);
INSERT INTO Numbers(number) VALUES(23);
INSERT INTO Numbers(number) VALUES(30);
INSERT INTO Numbers(number) VALUES(41);
INSERT INTO Numbers(number) VALUES(42);
INSERT INTO Numbers(number) VALUES(43);
INSERT INTO Numbers(number) VALUES(44);
INSERT INTO Numbers(number) VALUES(100);

-- Create default types
INSERT INTO Types VALUES("D");
INSERT INTO Types VALUES("I");
INSERT INTO Types VALUES("F");
INSERT INTO Types VALUES("R");

-- Create timereport
INSERT INTO TimeReports(userId, groupId, date, duration, type, number, week, signed) VALUES(3, 3, '2014-10-12', 60, 'I', 100, 1, 1);
INSERT INTO TimeReports(userId, groupId, date, duration, type, number, week, signed) VALUES(20, 3, '2014-10-12', 60, 'I', 100, 1, 1);

INSERT INTO TimeReports(userId, groupId, date, duration, type, number, week, signed) VALUES(3, 2, '2014-10-12', 60, 'I', 100, 1, 1);
INSERT INTO TimeReports(userId, groupId, date, duration, type, number, week, signed) VALUES(20, 2, '2014-10-12', 60, 'I', 100, 1, 1);
