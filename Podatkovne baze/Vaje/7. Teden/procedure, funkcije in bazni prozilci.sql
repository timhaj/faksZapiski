/*Tabeli MladoletniJadralci dodajte še bazne prožilce za brisanje in spreminjanje vrstic iz tabele jadralci.*/
-- bazni prožilec za DELETE
DELIMITER //
CREATE TRIGGER MladoletniJadralci_Delete
AFTER DELETE ON jadralec
FOR EACH ROW
BEGIN
        -- Pogoja o starosti v tem primeru ni potrebno preverjati
	DELETE FROM MladoletniJadralci WHERE jid = OLD.jid;
END //
DELIMITER ;

-- bazni prožilec za UPDATE
DELIMITER //
CREATE TRIGGER MladoletniJadralci_Update
AFTER UPDATE ON jadralec
FOR EACH ROW
BEGIN
    -- Najprej preverimo novo starost jadralca, če smo ga postarali in ga moramo zato izbrisati iz tabele
    IF NEW.starost >= 18 THEN
		DELETE FROM MladoletniJadralci WHERE jid = OLD.jid; -- Nujno OLD, ker je lahko bila sprememba na jid in starost hkrati
	END IF;
    -- Ali smo jadralca pomlajšali in ga moramo sedaj vstaviti v tabelo?
    IF OLD.starost >= 18 AND NEW.starost < 18 THEN
		INSERT INTO MladoletniJadralci(jid, ime, starost, rating) VALUES (NEW.jid, NEW.ime, NEW.starost, NEW.rating);
    END IF;
    -- Od tukaj naprej so samo spremembe, ki ohranjajo starost OLD in NEW pod 18.
    UPDATE MladoletniJadralci SET jid = NEW.jid, ime = NEW.ime, starost = NEW.starost, rating = NEW.rating WHERE jid = OLD.jid;
END //
DELIMITER ;


/*Napišite proceduro, ki bo izbrisala vsa naselje s populacijo nič in vam v zadnjem parametru “stevilo” vrnila stevilo izbrisanih naselji.*/

DELIMITER //
CREATE PROCEDURE IzbrisiPraznaNaselja(OUT stevilo INTEGER)
BEGIN
    SELECT COUNT(*) INTO stevilo
    FROM naselje n
    WHERE n.population = 0;
    -- Najprej je treba prešteti števlo naselji, ki bo izbrisanih, drugače bi vedno dobili rezulrat 0.
    DELETE FROM naselje WHERE population = 0;
END //
DELIMITER ;

/*Napišite proceduro StatistikaAlianse(ime), ki vrne šifro in ime alianse, imena njenih članov in za vsakega člana število njegovih naselji.*/

DELIMITER //
CREATE PROCEDURE StatistikaAlianse(IN imeAlianse VARCHAR(100))
BEGIN
	SELECT a.aid AS 'Sifra alianse', 
			a.alliance AS 'Ime alianse', 
                        i.player AS 'Ime clana',
                        COUNT(*) AS 'Stevilo naselji'
        FROM aliansa a JOIN igralec i USING(aid) JOIN naselje n USING(pid)
        WHERE a.alliance = imeAlianse
        GROUP BY a.aid, a.alliance, i.player;
END //
DELIMITER ;

/*Napišite funkcijo NajBarva(jid), ki vam za jadralca s šifro jid vrne barvo čolna, ki ga je največkrat rezerviral.  
    - Dodatno:* Če ima več najljubših barv naj vrne vse ločene z vejico! 
    Pomoč: GROUP_CONCAT(barva SEPAROTOR ', ')*/

DELIMITER //
CREATE FUNCTION NajBarva(jid INTEGER) RETURNS VARCHAR(10)
BEGIN 
	DECLARE najbarva VARCHAR(100);
	SELECT c.barva INTO najbarva
	FROM rezervacija r JOIN coln c USING(cid)
	WHERE r.jid = jid
	GROUP BY c.barva
	ORDER BY COUNT(*) DESC
	LIMIT 1;
        RETURN najbarva;
END //
DELIMITER ;

-- Zgornja funkcija vedno vrne natanko eno barvo tudi, če je jadralec na primer rezerviral dvakrat rdeč in dvakrat moder čoln.

•Dodatno:* Če ima več najljubših barv naj vrne vse ločene z vejico!
Pomoč: GROUP_CONCAT(barva SEPAROTOR ‘, ‘)

DELIMITER //
CREATE FUNCTION NajBarva(jid INTEGER) RETURNS VARCHAR(100) -- glede na velikost nizov lahko vrne največ 10 barv
BEGIN 
    DECLARE najbarva VARCHAR(100);
    SELECT GROUP_CONCAT(barva SEPARATOR ', ') INTO najbarva
    FROM (	SELECT c.barva AS barva, COUNT(*) AS st_rez  -- dvakrat isti SELECT stavek
			FROM rezervacija r JOIN coln c USING(cid)        
			WHERE r.jid = jid
			GROUP BY c.barva) AS rezBarve
    WHERE rezBarve.st_rez = (SELECT MAX(st_rez) FROM (	SELECT c.barva AS barva, COUNT(*) AS st_rez
														FROM rezervacija r JOIN coln c USING(cid)
														WHERE r.jid = jid
														GROUP BY c.barva) AS t);
    RETURN najbarva;
END //
DELIMITER ;

/*Tabeli Naselje dodajte bazne prožilce, ki izpiše napako, če vrednosti x in y nista med -400 in 400 ali če je population negativen.
    –Napako sprožite z ukazom: SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = ‘napaka'*/

-- Napake je potrebno preverjati samo pri INSERT in UPDATE

DELIMITER $
CREATE TRIGGER PreveriNaselje_Insert
BEFORE INSERT ON naselje -- Potreben je BEFORE, da prekinemo izvajanje, če je bila napaka
FOR EACH ROW
BEGIN
	IF (NEW.x < -400 OR NEW.x > 400) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Koordinata x ni na območju [-400, 400].";
    END IF;
    IF (NEW.y < -400 OR NEW.y > 400) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Koordinata y ni na območju [-400, 400].";
    END IF;
    IF (NEW.population < 0) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Populacija naselja ne sme biti negativna.";
    END IF;
END $
DELIMITER ;

-- Bazni prožilec za UPDATE je identičen tistemu za INSERT
-- V nekaterih SQL jezikih bi lahko napisali tudi "BEFORE UPDATE OR INSERT ON naselje"
-- Vendar MariaDB tega ne podpira

DELIMITER $
CREATE TRIGGER PreveriNaselje_Update
BEFORE UPDATE ON naselje -- Potreben je BEFORE, da prekinemo izvajanje, če je bila napaka
FOR EACH ROW
BEGIN
	IF (NEW.x < -400 OR NEW.x > 400) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Koordinata x ni na območju [-400, 400].";
    END IF;
    IF (NEW.y < -400 OR NEW.y > 400) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Koordinata y ni na območju [-400, 400].";
    END IF;
    IF (NEW.population < 0) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = "Populacija naselja ne sme biti negativna.";
    END IF;
END $
DELIMITER ;

/*Napišite proceduro premesti(emp_no, dept_no, datum), ki bo premestila zaposlenega z trenutnega delovnega mesta na nov oddelek. Parameter datum je začetek dela na tem odelku. */

DELIMITER //
CREATE PROCEDURE premesti(IN emp_no INTEGER, IN dept_no CHAR(4), IN datum DATE)
BEGIN
    -- zaklkjuči delo
    UPDATE dept_emp de
    SET to_date = datum
    WHERE de.emp_no = emp_no AND to_date = DATE("9999-01-01"); -- zamenjamo samo zadnji vnost
    -- odpri novo delovno mesto
    INSERT INTO dept_emp VALUES(emp_no, dept_no, datum, DATE("9999-01-01"));
END //
DELIMITER ;

/*Napišite funkcijo izplacila(emp_no, start_date, end_date), ki vrne vsoto vseh izplacil v tem obdobju. Predpostavite, da je izplacilo izdano točno 8 dni po to_date v tabeli salaries.*/

DELIMITER //
CREATE OR REPLACE FUNCTION izplacila(emp_no INTEGER, start_date DATE, end_date DATE) REAL
BEGIN
    DECLARE vsota REAL;
    SELECT SUM(salary) INTO vsota;
    FROM salaries s
    WHERE s.emp_no = emp_no;
    RETURN vsota;
END //
DELIMITER ;

/*Napišite bazni prožilec, ki prepreči vnos v tabelo salaries, če se izplačilo že prekriva z obstoječimi datumi. From_date in to_date sta lahko enaka.*/

DELIMITER //
CREATE TRIGGER check_salary_date
BEFORE INSERT ON salaries
FOR EACH ROW
BEGIN
    IF EXISTS(    SELECT *
                FROM salaries
                WHERE emp_no = NEW.emp_no AND(
                    (NEW.from_date < to_date AND NEW.from_date > from_date) OR
                    (NEW.to_date < to_date AND NEW.to_date > from_date))
        ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Plačilo za to obdobje že obstaja.';
    END IF;
END //
DELIMITER ;

/*Poiščite imena in priimke zaposlenih, ki so delali na vsaj dveh oddelkih, vendar so vedno imeli samo en naziv.*/

WITH st_oddelkov AS
(
  SELECT emp_no, COUNT(*) AS st_od
  FROM employees JOIN dept_emp USING(emp_no)
  GROUP BY emp_no
), st_nazivov AS
(
  SELECT emp_no, COUNT(*) AS st_na
  FROM employees JOIN titles USING(emp_no)
  GROUP BY emp_no
)
SELECT emp_no, first_name, last_name, st_od, st_na
FROM employees JOIN st_nazivov USING(emp_no) JOIN st_oddelkov USING(emp_no)
WHERE st_od >= 2 AND st_na = 1;

/*Napišite poizvedbo (CTE), ki vrne spodnji rezultat. Ta prikazuje, koliko je vseh zaposlenih na enem oddelku (skupaj_oddelek), koliko je zaposlenih v celotnem podjetju z določenim nazivom (po_nazivih) in koliko je zaposlenih na določenem oddelku z določenim nazivom (po_nazivih_in_oddelkih).*/

WITH oddelki_stats AS
(
  SELECT dept_no, dept_name, COUNT(*) AS skupaj_oddelek
  FROM departments JOIN dept_emp USING(dept_no)
  GROUP BY dept_no, dept_name
),
nazivi_stats AS
(
  SELECT title, COUNT(*) AS po_nazivih
  FROM titles
  GROUP BY title
), nazivi_in_oddelki_stats AS
(
  SELECT title, dept_no, COUNT(*) AS po_nazivih_in_oddelkih
  FROM titles JOIN employees USING(emp_no) JOIN dept_emp USING(emp_no)
  GROUP BY title, dept_no
)
SELECT dept_no, dept_name, skupaj_oddelek, title AS naziv, po_nazivih, po_nazivih_in_oddelkih
FROM oddelki_stats JOIN nazivi_in_oddelki_stats USING(dept_no) JOIN nazivi_stats USING(title)
ORDER BY dept_no ASC, po_nazivih DESC;