import pyodbc

conString = "DSN=DOMA" #Po potrebi spremenite
c = pyodbc.connect(conString)

cur1 = c.cursor() #curzor za poizvedbe in inicializacijo
cur2 = c.cursor() #curzor za update

try:
    cur1.execute("DROP TABLE optimal")
except pyodbc.DatabaseError:
    pass

cur1.execute("CREATE TABLE optimal ("
             "jid integer PRIMARY KEY,"
             "cid integer,"
             "skladnost integer,"
             "CHECK (skladnost <= 10 AND skladnost >= 0)"
             ")")
c.commit()

cur1.execute("SELECT * FROM coln")
colni = cur1.fetchall() #Colne shranimo, da lahko cez njih veckat iteriramo

cur1.execute("SELECT * FROM jadralec")

for j in cur1:
    print("Posodabljam jadralca {}".format(j.jid))
    najSkladnost = -1
    najCid = 0
    for r in colni:
        skladnost = (j.jid + r.cid) % 11
        if skladnost > najSkladnost:
            najSkladnost = skladnost
            najCid = r.cid
    cur2.execute("INSERT INTO optimal VALUES(?, ?, ?)", j.jid, najCid, najSkladnost)

c.commit()

#Test
cur1.execute("SELECT * FROM optimal")
print(cur1.description)
for r in cur1:
    print(r)
    
c.close()