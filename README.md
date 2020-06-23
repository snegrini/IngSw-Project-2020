# Prova Finale di Ingegneria del Software - AA 2019-2020
![alt text](src/main/resources/images/santorini.png)

Implementazione del gioco da tavolo [Santorini](http://www.craniocreations.it/prodotto/santorini/).

Il progetto consiste nell’implementazione di un sistema distribuito composto da un singolo server in grado di gestire una partita alla volta e multipli client (uno per giocatore) che possono partecipare ad una sola partita alla volta utilizzando il pattern MVC (Model-View-Controller).
Interazione e gameplay: linea di comando (CLI) e grafica (GUI).
La rete è stata gestita con l'utilizzo delle socket.

## Documentazione

### UML
I seguenti diagrammi delle classi rappresentano il primo, il modello secondo il quale il gioco dovrebbe essere stato implementato, il secondo contiene invece i diagrammi del prodotto finale nelle parti critiche riscontrate.
- [UML Iniziali](https://github.com/snegrini/ing-sw-2020-kala-lanzi-negrini/blob/master/deliveries/uml/uml_model_initial.jpg)
- [UML Finali](N/A)

### JavaDoc
La seguente documentazione include una descrizione per la maggior parte delle classi e dei metodi utilizzati, segue le tecniche di documentazione di Java e può essere consultata al seguente indirizzo: [Javadoc](N/A)

### Librerie e Plugins
|Libreria/Plugin|Descrizione|
|---------------|-----------|
|__JavaFx__|Libreria grafica|
|__Maven__|Strumento di automazione della compilazione utilizzato principalmente per progetti Java.|
|__JUnit__|Framework di unit test|

## Funzionalità
### Funzionalità Sviluppate
- Regole Complete
- CLI
- GUI
- Socket
- 2 FA (Funzionalità Avanzate):
    - __Persistenza:__ lo stato di una partita deve essere salvato su disco, in modo che la partita possa
        riprendere anche a seguito dell’interruzione dell’esecuzione del server.
    - __Undo:__ permette a un giocatore di annullare la propria
        mossa entro un periodo di 5 secondi da quando l’ha effettuata

### Jars
I Jar del progetto possono essere scaricati dal seguente link: [Jars](N/A).


## Esecuzione
### Santorini Client
Le seguenti istruzioni descrivono come eseguire il client CLI o GUI.

#### CLI
Per lanciare Santorini Client CLI digitare il comando:
```
java -jar santorini-client.jar --cli
```
#### GUI
Per poter lanciare la modalità GUI sono disponibili 2 modalità:
- effettuare doppio click sull'eseguibile ```santorini-client.jar```
- digitare da terminale il comando:
```
java santorini-client.jar
```

### Santorini Server
Per lanciare Santorini Server digitare il comando:
```
java -jar santorini-server.jar [--port <port_number>]
```
#### Parameters
- `--port` `-p` : permette di specificare la porta del server. Se non specificato il valore di default è __16847__;

## Componenti del gruppo
- [__Samuele Negrini__](https://github.com/snegrini)
- [__Andrea Lanzi__](https://github.com/AndreaLanzi-PoliMi)
- [__Samuel Kala__](https://github.com/samuelkala)
