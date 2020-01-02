# GK9.3.2 Middleware Engineering "High Availability" - Taskdescription
## Einführung
Als Lastverteilung (englisch Load Balancing) bezeichnet man in der Informatik der Verteilung von umfangreiche Berechnungen oder große Mengen von Anfragen auf mehrere parallel arbeitende Systeme. Dies kann sehr unterschiedliche Ausprägungen haben. Eine einfache Lastverteilung findet zum Beispiel auf Rechnern mit mehreren Prozessoren statt. Jeder Prozess kann auf einem eigenen Prozessor ausgeführt werden. Man unterscheidet eine Reihe von Algorithmen, genannt Load Balancing Methoden, um diese Verteilung durchzuführen.


## Ziele
Diese Übung soll helfen die Funktionsweise eines Load Balancers kennenzulernen und anwenden zu koennen. Dabei sollen die SchülerInnen die verschiedenen Methoden kennenlernen und anhand eines Beispiels umgesetzt werden.


## Voraussetzungen
* Grundlagen zu Load Balancing
* Java/Python Programmierkenntnisse


## Aufgabenstellung
Es soll ein Load Balancer mit mindestens 2 unterschiedlichen Load-Balancing Methoden implementiert werden. Eine Kombination von mehreren Methoden ist möglich. Die Berechnung bzw. das Service ist frei wählbar!

Folgende Load Balancing Methoden stehen zur Auswahl:

+ Weighted Round-Round
+ Least Connection
+ Weighted Least Connection
+ Agent Based Adaptive Balancing / Server Probes

Es sollen die einzelnen Server-Instanzen in folgenden Punkten belastet werden können:

+ Memory (RAM)
+ CPU Cycles

Bedenken Sie dabei, dass die einzelnen Load Balancing Methoden unterschiedlich auf diese Auslastung reagieren werden. Dokumentieren Sie dabei aufkommenden Probleme ausführlich.


## Abnahme/Tests
Für die Abnahme wird empfohlen, dass jeder Server eine Ausgabe mit entsprechenden Informationen ausgibt, damit die Verteilung der Anfragen demonstriert werden kann. Sie sollen auch eine sinnvolle Darstellung wählen, um die Resultate der Belastungstests zu dokumentieren.


## Fragestellung für die Dokumentation
Verlgeichen Sie die verwendeten Load Balancing Methoden und stellen Sie diese gegenüber.

+ Was kann als Gewichtung bei Weighted Round Robin verwendet werden?
+ Warum stellt die "Hochverfügbarkeit" von IT Systemen in der heutigen Zeit eine sehr wichtige Eigenschaft dar?
+ Welche anderen Massnahmen neben einer Lastverteilung müssen getroffen werden, um die "Hochverfügbarkeit" sicher zu stellen?
+ Was versteht man unter "Session Persistenz" und welche Schwierigkeiten ergeben sich damit?
+ Nennen Sie jeweils ein Beispiel, wo Session Persistenz notwendig bzw. nicht notwendig ist.
+ Welcher Unterschied besteht zwischen einer "server-side" bzw "client-side" Lastverteilungslösung?
+ Was versteht man unter dem "Mega-Proxy-Problem"?


## Bewertung
﻿Gruppengrösse: 1 Person
### Anforderungen "überwiegend erfüllt"
+ Dokumentation und Beschreibung der angewendeten Load Balancing Methode
+ Implementieren & dokumentieren einer Load Balancing Methode
### Anforderungen "zur Gänze erfüllt"
+ Implementieren & dokumentieren einer weiteren Load Balancing Methode
+ Gegenüberstellung der angewendeten Load Balancing Methoden
+ Durchführen & dokumentieren der Belastungstests (RAM/CPU)

## Implementierung

Als Basis habe ich die RMI Aufgabe von letztem Jahr verwendet.https://github.com/ftraxler-tgm/syt4-gk833-rmi-taskloadbalancer-ftraxler-tgm

Ich hab sie so erweitert, dass sich eine Engine immer mit ihrer Gewichtung registrieren muss, weil die Engine nicht weiß was für Load Balancing Methode benutzt wird.

### Weighted Round Robin

Jede Engine hat eine Gewichtung die angibt wie viele Anfragen der Server bekommt bevor. Wenn Server 1 zum Beispiel eine Gewichtung von 5 hat und der Server 2 eine von 2 landen die ersten fünf Anfragen bei Server 1 und die darauf folgenden Anfragen an Server 2.

Der Algorithmus schaut wie folgt aus:

```java
	//Es wird eine neue Liste erstellt und je nachdem wie hoch die Gewichtung ist, wird die 	Engine dementsprechend oft der Liste hinzugefügt.
	List<Compute> liste = new ArrayList<>();
    Iterator<Compute> iterator = serverList.iterator();
    while (iterator.hasNext()) {
        Compute serverItem = iterator.next();
        Integer weight = weightList.get(serverItem);
        if (weight > 0) {
            for (int i = 0; i < weight; i++) {
                liste.add(serverItem);
            }
        }

    }
    if (position > liste.size()) {
        position = 0;
    }
    return liste.get(position++).executeTask(t);
```

### Least-Connections

Die Methode funktioniert so, dass immer die Engine mit den geringsten aktiven Verbindungen ausgewählt wird. Deshalb habe ich eine Liste erstellt, welche die aktuellen Verbindungen jeder Engine enthält. Mithilfe dieser Liste habe ich dann berechnet welche Engine die geringsten Verbindungen hat:

```java
Compute smallest=null; // Die Variable für die kleinste Engine
int min = Integer.MAX_VALUE;
for(Compute key : leastConnection.keySet()) {
    int value = leastConnection.get(key);
    if(value < min) {
        min = value;
        smallest = key;
    }
}
T object = smallest.executeTask(t); //Task ausführen
```

"Smallest" entspricht der Engine mit den geringsten aktuellen Verbindungen, auf dieser wird der Task dann ausgeführt. 

#### Weighted Round Robin vs Least Connections:

Der Vorteil von Weighted Round Robin ist, dass man je nach der Leistung des Servers die Gewichtung anpassen kann denn der Server mit den wenigsten Connections ist nicht automatisch der am stärksten ausgelastete. Jedoch ist  Least Connection eine gute Lösung wenn man viele ähnlich ausgestattet Server am selben Ort hat.

#### Fragen:

+ Was kann als Gewichtung bei Weighted Round Robin verwendet werden?
  + Die Leistung des Servers oder die Ausfallsicherheit der Verbindung



+ Warum stellt die "Hochverfügbarkeit" von IT Systemen in der heutigen Zeit eine sehr wichtige Eigenschaft dar?
  + Weil immer mehr Daten übertragen werden und somit ein dauerhafter Datenverkehr besteht. Wenn dieser unterbrochen wird dann ist der Kunde demnach unzufrieden.





+ Welche anderen Massnahmen neben einer Lastverteilung müssen getroffen werden, um die "Hochverfügbarkeit" sicher zu stellen?
  + Die Ausfallsicherheit des Netzes oder des Servers





+ Was versteht man unter "Session Persistenz" und welche Schwierigkeiten ergeben sich damit?
  + Wenn ein Benutzer bei eine bestimmten eine Session startet werden alle weiteren Anfragen in dieser Session an den selben Server gesendet auch "Sticky Session" genannt.
  + Das heißt es kann aber leicht passieren, dass ein Server sehr stark ausgelastet sein. Was in weitere Folge zu einem Datenverlust führen kann wenn der Client zu einem anderen Server verschoben werden soll



+ Nennen Sie jeweils ein Beispiel, wo Session Persistenz notwendig bzw. nicht notwendig ist.

  + Bei einer Online-Banking Software wäre es sinnvoll, dass die Anfragen beim selben Server landen.

+ Welcher Unterschied besteht zwischen einer "server-side" bzw "client-side" Lastverteilungslösung?

  + Server-Side
    + Der Server entscheidet welcher Server die Anfrage bekommt und kann somit den am wenigsten ausgelasteten auswählen
  + Client-Side
    + Der Client hat eine Liste der Server und wählt einen zufällig aus. Jedoch gibt es einige Vorteile in den Bereichen Fehlertoleranz, Caching und Batching

+ Was versteht man unter dem "Mega-Proxy-Problem"?

  Wenn ein Client eine Anfrage schickt wir meist die Source-IP-Adresse verwendet um weiter Anfrage auch gleich an den richtigen Server zu schicken. Dies kann aber zu Problemen führen.

  In Umgebungen mit mehreren Proxys haben Clientanforderungen häufig unterschiedliche Source-IP-Adressen, auch wenn sie vom selben Client gesendet werden. Dies führt zu einer schnellen Vervielfachung von Persistenzsitzungen, in denen eine einzige Sitzung erstellt werden sollte. Dieses Problem wird als "Mega-Proxy-Problem" bezeichnet. Dies kann man ganz einfach mithilfe von Cookies lösen.


## Quellen
* [Comparing Load Balancing Algorithms](https://www.jscape.com/blog/load-balancing-algorithms)
* [Java RMI Tutorial - PI Calculation](https://docs.oracle.com/javase/tutorial/rmi/overview.html)
* [Weighted Round Robin](https://medium.com/@wolfbang/load-balance-algorithm-with-java-e7fb55fe788a)
* [Sticky Session](https://www.imperva.com/learn/availability/sticky-session-persistence-and-cookies/)
* [Server-Side vs Client-Side](http://soaessentials.com/client-side-load-balancing-vs-server-side-load-balancing-how-client-side-load-balancing-works/)
* [Mega-Proxy](https://docs.citrix.com/en-us/netscaler/12/load-balancing/load-balancing-persistence/source-ip-persistence.html)
