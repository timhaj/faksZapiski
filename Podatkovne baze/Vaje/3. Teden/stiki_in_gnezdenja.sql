/*Ugotovite ali imata kakšna dva čolna enako ime.*/
SELECT c.ime, c2.ime
FROM coln c, coln c2
WHERE c.ime = c2.ime AND c.cid > c2.cid;

/*Z gnezdenjem*/
SELECT c.ime
FROM coln c
WHERE c.cid IN(
    SELECT c.cid
    FROM coln c2
    WHERE c.ime = c2.ime and c.cid<c2.cid
);

/*Izpišite šifre čolnov daljših od 35 čevljev, ki so jih rezervirali jadralci mlajši od 40 let.*/
SELECT c.cid
FROM coln c
WHERE c.dolzina >= 35 and c.cid IN(
    SELECT r.cid
    FROM rezervacija r
    WHERE r.jid IN(
        SELECT j.jid
        FROM jadralec j
        WHERE j.starost < 40
    )
);

/*Za vse jadralce izpišite podatke o njihovih rezervacijah. Za tiste, ki še niso nič rezervirali, naj bodo polja o rezervacijah prazna. Rešite z uporabo zunanjega stika in izpis uredite po jid!*/
SELECT *
FROM jadralec j LEFT OUTER JOIN rezervacija r ON(r.jid = j.jid)
ORDER BY j.jid;

/*Za vse jadralce izpišite podatke o njihovih rezervacijah. Za tiste, ki še niso nič rezervirali, naj bodo polja o rezervacijah prazna. Rešite brez uporabe zunanjega stika in izpis uredite po jid!*/
SELECT *
FROM jadralec j
WHERE j.jid IN(
    SELECT r.jid
    FROM rezervacija r
)
ORDER BY j.jid;

/*Preverite, ali pri prejšnji nalogi dobite enak rezultat, kot pri uporabi zunanjega stika.*/
/*Ne, tukaj dobimo unique jadralce + brez tistih, ki niso nič rezervirali*/

/*Poiščite zaposlene, ki nikoli niso delali kot vodje oddelkov.*/
SELECT * 
FROM employees e
WHERE e.emp_no NOT IN(
    SELECT d.emp_no
    FROM dept_manager d
);

/*Poiščite šifre zaposlenih, ki so dobili izplačilo za manj kot tri dni in tudi za več kot 364 dni.*/
SELECT e.emp_no
FROM employees e
WHERE e.emp_no IN(
    SELECT s.emp_no
    FROM salaries s
    WHERE DATEDIFF(s.from_date, s.to_date) < 3
) AND e.emp_no IN(
    SELECT s.emp_no
    FROM salaries s
    WHERE DATEDIFF(s.from_date, s.to_date) > 364
);

/*Poiščite zaposlene, ki so napredovali iz naziva „Assistant Engineer“ na „Engineer“.*/
SELECT *
FROM employees e
WHERE e.emp_no IN(
    SELECT t.emp_no
    FROM titles t
    WHERE t.title = "Assistant Engineer" and t.emp_no IN(
        SELECT t.emp_no
        FROM titles t2
        WHERE t2.title = "Engineer" and t.to_date = t2.from_date
    )
);

/*Poiščite zaposlene, ki so se zaposlili, ko so bili stari 20 ali manj, in tiste zaposlene, ki na enem odelku delajo vsaj 15 let.*/
SELECT *
FROM employees e
WHERE DATEDIFF(e.hire_date, e.birth_date) <= 20*365 or e.emp_no IN(
    SELECT d.emp_no
    FROM dept_emp d
    WHERE DATEDIFF(d.to_date, d.from_date) >= 15*365
);

// FLOOR(DATEDIFF(hire_date, birth_date)/365) <= 20

/*Izpišite imena vseh igralcev, ki pripadajo naslednjim aliansam: GVERILA, IPERIT, *!NFS!*, PaX™, MGP I, -MGP-, sladoled, CYANIDE, *!NFS!*2, SVN, MGP, ANTHRAX, Uporniki, MGP A, GVERIL4, LEGEND, LUDI DDT, T•W in G.R.™*/
SELECT i.player
FROM igralec i
WHERE i.aid IN(
    SELECT a.aid
    FROM aliansa a
    WHERE a.alliance IN("GVERILA", "IPERIT", "*!NFS!*", "PaX™", "MGP I", "-MGP-", "sladoled", "CYANIDE", "*!NFS!*2", "SVN", "MGP", "ANTHRAX", "Uporniki", "MGP A", "GVERIL4", "LEGEND", "LUDI DDT", "T•W", "G.R.™")
);

/*Izpišite imena igralcev, ki imajo vsaj eno naselje od točke (0,0) oddaljeno za več kot 20, vendar manj kot 50 enot.*/
SELECT i.player
FROM igralec i
WHERE i.pid IN(
    SELECT n.pid
    FROM naselje n
    WHERE abs(n.x) + abs(n.y) BETWEEN 20 AND 50
);

/*Izpišite imena igralcev, ki imajo v vsakem kvadrantu vsaj eno naselje.*/
SELECT i.player
FROM igralec i
WHERE i.pid IN(
    SELECT n.pid
    FROM naselje n
    WHERE n.x > 0 and n.y > 0
) AND i.pid IN(
    SELECT n.pid
    FROM naselje n
    WHERE n.x > 0 and n.y < 0
) AND i.pid IN(
    SELECT n.pid
    FROM naselje n
    WHERE n.x < 0 and n.y > 0
) AND i.pid IN(
    SELECT n.pid
    FROM naselje n
    WHERE n.x < 0 and n.y < 0
);