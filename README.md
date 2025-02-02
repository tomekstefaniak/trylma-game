# Trylma

### Autorzy
Błażej Pawluk, Tomasz Stefaniak

### Intrukcja uruchomienia serwera i klienta
Projekt zawiera 3 pliki pom.xml z których ten znajdujący się bezpośrednio pod /chinesecheckers to projekt nadrzędny.  
Do budowania i uruchomienia można wykorzystać gotowe skrypty do basha pod /scripts.
W przypadku uruchamiania skryptów cwd musi być ścieżką do repozytorium. Opis działania skryptów:  
> pkg_client.sh robi clean i package tylko na projekcie podrzędnym klienta  
> pkg_server.sh robi clean i package tylko na projekcie podrzędnym serwera  
> run_client.sh uruchamia klienta  
> run_clients.sh uruchamia n klientów gdzie n podaje się jako argument  
> run_server.sh uruchamia serwer z podanymi argumentami  
