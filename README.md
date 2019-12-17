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

Die Methode funktioniert so, dass immer die Engine mit den geringsten aktiven Verbindungen ausgewählt wird.


## Quellen
* [Comparing Load Balancing Algorithms](https://www.jscape.com/blog/load-balancing-algorithms)
* [Java RMI Tutorial - PI Calculation](https://docs.oracle.com/javase/tutorial/rmi/overview.html)
* [Weighted Round Robin](https://medium.com/@wolfbang/load-balance-algorithm-with-java-e7fb55fe788a)
