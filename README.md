# Semplice Blocco Note con Password e Salvataggio in SQLite tramite Android Room

## Introduzione

Questa è una semplice applicazione **Blocco Note** che permette agli utenti di:
- Impostare una **password** per proteggere l'accesso (la password predefinita è `4848`).
- Salvare le note in un **database SQLite** tramite **Android Room** per una gestione sicura e persistente dei dati.

## Funzionalità

- **Login** protetto da password: l'app richiede che venga inserita la password corretta per accedere alle note.
- **Gestione delle Note**: puoi aggiungere, modificare,eliminare e visualizzare le note.
- **SQLite con Android Room**: i dati vengono salvati in un database SQLite, consentendo un accesso rapido e sicuro alle informazioni.

## Componente Tecnico

- **Android Room** è utilizzato per la gestione del database SQLite.
- **Jetpack Compose** è utilizzato per l'interfaccia utente moderna e reattiva.
- **ViewModel** gestisce la logica aziendale e la persistenza dei dati.

### 1. **SqlController**
- gestisce i dati
### 2. **GenericController**
- gestisce i   **meta-dati**


