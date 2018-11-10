SPOOL project.out
SET ECHO ON
SET linesize 1000

/*
CIS 353 - Database Design Project
Jarod Hanko
Andrew Prins
Robert Heine
Christopher Fracassi
Adam Stewart
*/

--
-- Drop the tables (in case they already exist)
--

DROP TABLE Document CASCADE CONSTRAINTS;
DROP TABLE Shadow_Copy CASCADE CONSTRAINTS;
DROP TABLE UserTbl CASCADE CONSTRAINTS;
DROP TABLE Directory CASCADE CONSTRAINTS;
DROP TABLE Webapp CASCADE CONSTRAINTS;
DROP TABLE Webapp_File_Type CASCADE CONSTRAINTS;
DROP TABLE Document_Sharer CASCADE CONSTRAINTS;
DROP TABLE Directory_Sharer CASCADE CONSTRAINTS;

--
-- Create the tables
--
CREATE TABLE  Document(
    ID            	INT,
    FileName        VARCHAR2(25 char),
    FileType        VARCHAR2(25 char),
    FileLocation    VARCHAR2(25 char),
    FileSize        INT,
    LastAccessed    DATE,
    LastModified    DATE,
    DateCreated     DATE,
    Owner         	INT,
    DirID           INT,
    WebApp        	VARCHAR(25 char),
	CONSTRAINT PK_1 PRIMARY KEY (ID),
	CONSTRAINT CK_1 CHECK (Length(FileName) >= 4),
	CONSTRAINT CK_2 CHECK (DateCreated <= LastModified)
);

CREATE TABLE Shadow_Copy(
    DateModified     DATE,
    DocID            INT,
    FileLocation     VARCHAR2(25 char),
    ModifiedBy       INT,
    CONSTRAINT PK_2 PRIMARY KEY (DateModified, DocID)
);

CREATE TABLE UserTbl (
    UserID         	INT,
    Email         	VARCHAR2(25 char),
    Name         	VARCHAR2(25 char),
    DateJoined     	DATE,
    CONSTRAINT PK_3 PRIMARY KEY (UserID)
);

CREATE TABLE Directory (
    ID         		INT,
    dateCreated     DATE,
    Name         	VARCHAR2(25 char),
    Owner     		INT,
	Parent        	INT,
	CONSTRAINT PK_4 PRIMARY KEY (ID),
	CONSTRAINT CK_33 CHECK (Parent <> NULL OR ID = 1)
);

CREATE TABLE Webapp (
    Name         	VARCHAR2(25 char),
    Description     VARCHAR2(40 char),
    CONSTRAINT PK_5 PRIMARY KEY(Name) 
);

CREATE TABLE Webapp_File_Type(
    AppName        	VARCHAR2(25 char),
    AssocFileType   VARCHAR2(25 char),
	CONSTRAINT PK_6 PRIMARY KEY (AppName, AssocFileType)
);

CREATE TABLE Document_Sharer(
    UserID            INT,
    DocID          INT,
    Permission     VarChar2(4),
    SharedDate     DATE,
    CONSTRAINT PK_7 PRIMARY KEY (UserID, DocID),
	CONSTRAINT CK_4 CHECK (Permission IN ('view', 'edit'))
);

CREATE TABLE Directory_Sharer (
    DirID         	INT,
    UserID         	INT,
    Permission     	VarChar2(4),
    SharedDate     	DATE,
    CONSTRAINT PK_8 PRIMARY KEY (DirID, UserID),
	CONSTRAINT CK_5 CHECK (Permission IN ('view', 'edit'))
);

ALTER TABLE Document
ADD	(CONSTRAINT FK_1 FOREIGN KEY (Owner) REFERENCES UserTbl(UserID),
	CONSTRAINT FK_2 FOREIGN KEY (DirID) REFERENCES Directory(ID)
);

ALTER TABLE Shadow_Copy
ADD CONSTRAINT FK_3 FOREIGN KEY(ModifiedBy) REFERENCES USERTbl(UserID);

ALTER TABLE Directory
ADD	(CONSTRAINT FK_4 FOREIGN KEY (Owner) REFERENCES UserTbl(UserID),
	CONSTRAINT FK_5 FOREIGN KEY (Parent) REFERENCES Directory(ID)
);

ALTER TABLE WebApp_File_Type
ADD	CONSTRAINT FK_6 FOREIGN KEY (AppName) REFERENCES Webapp(Name);

ALTER TABLE Document_Sharer
ADD(CONSTRAINT FK_7 FOREIGN KEY (DocID) REFERENCES Document(ID),
	CONSTRAINT FK_8 FOREIGN KEY (UserID) REFERENCES UserTbl(UserID)
);

ALTER TABLE Directory_Sharer
ADD(CONSTRAINT FK_9 FOREIGN KEY (DirID) REFERENCES Directory(ID),
	CONSTRAINT FK_10 FOREIGN KEY (UserID) REFERENCES UserTbl(UserID)
);


SET FEEDBACK OFF

--
--Populate Database
--

alter session set NLS_DATE_FORMAT = 'MM-DD-YYYY HH:MI PM';
-- Seed Users
INSERT INTO UserTbl VALUES(1,'codemaster2@gmail.com','Kobe Marx','03-03-2005 03:33 PM');
INSERT INTO UserTbl VALUES(2,'kooldude23@yahoo.com','Kevin Smith','03-15-1969 06:23 PM');
INSERT INTO UserTbl VALUES(3,'coolguy@hotmail.com','James Potter','02-29-2004 04:50 PM');
INSERT INTO UserTbl VALUES(4,'fakeemail@mail.ru','Sergei Roldugin','12-01-2007 06:26 AM');
-- Seed Directories
INSERT INTO Directory VALUES (1, '03-03-2005 03:33 PM', '~root', 1, NULL);
INSERT INTO Directory VALUES (2, '03-15-1969 06:23 PM', '~root', 2, NULL);
INSERT INTO Directory VALUES (3, '02-29-2004 04:50 PM', '~root', 3, NULL);
INSERT INTO Directory VALUES (4, '12-01-2007 06:26 AM', '~root', 4, NULL);
INSERT INTO Directory VALUES (5, '03-03-2005 03:36 PM', 'Pics', 4, 4);
INSERT INTO Directory VALUES (6, '03-03-2005 03:39 PM', 'Docs', 1, 1);
INSERT INTO Directory VALUES (7, '03-16-1969 09:27 PM', 'TestFolder', 2, 2);
-- Seed WebApps
INSERT INTO WebApp VALUES ('FREE_IMG_VIEW3R','See images 4 free!!!!!');
INSERT INTO WebApp VALUES ('DocReadr','Innovative way to view .doc files.');
INSERT INTO WebApp VALUES ('SQLEdit','Built in SQL interpreter');
INSERT INTO WebApp VALUES ('Microsoft Excel','Official Microsoft Excel Plugin');
-- Seed WebAppFileTypes
INSERT INTO WebApp_File_Type VALUES('FREE_IMG_VIEW3R','gif');
INSERT INTO WebApp_File_Type VALUES('FREE_IMG_VIEW3R','png');
INSERT INTO WebApp_File_Type VALUES('FREE_IMG_VIEW3R','jpg');
INSERT INTO WebApp_File_Type VALUES('DocReadr','doc');
INSERT INTO WebApp_File_Type VALUES('DocReadr','docx');
INSERT INTO WebApp_File_Type VALUES('DocReadr','txt');
INSERT INTO WebApp_File_Type VALUES('DocReadr','pdf');
INSERT INTO WebApp_File_Type VALUES('SQLEdit','sql');
INSERT INTO WebApp_File_Type VALUES('Microsoft Excel','xls');
INSERT INTO WebApp_File_Type VALUES('Microsoft Excel','xlsx');
INSERT INTO WebApp_File_Type VALUES('Microsoft Excel','csv');
-- Seed Documents
INSERT INTO DOCUMENT VALUES(1,'Doc1', 'txt', '/Documents',215,'04-10-2016 06:00 PM','02-16-2016 08:00 AM', '02-15-2016 04:22 PM', 1,1, 'DocReadr');
INSERT INTO DOCUMENT VALUES(2, 'NotVirus', 'exe', '/Downloads', 2612, '01-16-2016 06:42 PM','04-14-2016 3:22 PM', '03-02-2016 04:56 PM', 2, 2, NULL);
INSERT INTO DOCUMENT VALUES(3, 'Doc3', 'sql', '/CIS353', 4, '04-12-2016 03:03 PM', '04-12-2016 03:03 AM', '04-12-2016 03:03 AM', 3, 5, 'SqlEdit');
INSERT INTO DOCUMENT VALUES(4, 'Doc4', 'doc', '/Home', 420, '04-10-2016 05:50 PM', '04-11-2016 6:11 PM', '04-01-2016 4:20 PM', 1, 6, 'DocReadr');
INSERT INTO DOCUMENT VALUES(5, 'falsified_tax_info', 'xlsx', '/Home', 309, '02-29-1996 04:44 PM', '02-29-1996 04:44 PM', '12-01-1970 7:03 PM', 2, 2, 'Microsoft Excel');
INSERT INTO DOCUMENT VALUES(6,'img1', 'jpg', '/Pictures',459,'04-14-2016 07:45 PM','04-12-2016 03:03 AM', '04-12-2016 03:03 AM', 4,5, 'FREE_IMG_VIEW3R');
INSERT INTO DOCUMENT VALUES(7,'img2', 'png', '/Pictures', 333, '04-02-2016 05:20 PM','04-01-2016 5:01 PM','03-20-2016 03:33 PM', 4,5, 'FREE_IMG_VIER3R');
INSERT INTO DOCUMENT VALUES(8,'blackmail', 'jpg', '/Pictures',572,'06-06-2015 06:42 PM','06-04-2015 03:00 AM', '06-04-2015 03:00 AM', 4,4, 'FREE_IMG_VIEW3R');
INSERT INTO DOCUMENT VALUES(9, 'img4', 'gif', '/Pictures', 3541, '10-14-2014 03:23 PM', '10-14-2014 03:23 PM', '10-10-2010 10:10 PM', 4, 5, 'FREE_IMG_VIEW3R');
INSERT INTO DOCUMENT VALUES(10,'img5', 'jpg', '/Pictures',225,'06-08-2015 08:54 PM','06-08-2015 08:54 PM', '06-08-2015 08:54 PM', 4,5, 'FREE_IMG_VIEW3R');
INSERT INTO DOCUMENT VALUES(11, 'img6', 'jpg', '/Pictures', 442, '06-08-2015 08:57 PM', '06-08-2015 08:57 PM', '06-08-2015 08:57 PM', 4, 5, 'FREE_IMG_VIEW3R');
INSERT INTO DOCUMENT VALUES(12, 'img7', 'png', '/Pictures', 734, '06-08-2015 08:59 PM', '06-08-2015 08:59 PM', '06-08-2015 08:59 PM', 4, 5, 'FREE_IMG_VIEW3R');
-- Seed Directory_Sharers
INSERT INTO Directory_Sharer VALUES(5, 1, 'view', '03-03-2005 03:39 PM');
INSERT INTO Directory_Sharer VALUES(6, 4, 'edit', '03-03-2005 03:42 PM');
INSERT INTO Directory_Sharer VALUES(7, 1, 'edit', '03-03-2005 03:39 PM');
-- Seed Document_Sharers
INSERT INTO Document_Sharer VALUES(2,1,'edit','02-15-2016 08:23 AM');
INSERT INTO Document_Sharer VALUES(3,2,'edit','04-10-2016 10:42 PM');
INSERT INTO Document_Sharer VALUES(2,3,'edit','04-11-2016 02:31 PM');
INSERT INTO Document_Sharer VALUES(3,4,'edit','04-11-2016 02:33 PM');
INSERT INTO Document_Sharer VALUES(4,5,'edit','02-28-2004 08:02 AM');
INSERT INTO Document_Sharer VALUES(2,6,'edit','04-12-2016 03:23 PM');
INSERT INTO Document_Sharer VALUES(2,7,'edit','04-01-2016 04:59 PM');
INSERT INTO Document_Sharer VALUES(3,8,'view','06-04-2015 06:00 AM');
INSERT INTO Document_Sharer VALUES(3,9,'edit','10-10-2014 07:13 PM');
INSERT INTO Document_Sharer VALUES(1,10,'edit','06-08-2015 08:50 PM');
INSERT INTO Document_Sharer VALUES(1,11,'edit','06-08-2015 08:50 PM');
INSERT INTO Document_Sharer VALUES(1,12,'edit','06-08-2015 08:50 PM');
-- Seed Shadow_Copies
INSERT INTO Shadow_Copy VALUES('02-16-2016 07:55 AM',1,'/temp/docs/',2);
INSERT INTO Shadow_Copy VALUES('04-14-2016 03:17 PM',2,'/temp/exe/',3);
INSERT INTO Shadow_Copy VALUES('04-12-2016 02:58 AM',3,'/temp/docs',2);
INSERT INTO Shadow_Copy VALUES('04-11-2016 06:06 PM',4,'/temp/docs/',3);
INSERT INTO Shadow_Copy VALUES('02-29-2004 04:39 PM',5,'/temp/data/',4);
INSERT INTO Shadow_Copy VALUES('04-12-2016 02:58 AM',6,'/temp/pics/',2);
INSERT INTO Shadow_Copy VALUES('04-01-2016 05:01 PM',7,'/temp/pics/',2);
INSERT INTO Shadow_Copy VALUES('06-04-2015 03:00 AM',8,'/temp/pics/',4);
INSERT INTO Shadow_Copy VALUES('10-14-2014 03:17 PM',9,'/temp/pics/',3);
INSERT INTO Shadow_Copy VALUES('06-08-2015 08:54 PM',10,'/temp/pics/',1);
INSERT INTO Shadow_Copy VALUES('06-08-2015 08:52 PM',11,'/temp/pics/',1);
INSERT INTO Shadow_Copy VALUES('06-08-2015 08:54 PM',12,'/temp/pics/',1);

SET FEEDBACK ON
COMMIT

--
-- Database Printout
--
SELECT * FROM Document;
SELECT * FROM Shadow_Copy;
SELECT * FROM UserTbl;
SELECT * FROM Directory;
SELECT * FROM WebApp;
SELECT * FROM Webapp_File_Type;
SELECT * FROM Document_Sharer;
SELECT * FROM Directory_Sharer;
--

--
--SQL QUERIES
--

-- Q1 - Join
-- We want to get the document filename and type for each shadow copy as well as the id of the parent 
-- directory of that document (if any)
SELECT D.FileName, DY.ID, U.email
FROM Shadow_Copy SC, Document D, Directory DY
WHERE D.ID = SC.DocID
      AND DY.ID = D.DirID;

-- Q2 - Self-Join
-- Get the id and name of the root directories (if they have any directories in them) along with the name and id of all of the first level directories.
SELECT A.ID, A.Name, B.ID, B.Name
FROM Directory A, Directory B
WHERE B.Parent = A.ID 
AND A.Parent IS NULL;

-- Q3 - Union
-- All users with id < 2 unioned with all users with id >3
SELECT U1.Name
FROM UserTbl U1
WHERE U1.UserID < 2
UNION
SELECT U2.Name
FROM UserTbl U2
WHERE U2.UserID >3;

-- Q4 - Intersect
-- Select the document sharers that share both documents
SELECT B.UserID
FROM Document A, Document_Sharer B
WHERE A.ID = 1
	AND A.ID = B.DocID
INTERSECT
SELECT D.UserID
FROM Document C, Document_Sharer D
WHERE C.ID = 3
	AND C.ID = D.DocID;

-- Q5 - Minus, Division, AVG, NON-CORRELATED SUBQUERY
-- Returns every non-root directory and the root directories that also have no files in them
SELECT Dir.ID, Dir.Name
FROM Directory Dir
WHERE NOT EXISTS(
	(SELECT Dir.ID, Dir.Name
	FROM Document D
	WHERE Dir.ID = DirID)
	MINUS
	(SELECT DISTINCT Dir.ID, Dir.Name
	FROM Document D
	WHERE Dir.ID = D.DirID AND Dir.Parent IS NOT NULL));

-- Q6 - Sum
-- Finds the sum of all the filesizes of all the documents
SELECT SUM(FileSize) 
FROM Document;

-- Q7 - Max
-- Finds the last date access of all the documents
SELECT MAX(LastAccessed) 
FROM Document;

-- Q8 - Min
-- Finds the smallest filesize of all the documents
SELECT MIN(FileSize)
FROM Document;

-- Q9 - GROUP BY, ORDER BY, HAVING
-- Finds the userid, user email, and the count of all their documents
-- which were modified last time they were accessed
-- for every user with 3 or more documents
SELECT U.UserID, U.Email, COUNT(*)
FROM UserTbl U, Document D
WHERE D.LastAccessed = D.LastModified 
	AND U.UserID = D.Owner
GROUP BY U.UserID, U.Email
HAVING COUNT(*) > 3
ORDER BY U.UserID;

-- Q10 - Correlated Subquery
-- Gets the user that is the owner of document with id 1
SELECT *
FROM UserTbl A
WHERE A.UserID = (
    SELECT B.Owner 
	FROM Document B 
	WHERE A.UserID = B.Owner 
		AND B.ID = 1);

-- Q11 - Outer Join
-- Selects uID and last modified date of all users' documents.
SELECT U.UserID, D.LastModified 
FROM UserTbl U LEFT OUTER JOIN Document D 
ON U.UserID = D.Owner;

-- Q12 - AVG
SELECT AVG(FileSize)
FROM Document;


--
--Testing IC's
--

--Testing: PK_3
--Shouldn't be able to insert a user with an ID of 3, since it already exists.
INSERT INTO UserTbl VALUES(3,'databasestudent@mail.school.com','Wilfred Wong','07-3-2006 12:31 PM');
--Can't update since it references other data.
UPDATE UserTbl SET UserID = 7 WHERE UserID = 4;
--Can't delete since it references other data.
DELETE FROM UserTbl WHERE UserID = 2;
COMMIT

--Testing: FK_3
--Can't update since it references other data.
UPDATE UserTbl SET UserID = 7 WHERE UserID = 4;
--Can't delete since it references other data.
DELETE FROM UserTbl WHERE UserID = 1;
COMMIT

--Testing: CK_1
--Can't insert a document with a name less than 4 chars.
INSERT INTO DOCUMENT VALUES(13,'me', 'txt', '/Documents',222,'01-01-2016 01:00 PM','01-01-2016 01:00 PM', '01-01-2015 04:32 PM', 1,1, 'DocReadr');
--Can't update a document to have a name less than 4 chars.
UPDATE Document SET FileName = 'IS' WHERE ID = 2;
COMMIT

--Testing: CK_2
--Can't insert a document that was modified before it was created.
INSERT INTO DOCUMENT VALUES(13,'me', 'txt', '/Documents',222,'01-01-2016 01:00 PM','01-01-2016 01:00 PM', '01-02-2016 04:32 PM', 1,1, 'DocReadr');
--Can't update a document to be modified before it was created.
UPDATE Document SET DateCreated = '06-02-2016 04:56 PM' WHERE ID = 2;
COMMIT

SPOOL OFF



