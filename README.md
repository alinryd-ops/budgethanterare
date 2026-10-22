# Budgethanteraren

Budgethanteraren är ett konsolprogram i Java där man lägger till inkomster och utgifter, ser saldo och summa per kategori, filtrerar transaktioner och sparar dem till en CSV-fil. Projektet byggs med Maven och testas med JUnit 5. Menyn har sex val: 1 Lägg till, 2 Visa alla, 3 Saldo/kategori, 4 Filtrera, 5 Spara, 6 Avsluta.

## Bygga och köra

- Testerna körs i IntelliJ via Maven-fliken (Lifecycle > test).
- Programmet startas genom att köra `Main` i IntelliJ, med `-Dstdin.encoding=UTF-8` i VM options (se felsökningen om `Lön` nedan).

## Arbetssätt

Jag har arbetat i små steg och försökt förstå varje steg innan jag gick vidare.

**Planering.** Jag planerade strukturen i pseudokod innan jag skrev kod, och gick igenom kravspecifikationen steg för steg.

**Tester.** Jag skrev kod och tester själv. När ett test blev grönt ändrade jag koden med flit för att se att testet också kunde bli rött. Tre tester kunde inte bli röda först: `fileDoesNotExist`, `readsFieldsInCorrectPlaces` och loggningen.

**Val jag gjorde och varför.**

- **CSV som filformat:** det är enkelt, mänskligt läsbart och kräver ingen extern dependency. Svaghet: en kategori med komma (till exempel "Mat, dryck") förskjuter fälten.
- **Saknad fil ger ett tomt `Repository`:** första starten är inte ett fel. En tom fil är något annat än en saknad fil.
- **Trasig rad i filen:** jag övervägde två alternativ. A) Avbryta med ett eget undantag, vilket är striktare men gör att en enda trasig rad stoppar hela filen. B) Hoppa över raden och logga WARNING, vilket ger tillgång till all giltig data men gör att en rad kan försvinna om loggen inte läses. **Jag valde B**, för att programmet ska starta med så mycket giltig data som möjligt.
- **Fel vid start:** programmet skriver ett meddelande, loggar SEVERE och avslutar. Jag valde det för att inte riskera att användarens gamla fil skrivs över av en tom när hen sparar. Nackdel: appen startar inte alls.
- **`FileStorageException` (checked):** både `saveFile` och `readFile` kastar den, så att `Menu` kan hantera ett filfel på ett ställe.
- **Kontroll av kategori:** `category == null || category.isBlank()`. Kollar jag `null` först kraschar inte `isBlank()` om kategorin saknas.

**Hjälp.** Jag har använt Claude som bollplank (se AI-användning) och frågat min kursledare när jag fastnade, till exempel vid kodningsbuggen.

## Felsökning

### Ett test som alltid blir grönt?

Ingen riktig bugg, men en fundering. När jag gjorde testerna och de blev gröna ändrade jag min kod med flit för att se att testet även blev rött om jag hade skrivit fel. Till exempel ändrade jag kategorin i `digitsAndLettersFirst` till `123`, och då såg jag att testet inte alltid blir grönt. Ett test som aldrig kan bli rött bevisar ingenting.

### Dokumenterad bugg: NoSuchFileException i readFile

**Vad jag förväntade mig**
Efter att jag började läsa från fil i `readFile` skulle testet `fileDoesNotExist` fortfarande vara grönt. Min regel är att en saknad fil ger ett tomt `Repository` (0 transaktioner), eftersom filen inte finns första gången någon startar programmet.

**Vad som hände istället**
Testet blev rött med `java.nio.file.NoSuchFileException: non-existing.csv`. Testet hade varit grönt innan jag la till raden med `Files.readAllLines`.

**Hur jag letade**
Jag satte en breakpoint på raden med `Files.readAllLines` i `readFile` och körde testet med debuggern. När jag tryckte Step Over hoppade programmet direkt ut ur metoden. I Console fanns en stack trace. De två raderna som var min egen kod pekade på `FileStorage.readFile` och `FileStorageTest.fileDoesNotExist`. Raden som kraschade var alltså `readAllLines`, som försökte öppna en fil som inte finns.

**Hur jag löste det**
Jag la en kontroll före `readAllLines`: om `Files.notExists(...)` är sann returnerar `readFile` ett tomt `Repository` direkt. Kontrollen måste ligga före, annars kraschar programmet innan den hinner köras. Testet blev grönt igen.

**Vad jag lärde mig**
Ett test som är grönt kan bli rött av en ny rad, och det är därför man kör alla tester efter varje ändring. Jag lärde mig också skillnaden mellan en tom fil (ger en tom lista) och en fil som inte finns (ger ett fel).

### Dokumenterad bugg: "Lön" blev "LÃ¶n"

**Symptom:** När jag skrev kategorin `Lön` i menyn stod `LÃ¶n` i `transactions.csv`.

**Hur jag hittade var felet satt:**

- Filen hade redan fel text, så felet kom inte från utskriften.
- Min kursledare misstänkte inmatningen från tangentbordet.
- Jag la en tillfällig debug-rad med `category.length()`. `Lönerna` gav längd 8 i stället för 7, alltså ett tecken för mycket. Då visste jag att texten var fel redan när den kom in via `IO.readln`.

**Vad jag mätte:** `file.encoding` visade UTF-8, medan `stdin.encoding` och `stdout.encoding` visade Cp1252 (Windows-kodning). `ö` är två byte i UTF-8, och läses de som Cp1252 blir det två tecken.

**Orsak:** Min kursledare bedömer att felet ligger i IntelliJ: `IO.readln` läser inte med UTF-8. Det är alltså inte något vi har gjort fel i vår egen kod.

**Lösning:** Tillsammans med kursledaren la jag till `-Dstdin.encoding=UTF-8` i VM options för `Main` i IntelliJs run-konfiguration, för att tvinga inläsningen till UTF-8. Efteråt blev det `Lön` i filen.

**Osäkert:** Jag har inte bevisat exakt varför `stdin.encoding` visade Cp1252 när flaggan fanns. Lösningen fungerar, men mekanismen är inte bevisad av mig.

**Känd begränsning:** Flaggan ligger i IntelliJs run-konfiguration, inte i repot. Den som kör programmet från ett vanligt Windows-fönster behöver starta med `java -Dstdin.encoding=UTF-8 ...`.

*Redovisning:* AI användes bara för information kring den här buggen. Felsökningen gjorde jag själv, och VM-optionen satte jag tillsammans med kursledaren.

### Loggfilen hade 4000 rader

`root.setLevel(Level.ALL)` gjorde att Javas egna loggare (JMX/RMI) också skrev till loggen. Jag ändrade till INFO för root och ALL bara för paketet `budgetprogram`.

## Loggning

Alla fyra nivåer används. Javas namn står först, vanliga namn inom parentes.

- **FINE (DEBUG):** detaljer för felsökning. Används i `FileStorage` och `Menu`.
- **INFO:** normala händelser, till exempel att en fil lästes. Används i `FileStorage` och `Menu`.
- **WARNING:** något är fel men programmet fortsätter, till exempel en trasig rad som hoppas över. Används i `FileStorage`.
- **SEVERE (ERROR):** åtgärden misslyckades, till exempel att filen inte kunde läsas eller sparas. Används i `FileStorage` och `Menu`.

FINE syns inte som standard i Javas konsol, därför finns `LoggingSetup`. Den skriver loggen till filen `budgetprogram.log` i stället för konsolen, så att användaren inte ser loggraderna i menyn.

## Reflektion: generics och Stream API (VG)

### Generics (`Repository<T>`)

`Repository<T>` är en klass som kan hålla vilken typ av objekt som helst. `T` är en platshållare för typen, så jag skrev klassen en gång och kan använda den till andra saker än transaktioner utan att ändra något, till exempel `Repository<String>`. Kompilatorn kontrollerar också typen åt mig, så jag slipper `Object` och egen casting.

Metoderna `findWhere` och `sortedBy` tar emot en regel (`Predicate` och `Comparator`). Därför behöver jag inte en egen metod för varje filter. Loopen i `findWhere` är likadan varje gång, och det enda som skiljer är regeln, till exempel `tx -> tx.type() == wanted`.

### Stream API

I `BudgetCalculator` har jag båda sätten och kan jämföra dem. `getBalance`, `getExpenses` och `getIncomes` använder loopar med `if` och hjälpvariabler. `getExpensesByCategory` och `getIncomesByCategory` använder streams med `groupingBy` och `summingDouble`. Stream-versionerna blev kortare och lättare att läsa, eftersom de beskriver vad jag vill ha (summan per kategori) i stället för hur jag ska räkna steg för steg.

`groupingBy` delar upp transaktionerna i grupper efter kategori, och `summingDouble` summerar beloppen i varje grupp. Resultatet är en `Map` med kategorin som nyckel och summan som värde. Utan streams hade jag behövt skapa en `HashMap` själv och för varje transaktion kolla om kategorin redan finns innan jag lägger till beloppet. Det är där streams sparar mest kod. En enkel summa, som i `getExpenses`, fungerar bra med en loop.

## AI-användning

Jag använder Claude och utvecklar genom att ha planerat med pseudokod hur strukturen ska vara. Med tanke på liten erfarenhet av detta arbetssätt använder jag Claude genom att be den ta kravspecifikationen lugnt och metodiskt steg för steg, för att undvika att uppgiften görs på ett fel sätt. Claude är även ett bollplank där jag skriver kod och test själv, skickar sedan in det jag har skrivit och den styr mig åt rätt håll om koden är fel. Det är alltså min kod, men AI får agera som en senior utvecklare och styra mig åt rätt håll.
