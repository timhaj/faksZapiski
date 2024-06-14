use vaje 63230099;

/*
select 1; -- izpiše 1
select abc; -- izpiše napako, ker ne pozna tega polja
select "abc"; -- deluje, izpiše abc
select "abc",'def' from coln; -- če želimo niz, potem so obvezni enojni ali dvojni narekovaji
select 1 as polje; -- preimenuje atribut/kolono v "polje"
select 1 as polje from coln; -- enako, le da izpiše tolikokrat kot je zapisov v tabeli coln
select sin(90); -- izpiše 0.8939966636005579, lahko damo poljuben aritmetični izraz
select 1 from coln; -- izpiše 1 tolikokrat, kolikor je vrstic v čolnih
select n from coln; -- enako kot select 1, če je n neko število
-- enako deluje če damo več argumentov 1,2,3
*/


/*
Jadralec
Izpišite imena jadralcev s sodimi ratingi.
Ugotovite, ali imata kakšna dva čolna enako ime.
Izpišite imena jadralcev z lihimi ratingi.
Izpišite imena čolnov daljših od 35 čevljev, ki so jih rezervirali jadralci stari 35 let ali manj.
Za vse jadralce, ki so rezervirali čoln Bavaria ali Sun Odyssey, izpišite jadralčevo ime in datum rezervacije.
Ugotovite, ali imajo vsi jadralci različna imena.
Izpišite imena jadralcev, ki so v koledarskem poletju 2006 rezervirali čoln, katerega ime vsebuje sonce (sun).


Employees:
Izpišite vse priimke zaposlenih, ki vsebujejo ‘ski’. 
Izpišite vse možne oddelke. 
Izpišite vsa imena in priimke zaposlenih, ki imajo vsaj eno leto plačo 70000 ali več.
Izpišite imena in priimke vseh zaposlenih z nazivom 'Senior Staff'. 
Izpišite vse priimke zaposlenih, ki se začnejo z nizom ‘Pea’. 
Za zaposlene iz naloge 5. izpišite še ustrezen naziv. 
Izpišite vse podatke zaposlenih, ki so rojeni med vključno letoma 1950 in 1960 in se njihova imena končajo na samoglasnik ter so bili najeti leta 1990 ali kasneje.


Travian:
Izpišite vse alianse, ki vsebujejo ‘mgp’. 
Izpišite vsa možna plemena. 
Izpišite vsa imena igralcev, ki imajo vsaj eno naselje z populacijo 1000 ali več. 
Izpišite imena vseh naselji igralca Ronin. 
Izpišite vsa imena igralcev, ki se začnejo z nizom ‘moj’. 
Za igralce iz naloge 5. izpišite še ime pripadajočega plemena. 
Izpišite vse podatke naselji, ki so v I. Kvadrantu (x >= 0, y >= 0) in se njihova imena končajo na samoglasnik ter imajo populacijo 750 ali več.
*/


/*Jadralci*/
select ime, rating
from jadralec
where rating mod 2 = 0;

select c1.ime
from coln c1, coln c2
where c1.ime = c2.ime and c1.cid<c2.cid;

select ime, rating
from jadralec
where rating mod 2 = 1;

select c.ime
from coln c inner join rezervacija r on(r.cid = c.cid) inner join jadralec j on(j.jid = r.jid)
where c.dolzina > 35 and j.starost <= 35;

select j.ime, r.dan
from coln c inner join rezervacija r on(r.cid = c.cid) inner join jadralec j on(j.jid = r.jid)
where c.ime in("Sun Odyssey", "Bavaria");

select count(ime)=count(distinct ime) 
from jadralec;    /*Ce vrne 0 imata dva isto ime, ce je pa 1, pa imajo vsi unique*/

select j.ime
from coln c inner join rezervacija r on(r.cid = c.cid) inner join jadralec j on(j.jid = r.jid)
where lower(c.ime) like "%sun%" and extract(year from r.dan) = 2006;

/*Employees*/
select e.last_name
from employees e
where lower(e.last_name) like "%ski%";

select d.dept_name
from departments d;

select e.first_name, e.last_name
from employees e inner join salaries s on(e.emp_no = s.emp_no)
where s.salary >= 70000
group by e.first_name, e.last_name;

select e.first_name, e.last_name
from employees e inner join titles t on(e.emp_no = t.emp_no)
where t.title = "Senior Staff";

select e.last_name
from employees e
where e.last_name like "Pea%";

select e.last_name, t.title
from employees e inner join titles t on(e.emp_no = t.emp_no)
where e.last_name like "Pea%";

select e.*
from employees e
where (extract(year from e.birth_date) between 1950 and 1960) and e.first_name regexp "[aeiou]$" and extract(year from e.hire_date) > 1990
limit 10;

/*Travian*/
select *
from aliansa a
where lower(a.alliance) like "%mgp%";

select p.tribe
from pleme p;

select i.player
from igralec i inner join naselje n on(i.pid = n.pid)
where n.population >= 1000
group by i.player
having count(*) > 1;

select n.village
from igralec i inner join naselje n on(i.pid = n.pid)
where i.player = "Ronin";

select i.player
from igralec i
where lower(i.player) like "moj%";

select i.player, p.tribe
from igralec i inner join pleme p on(i.tid = p.tid)
where lower(i.player) like "moj%";

select *
from naselje n
where n.village regexp "[aeiou]$" and n.x >= 0 and n.y >= 0 and n.population >= 750;
