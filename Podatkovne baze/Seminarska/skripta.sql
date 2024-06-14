/*1*/

CREATE TABLE pleme(
    tid int,
    tribe varchar(100),
    PRIMARY KEY(tid)
);

CREATE TABLE aliansa(
    aid int,
    alliance varchar(100),
    PRIMARY KEY(aid)
);

CREATE TABLE igralec(
    pid int,
    player varchar(100),
    tid int,
    aid int,
    FOREIGN KEY(tid) REFERENCES pleme(tid) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY(aid) REFERENCES aliansa(aid) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY(pid)
);

CREATE TABLE naselje(
    id int,
    x int,
    y int,
    vid int,
    village varchar(100),
    population int,
    pid int,
    FOREIGN KEY(pid) REFERENCES igralec(pid) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO pleme VALUES
(1,'Rimljani'),
(2,'Tevtoni'),
(3,'Galci'),
(4,'Narava'),
(5,'Natarji');

INSERT INTO aliansa (SELECT DISTINCT aid, alliance FROM x_world where aid != 0);
INSERT INTO igralec (SELECT DISTINCT pid, player, tid, aid FROM x_world WHERE tid NOT IN(6,7) AND aid != 0); /*V navodilih piše samo od 1-5, 6 pa 7 damo ven*/
INSERT INTO naselje (SELECT DISTINCT id, x, y, vid, village, population, pid FROM x_world WHERE tid NOT IN(6,7) AND aid != 0); /*enak razlog kot zgoraj (drugače, če navodilo to ni zahtevalo, preprosto damo WHERE pogoj pri obeh poizvedbah ven, ter dodamo pleme 6 pa 7 v tabelo pleme)*/

/*2*/
/*a*/
CREATE VIEW x_view AS(
    SELECT n.id, n.x, n.y, i.tid, n.vid, n.village, i.pid, i.player, a.aid, a.alliance, n.population 
    FROM igralec i inner join pleme p on(p.tid=i.tid) inner join aliansa a on(a.aid=i.aid) inner join naselje n on(n.pid=i.pid)
);

/*b*/
SELECT IF((SELECT COUNT(*) FROM x_world) = (SELECT COUNT(*) FROM x_view),"Sta","Nista") AS "Ali sta tabeli enaki?";
/*vemo, da sta strukturno enaki in da nekatera vsebina je enaka (za plemena od 1-5), zato samo preverimo, če se razlikujeta v številu vrstic*/

/*c*/
CREATE TABLE top5 AS(
    SELECT a.alliance, COUNT(*) AS SteviloNaselij
    FROM aliansa a inner join igralec i on(i.aid=a.aid) inner join naselje n on(n.pid=i.pid)
    GROUP BY a.alliance
    ORDER BY SteviloNaselij DESC
    LIMIT 5
);

DELIMITER //
CREATE TRIGGER posodobiTop5_Insert
AFTER INSERT ON naselje
FOR EACH ROW
BEGIN
    DELETE FROM top5;
    INSERT INTO top5 (
        SELECT a.alliance, COUNT(*) AS SteviloNaselij
        FROM aliansa a inner join igralec i on(i.aid=a.aid) inner join naselje n on(n.pid=i.pid)
        GROUP BY a.alliance
        ORDER BY SteviloNaselij DESC
        LIMIT 5
    );
END //
DELIMITER ;

/*predvidevam da ste hoteli samo za INSERT, vendar za UPDATE pa DELETE ne škodi če dodam, ker je ista logika*/

DELIMITER //
CREATE TRIGGER posodobiTop5_Update
AFTER UPDATE ON naselje
FOR EACH ROW
BEGIN
    DELETE FROM top5;
    INSERT INTO top5 (
        SELECT a.alliance, COUNT(*) AS SteviloNaselij
        FROM aliansa a inner join igralec i on(i.aid=a.aid) inner join naselje n on(n.pid=i.pid)
        GROUP BY a.alliance
        ORDER BY SteviloNaselij DESC
        LIMIT 5
    );
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER posodobiTop5_Delete
AFTER DELETE ON naselje
FOR EACH ROW
BEGIN
    DELETE FROM top5;
    INSERT INTO top5 (
        SELECT a.alliance, COUNT(*) AS SteviloNaselij
        FROM aliansa a inner join igralec i on(i.aid=a.aid) inner join naselje n on(n.pid=i.pid)
        GROUP BY a.alliance
        ORDER BY SteviloNaselij DESC
        LIMIT 5
    );
END //
DELIMITER ;

/*3*/
/*a*/
SELECT i.player AS "Najvecje naselje ima:"
FROM igralec i INNER JOIN naselje n ON(n.pid=i.pid)
WHERE n.vid IN(
    SELECT nas.vid
    FROM (
        SELECT vid, MAX(population)
        FROM naselje
    ) AS nas
);

/*b*/
SELECT COUNT(*) AS "Stevilo igralcev z nadpovprecnim naseljem:"
FROM igralec
WHERE pid IN(
    SELECT i.pid
    FROM igralec i INNER JOIN naselje n ON(i.pid=n.pid)
    GROUP BY i.pid
    HAVING SUM(n.population) >= (SELECT AVG(population) FROM naselje)
);

/*c*/
SELECT n.*
FROM igralec i INNER JOIN naselje n ON(n.pid=i.pid)
WHERE i.aid = 0
ORDER BY n.x DESC, n.y DESC;

/*d*/
SELECT p.tribe AS "Najstevilcnejse pleme je:"
FROM pleme p INNER JOIN igralec i ON(i.tid=p.tid) INNER JOIN naselje n ON(n.pid=i.pid)
GROUP BY p.tribe
HAVING SUM(n.population) = (
    SELECT plem.skupno
    FROM (
        SELECT p.tid, SUM(n.population) AS skupno
        FROM pleme p INNER JOIN igralec i ON(i.tid=p.tid) INNER JOIN naselje n ON(n.pid=i.pid)
        GROUP BY p.tid
        ORDER BY SUM(n.population) DESC
        LIMIT 1
    ) AS plem    
);
/*v primeru, če sta dva plemena z isto populacijo, vrne oba (detajli :) )*/

/*e*/
SELECT COUNT(*)
FROM aliansa
WHERE aid IN(
    SELECT a.aid
    FROM aliansa a INNER JOIN igralec i ON(a.aid=i.aid) INNER JOIN naselje n ON(n.pid=i.pid)
    GROUP BY a.aid
    HAVING SUM(n.population) >= (
        SELECT AVG(tab.skupno)
        FROM (
            SELECT a.aid, SUM(n.population) as skupno
            FROM aliansa a INNER JOIN igralec i ON(a.aid=i.aid) INNER JOIN naselje n ON(n.pid=i.pid)
            GROUP BY a.aid
        ) AS tab        
    ) 
);

/*g*/
DELIMITER //
CREATE PROCEDURE populacijaNaRazdalji(IN x INTEGER, IN y INTEGER, IN razdalja INTEGER, OUT populacija INTEGER)
BEGIN
    /*A ni malce brezveze preverjat ce je izven obmocja? Ker ce ne obstaja bo vrnil 0?*/
    IF (x NOT BETWEEN -400 AND 400) OR (y NOT BETWEEN -400 AND 400) THEN
        SIGNAL SQLSTATE '42000' SET MESSAGE_TEXT = 'Error: x in y morata imeti zalogo vrednosti: [-400, 400]';
    ELSE
        SELECT SUM(n.population) INTO populacija
        FROM naselje n
        WHERE n.x BETWEEN x-razdalja AND x+razdalja AND n.y BETWEEN y-razdalja AND y+razdalja;
    END IF;
END //
DELIMITER ;
CALL populacijaNaRazdalji(5,5,10,@pop);
SELECT @pop;

/*h*/
SELECT DISTINCT i.player
FROM igralec i
WHERE i.pid IN(
    SELECT i.pid
    FROM igralec i INNER JOIN naselje n ON(n.pid=i.pid)
    WHERE n.x BETWEEN 100 AND 200 AND n.y BETWEEN 0 AND 100
) AND i.pid NOT IN(
    SELECT i.pid
    FROM igralec i INNER JOIN naselje n ON(n.pid=i.pid)
    WHERE n.x NOT BETWEEN 100 AND 200 AND n.y NOT BETWEEN 0 AND 100
);

/*i*/
/*ni cisto jasno kaj naj poizvedba tocno vrne*/
SELECT n.*
FROM naselje n
WHERE n.population = 1000;

CREATE INDEX poPopulaciji ON naselje(population);

/*j*/
SELECT i.player
FROM igralec i INNER JOIN naselje n ON(n.pid=i.pid)
WHERE n.population <= (
    SELECT povp * 0.03
    FROM (
        SELECT i2.pid AS igralc, AVG(n2.population) AS povp
        FROM igralec i2 INNER JOIN naselje n2 ON(n2.pid=i2.pid)
        GROUP BY igralc
    ) AS povpIgralca
    WHERE igralc = i.pid
);

/*4*/
import pyodbc

ConnectionString = 'DSN=FRI;UID=pb63230099;PWD=63230099'
c = pyodbc.connect(ConnectionString)
cursor = c.cursor()
cursor2 = c.cursor()
cursor.execute('''
		SELECT a.aid
		FROM aliansa a inner join igralec i on(i.aid=a.aid) inner join naselje n on(n.pid=i.pid)
		GROUP BY a.aid
		ORDER BY SUM(n.population) DESC
		LIMIT 1''')
aliansa = [x for x in cursor][0][0]
cursor.execute("CREATE TABLE gostotaPopulacije(gostota float, x int, y int)")
cursor.execute("CREATE TABLE gostotaAlianse(gostota float, x int, y int)")
c.commit()
i = -400
j = -400
# od -400 do -391 je 10 obmocij, med -400 pa -391 pa je 11...
while(i <= 400):
    while(j <= 400):
        cursor.execute("INSERT INTO gostotaPopulacije (SELECT SUM(n.population)/100 AS gostota, %s AS x, %s AS y FROM naselje n WHERE n.x BETWEEN %d AND %d AND n.y BETWEEN %d AND %d)" % (str(i),str(j),i,i+9,j,j+9))
        cursor2.execute("INSERT INTO gostotaAlianse (SELECT SUM(n.population)/100 AS gostota, %s AS x, %s AS y FROM naselje n inner join igralec i on(i.pid=n.pid) inner join aliansa a on(a.aid=i.aid) WHERE n.x BETWEEN %d AND %d AND n.y BETWEEN %d AND %d AND a.aid=%d)" % (str(i), str(j), i,i+9,j,j+9,aliansa))
        j += 10
    i += 10
    j = -400
c.commit()

/*5*/

