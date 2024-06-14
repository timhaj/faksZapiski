/*Travian*/
/*Kolikšen je delež igralcev brez alianse (aid = 0)? */
SELECT ((SELECT COUNT(*) FROM igralec WHERE aid = 0)/COUNT(*)) AS delez
FROM igralec;
#ali
SELECT COUNT(*)/(SELECT COUNT(*) FROM igralec) AS delez
FROM igralec
WHERE aid = 0;

/*Ustvarite začasno tabelo StatistikaIgralcev, ki vsebuje imena igralcev, imena njihovih alians ter število igralečevih naselji in skupno število prebivalcev. Če igralec nima alianse, naj se za ime alianse izpiše „Brez alianse“.*/
CREATE TEMPORARY TABLE StatistikaIgralcev(
    pid INTEGER PRIMARY KEY, #po navodilih, primarni ključ ni potreben lahko pa je
    player VARCHAR(100),
    alliance VARCHAR(100),
    st_naselji INTEGER,
    pop INTEGER
)AS
SELECT i.pid, i.player, IF(aid=0,'Brez alianse', a.alliance) AS alliance, COUNT(*) AS st_naselji, SUM(population) AS pop  #ali IF(a.alliance="", 'Brez alianse', a.alliance)
FROM igralec i JOIN aliansa a USING(aid) JOIN naselje n USING(pid)
GROUP BY i.pid, i.player, a.alliance

/*Za vsakega igralca izpiši ime njegovega največjega mesta.*/
SELECT pid, 
        player, 
        (SELECT village
            FROM igralec i JOIN naselje n USING(pid)
            WHERE i.pid = igralec.pid
            ORDER BY population DESC
            LIMIT 1) 
        AS najvecje_naselje
FROM igralec;

/*Začasni tabeli StatistikaIgralcev dodajte atribut z imenom “Največje mesto”. */
ALTER TABLE StatistikaIgralcev ADD COLUMN `Največje mesto` VARCHAR(100);

/*Napolnite stolpec iz prejšnje naloge*! */
UPDATE StatistikaIgralcev SET Najvecje_mesto = (
    SELECT village
            FROM igralec i JOIN naselje n USING(pid)
            WHERE i.pid = StatistikaIgralcev.pid
            ORDER BY population DESC
            LIMIT 1
    ); 

/*Employees*/
/*V tabeli employees ustvarite stolpec years_working, ki bo vseboval vrednost tipa double. Vrednosti ustrezno napolnite.*/
ALTER TABLE employees ADD column years_working double;

UPDATE employees e set years_working = (
    SELECT SUM(LEAST(DATEDIFF(to_date, from_date),DATEDIFF("2002-08-01", from_date))+1)
    FROM dept_emp de
    WHERE de.emp_no = e.emp_no) / 365
WHERE emp_no > 0;

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

/*Jadralci*/
/*Definirajte začasno tabelo PolnoletniJadralci, ki vsebuje samo jadralce polnoletne jadralce, nato dodajte v tabelo jadralca Tineta, starega 18 let, ki ima rating 8. */

CREATE TEMPORARY TABLE PolnoletniJadralci(
  jid INTEGER PRIMARY KEY AUTO_INCREMENT,
  ime VARCHAR(20),
  starost REAL,
  rating INTEGER,
  CHECK( starost >= 18)) -- koristna omejitev glede na ime tabele
AS SELECT jid, ime, starost, rating
FROM jadralec
WHERE starost >= 18;

INSERT INTO PolnoletniJadralci(ime, starost, rating) VALUES ('Tine', 18, 8); -- ker je uporabljen AUTO_INCREMENT, se avtomatsko izbere PK