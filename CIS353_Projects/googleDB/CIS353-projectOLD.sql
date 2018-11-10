SPOOL project.out
SET ECHO ON

/*
CIS 353 - Database Design Project
Jarod Hanko
Andrew Prins
Robert Heine
Christopher Fracassi
Adam Stewart
*/

-- Drop the tables (in case they already exist)
--

DROP TABLE Document CASCADE CONSTRAINTS;
DROP TABLE Shadow_Copy CASCADE CONSTRAINTS;
DROP TABLE Usr CASCADE CONSTRAINTS;
DROP TABLE Directory CASCADE CONSTRAINTS;
DROP TABLE Webapp CASCADE CONSTRAINTS;
DROP TABLE Webapp_File_Type CASCADE CONSTRAINTS;
DROP TABLE Document_Sharers CASCADE CONSTRAINTS;
DROP TABLE Directory_Sharers CASCADE CONSTRAINTS;

--
-- Create the tables
--
CREATE TABLE  Document(
    Id            	INT,
    FileName        VARCHAR2(255 char),
    FileType        VARCHAR2(255 char),
    FileLocation    VARCHAR2(255 char),
    FileSize        INT,
    LastAccessed    DATE,
    LastModified    DATE,
    DateCreated     DATE,
    Owner         	INT,
    DirID           INT,
    WebApp        	VARCHAR(127 char),
--Document Constraints
--PK_1: Document IDs must be unique.
	CONSTRAINT PK_1 PRIMARY KEY (Id),
--FK_1: Every document owner must be a user.
	CONSTRAINT FK_1 FOREIGN KEY (Owner) REFERENCES Usr(UserID)
		DEFERRABLE INITIALLY DEFERRED,
--FK_2: Every document directory must have a directory ID.
	CONSTRAINT FK_2 FOREIGN KEY (DirID) REFERENCES Directory(ID)
		DEFERRABLE INITIALLY DEFERRED,
--CK_1: A document name must be at least 4 chars.
	CONSTRAINT CK_1 CHECK (Length(‘FileName’) > 4),
--CK_2: A document can't be modified before it was created.
	CONSTRAINT CK_2 CHECK (DateCreated < DateModified)
);

CREATE TABLE Shadow_Copy(
    DateModified     DATE,
    DocID            INT,
    FileLocation     VARCHAR2(255 char),
    ModifiedBy       VARCHAR2(127 char),
--Shadow_Copy Constraints	
--PK_2: A shadow-copy's DateModified and ID must be unique.
	CONSTRAINT PK_2 PRIMARY KEY (DateModified, Id),
--FK_3: Last modifications must be made by a user.
    CONSTRAINT FK_3 (ModifiedBy) references USR(Name)
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Usr (
    UserID         	INT,
    Email         	VARCHAR2(255 char),
    Name         	VARCHAR2(127 char),
    DateJoined     	DATE,
--Usr Contraints
--CK_3: User IDs must be unique.
    CONSTRAINT CK_3 PRIMARY KEY (UserID)
);

CREATE TABLE Directory (
    ID         		INT,
    dateCreated     DATE,
    Name         	VARCHAR2(127 char),
    Owner     		INT,
	Parent        	INT,
--Directory Constraints
--PK_4: Directory IDs must be unique.
	CONSTRAINT PK_4 PRIMARY KEY (ID),
--FK_4: The owner of a directory must be a user.
	CONSTRAINT FK_4 FOREIGN KEY (Owner) REFERENCES Usr(UserID)
		DEFERRABLE INITIALLY DEFERRED,
--FK_5: The parent of a directory must have a directory ID.
	CONSTRAINT FK_5 FOREIGN KEY (Parent) REFERENCES Directory(ID)
		DEFERRABLE INITIALLY DEFERRED,
--CK_3: A directory can't have more than 1 parent.
	CONSTRAINT CK_3 CHECK (Parent <> NULL OR ID = 1)
);

CREATE TABLE Webapp (
    Name         	VARCHAR2(127 char),
    Description     VARCHAR2(255 char),
--Webapp Constraints
--PK_5: A Webapp name must be unique.
    CONSTRAINT PK_5 PRIMARY KEY(Name) 
);

CREATE TABLE Webapp_File_Type(
    AppName        	VARCHAR2(255 char),
    AssocFileType   VARCHAR2(255 char),
--Webapp_File_Type Constraints
--PK_6: A Webapp_File_Type must have a unique name and associated file type.
	CONSTRAINT PK_6 PRIMARY KEY (AppName, AssocFileType),
--FK_6: The app name must have a name in the Webapps.
	CONSTRAINT FK_6 FOREIGN KEY (AppName) REFERENCES Webapp(Name)
		DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Document_Sharers(
    UserID            INT,
    DocID          INT,
    Permission     VarChar2,
    SharedDate     DATE,
--Document_Sharers Constraints
--PK_7: Document_Sharers must have a unique user ID and document ID.
    CONSTRAINT PK_7 PRIMARY KEY (UserID, DocID),
--FK_7: The document ID being shared must be a real document.
    CONSTRAINT FK_7 FOREIGN KEY (DocID) REFERENCES Document(ID)
		DEFERRABLE INITIALLY DEFERRED,
--FK_8: The user sharing the document must be a real user.
	CONSTRAINT FK_8 FOREIGN KEY (UserID) REFERENCES Usr(UserID)
		DEFERRABLE INITIALLY DEFERRED,
--FK_9: The possible permissions of a document must be 'view' or 'edit'.
	CONSTRAINT CK_4 CHECK (Permission IN (‘view’, ‘edit’))
);

CREATE TABLE Directory_Sharers (
    DirID         	INT,
    UserID         	INT,
    Permission     	INT,
    SharedDate     	DATE,
--Directory_Sharers Constraints
--PK_8: Directory_Sharers must have a unique directory ID and user ID.
    CONSTRAINT PK_8 PRIMARY KEY (DirID, UserID),
--FK_9: The directory ID being shared must be a real directory.
    CONSTRAINT FK_9 FOREIGN KEY (DirID) REFERENCES Directory(ID)
		DEFERRABLE INITIALLY DEFERRED,
--FK_10: The user sharing the document must be a real user.
	CONSTRAINT FK_10 FOREIGN KEY (UserID) REFERENCES Usr(UserID)
		DEFERRABLE INITIALLY DEFERRED
);

SET FEEDBACK ON
COMMIT


SET FEEDBACK OFF
-- Seed Documents
INSERT INTO DOCUMENTS(1,’Doc1’, ‘txt’, ‘/Documents’,215,’04-10-2016 06:00 PM’,’02-16-2016 08:00 AM’, ‘02-15-2016 04:22 PM’, 1,1, ‘DocReadr’);
INSERT INTO DOCUMENTS (2, ‘NotVirus’, ‘exe’, ‘/Downloads’, 2612, ‘01-16-2016 06:42 PM’, ‘04-14-2016 3:22 PM’, ‘03-02-2016 04:56 PM’, 2, 2, NULL);
INSERT INTO DOCUMENTS (3, ‘Doc3’, ‘sql’, ‘/CIS353’, 4, ‘04-12-2016 03:03 PM’, ‘04-12-2016 03:03 AM’, ‘04-12-2016 03:03 AM’, 3, 5, ’SqlEdit’);
INSERT INTO DOCUMENTS (4, ‘Doc4’, ‘doc’, ‘/Home’, 420, ‘04-10-2016 05:50 PM’, ‘04-11-2016 6:11 PM’, ‘04-01-2016 4:20 PM’, 1, 6, ‘DocReadr’);
INSERT INTO DOCUMENTS(5, ‘falsified_tax_info’, ‘xlsx’, ‘/Home’, 309, ‘02-29-1996 04:44 PM’, ‘02-29-1996 04:44 PM’, ‘12-01-1970 7:03 PM’, 2, 2, ‘Microsoft Excel’);
INSERT INTO DOCUMENTS(6,’img1’, ‘jpg’, ‘/Pictures’,459,’04-14-2016 07:45 PM’,’04-12-2016 03:03 AM’, ‘04-12-2016 03:03 AM’, 4,5, ‘FREE_IMG_VIEW3R’);
INSERT INTO DOCUMENTS(7,’img2’, ‘png’, ‘/Pictures’, 333, ’04-02-2016 05:20 PM’,’04-01-2016 5:01 PM’, ‘03-20-2016 03:33 PM’, 4,5, ‘FREE_IMG_VIER3R’);
INSERT INTO DOCUMENTS(8,’blackmail’, ‘jpg’, ‘/Pictures’,572,’06-06-2015 06:42 PM’,’06-04-2015 03:00 AM’, ‘06-04-2015 03:00 AM’, 4,4, ‘FREE_IMG_VIEW3R’);
INSERT INTO DOCUMENTS(9, ‘img4’, ‘gif’, ‘/Pictures’, 3541, ‘10-14-2014 03:23 PM’, ‘10-14-2014 03:23 PM’, ‘10-10-2010 10:10 PM’, 4, 5, ‘FREE_IMG_VIEW3R’);
INSERT INTO DOCUMENTS(10,’img5’, ‘jpg’, ‘/Pictures’,225,’06-08-2015 08:54 PM’,’06-08-2015 08:54 PM’, ‘06-08-2015 08:54 PM’, 4,5, ‘FREE_IMG_VIEW3R’);
INSERT INTO DOCUMENTS(11, ‘img6’, ‘jpg’, ‘/Pictures’, 442, ‘06-08-2015 08:57 PM’, ‘06-08-2015 08:57 PM’, ‘06-08-2015 08:57 PM’, 4, 5, ‘FREE_IMG_VIEW3R’);
INSERT INTO DOCUMENTS(12, ’img7’, ‘png’, ‘/Pictures’, 734, ’06-08-2015 08:59 PM’, ’06-08-2015 08:59 PM’, ‘06-08-2015 08:59 PM’, 4, 5, ‘FREE_IMG_VIEW3R’);
-- Seed Directories
INSERT INTO Directories VALUES (1, ‘03-03-2005 03:33 PM’, ‘~root’, 1, NULL);
INSERT INTO Directories VALUES (2, ‘03-15-1969 06:23 PM’, ‘~root’, 2, NULL);
INSERT INTO Directories VALUES (3, ‘02-29-2004 04:50 PM’, ‘~root’, 3, NULL);
INSERT INTO Directories VALUES (4, ‘12-01-2007 06:26 AM’, ‘~root’, 4, NULL);
INSERT INTO Directories VALUES (5, ‘03-03-2005 03:36 PM’, ‘Pics’, 4, 4);
INSERT INTO Directories VALUES (6, ‘03-03-2005 03:39 PM’, ‘Docs’, 1, 1);
INSERT INTO Directories VALUES (7, ‘03-16-1969 09:27 PM’, ‘TestFolder’, 2, 2);
-- Seed WebApps
INSERT INTO WebApp VALUES (‘FREE_IMG_VIEW3R’,’See images 4 free!!!!!’);
INSERT INTO WebApp VALUES (‘DocReadr’,’Innovative way to view .doc files.’);
INSERT INTO WebApp VALUES (‘SQLEdit’,’Built in SQL interpreter’);
INSERT INTO WebApp VALUES (‘Microsoft Excel’,’Official Microsoft Excel Plugin’);
-- Seed WebAppFileTypes
INSERT INTO WebAppFileTypes VALUES(‘FREE_IMG_VIEW3R’,’gif’);
INSERT INTO WebAppFileTypes VALUES(‘FREE_IMG_VIEW3R’,’png’);
INSERT INTO WebAppFileTypes VALUES(‘FREE_IMG_VIEW3R’,’jpg’);
INSERT INTO WebAppFileTypes VALUES(‘DocReadr’,’doc’);
INSERT INTO WebAppFileTypes VALUES(‘DocReadr’,’docx’);
INSERT INTO WebAppFileTypes VALUES(‘DocReadr’,’txt’);
INSERT INTO WebAppFileTypes VALUES(‘DocReadr’,’pdf’);
INSERT INTO WebAppFileTypes VALUES(‘SQLEdit’,’sql’);
INSERT INTO WebAppFileTypes VALUES(‘Microsoft Excel’,’xls’);
INSERT INTO WebAppFileTypes VALUES(‘Microsoft Excel’,’xlsx’);
INSERT INTO WebAppFileTypes VALUES(‘Microsoft Excel’,’csv’);
-- Seed Document_Sharers
INSERT INTO Document_Sharers VALUES(2,1,’edit’,’02-15-2016 08:23 AM’);
INSERT INTO Document_Sharers VALUES(3,2,’edit’,’04-10-2016 10:42 PM’);
INSERT INTO Document_Sharers VALUES(2,3,’edit’,’04-11-2016 02:31 PM’);
INSERT INTO Document_Sharers VALUES(3,4,’edit’,’04-11-2016 02:33 PM’);
INSERT INTO Document_Sharers VALUES(4,5,’edit’,’02-28-2004 08:02 AM’);
INSERT INTO Document_Sharers VALUES(2,6,’edit’,’04-12-2016 03:23 PM’);
INSERT INTO Document_Sharers VALUES(2,7,’edit’,’04-01-2016 04:59 PM’);
INSERT INTO Document_Sharers VALUES(3,8,’view’,’06-04-2015 06:00 AM’);
INSERT INTO Document_Sharers VALUES(3,9,’edit’,’10-10-2014 07:13 PM’);
INSERT INTO Document_Sharers VALUES(1,10,’edit’,’06-08-2015 08:50 PM’);
INSERT INTO Document_Sharers VALUES(1,11,’edit’,’06-08-2015 08:50 PM’);
INSERT INTO Document_Sharers VALUES(1,12,’edit’,’06-08-2015 08:50 PM’);
-- Seed Directory_Sharers
INSERT INTO Directory_Sharer VALUES(5, 1, ‘view’, ‘03-03-2005 03:39 PM’);
INSERT INTO Directory_Sharer VALUES(6, 4, ‘edit’, ‘03-03-2005 03:42 PM’);
INSERT INTO Directory_Sharer VALUES(7, 1, ‘edit’, ‘03-03-2005 03:39 PM’);
-- Seed Shadow_Copies
INSERT INTO Shadow_Copy VALUES(‘02-16-2016 07:55 AM’,1,’Doc1’,’txt’,’/temp/docs/’,2);
INSERT INTO Shadow_Copy VALUES(‘04-14-2016 03:17 PM’,2,’NotVirus’,’exe’,’/temp/exe/’,3);
INSERT INTO Shadow_Copy VALUES(‘04-12-2016 02:58 AM’,3,’Doc3’,’sql’,’/temp/docs’,2);
INSERT INTO Shadow_Copy VALUES(‘04-11-2016 06:06 PM’,4,’Doc4’,’doc’,’/temp/docs/’,3);
INSERT INTO Shadow_Copy VALUES(‘02-29-2004 04:39 PM’,5,’falsified_tax_info’,’xlsx’,’/temp/data/’,4);
INSERT INTO Shadow_Copy VALUES(‘04-12-2016 02:58 AM’,6,’img1’,’jpg’,’/temp/pics/’,2);
INSERT INTO Shadow_Copy VALUES(‘04-01-2016 05:01 PM’,7,’img2’,’png’,’/temp/pics/’,2);
INSERT INTO Shadow_Copy VALUES(‘06-04-2015 03:00 AM’,8,’blackmail’,’jpg’,’/temp/pics/’,4);
INSERT INTO Shadow_Copy VALUES(‘10-14-2014 03:17 PM’,9,’img4’,’gif’,’/temp/pics/’,3);
INSERT INTO Shadow_Copy VALUES(‘06-08-2015 08:54 PM’,10,’img5’,’jpg’,’/temp/pics/’,1);
INSERT INTO Shadow_Copy VALUES(‘06-08-2015 08:52 PM’,11,’img6’,’jpg’,’/temp/pics/’,1);
INSERT INTO Shadow_Copy VALUES(‘06-08-2015 08:54 PM’,12,’img7’,’png’,’/temp/pics/’,1);
-- Seed Users
INSERT INTO Users VALUES(1,’codemaster2@gmail.com’,’Kobe Marx’,’03-03-2005 03:33 PM’);
INSERT INTO Users VALUES(2,’kooldude23@yahoo.com’,’Kevin Smith’,’03-15-1969 06:23 PM’);
INSERT INTO Users VALUES(3,’coolguy@hotmail.com’,’James Potter’,’02-29-2004 04:50 PM’);
INSERT INTO Users VALUES(4,’fakeemail@mail.ru’,’Sergei Roldugin’,’12-01-2007 06:26 AM’);


--
-- Database Printout
--
SELECT * FROM TABLE Document;
SELECT * FROM TABLE Shadow_Copy;
SELECT * FROM TABLE User;
SELECT * FROM TABLE Directory;
SELECT * FROM TABLE WebApp;
SELECT * FROM TABLE Webapp_File_Type;
SELECT * FROM TABLE Document_Sharers;
SELECT * FROM TABLE Directory_Sharers;
--

--
--SQL QUERIES
--

-- Q1 - Join
-- We want to get the document filename and type for each shadow copy as well as the id of the parent directory of that document (if any), the email of its owner, and the Description of the Webapp associated with it
SELECT D.FileName, DY.ID, U.email, W.description
FROM Shadow_Copy SC, Document D, Directory DY, User U, WebApp W
WHERE D.ID = SC.DocID
      AND DY.ID = D.DirID
      AND U.ID = D.Owner
      AND W.Name = D.WebApp;

-- Q2 - Self-Join
-- Get the name of the root along with the name and id of all of the first level directories.
SELECT A.Name, B.Name, B.ID
FROM Directories A, Directories B
WHERE B.Parent = A.ID 
AND A.Parent IS NULL;

-- Q3 - Union
-- All users with id < 2 unioned with all users with id >4
SELECT U1.Name
FROM     Users U1
WHERE U1.ID < 2
UNION
SELECT U2.Name
FROM Users U2
WHERE U2.ID >4;

-- Q4 - Intersect
-- Select the document shares that share both documents
SELECT B.UserID
FROM Document A, Document_Sharers B
WHERE A.ID = 1
	AND A.ID = B.DocID
INTERSECT
SELECT B.UserID
FROM Document C, Document_Sharers D
WHERE C.ID = 3
	AND C.ID = D.DocID;

-- Q5 - Minus, Division, AVG, NON-CORRELATED SUBQUERY
-- Every directory who doesn’t have a parent, but has all directories are less than 128

SELECT D.ID, D.FileName, D.FileType 
FROM Directory D
WHERE NOT EXISTS(
    (SELECT D.ID, D.FileName, D.FileType
	FROM Directory D
	WHERE D.parent IS NULL)
MINUS
	(SELECT D.ID, D.FileName, D.FileType
	FROM Directories
	WHERE 128 < 
		(SELECT AVG(D.size)
		FROM Directory D)
);

-- Q6 - Sum
-- Finds the sum of all the filesizes of all the documents
SELECT SUM(FileSize) 
FROM Documents;

-- Q7 - Max
-- Finds the last date access of all the documents
SELECT MAX(DateAccessed) 
FROM Documents;

-- Q8 - Min
-- Finds the smallest filesize of all the documents
SELECT MIN(FileSize)
FROM Documents;

-- Q9 - GROUP BY, ORDER BY, HAVING
-- Finds the userid, user email, and the count of all their documents
-- for every user with 3 or more documents
SELECT U.UserID, U.Email, COUNT(*)
FROM User U, Documents D
WHERE D.LastAccessed = D.LastModified 
	AND U.UserID = D.Owner
GROUP BY U.UserID, U.Email
HAVING COUNT(*) > 3
ORDER BY U.UserID;

-- Q10 - Correlated Subquery
-- Gets the user that is the owner of document with id 1
SELECT *
FROM User A
WHERE A.UserID = (
    SELECT B.Owner 
	FROM Document B 
	WHERE A.UserID = B.Owner 
		AND B.ID = 1);

-- Q11 - Outer Join
-- DESCRIPTION NEEDED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
SELECT U.UserID, D.LastModified 
FROM User U LEFT OUTER JOIN Documents D 
ON U.UserID = D.Owner;


--
--Testing IC's
--

--Testing: PK_3
--<TODO: SQL INSERT, UPDATE, DELETE>
-- COMMIT

--Testing: FK_3
--<TODO: SQL INSERT, UPDATE, DELETE>
-- COMMIT

--Testing: CK_1
--<TODO: SQL INSERT, UPDATE, DELETE>
--COMMIT

--Testing: CK_2
--<TODO: SQL INSERT, UPDATE, DELETE>
-- COMMIT

SPOOL OFF



