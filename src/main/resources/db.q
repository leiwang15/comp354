DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS Activity;
DROP TABLE IF EXISTS PERTActivity;
DROP TABLE IF EXISTS OnNodeActivity;
DROP TABLE IF EXISTS ActivityPredecessor;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS UserActivity;
DROP TABLE IF EXISTS ActivityProgressHistory;
DROP TABLE IF EXISTS ProjectUser;

CREATE TABLE Project (
	ProjectID		INTEGER				PRIMARY KEY AUTOINCREMENT,
	Name			VARCHAR(100)		NOT NULL,
	Desc			TEXT
);

CREATE TABLE Activity (
	ActivityID	INTEGER				PRIMARY KEY AUTOINCREMENT,
	ProjectID		INTEGER				NOT NULL REFERENCES Project(ProjectID), 
	Name			VARCHAR(50)			NOT NULL, 
	Desc			TEXT
);

CREATE TABLE PERTActivity (
	ActivityID	INTEGER				NOT NULL REFERENCES Activity(ActivityID),
	Pessimistic	INTEGER				NOT NULL, 
	Optimistic	INTEGER				NOT NULL,
	Estimated		INTEGER				NOT NULL
);

CREATE TABLE OnNodeActivity (
	ActivityID	INTEGER				NOT NULL REFERENCES Activity(ActivityID),
	Duration		INTEGER				NOT NULL
);

CREATE TABLE ActivityPredecessor (
	ActivityID	INTEGER				NOT NULL REFERENCES Activity(ActivityID),
	PredID			INTEGER				NOT NULL REFERENCES Activity(ActivityID),
	PRIMARY KEY (ActivityID, PredID)
);

CREATE TABLE Role (
	Type			CHAR(1)				PRIMARY KEY,
	Name			VARCHAR(50)			NOT NULL
);

CREATE TABLE User (
	UserID			INTEGER 				PRIMARY KEY AUTOINCREMENT,
	FirstName	VARCHAR(50)  		NOT NULL,
	LastName		VARCHAR(50)			NOT NULL,
	Role			CHAR(1)				NOT NULL REFERENCES Role(Type),
	UserName		VARCHAR(50)			NOT NULL,
	PassWord		VARCHAR(50)			NOT NULL
);

CREATE TABLE UserActivity (
	UserID			INTEGER 				REFERENCES User(UserID),
	ActivityID	INTEGER				REFERENCES Activity(ActivityID),
	PRIMARY KEY (UserID, ActivityID)
);

CREATE TABLE ActivityProgressHistory (
	ChangeID		INTEGER				PRIMARY KEY AUTOINCREMENT,
	UserID			INTEGER 				NOT NULL REFERENCES User(UserID),
	ActivityID	INTEGER				NOT NULL REFERENCES Activity(ActivityID),
	Progress		INTEGER				NOT NULL DEFAULT 0 CHECK (Progress BETWEEN 0 AND 100),
	Comment		TEXT,
	Timestamp	DATETIME				DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ProjectUser (
	ProjectID		INTEGER				NOT NULL REFERENCES Project(ProjectID),
	UserID			INTEGER				NOT NULL REFERENCES User(UserID),
	PRIMARY KEY (ProjectID, UserID) 
);

INSERT INTO Role(Type, Name) VALUES ('M', 'Manager');
INSERT INTO Role(Type, Name) VALUES ('U', 'User');