-- Crearea bazei de date
CREATE DATABASE IF NOT EXISTS realestate_db;
USE realestate_db;

-- Tabela Agentie (08.01.a)
CREATE TABLE Agentie (
    id_agentie INT PRIMARY KEY AUTO_INCREMENT,
    nume VARCHAR(100) NOT NULL,
    telefon VARCHAR(15)
);

-- Tabela Tip (08.01.c)
CREATE TABLE Tip (
    id_tip INT PRIMARY KEY AUTO_INCREMENT,
    denumire VARCHAR(50) NOT NULL,
    caracteristici VARCHAR(500)
);

-- Tabela Spatiu (08.01.b)
CREATE TABLE Spatiu (
    id_spatiu INT PRIMARY KEY AUTO_INCREMENT,
    adresa VARCHAR(200) NOT NULL,
    zona INT NOT NULL,
    suprafata DECIMAL(10,2) NOT NULL,
    id_tip INT NOT NULL,
    FOREIGN KEY (id_tip) REFERENCES Tip(id_tip)
);

-- Tabela Oferta (08.01.d)
CREATE TABLE Oferta (
    id_agentie INT NOT NULL,
    id_spatiu INT NOT NULL,
    vanzare CHAR(1) NOT NULL CHECK (vanzare IN ('D', 'N')),
    pret DECIMAL(15,2) NOT NULL,
    moneda VARCHAR(3) NOT NULL,
    PRIMARY KEY (id_agentie, id_spatiu),
    FOREIGN KEY (id_agentie) REFERENCES Agentie(id_agentie),
    FOREIGN KEY (id_spatiu) REFERENCES Spatiu(id_spatiu)
);

-- Tabela Exceptii (08.08)
CREATE TABLE Exceptii (
    id_agentie INT NOT NULL,
    id_spatiu INT NOT NULL,
    vanzare CHAR(1) NOT NULL,
    pret DECIMAL(15,2) NOT NULL,
    moneda VARCHAR(3) NOT NULL,
    natura_exceptiei VARCHAR(500),
    PRIMARY KEY (id_agentie, id_spatiu),
    FOREIGN KEY (id_agentie, id_spatiu) REFERENCES Oferta(id_agentie, id_spatiu)
);

-- 08.01.e (cheile străine sunt deja definite mai sus)

-- 08.01.f (telefonul este deja în Agentie)

-- 08.02.a) constrangere la nivel atribut pentru suprafata
ALTER TABLE Spatiu ADD CONSTRAINT chk_suprafata CHECK (suprafata BETWEEN 20 AND 1000);

-- 08.02.b) constrangere la nivel tupla pentru caracteristici si denumire
DELIMITER //
CREATE TRIGGER chk_caracteristici_denumire
BEFORE INSERT ON Tip
FOR EACH ROW
BEGIN
    IF (NEW.caracteristici LIKE '%camere%' AND NEW.denumire NOT LIKE '%apartament%') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Dacă caracteristicile conțin "camere", denumirea trebuie să fie "apartament"';
    END IF;
END//
DELIMITER ;

-- popularea tabelelor cu date
INSERT INTO Agentie (id_agentie, nume, telefon) VALUES
(1, 'Imobiliare RO', '0740123456'),
(2, 'Agentia Transilvania', '0741123456'),
(3, 'Turda Real Estate', '0742123456'),
(4, 'Cluj Napoca Imobiliare', '0743123456'),
(5, 'Agentia Turda', '0744123456'),
(6, 'AgentieRoyal', '0745123456'),
(7, 'Bucuresti Imobiliare', '0746123456'),
(8, 'Timisoara Real Estate', '0747123456');

INSERT INTO Tip (id_tip, denumire, caracteristici) VALUES
(1, 'apartament', '2 camere, living, bucatarie, baie'),
(2, 'apartament', '3 camere, living, bucatarie, 2 bai'),
(3, 'apartament', '4 camere, living, bucatarie, 2 bai, balcon'),
(4, 'garsoniera', 'o camera, bucatarie, baie'),
(5, 'garaj', 'loc parcare acoperit'),
(6, 'casa', '3 dormitoare, living, bucatarie, 2 bai, gradina'),
(7, 'birou', 'spatiu comercial, central, acces usor'),
(8, 'apartament', '2 camere, living decomandat, balcon'),
(9, 'apartament', '3 camere, living, bucatarie, 2 bai, balcon mare');

INSERT INTO Spatiu (id_spatiu, adresa, zona, suprafata, id_tip) VALUES
(1, 'Turda, Strada Turda, Nr. 15', 1, 65.00, 2),
(2, 'Turda, Strada Turturelelor, Bloc A5', 1, 45.00, 4),
(3, 'Turda, Strada Principala, Nr. 23', 2, 85.00, 3),
(4, 'Turda, Strada Turturelelor, Nr. 10', 3, 120.00, 6),
(5, 'Cluj-Napoca, Strada Turda, Nr. 34', 1, 55.00, 1),
(6, 'Cluj-Napoca, Bulevardul Turda, Ap. 12', 1, 35.00, 4),
(7, 'Cluj-Napoca, Strada Turturelelor, Nr. 8', 2, 75.00, 2),
(8, 'Turda, Strada Turturelelor, Nr. 1', 1, 25.00, 5),
(9, 'Turda, Strada Garajelor, Nr. 5', 2, 30.00, 5),
(10, 'Turda, Strada Turturelelor, Vila 3', 3, 150.00, 6),
(11, 'Turda, Strada Turda, Casa 7', 2, 110.00, 6),
(12, 'Bucuresti, Sector 1, Ap. 45', 1, 60.00, 2),
(13, 'Brasov, Centru, Nr. 78', 1, 40.00, 4),
(14, 'Sibiu, Piata Mare, Nr. 23', 1, 90.00, 3),
(15, 'Timisoara, Complex, Nr. 56', 2, 70.00, 2),
(16, 'Cluj-Napoca, Marasti, Nr. 100', 2, 85.00, 8),
(17, 'Bucuresti, Sector 6, Nr. 200', 3, 95.00, 9),
(18, 'Iasi, Copou, Nr. 50', 1, 65.00, 1),
(19, 'Cluj-Napoca, Manastur, Nr. 75', 2, 55.00, 4),
(20, 'Timisoara, Cetate, Nr. 30', 1, 45.00, 7);

INSERT INTO Oferta (id_agentie, id_spatiu, vanzare, pret, moneda) VALUES
-- oferte pentru teste interogari
(1, 1, 'D', 80000.00, 'EUR'),
(2, 2, 'N', 350.00, 'EUR'),
(3, 3, 'D', 120000.00, 'EUR'),
(4, 4, 'D', 150000.00, 'EUR'),
(5, 5, 'D', 90000.00, 'EUR'),
(6, 6, 'N', 300.00, 'EUR'),
(1, 7, 'D', 25000.00, 'EUR'),
(2, 8, 'N', 80.00, 'EUR'),
(3, 9, 'N', 100.00, 'EUR'),
(4, 10, 'D', 200000.00, 'EUR'),
(5, 11, 'N', 400.00, 'EUR'),
(6, 12, 'D', 110000.00, 'EUR'),
(1, 13, 'N', 250.00, 'EUR'),
(2, 14, 'D', 130000.00, 'EUR'),
(3, 15, 'D', 95000.00, 'EUR'),

-- oferte pentru intervalul 10k-50k EUR (pentru 08.03.b)
(4, 16, 'D', 15000.00, 'EUR'),
(5, 17, 'D', 25000.00, 'EUR'),
(6, 18, 'D', 35000.00, 'EUR'),
(1, 19, 'D', 45000.00, 'EUR'),

-- oferte pentru apartamente de inchiriat 100-400 EUR (pentru 08.04.a)
(2, 1, 'N', 150.00, 'EUR'),
(3, 5, 'N', 200.00, 'EUR'),
(4, 7, 'N', 300.00, 'EUR'),
(5, 12, 'N', 350.00, 'EUR'),

-- oferte pentru comparatie preturi (pentru 08.04.b)
(1, 2, 'D', 50000.00, 'EUR'),
(1, 3, 'D', 50100.00, 'EUR'),
(2, 4, 'D', 75000.00, 'EUR'),
(2, 5, 'D', 75050.00, 'EUR'),
(3, 6, 'D', 90000.00, 'EUR'),
(3, 7, 'D', 90080.00, 'EUR'),

-- oferte pentru 3 camere (pentru 08.05.a)
(4, 8, 'D', 85000.00, 'EUR'),
(5, 9, 'D', 120000.00, 'EUR'),
(6, 10, 'D', 85000.00, 'EUR'),

-- oferte pentru garsoniere (pentru 08.06.a)
(1, 11, 'D', 40000.00, 'EUR'),
(2, 13, 'D', 45000.00, 'EUR'),
(3, 14, 'D', 50000.00, 'EUR'),
(4, 15, 'D', 55000.00, 'RON'),

-- oferte pentru garaje (pentru 08.06.b)
(5, 16, 'N', 120.00, 'EUR'),
(6, 17, 'N', 150.00, 'EUR'),
(1, 18, 'N', 180.00, 'EUR'),
(2, 19, 'N', 200.00, 'EUR'),
(3, 20, 'N', 250.00, 'EUR');

-- procedura stocata (08.08 adaptata pentru MySQL)
DELIMITER //
CREATE PROCEDURE GetOferteByTipSpatiu(IN p_denumire_tip VARCHAR(50))
BEGIN
    SELECT s.id_spatiu, s.adresa, s.zona, s.suprafata, o.pret, o.moneda, o.vanzare,
           CASE o.vanzare 
               WHEN 'D' THEN 'Vânzare' 
               ELSE 'Închiriere' 
           END as tip_oferta
    FROM Spatiu s
    JOIN Tip t ON s.id_tip = t.id_tip
    JOIN Oferta o ON s.id_spatiu = o.id_spatiu
    WHERE t.denumire = p_denumire_tip
    ORDER BY o.pret;
END//
DELIMITER ;

-- trigger 1: modificare pret dupa suprafata (08.09.a adaptat pentru MySQL)
DELIMITER //
CREATE TRIGGER ModificarePretDupaSuprafata
AFTER UPDATE ON Spatiu
FOR EACH ROW
BEGIN
    IF OLD.suprafata != NEW.suprafata AND OLD.suprafata != 0 THEN
        UPDATE Oferta
        SET pret = ROUND(pret * (NEW.suprafata / OLD.suprafata), 2)
        WHERE id_spatiu = NEW.id_spatiu;
    END IF;
END//
DELIMITER ;

-- crearea unei view pentru AgentieRoyal (pentru trigger instead of)
CREATE VIEW Oferte_AgentieRoyal AS
SELECT s.id_spatiu, s.id_tip, t.denumire, t.caracteristici, 
       s.adresa, s.zona, s.suprafata, o.pret, o.vanzare, o.moneda
FROM Agentie a 
JOIN Oferta o ON a.id_agentie = o.id_agentie
JOIN Spatiu s ON o.id_spatiu = s.id_spatiu
JOIN Tip t ON s.id_tip = t.id_tip
WHERE a.nume = 'AgentieRoyal';

-- trigger 2: instead of pentru view (simulare pentru MySQL)
DELIMITER //
CREATE TRIGGER InserareOfertaRoyal
BEFORE INSERT ON Oferta
FOR EACH ROW
BEGIN
    DECLARE v_agentie_royal_id INT;
    
    -- verifica daca agentia este AgentieRoyal
    SELECT id_agentie INTO v_agentie_royal_id 
    FROM Agentie 
    WHERE nume = 'AgentieRoyal' AND id_agentie = NEW.id_agentie;
    
    IF v_agentie_royal_id IS NOT NULL THEN
        -- logica speciala pentru AgentieRoyal
        SET NEW.pret = NEW.pret * 0.95; -- Reducere 5% pentru AgentieRoyal
    END IF;
END//
DELIMITER ;

-- indexuri pentru performanta
CREATE INDEX idx_spatiu_adresa ON Spatiu(adresa);
CREATE INDEX idx_spatiu_zona ON Spatiu(zona);
CREATE INDEX idx_oferta_pret ON Oferta(pret);
CREATE INDEX idx_oferta_vanzare_moneda ON Oferta(vanzare, moneda);
CREATE INDEX idx_tip_denumire ON Tip(denumire);

-- mesaj de confirmare
SELECT 'Baza de date creata cu succes!' as Status;

-- afisare statistici
SELECT 
    (SELECT COUNT(*) FROM Agentie) as Total_Agentii,
    (SELECT COUNT(*) FROM Spatiu) as Total_Spatii,
    (SELECT COUNT(*) FROM Oferta) as Total_Oferte,
    (SELECT COUNT(*) FROM Tip) as Total_Tipuri;

