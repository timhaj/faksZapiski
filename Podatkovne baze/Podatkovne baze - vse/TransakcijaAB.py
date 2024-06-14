# pyODBC
import pyodbc
#try:
#    cn1.close()
#    cn2.close()
#except:
#    pass

# MariaDB/MySQL
conn = "DSN=FRI;DATABASE=sandbox;UID=pb;PWD=pbvaje"
cn1 = pyodbc.connect(conn, autocommit=False)
cn2 = pyodbc.connect(conn, autocommit=False)
print("Povezava na strežnik A in B sta uspeli.")

# Izpis rezultatov poizvedbe (pomožna funkcija)
def tabela(rez):
  try:
    # Glava
    for g in rez.description:
        print(g[0],end="\t")
    print("\n"+"-"*31)
    # Vsebina
    count = 0;
    for r in rez.fetchall():
        if count < 5:
            for a in r:
                print(a,end="\t")
            print()
        count = count + 1
    # Število vrstic
    print("Vseh vrstic je", rez.rowcount)
  except Exception(e):
    pass

print("Pomožne funkcije so naložene.")
input("Press ENTER to continue...")
c1=cn1.cursor()
c2=cn2.cursor()

# Nastavitve veljajo za vse nadaljnje NOVE transakcije

# MariaDB/MySQL
c1.execute("SET SESSION innodb_lock_wait_timeout = 5") # Čas v sekundah
c2.execute("SET SESSION innodb_lock_wait_timeout = 5") # Čas v sekundah

# MariaDB/MySQL
c1.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED") # Preizkusite obnašanje pri
c2.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED") # različnih stopnjah izolacije

# READ-UNCOMMITTED , READ-COMMITTED , REPEATABLE-READ , SERIALIZABLE

# Začnemo NOVE transakcije!
c1.commit()
c2.commit()

c1.execute("DROP TABLE IF EXISTS rezervacija444")
c1.execute("DROP TABLE IF EXISTS jadralec444")
c1.execute("""CREATE TABLE jadralec444 AS
              SELECT * from vaje.jadralec""")
c1.execute("ALTER TABLE jadralec444 ADD PRIMARY KEY(jid)")
# c1.execute("ALTER TABLE jadralec ENGINE MYISAM") # Privzeto: InnoDB
c1.commit();
input("Press ENTER to continue...")
print("line (60): Povezava A")
c1.execute("SELECT * FROM jadralec444")
tabela(c1)
input("Press ENTER to continue...")
print("line (64): Povezava B")
c2.execute("SELECT * FROM jadralec444 -- LOCK IN SHARE MODE") # Po potrebi briši SQL komentar --
tabela(c2)
input("Press ENTER to continue...")
print("line (69): Povezava A - posodobitev ratinga")
try:
    c1.execute("UPDATE jadralec444  \
                SET rating = 2  \
                WHERE jid = 29")
except Exception as e:
    print(e)
input("Press ENTER to continue...")
print("Line (76): Povezava A")
tabela(c1.execute("SELECT * FROM jadralec444"))
input("Press ENTER to continue...")
print("Line (79): Povezava B")
tabela(c2.execute("SELECT * FROM jadralec444"))
input("Press ENTER to continue...")
print("line (82): Povezava A - commit")
c1.commit()
input("Press ENTER to continue...")
print("line (85): Povezava A")
tabela(c1.execute("SELECT * FROM jadralec444"))
input("Press ENTER to continue...")
print("line (88): Povezava B")
tabela(c2.execute("SELECT * FROM jadralec444"))
input("Press ENTER to continue...")
# Začnemo nove transakcije
print("line (92): Povezava A in B - rollback")
c1.rollback()
c2.rollback()
input("Press ENTER to continue...")
print("line (96): Povezava A izpis ratinga")
c1.execute(""" SELECT rating
               FROM jadralec444
               WHERE jid = 29""")
rating = c1.fetchone()[0]
print (rating)
input("Press ENTER to continue...")
print("line (103): Povezava B izpis ratinga")
c2.execute(""" SELECT rating
               FROM jadralec444
               WHERE jid = 29""")
rating = c2.fetchone()[0]
print (rating)
input("Press ENTER to continue...")
print("line (110): Povezava A - sprememba ratinga")
c1.execute("UPDATE jadralec444  \
            SET rating = {}  \
            WHERE jid = 29".format(rating + 1))
#c1.commit()
input("Press ENTER to continue...")
print("line (116): Povezava B - sprememba ratinga")
c2.execute("UPDATE jadralec444  \
            SET rating = {}  \
            WHERE jid = 29".format(rating + 1))
#c2.commit()
input("Press ENTER to continue...")
print("line (122): Povezava A in B - commit")
c1.commit()
c2.commit()

print("line (126): Povezava A")
tabela(c1.execute("SELECT * FROM jadralec444"))
input("Press ENTER to continue...")
print("line (129): Povezava B")
tabela(c2.execute("SELECT * FROM jadralec444"))
input("Press ENTER to continue...")
print("line (130): Koncano")