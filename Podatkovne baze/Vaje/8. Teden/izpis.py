import pyodbc
from colorama import Fore

def izpis(curzor, ids):
    for id in ids:
        curzor.execute("SELECT * FROM dept_manager WHERE emp_no = ?", id)
        is_manager = curzor.rowcount
        curzor.execute("SELECT * FROM employees JOIN salaries USING(emp_no) WHERE emp_no = ?", id)
        data = curzor.fetchall();
        top2 = [0, 0]
        top5 = [0, 0, 0, 0, 0]
        for r in data:
            if r[6] > any(top2):
                top2[top2.index(min(top2))] = r[6]
            if r[6] > any(top5):
                top5[top5.index(min(top5))] = r[6]

        for r in data:
            if r[6] >= min(top5):
                if is_manager > 0:
                    print(Fore.RED + "{0:10}".format(str(r[0])) + Fore.RESET, end='')
                else:
                    print("{0:10}".format(str(r[0])), end='')
                print("{0:15}{1:15}{2:15}{3:15}".format(r[2], r[3], str(r[7]), str(r[8])), end='')
                if r[6] >= min(top2):
                    print(Fore.GREEN + str(r[6]) + Fore.RESET)
                else:
                    print(str(r[6]))


if __name__ == '__main__':
    #Spremenite DSN, da se pravilno povežete na bazo z vašimi nastavitvami
    c = pyodbc.connect('DSN=FRI server;DATABASE=employees')
    x = c.cursor()
    print("{0:10}{1:15}{2:15}{3:15}{4:15}{5:15}".format('Id', 'Ime', 'Priimek', 'od', 'do', 'Izplačilo'))

    izpis(x, (110022, 10001, 111877))
    c.close()
