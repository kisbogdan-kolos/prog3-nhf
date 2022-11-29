# Programozás alapjai 3 házi feladat

## Kis-Bogdán Kolos | `N3PTUN`

## Jóságtár - Programozói dokumentáció

## Metódusok leírása

A függvények és osztályok leírása JavaDoc formátumban van minden osztályhoz és függvényhez megírva.

## Program fordítása és futtatása

### VS Code

A `.vscode/settings.json` fájlban minden benne van, a VS Code-ban szükséges futtatáshoz.

> Ehhez fel kell telepíteni az `Extension Pack for Java` nevű bővítményt

### Parancssor

#### Fordítás

```bash
git clone https://github.com/kisbogdan-kolos/prog3-nhf.git
cd prog3-nhf/
mkdir lib
wget https://repo.mavenlibs.com/maven/org/json/json/20220924/json-20220924.jar -O lib/json.jar
javac -d bin -cp lib/*.jar --source-path src/ src/quotes/App.java
jar cfm quotes.jar MANIFEST.mf -C bin .
```

#### Futtatás

```bash
java -jar quotes.jar
```

#### Példa adatbázis letöltése

```bash
wget https://www.kszi2.hu/~kolos/josagtar/db.json
```

<div style="page-break-after: always;"></div>

## Osztálydiagram

![Osztálydiagram](class.svg)
