/*Ponovitev in delo z datumi*/
/*Poišči vse evklidske in manhattan razdalje med naselji igralca “hinko”.*/
SELECT i1.player, n1.village, n2.village,
	sqrt((n1.x - n2.x)*(n1.x - n2.x) + (n1.y - n2.y)*(n1.y - n2.y)) AS evlkidska, #izračun evklidske razdalje
        abs(n1.x - n2.x) + abs(n1.y - n2.y) AS manhattan #izračun manhattan razdalje
FROM igralec i1, igralec i2, naselje n1, naselje n2
WHERE i1.player = "hinko" #izberemo samo željenega igralca 
	 AND i2.player = "hinko" #ravno tako iz druge tabele
         AND i1.pid = n1.pid #z obema tabelama naredimo stik
         AND i2.pid = n2.pid
         AND n1.vid > n2.vid; #izločimo dvojnike pri izpisu

/*Izpiši imena mesecev v katerih so bile opravljene rezervacije.*/
SELECT DISTINCT MONTHNAME(dan)
FROM rezervacija;

/*Poišči imena in njihova pripadajoča plemena vseh igralev, ki pripadajo aliansam, ki vsebujejo znak ‘™’.*/
SELECT i.player, p.tribe
FROM igralec i INNER JOIN pleme p ON(p.tid = i.tid) INNER JOIN aliansa a ON(a.aid = i.aid)
WHERE a.alliance LIKE "%™%";

/*Poišči vsa imena igralcev, ki ne vsebujejo alfa-numeričnih znakov.*/
SELECT i.player
FROM igralec i
WHERE i.player REGEXP "^[^a-z0-9A-Z]+$";

/*Ustvari navidezni atribut “Rangiran”, ki vsebuje vrednost ‘Da’, če ima jadralec podan rang, sicer ‘Ne’.*/
SELECT j.*, IF(rating IS NOT NULL, "Da", "Ne") as Rangiran
FROM jadralec j;

/*Z IF stavkom polepšajte izpis pri 2 in 6 nalogi prejšnjega tedna (jadralci).*/

/*Poišči šifre (emp_no) vseh, ki so na vsaj enem odelku bili zaposleni manj kot 5 let in na njem ne delajo več.*/
SELECT emp_no
FROM dept_emp
WHERE DATE_SUB(DATE(to_date), INTERVAL 5 YEAR) <= DATE(from_date);

/*Poišči plače oziroma honorarje, ki so bili izplačani za največ sedem dni.*/
SELECT *
FROM salaries
WHERE DATEDIFF(to_date, from_date) <= 7;

/*Poišči ime in priimek zaposlenih, ki so bili vodje odelkov manj kot 3 leta.*/
SELECT e.first_name, e.last_name, d.from_date, d.to_date
FROM employees e INNER JOIN dept_manager d ON(d.emp_no = e.emp_no)
WHERE DATE_ADD(d.from_date, INTERVAL 3 YEAR) > d.to_date;

/*Poišči ime, priimek in ime odelka samo trenutnih vodji odelkov.*/
SELECT e.first_name, e.last_name, dep.dept_name
FROM employees e INNER JOIN dept_manager d ON(d.emp_no = e.emp_no) INNER JOIN departments dep ON(dep.dept_no = d.dept_no)
WHERE year(d.to_date) = 9999;

/*Poišči alianse, ki imajo vsaj eno naselje na območju (-50, -50) do (50, 50).*/
SELECT DISTINCT a.alliance
FROM igralec i INNER JOIN aliansa a ON(a.aid = i.aid) INNER JOIN naselje n ON(n.pid = i.pid)
WHERE n.x BETWEEN -50 and 50 AND n.y BETWEEN -50 and 50;

/*Poišči alianse, ki imajo naselja izven območja (-50, -50) do (50, 50). (Brez gnezdenja ali agregacijskih operacij)*/
SELECT DISTINCT a.alliance
FROM igralec i INNER JOIN aliansa a ON(a.aid = i.aid) INNER JOIN naselje n ON(n.pid = i.pid)
WHERE n.x NOT BETWEEN -50 and 50 AND n.y NOT BETWEEN -50 and 50;

/*Poišči vse pare naselji, katerim se populacija razlikuje za največ 100.*/
SELECT n.village, n2.village, n.population, n2.population
FROM naselje n, naselje n2
WHERE abs(n.population - n2.population) <= 100 and n.vid < n2.vid;

/*Poišči vse pare jadralcev, ki imajo enak rang.*/
SELECT j.ime, j2.ime
FROM jadralec j, jadralec j2
WHERE j.rating = j2.rating and j.jid < j2.jid;

/*Poišči vse pare jadralcev, ki še nimajo ranga.*/
SELECT j.ime, j2.ime
FROM jadralec j, jadralec j2
WHERE j.rating IS NULL and j2.rating IS NULL and j.jid < j2.jid;

/*Poišči pare igralcev in njihovo pleme alianse “Nwm”, ki pripadajo istemu plemenu.*/
SELECT i.player, i2.player, p.tribe
FROM igralec i, igralec i2, aliansa a, pleme p
WHERE a.alliance = "Nwm" and i.aid = a.aid and i2.aid = a.aid and i.tid = i2.tid and i.tid = p.tid and i2.tid = p.tid and i.pid < i2.pid;

/*Relacijska algebra*/
/*Poišči šifre vseh Henrikov (ime jadralca).*/
SELECT j.jid
FROM jadralec j
WHERE j.ime = "Henrik";

/*Poišči imena vseh čolnov, daljših od 20 čevljev.*/
SELECT c.ime
FROM coln c
WHERE c.dolzina >= 20;

/*Izpiši pare imen (jadralec, čoln).*/
SELECT j.ime, c.ime
FROM jadralec j, coln c;

/*Izpišite šifre čolnov z nepopolnimi (manjkajočimi) podatki.*/
SELECT c.cid
FROM coln c
WHERE c.ime IS NULL or c.dolzina IS NULL or c.barva IS NULL;

/*Izpiši imena vseh doslej rezerviranih čolnov.*/
SELECT DISTINCT c.ime
FROM coln c INNER JOIN rezervacija r ON(c.cid = r.cid);

/*Kateri izmed jadralcev še ni rezerviral nobenega čolna? (Z uporabo jezika SQL še ne znamo!)*/
SELECT j.ime
FROM jadralec j LEFT JOIN rezervacija r ON(r.jid = j.jid)
GROUP BY j.ime
HAVING COUNT(r.jid) = 0;

/*Poišči imena zaposlenih, ki so bili s svojim prvim dnem dela zaposleni kot vodja oddelka.*/
SELECT e.first_name
FROM employees e INNER JOIN dept_manager d ON(d.emp_no = e.emp_no)
WHERE d.from_date = e.hire_date;

/*Poišči imena in priimke zaposlenih, ki nikoli niso delali kot vodje oddelkov.*/
SELECT e.first_name, e.last_name
FROM employees e LEFT JOIN dept_manager d ON(d.emp_no = e.emp_no)
GROUP BY e.first_name, e.last_name
HAVING COUNT(d.emp_no) = 0;

/*Poišči zaposlene z enakim imenom in priimkom.*/
SELECT e.first_name, e.last_name
FROM employees e
WHERE e.emp_no IN(
    SELECT e.emp_no
    FROM employees e2
    WHERE e2.first_name = e.first_name AND e2.last_name = e.last_name AND e2.emp_no < e.emp_no
);

/*Poišči ime, priimek in ime odelka samo trenutnih vodji odelkov.*/
SELECT e.first_name, e.last_name, dep.dept_name
FROM employees e INNER JOIN dept_manager d ON(d.emp_no = e.emp_no) INNER JOIN departments dep ON(dep.dept_no = d.dept_no)
WHERE year(d.to_date) = 9999;

/*Poiščite plače za vodje oddelkov in njihove nazive.*/
SELECT e.first_name, e.last_name, s.salary, t.title
FROM employees e INNER JOIN dept_manager d ON(d.emp_no = e.emp_no) INNER JOIN salaries s ON(s.emp_no = e.emp_no) INNER JOIN titles t ON(t.emp_no = e.emp_no)
WHERE year(d.to_date) = 9999;

/*Izpišite šifre naselji, ki pripadajo igralcu „matija“.*/
SELECT n.vid
FROM naselje n INNER JOIN igralec i ON(i.pid = n.pid)
WHERE lower(i.player) LIKE "%matija%"; /*nisem cisto preprican, ce je ime dejansko matija, ali samo da vsebuje "matija"*/

/*Izpišite vsa imena naselji, ki se začnejo ali končajo s cifro.*/
SELECT n.village
FROM naselje n
WHERE n.village REGEXP "^[0-9]" OR n.village REGEXP "[0-9]$";

/*Izpišite šifre in imena igralcev, ki pripadajo aliansi „sladoled“ ali „ANIMALS“. Rešite tudi z unijo.*/
SELECT i.pid, i.player
FROM igralec i INNER JOIN aliansa a ON(a.aid = i.aid)
WHERE lower(a.alliance) = "sladoled"
UNION 
SELECT i.pid, i.player
FROM igralec i INNER JOIN aliansa a ON(a.aid = i.aid)
WHERE a.alliance = "ANIMALS";

/*Izpišite imena naselji in število prebivalcev naselji, ki niso v tretjem kvadrantu.*/
SELECT n.village, n.population
FROM naselje n
WHERE (n.x >= 0 and n.y >= 0) or (n.x >= 0 and n.y <= 0) or (n.x <= 0 and n.y >= 0);

/*Izpišite imena igralcev brez alianse.*/
SELECT *
FROM igralec i
WHERE i.aid = 0;

/*Izpišite imena igralcev, ki ne pripadajo plemenom, ki se začnejo na črko „N“.*/
SELECT i.player
FROM igralec i INNER JOIN pleme p ON(p.tid = i.tid)
WHERE lower(p.tribe) NOT LIKE "n%";