# pyODBC
import pyodbc
#try:
#    cn2.close()
#except:
#    pass

# MariaDB/MySQL
conn = "DSN=FRI;DATABASE=sandbox;UID=pb;PWD=pbvaje"
cn2 = pyodbc.connect(conn, autocommit=False)
c2=cn2.cursor()
print("Povezava na strežnik transakcije B je uspela.")
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
# Nadaljuj s korakom A1
print("Pomoćni elemenit trasakcije B so naloženi.")
print("Nadaljuj s korakom A1.")
input("Press Enter to continue... (B1)")

# Korak B1: nastavi transakcijske parametre za vse nadaljnje nove transakcije
# Timeout ob predolgem zaklepanju
c2.execute("SET SESSION innodb_lock_wait_timeout = 5") # Cas v sekundah
# Preizkusite razlicne stopnje izolacije
c2.execute("SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED")
# Zacnemo novo transakcijo
c2.commit()
print("B1: Nastavi transakcijske parametre za vse nadaljnje transakcije.")
input("Press Enter to continue... (B2)")

# Branje neobstojecega podatka (dirty read)
# stopnja izolacije transakcije B mora biti vsaj READ COMMITTED
# Korak B2: izpis vsebine tabele pred spremembo
c2.execute("SELECT * FROM jadralec")
tabela(c2)
# Nadaljuj s korakom A4
print("B2: Izpis vsebine tabele pred spremebo.")
print("Nadaljuj s korakom A4.")
input("Press Enter to continue... (B3)")

# Korak B3: izpis vsebine tabele po spremembi v prvi povezavi
tabela(c2.execute("SELECT * FROM jadralec"))
print("B3: Izpis vsebine tabele po spremembi v prvi povezavi.")
print("Nadaljuj s korakom A6.")
input("Press Enter to continue... (B4)")

# Korak B4: izpis vsebine tabele po razveljavitvi spremembe v prvi povezavi
tabela(c2.execute("SELECT * FROM jadralec"))
print("B4: Izpis vsebine tabele po spremebi v A, ter po razveljavitvi v A.")
input("Press Enter to continue... (B5)")

# Pri READ UNCOMMITED so vidne tudi nepotrjene spremembe, pri višjih stopnjah izolacije pa ne.
# Izgubljeno ažuriranje
# Korak B5: zacetek nove transakcije in izpis ratinga
c2.rollback()
c2.execute(""" SELECT rating
               FROM jadralec
               WHERE jid = 29""")
rating = c2.fetchone()[0]
print(rating)
print("B5: Začetek nove transakcije in izpis ratinga.")
print("Nadaljuj s korakom A7.")
input("Press Enter to continue... (B6)")
# Nadaljuj s korakom A7

# Korak B6: sprememba ratinga
c2.execute("UPDATE jadralec  \
            SET rating = {}  \
            WHERE jid = 29".format(rating + 10))
# Ne glede na stopnjo izolacije dobimo obvestilo:
# Lock wait timeout exceeded; try restarting transaction (1205)
# (razen ce namesto privzetega InnoDB/XtraDB uporabimo netransakcijski shranjevalni pogon MyISAM/ARIA)
# Nadaljujemo lahko le z MyISAM
print("B6: Spremeba ratinga.")
print("Nadaljujemo lahko le z netransakcijskim shranjevalnim pogonom. MyISAM ali ARTA.")
input("Press Enter to continue... (B7)")

# Korak B7: potrditev sprememb in izpis
c2.commit()
tabela(c2.execute("SELECT * FROM jadralec"))
# Neponovljivo branje (non-repeatable read)
# Potrebujemo najmanj REPEATABLE READ v transakciji A
print("B7: Potrditev sprememb in izpis.")
input("Press Enter to continue... (B8)")


# Korak B8: zacetek transakcije, sprememba in potrditev
c2.commit()
c2.execute("UPDATE jadralec  \
            SET rating = {}  \
            WHERE jid = 29".format(666))
c2.commit()
# Nadaljuj s korakom A11
# Fantomsko branje (phantom read)
# Potrebujemo SERIALIZABLE v transakciji A
print("B8: Začetek nove transakcije, sprememba in potrditev.")
print("Za nadaljevanje mora biti transakcija A SERIALIZABLE.")
print("Nadaljuj s korakom A11.")
input("Press Enter to continue... (B9)")

# Korak B9: zacetek transakcije, sprememba in potrditev
c2.commit()
c2.execute("INSERT INTO jadralec VALUES(25,'PHANTOM',42, 666)")
c2.commit()
# Pri SERIALIZABLE pride do time-outa, sicer do fantomske vrstice
# Nadaljuj s korakom A13
print("B9: začetek transakcije, spremeba in potrditev.")
print("Nadaljuj s korakom A13.")
input("Press Enter to continue... (B10)")

c2.commit()
# Mrtva zanka

print("Mrtva zanka.")
input("Press Enter to continue... (B10)")


# Korak B10: prva sprememba in zaklepanje
c2.commit()
c2.execute("UPDATE jadralec  \
            SET rating = {}  \
            WHERE jid = 29".format(229))
# Nadaljuj s korakom A15
print("B10: Nova sprememba in zaklepanje.")
print("Nadaljuj s korakom A15.")
input("Press Enter to continue... B(11)")


# Korak B11: druga sprememba in zaklepanje
c2.execute("UPDATE jadralec  \
            SET rating = {}  \
            WHERE jid = 22".format(222))
c2.commit()
# Pride do pojava mrtve zanke, zato se transakcija prekine in razveljavi.
# Nadaljuj s korakom A16.
# Za ponovitev ponovno izvršimo koraka B10 in B11.
print("B11: Druga sprememba in zaklepanje.")
print("Nadaljuj s korakom A16. Za ponovitev s korakoma B10 in B11.")
input("Press Enter to continue... (Konec B)")