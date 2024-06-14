
/*Jadralci*/
/*1.Poiščite imena čolnov, ki so jih rezervirali vsi jadralci.*/
SELECT c.ime
FROM coln c INNER JOIN rezervacija r ON(r.cid = c.cid) INNER JOIN jadralec j ON(j.jid = r.jid)
GROUP BY c.ime
HAVING COUNT(DISTINCT j.jid) = (SELECT COUNT(*) 
                                FROM jadralec);

/*2.Preverite, ali so vsi čolni z dolžino nad 35 čevljev iste barve (s kvantifikatorji).*/
SELECT *
FROM coln
WHERE dolzina > 35 AND NOT barva = ALL(
                                    SELECT barva
                                    FROM coln
                                    WHERE dolzina > 35)

/*3.Preverite, ali so vsi čolni z dolžino nad 35 čevljev iste barve (s skupinskimi operatorji).*/
SELECT IF(COUNT(DISTINCT barva)>1,'Ne','Da') AS 'Vsi enaki?'
FROM coln
WHERE dolzina > 35

/*4.Izpišite šifre, imena čolnov in število njihovih rezervacij urejeno v padajočem vrstnem redu.*/
SELECT c.cid, c.ime, COUNT(*) AS stev
FROM coln c INNER JOIN rezervacija r ON(r.cid = c.cid)
GROUP BY c.cid, c.ime
ORDER BY stev DESC;

/*5.Izpišite imena, šifre in število rezervacij  vsakega jadralca. Kdor ni rezerviral ničesar, bo imel 0 rezervacij. Izpis uredite padajoče po številu rezervacij in naraščajoče po imenu jadralca.*/
SELECT j.ime, j.jid, COUNT(r.dan) as stev
FROM jadralec j LEFT JOIN rezervacija r ON(r.jid = j.jid)
GROUP BY j.ime, j.jid
ORDER BY stev DESC, j.ime ASC;


/*6.Izpišite imena in šifre vseh jadralcev, ki so rezervirali nadpovprečno število čolnov.*/
SELECT j.ime, j.jid
FROM jadralec j INNER JOIN rezervacija r ON(r.jid = j.jid)
GROUP BY j.ime, j.jid
HAVING COUNT(*) > (
    SELECT AVG(neki.stev)
    FROM (
        SELECT j.jid, COUNT(*) as stev
        FROM jadralec j INNER JOIN rezervacija r ON(r.jid = j.jid)
        GROUP BY j.jid
    ) AS neki
);

/*Employees*/
/*1.Za vsakega zaposlenega izpišite število različnih oddelkov, na katerih je delal.*/
SELECT e.emp_no, COUNT(DISTINCT d.dept_no)
FROM employees e LEFT JOIN dept_emp d ON(d.emp_no = e.emp_no)
GROUP BY e.emp_no;

/*2.Za vsak oddelek izpišite število zaposlenih. Razvrstite oddelke od tistega z največ do tistega z najmanj zaposlenimi.*/
SELECT d.dept_no, COUNT(DISTINCT de.emp_no) AS stev
FROM dept_emp de RIGHT JOIN departments d ON(d.dept_no = de.dept_no)
GROUP BY d.dept_no
ORDER BY stev DESC;

/*3.Poiščite imena zaposlenih, ki so imeli vsaj tri različne nazive.*/
SELECT e.first_name, e.last_name
FROM employees e INNER JOIN titles t ON(t.emp_no = e.emp_no)
GROUP BY e.first_name, e.last_name
HAVING COUNT(DISTINCT t.title) >= 3;

/*4.Izpišite številke zaposlenih in njihovih prvih plač urejene po padajočem vrstnem redu zneska prve plače.*/
SELECT PrvaPl.emp_no, PrvaPl.salary 
FROM (SELECT emp_no, salary -- vse prve plače
            FROM salaries s
            WHERE from_date <= ALL (SELECT s1.from_date 
                                    FROM salaries s1 
                                    WHERE s1.emp_no=s.emp_no)) AS PrvaPl
ORDER BY PrvaPl.salary desc;

/*5.Ali ima kateri oddelek zaposlenih več žensk, kot moških.*/
select dept_name, dept_no, sum(if(gender="M",1,0)) AS male, sum(if(gender="F",1,0)) AS female
from dept_emp natural join employees natural join departments
group by dept_name, dept_no
having male < female;

/*Travian*/
/*1.Za vsakega igralca izpišite število njegovih naselji.*/
SELECT i.player, COUNT(DISTINCT n.id)
FROM igralec i LEFT JOIN Naselje n ON(n.pid = i.pid)
GROUP BY i.player;

/*2.Za vsako alianso izpišite število njenih igralcev. Razvrstite alianse od tiste z največ do tiste z najmanj igralci.*/
SELECT a.alliance, COUNT(DISTINCT i.aid) as stev
FROM aliansa a LEFT JOIN igralec i ON(i.aid = a.aid)
GROUP BY a.alliance
ORDER BY stev DESC;

/*3.Za vsako alianso izpišite število njenih naselji in skupno populacijo.*/
SELECT a.alliance, COUNT(DISTINCT n.id), SUM(n.population)
FROM aliansa a LEFT JOIN igralec i ON(i.aid = a.aid) INNER JOIN naselje n ON(n.pid = i.pid)
GROUP BY a.alliance;

/*4.Katero pleme je najštevilčnejše. (Glede na skupno populacijo).*/
SELECT p.tid, p.tribe, SUM(n.population)
FROM pleme p INNER JOIN igralec i ON(p.tid = i.tid) INNER JOIN naselje n ON(n.pid = i.pid)
GROUP BY tid, tribe
ORDER BY SUM(n.population) DESC 
LIMIT 1

/*5.Za vsakega igralca izpišite povprečno velikost njegovega naselja(glede na populacijo), vendar samo za tiste igralce, ki imajo vsaj pet naselji*/
SELECT pid, player, AVG(population)
FROM igralec JOIN naselje USING(pid)
GROUP BY pid, player
HAVING COUNT(*) > 4

/*6.Za vsak kvadrant preštejte število naselji na tem kvadrantu.**/
SELECT COUNT(*), SIGN(x), SIGN(y),
CASE
    WHEN SIGN(x) > 0 AND SIGN(y) > 0 THEN 'I kvadrant'
    WHEN SIGN(x) > 0 AND SIGN(y) < 0 THEN 'II kvadrant'
    WHEN SIGN(x) < 0 AND SIGN(y) < 0 THEN 'III kvadrant'
    ELSE 'IV kvadrant'
END AS 'Kvadrant'
FROM naselje
WHERE x != 0 AND y != 0
GROUP BY y > 0, x > 0
ORDER BY 4 ASC;