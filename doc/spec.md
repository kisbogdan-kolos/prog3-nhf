# Programozás alapjai 3 házi feladat

## Kis-Bogdán Kolos | `N3PTUN`

## Jóságtár

## Feladat ismertetése és use-casek

A program emberek aranyköpéseinek nyilvántartására alkalmas. A felhasználónak lehetősége van az embereket és aranyköpéseket kezelni, valamint ezeket különböző formákban megtekinteni.

A program induláskor megpróbál betölteni egy alapértelmezett állományt, amiben az adatok találhatóak. Miután ez megtörtént, egy véletlenszerűen kiválasztott aranyköpéssel üdvözli felhasználót. Sikertelen betöltés esetén a felhasználónak meg kell adnia egy létező állományt, vagy egy újat létrehozni. Betöltés után is természetesen lehet változtatni a jelenleg használt állományt.

Sikeres betöltés után a felhasználónak lehetősége van megtekinteni az aranyköpéseket és embereket, valamint ezeket szerkeszteni, valamint új aranyköpést hozzáadni. Itt szintén lehet új véletlenszerűen kiválasztott aranyköpést kérni.

### Aranyköpések megtekintése és szerkesztése

A megtekintés opció kiválasztása után az összes aranyköpés megjelenik egy listában. A listát lehet szűrni adott emberre, valamint lehet benne keresni is. Itt is van lehetőség új hozzáadására. Egy aranyköpésre kattintva megtekinthető az nagyobb ablakban, valamint szerkeszthető is. Szerkesztésnél lehetőség van módosítani a hozzárendelt embert is, valamint törölni az adott aranyköpést.

Egy aranyköpés három részből áll:

1. kontextus előtte
1. idézet
1. kontextus utána

Az idézet mezőt kötelező kitölteni, a kontextusok opcionálisak. Minden aranyköpés pontosan egy emberhez van hozzárendelve.

### Emberek megtekintése és szerkesztése

Az emberek lista kiválasztása után az összes ember megjelenik egy listában. Minden ember mellett látható az is, hogy hány aranyköpése van. Az emberre kattintva szerkeszthető, ahonnan lehet törölni is. Törölni csak akkor lehet, ha nincs aranyköpése. Egy emberhez tartozik a teljes neve, rövidített neve és megjegyzés is. Megjegyzésre akkor lehet szükség, ha több azonos nevű ember van. Aranyköpés hozzáadásakor van lehetőség a megjegyzés megtekintésére.

Itt van lehetőség új emberek hozzáadására is.

### Fájlkezelés

A program minden módosítást automatikusan fájlba ment, a módosítás nem visszavonható. A fájl `JSON` formátumú, amiben minden adat benne van a program működéséhez.

Példa fájl:

```json
{
	"people": [
		{
			"id": "b18b5755-e811-4866-8e99-9161e65b7937",
			"fullName": "Kiss Pista",
			"shortName": "KissP"
		},
		{
			"id": "df30b7bd-9d41-406a-af01-971890b9256d",
			"fullName": "Antal János",
			"shortName": "AntalJ",
			"notes": "Kerékpártolvaj"
		}
	],
	"quotes": [
		{
			"id": "1442adfd-8e90-4ec0-8505-ba3ed197fff6",
			"person": "df30b7bd-9d41-406a-af01-971890b9256d",
			"quote": {
				"contextBefore": "Ellop egy biciklit",
				"quote": "Helló haver! Nem kell egy bringa? Nem lopott."
			}
		},
		{
			"id": "2f32e147-0d53-400d-ad84-064351e7973a",
			"person": "b18b5755-e811-4866-8e99-9161e65b7937",
			"quote": {
				"quote": "Kiss Pista vagyok, történész."
			}
		}
	]
}
```

Itt minden személyhez meg kell lennie a nevének és rövid nevének, de a megjegyzés opcionális. Az idézeteknél a személy és az idézet kötelező, a két kontextus nem.

Az azonosítóknak meg kell lenniük, és egyedieknek kell lenni. Az aranyköpés ember azonosítójának tényleges emberre kell mutatnia.

## Megvalósítás

A megvalósításhoz `Java`-t és `Java Swing`-et fogok használni. A `JSON` kezelésre az `org.json` könyvtárat fogom használni. Az azonosítóként használt `UUID`-t a `java.util.UUID` könyvtár felelős.

A program megnyitásakor beolvassa az előre beállított `quotes.json` fájlt. Ezt parse-olja, majd átvizsgálja, hogy minden része megfelel-e a program elvárásainak. Átvizsgálás közben automatikusan hozzáadja az embereket és aranyköpéseket egy-egy `HashMap`-hez, amiben azonosító szerint vannak tárolva. Ha egy aranyköpéshez csak valós felhasználó azonosító tartozhat. Ha beolvasás körben bármi hibát tapasztal, akkor azonnal leáll és kiírja a hiba helyét.

A program használata közben a két `HashMap`-ben tárolt adatokkal dolgozik a program, amikor az módosul, akkor automatikusan menti a fájlt.

A fájlformátum az előző fejezetben látható.
