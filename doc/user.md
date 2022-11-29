# Programozás alapjai 3 házi feladat

## Kis-Bogdán Kolos | `N3PTUN`

## Jóságtár - Felhasználói dokumentáció

## Program működése

A program elindításakor megpróbálja betölteni az alapértelmezett adatbázist (`db.json`). Amennyiben betöltéskor bármilyen hibába ütközik, erről értesíti a felhasználót, és új adatbázis fájl betöltését kéri.

### Főoldal

A főoldalon sikeres adatbázis betöltés után egy véletlenszerűen kiválasztott aranyköpés jelenik meg. Ezt a menüsorban lévő `Random idézet` menüben lévő `Új random izézet` gombbal lehet újra randomizálni.

A `Fájl` menüben lehet átlépni az emberek és idézetek kezelése oldalakra, valamint azonnal új idézetet hozzáadni.

Az `Adatbázis` menüben az adatbázis beállítási oldalát lehet elérni.

### Adatbázis beállítása

Az adatbázis alapértelmezetten a program futtatási helyén levő `db.json` fájlt nyitja meg. A jelenleg használt fájlt a `Fájl kiválasztása` gombbal lehet módosítani. Fájlválasztás után a megjelenik az újonnan választott fájl neve, esetleg elérési útja a `Jelenlegi fájl` mezőben.

> Amennyiben a fájl a program futtatási helyén, vagy annak egy almappájában van, akkor relatív elérési utat fog kiírni, minden más esetben abszolút elérési út lesz.

A `Mentés` gomb hatására a program megpróbálja betölteni az adatbázist. Amennyiben betöltéskor bármilyen hibába ütközik, erről értesíti a felhasználót, és új adatbázis fájl kiválasztását kéri.

A `Mégse` gomb értelemszerűen elveti a módosításokat.

### Emberek kezelése

Az ablakban egy táblázatban látható az összes adatbázisban lévő ember. Az egyes emberek mellett lévő `Szerkeszt` gombbal lehet szerkeszteni, ahonnan törölni is lehet.

#### Ember szerkesztése

A szerkesztés ablakban szerkeszthető az ember teljes és rövid neve, valamint a megjegyzés. A `Mentés`, `Mégse` és `Törlés` gombok működése egyértelmű.

> Adott embert csak akkor lehet törölni, ha nincs hozzárendelve idézet.

> Ember teljes és rövid neve nem lehet üres. Ha bármelyik üres, a program ezt jelzi, és nem engedi elmenteni.

#### Ember hozzáadása

A szerkesztéshez analóg módon működik.

### Idézetek kezelése

Az ablakban egy táblázatban látható az összes idézet. Az egyes idézetek mellett lévő `Szerkeszt` gombbal szerkeszteni, a `Megnéz` gombbal pedig megtekinteni lehet az adott idézetet.

#### Szűrés

A legfelső sorban található a szűrő. A legördülő menü segítségével lehet emberre szűrni, vagy kikapcsolni a szűrést a `MINDEN` opció választásával. A szövegdobozba gépelt szöveggel idézetre vagy kontextusra lehet szűrni. Egy idézet akkor fog belekerülni a listába, ha mindkét szűrési feltétel teljesül rá.

> Nem lehet egyszerre több emberre szűrni.

#### Megtekintés

Idézet megtekintése külön ablakban történik.

#### Idézet szerkesztése

A szerkesztés ablakban szerkeszthető az idézet szövege, kontextusa, valamint módosítható a szerzője is.

> Az idézet nem lehet üres. Ha üres, a program ezt jelzi, és nem engedi elmenteni.

#### Idézet hozzáadása

A szerkesztéshez analóg módon működik.

> Alapértelmezetten a legelső ember lesz kiválasztva szerzőnek.
