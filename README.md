# Trade Engine Lite

Mini-Java-Projekt für den Einstieg in Java im Calypso-/Banking-Kontext.

## Ziel

Das Projekt modelliert einfache Trades, validiert fachliche Mindestregeln und erzeugt einfache Auswertungen.

## Projektstruktur

```text
src/main/java/com/adrian/tradeengine/
  model/       Fachliche Datenobjekte
  service/     Fachliche Verarbeitung und Auswertung
  validation/  Validierungsregeln
  io/          Import/Export, z. B. CSV

src/test/java/com/adrian/tradeengine/
  model/       Tests für Modellklassen
  service/     Tests für Auswertungslogik
  validation/  Tests für Validierungslogik
  io/          Tests für Import/Export

## Teststrategie

Das Projekt verwendet JUnit 5 für Unit Tests.

Aktuell getestet werden:

- Modellklassen: korrekte Konstruktion von Trade-Objekten
- Analytics: Filtern, Gruppieren und Aggregieren von Trades
- Validation: positive und negative Validierungsfälle
- CSV-Import: Parsing einzelner CSV-Zeilen
- Import-Pipeline: CSV-Zeilen werden in Trades umgewandelt und validiert

Tests werden ausgeführt mit:

```bash
mvn test