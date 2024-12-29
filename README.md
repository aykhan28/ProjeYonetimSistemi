# ProjeYonetimSistemi

MySQL Kodları:
USE vtys;

CREATE TABLE users (
    gmail VARCHAR(255) PRIMARY KEY ,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL
);

CREATE TABLE projects (
    idprojects INT PRIMARY KEY AUTO_INCREMENT,
    projectname VARCHAR(255) NOT NULL,
    startTime VARCHAR(255) NOT NULL,
    finishTime VARCHAR(255) NOT NULL,
    gmail VARCHAR(255)
);

CREATE TABLE workers (
    idWorkers INT PRIMARY KEY AUTO_INCREMENT,
    gmail VARCHAR(255),
    workerName VARCHAR(255) NOT NULL,
    workergmail VARCHAR(255) NOT NULL,
    idpozisyon INT
   
);

CREATE TABLE missions (
    idmissions  INT PRIMARY KEY AUTO_INCREMENT,
    idprojects INT,
    idWorkers INT,
    mission VARCHAR(255) NOT NULL,
    missionstart VARCHAR(255) NOT NULL,
    missionfinish VARCHAR(255) NOT NULL,
    iddurum INT
);

CREATE TABLE pozisyon (
    idpozisyon INT PRIMARY KEY,
    pozisyoncol VARCHAR(255) NOT NULL
);

CREATE TABLE durum (
    iddurum INT PRIMARY KEY,
    durumcol VARCHAR(255) NOT NULL
);
