DROP DATABASE IF EXISTS toimistovarausjarjestelma;

CREATE DATABASE IF NOT EXISTS toimistovarausjarjestelma;

USE toimistovarausjarjestelma;

CREATE TABLE Asiakas
(
  asiakasID INT NOT NULL AUTO_INCREMENT,
  etunimi VARCHAR(20) NOT NULL,
  sukunimi VARCHAR(20) NOT NULL,
  sahkopostiosoite VARCHAR(50) NOT NULL,
  lahiosoite VARCHAR(50) NOT NULL,
  postitoimipaikka VARCHAR(30) NOT NULL,
  postinumero VARCHAR(5) NOT NULL,
  puhelinnumero VARCHAR(15) NOT NULL,
  PRIMARY KEY (asiakasID),
  INDEX asiakas_id_sukunimi_indeksi (asiakasID, sukunimi)
);

CREATE TABLE Toimipiste
(
  toimipisteID INT NOT NULL AUTO_INCREMENT,
  nimi VARCHAR(50) NOT NULL,
  lahiosoite VARCHAR(50) NOT NULL,
  postitoimipaikka VARCHAR(30) NOT NULL,
  postinumero VARCHAR(5) NOT NULL,
  kapasiteetti INT NOT NULL,
  vrkhinta DOUBLE NOT NULL,
  PRIMARY KEY (toimipisteID),
  INDEX toimipiste_id_nimi_indeksi (toimipisteID, nimi)
);

CREATE TABLE Varaus
(
  varausID INT NOT NULL AUTO_INCREMENT,
  alkupvm DATE NOT NULL,
  loppupvm DATE NOT NULL,
  varausaika TIMESTAMP NOT NULL,
  toimipisteID INT NOT NULL,
  asiakasID INT NOT NULL,
  PRIMARY KEY (varausID),
  FOREIGN KEY (toimipisteID) REFERENCES Toimipiste(toimipisteID) ON DELETE CASCADE,
  FOREIGN KEY (asiakasID) REFERENCES Asiakas(asiakasID) ON DELETE CASCADE,
  INDEX varaus_ID_toimipiste_asiakas_indeksi (varausID, toimipisteID, asiakasID)
);

CREATE TABLE Lasku
(
  laskuID INT NOT NULL AUTO_INCREMENT,
  etunimi VARCHAR(20) NOT NULL,
  sukunimi VARCHAR(20) NOT NULL,
  lahiosoite VARCHAR(50) NOT NULL,
  postitoimipaikka VARCHAR(30) NOT NULL,
  postinumero VARCHAR(5) NOT NULL,
  summa DOUBLE NOT NULL,
  maksunTila BOOLEAN NOT NULL,
  asiakasID INT NOT NULL,
  varausID INT NOT NULL,
  PRIMARY KEY (laskuID),
  FOREIGN KEY (asiakasID) REFERENCES Asiakas(asiakasID) ON DELETE CASCADE,
  FOREIGN KEY (varausID) REFERENCES Varaus(varausID) ON DELETE CASCADE,
  INDEX lasku_id_asiakas_varaus_indeksi (laskuID, asiakasID, varausID)
);

CREATE TABLE Palvelu
(
  palveluID INT NOT NULL AUTO_INCREMENT,
  nimi VARCHAR(50) NOT NULL,
  hinta DOUBLE NOT NULL,
  tyyppi VARCHAR(20) NOT NULL,
  toimipisteID INT NOT NULL,
  PRIMARY KEY (palveluID),
  FOREIGN KEY (toimipisteID) REFERENCES Toimipiste(toimipisteID) ON DELETE CASCADE,
  INDEX palvelu_nimi_toimipiste (nimi, toimipisteID)
);

CREATE TABLE VarauksenPalvelut
(
  varausID INT NOT NULL,
  palveluID INT NOT NULL,
  FOREIGN KEY (varausID) REFERENCES Varaus(varausID) ON DELETE CASCADE,
  FOREIGN KEY (palveluID) REFERENCES Palvelu(palveluID) ON DELETE CASCADE,
  INDEX varaus_id_palvelu_id_indeksi (varausID, palveluID)
);


ALTER TABLE Asiakas AUTO_INCREMENT = 101;

ALTER TABLE Varaus AUTO_INCREMENT = 11;

ALTER TABLE Lasku AUTO_INCREMENT = 5051;
