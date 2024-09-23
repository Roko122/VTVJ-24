INSERT INTO Asiakas (etunimi, sukunimi, sahkopostiosoite, lahiosoite, postitoimipaikka, postinumero, puhelinnumero) 
	VALUES 
	('Anna', 'Aalto', 'anna.aalto@gmail.com', 'Kukkapolku 12', 'Helsinki', '00100', '+358401234567'),
	('Mikko', 'Mäkinen', 'mikko.makinen@gmail.com', 'Tammitie 23', 'Helsinki', '00220', '+358404314144'),
	('Hanna', 'Hakkarainen', 'hanna.hakkarainen@gmail.com', 'Vanhatie 21', 'Tampere', '12200', '+358406489374'),
	('Juha', 'Järvinen', 'juha.jarvinen@gmail.com', 'Koivukuja 11', 'Jyväskylä', '50300', '+358405974385'),
	('Liisa', 'Lehtonen', 'liisa.lehtonen@gmail.com', 'Kalliotie 36', 'Rovaniemi', '10053', '+358453958631');

INSERT INTO Toimipiste (nimi, lahiosoite, postitoimipaikka, postinumero, kapasiteetti, vrkhinta) 
	VALUES 
	('Toimisto Aurora', 'Aurorankatu 10', 'Joensuu', '80100', 8, 250.55),
	('Toimisto Tori', 'Torikatu 15', 'Joensuu', '80120', 5, 120.40),
	('Toimisto Torni', 'Tornitie 24', 'Joensuu', '80120', 4, 90.15),
	('Toimisto Karjala', 'Karjalankatu 32', 'Joensuu', '80120', 8, 220.20),
	('Toimisto Puistokatu', 'Puistokatu 36', 'Joensuu', '80250', 8, 235.40);
	
INSERT INTO Varaus (alkupvm, loppupvm, varausaika, toimipisteID, asiakasID) 
	VALUES 
	('2024-04-20', '2024-05-22', NOW(), 1, 101),
	('2024-04-21', '2024-05-23', NOW(), 2, 102),
	('2024-04-22', '2024-05-24', NOW(), 3, 103),
	('2024-04-23', '2024-05-25', NOW(), 4, 104),
	('2024-04-24', '2024-05-26', NOW(), 5, 105);
	
INSERT INTO Lasku (etunimi, sukunimi, lahiosoite, postitoimipaikka, postinumero, summa, maksunTila, asiakasID, varausID) 
	VALUES 
	('Anna', 'Aalto', 'Kukkapolku 12', 'Helsinki', '00100', 120.00, 0, 101, 11),
	('Mikko', 'Mäkinen', 'Tammitie 23', 'Helsinki', '00220', 150.00, 0, 102, 12),
	('Hanna', 'Hakkarainen', 'Vanhatie 21', 'Tampere', '12200', 180.00, 0, 103, 13),
	('Juha', 'Järvinen', 'Koivukuja 11', 'Jyväskylä', '50300', 90.00, 0, 104, 14),
	('Liisa', 'Lehtonen', 'Kalliotie 36', 'Rovaniemi', '10053', 200.00, 0, 105, 15);
	
INSERT INTO Palvelu (nimi, hinta, tyyppi, toimipisteID) 
	VALUES 
	('Kahvitarjoilu', 20.00, 'Lisäpalvelu', 1),
	('Tulostin', 50.00, 'Laite', 1),
	('Wi-Fi', 5.00, 'Lisäpalvelu', 1),
	('Siivouspalvelu', 30.00, 'Lisäpalvelu', 2),
	('Tulostin', 50.00, 'Laite', 2),
	('Kahvinkeitin', 10.00, 'Laite', 2),
	('Wi-Fi', 5.00, 'Lisäpalvelu', 2),
	('Kahvitarjoilu', 20.00, 'Lisäpalvelu', 3),
	('Siivouspalvelu', 30.00, 'Lisäpalvelu', 3),
	('30" näyttö', 10.00, 'Laite', 3),
	('Wi-Fi', 5.00, 'Lisäpalvelu', 4),
	('Tulostin', 50.00, 'Laite', 4),
	('30" näyttö', 10.00, 'Laite', 4),
	('Wi-Fi', 5.00, 'Lisäpalvelu', 5);
	
INSERT INTO VarauksenPalvelut (varausID, palveluID)
	VALUES
	(11, 1),
	(11, 3),
	(13, 9),
	(14, 11),
	(14, 12),
	(14, 13),
	(15, 14);