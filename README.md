# Mobilalkfejl - Ruházati webshop

# Pixel 5 eszközön, Android 13-on, jdk 11-el tesztelve:

Fordítási hiba nincs ✅

Futtatási hiba nincs ✅

Firebase autentikáció meg van valósítva: ✅

Be lehet jelentkezni és regisztrálni ✅

Adatmodell definiálása (az alkalamazás témájába illő entitáshoz tartozik osztály, amely az adatbázisban el van mentve, pl. egy Shop alkalmazásnál létezik az Item osztály, ami egy megvásárolandó termék adatait tárolja) ✅ : ShoppingItem, OrderItem

Legalább 4 különböző activity használata ✅ MainActivity, RegisterActivity, CartActivity, OrderActivity, ProfileActivity

Beviteli mezők beviteli típusa megfelelő (jelszó kicsillagozva, email-nél megfelelő billentyűzet jelenik meg stb.) ✅ regisztráció, bejelentkezés, és a profilon is

ConstraintLayout és még egy másik layout típus használata ✅ ConstraintLayout, LinearLayout...

Reszponzív: ✅
- különböző kijelző méreteken is jól jelennek meg a GUI elemek (akár tableten is) ✅
- elforgatás esetén is igényes marad a layout ✅ lehet görgetni a termékeket

Legalább 2 különböző animáció használata ✅ slide_in_row.xml, slide_in_cart_item.xml

Intentek használata: navigáció meg van valósítva az activityk között (minden activity elérhető) ✅ vissza gombbal is

Legalább egy Lifecycle Hook használata a teljes projektben: ✅
- onCreate nem számít ✅
- az alkalmazás funkcionalitásába értelmes módon beágyazott, azaz pl. nem csak egy logolás ✅ onResume(), ...

Legalább 2 olyan Android erőforrás használata, amihez kell android permission és értelmesen beágyazottak az alkalmazás funkcionalitásába ✅ internet_access és notification

2 különböző rendszerszolgáltatás (háttér szolgáltatás): ✅
- notification ✅ android 13-on is és lollipoptól felfele megjelenik hangértesítéssel
- alarm manager ✅
- job scheduler ✅

CRUD műveletek mindegyike megvalósult ✅ rendelések létrehozása firebaseba, termékek lekérése firebaseból, egyes profil adatok módosítása firebaseban, rendelések visszavonása (törlése) firebaseból

Legalább 3 komplex Firestore lekérdezés megvalósítása, amely indexet igényel (ide tartoznak: where feltétel, rendezés, léptetés, limitálás) ✅ termékek rendezése és limitált megjelenítése ha nincs töltőn az eszköz, növekvő, csökkenő, és legjobb értékelés szerinti rendezése...

Szubjektív pontozás a projekt egészére vonatkozólag:
levonható a maximumból, ha igénytelen, összecsapott, látszik hogy nem foglalkozott vele, kísértetiesen hasonlít a videóban létrehozotthoz, stb. 🙏🏻 azért légyszi ne 0 pontot adj rá, sok munka van benne ahhoz ty

