/*Travian*/
/*Po tabeli Naselje pogosto preiskujemo po atributih village in population posamezno, ter po (x,y) skupaj. Kreirajte ustrezne indekse glede na besedilo!*/
CREATE INDEX po_imenih ON naselje (village) ;
CREATE INDEX po_populacijo ON naselje (population) ;
CREATE INDEX po_koordinatah ON naselje (x, y) ;

/*Employees*/
/* tabeli employees ustvarite stolpec years_working, ki bo vseboval vrednost tipa double. Vrednosti ustrezno napolnite.*/
ALTER TABLE employees ADD column years_working double;

UPDATE employees e set years_working = (
    SELECT SUM(LEAST(DATEDIFF(to_date, from_date),DATEDIFF("2002-08-01", from_date))+1)
    FROM dept_emp de
    WHERE de.emp_no = e.emp_no) / 365
WHERE emp_no > 0;

/*Ustvarite pogled place, ki bo vsebovala emp_no in atributa salary_per_day in salary_per_week za vse vrstice iz tabele salaries.*/
CREATE OR REPLACE VIEW place AS
SELECT emp_no,  salary/LEAST(DATEDIFF(to_date, from_date),DATEDIFF("2002-08-01", from_date)+1) AS salary_per_day, salary/LEAST(DATEDIFF(to_date, from_date),DATEDIFF("2002-08-01", from_date)+1)*7 AS salary_per_week  FROM salaries;

/*Ustvarite pogled place2, ki bo vsebovala emp_no, skupno vsoto izplačil vsaki osebi (total_pay) in njegovo povprečno tedensko plačo (weekly_pay).*/
CREATE OR REPLACE VIEW place2 AS
SELECT emp_no, SUM(salary) AS total_pay, SUM(salary)/SUM(LEAST(DATEDIFF(to_date, from_date),DATEDIFF("2002-08-01", from_date))+1) AS weekly_pay
FROM salaries
GROUP BY emp_no;

/*Jadralci*/
/*Definirajte začasno tabelo PolnoletniJadralci, ki vsebuje samo jadralce polnoletne jadralce, nato dodajte v tabelo jadralca Tineta, starega 18 let, ki ima rating 8.*/
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

/*Definirajte pogled StatistikaColnov, ki bo za vsak čoln izpisal osnovne podatke (šifra, ime, dolžina), število rezervacij,  število različnih jadralcev, ki so ga rezervirali in povprečni rating jadralcev, ki so ga rezervirali.*/
CREATE VIEW StatistikaColnov(cid, ime, dolzina, st_rez, st_jad, povp_rating) AS
SELECT c.cid, c.ime, c.dolzina, COUNT(*) AS st_rez, COUNT(DISTINCT j.jid) AS st_jad, AVG(j.rating) AS povp_rating
FROM coln c JOIN rezervacija r USING(cid) JOIN jadralec j USING(jid)
GROUP BY c.cid, c.ime, c.dolzina;

/*Definirajte pogleed StatistikaJadralcev, ki bo za vsakega jadralca poleg njegovih podatkov (šifra in ime) vseboval tudi število rezervacij čolnov, povprečno dolžino in prevladujočo barvo* rezerviranih čolnov.*/
CREATE VIEW StatistikaJadralcev(jid, ime, st_rez, povp_dol, naj_barva) AS
SELECT j.jid, j.ime, COUNT(*) AS st_rez, AVG(dolzina) AS povp_dol, 
    (SELECT barva
        FROM coln c2 JOIN rezervacija r2 USING(cid)
        WHERE r2.jid = j.jid
        GROUP BY barva
        ORDER BY COUNT(*) DESC -- če ima jadralec več najljubših barv izpise samo eno
        LIMIT 1)
    AS naj_barva
FROM jadralec j JOIN rezervacija r USING(jid) JOIN coln c USING(cid)
GROUP BY j.jid, j.ime;