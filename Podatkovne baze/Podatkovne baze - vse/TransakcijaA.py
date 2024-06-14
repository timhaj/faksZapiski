# pyODBC
import pyodbc
#try:
#    cn1.close()
#except:
#    pass

# MariaDB/MySQL
conn = "DSN=FRI;DATABASE=sandbox;UID=pb;PWD=pbvaje"
cn1 = pyodbc.connect(conn, autocommit=False)
c1=cn1.cursor()
print("Povezava na strežnik transakcije A je uspela.")
# In [ ]:
# Izpis rezultatov poizvedbe (pomožna funkcija)
def tabela(rez):
  try:
    # Glava
    for g in rez.description:
        print(g[0],end="\t")
    print("\n"+"-"*31)
    # Vsebina
    for r in rez.fetchall():
        for a in r:
            print(a,end="\t")
        print()
    # Število vrstic
    print("Vseh vrstic je", rez.rowcount)
  except Exception(e):
    pass
# Nadaljuj s pomožnimi elementi transakcije B
print("Pomožni elementi transakcije A so naloženi.")
print("Nadaljuj s pomožnimi elementi transakcije B.")
input("Press Enter to continue... (A1)")

# Korak A1: nastavi transakcijske parametre za vse nadaljnje nove transakcije
# Timeout ob predolgem zaklepanju
c1.execute("SET SESSION innodb_lock_wait_timeout = 5") # Cas v sekundah
# Preizkusite razlicne stopnje izolacije
c1.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED")
# Zacnemo novo transakcijo
c1.commit()
print("A1: Nastavljeni so transakcijski parametri za vse nove nadalnje transakcije.")
input("Press Enter to continue... (A2)")

# Korak A2: kreiramo in napolnimo testno tabelo
c1.execute("DROP TABLE IF EXISTS rezervacija")
c1.execute("DROP TABLE IF EXISTS jadralec")
c1.execute("CREATE TABLE jadralec AS "
              "SELECT * from vaje.jadralec")
c1.execute("ALTER TABLE jadralec ADD PRIMARY KEY(jid)")
#c1.execute("ALTER TABLE jadralec ENGINE MYISAM") # Privzeto: InnoDB
c1.commit();
print("Kreirane so testne tabele.")
input("Press Enter to continue... (A3)")

# Korak A3: izpis vsebine tabele pred spremembo
c1.execute("SELECT * FROM jadralec")
tabela(c1)
# Nadaljuj s korakom B1
# Branje neobstojecega podatka (dirty read)
# stopnja izolacije mora biti vsaj READ COMMITTED
print("A3: Izpis tabel pred spremembo.")
print("Nadaljuj s korakom B1.")
input("Press Enter to continue... (A4)")

# Korak A4: sprememba tabele
try:
    c1.execute("UPDATE jadralec  \
                SET rating = 222  \
                WHERE jid = 29")
except Exception as e:
    print(e)
print("A4: Spremeba tabele. Posodobljen rating jadralca 29.")
input("Press Enter to continue... (A5)")


# Korak A5: izpis vsebine tabele po spremembi
tabela(c1.execute("SELECT * FROM jadralec"))
print("A5: Izpis tabele po spremembi.")
print("Nadaljuj s korakom B3.")
input("Press Enter to continue... (A6)")
# Nadaljuj na koraku B3

# Korak A6: razveljavi spremembe in ponovno izpiši
c1.rollback()
tabela(c1.execute("SELECT * FROM jadralec"))
# Izgubljeno ažuriranje
print("A6: Razveljavi spremembe transakcije A in ponovno izpiši tabelo.")
print("Nadaljuj s korakom B4.")
input("Press Enter to continue... (A7)")


# Korak A7: zacetek nove transakcije in izpis ratinga
c1.rollback()
c1.execute(""" SELECT rating
               FROM jadralec
               WHERE jid = 29""")
rating = c1.fetchone()[0]
print (rating)
print("A7: Začni novo transakcijo in izpiši rating.")
input("Press Enter to continue... (A8)")

# Korak A8: sprememba ratinga
c1.execute("UPDATE jadralec  \
            SET rating = {}  \
            WHERE jid = 29".format(rating + 100))
print("A8: Ponvna sprememba ratinga.")
print("Nadaljuj s korakom B6.")
input("Press Enter to continue... (A9)")
# Nadaljuj s korakom B6


# Korak A9: potrjevanje sprememb in izpis
c1.commit()
tabela(c1.execute("SELECT * FROM jadralec"))
# Neponovljivo branje (`non-repeatable read`)
# Potrebuje stopnjo izolacije najmanj REPEATABLE READ
# Alernativno: SELECT ...  LOCK IN SHARE MODE na kateri koli stopnji izolacije
print("A9: Potrditev sprememb in izpis.")
input("Press Enter to continue... (A10)")

# Korak A10: zacetek transakcije in prvi izpis
c1.commit()
tabela(c1.execute("SELECT * FROM jadralec"))
print("A10: Začetek transakcije in prvi izpis.")
print("Nadaljuj s korakom B7.")
input("Press Enter to continue... (A11)")
# Nadaljuj s korakom B7


# Korak A11: nadaljevanje transakcije in drugi izpis
tabela(c1.execute("SELECT * FROM jadralec"))
# Fantomsko branje (`phantom read`)
# Potrebujemo SERIALIZABLE
print("A11: Nadaljevanje transakcije in drugi izpis.")
input("Press Enter to continue... (A12)")

# Korak A12: zacetek transakcije in prvi izpis
c1.commit()
tabela(c1.execute("SELECT * FROM jadralec"))
# Nadaljuj s korakom B8
print("A12: Začetek transkacije in prvi izpis.")
print("Nadaljuj s korakom B8.")
input("Press Enter to continue... A(13)")

# Korak A13: nadaljevanje transakcije in drugi izpis
tabela(c1.execute("SELECT * FROM jadralec"))
# Ce je stopnja izolacije nižja od SERIALIZABLE pride do pojava fantomske vrstice.
# Mrtva zanka
print("A13: nadaljevanje transakcije in drugi izpis.")
input("Press Enter to continue... A(14)")

# Korak A14: prva sprememba in zaklepanje
c1.commit()
c1.execute("SET innodb_lock_wait_timeout = 500") # Daljši timeout samo za trenutno transakcijo
c1.execute("UPDATE jadralec  \
            SET rating = {}  \
            WHERE jid = 22".format(122))
# Nadaljuj s korakom B9.
print("A14: Prva sprememba in zaklepanje.")
print("Nadaljuj s korakom B9.")
input("Press Enter to continue... (A15)")

# Korak A15: druga sprememba in zaklepanje
c1.execute("UPDATE jadralec  \
            SET rating = {}  \
            WHERE jid = 29".format(129))
c1.commit()
# Cakamo na transakcijo B, dokler se ta ne prekine zaradi pojava mrtve zanke.
# Nadaljuj s korakom B10.
print("A15: Druga sprememba in zaklepanje.")
print("Nadaljuj s korakom B10.")
input("Press Enter to continue...")

# Korak A16: izpis
tabela(c1.execute("SELECT * FROM jadralec"))
print("A16: Izpis tabele.")