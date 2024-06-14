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