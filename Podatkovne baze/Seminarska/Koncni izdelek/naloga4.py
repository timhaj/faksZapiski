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

