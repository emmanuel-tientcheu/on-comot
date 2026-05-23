# Contexte 4 : Paiement & Transaction externe

## Entités
- `TransactionPaiement`
- `LogPaiement`

## Value Objects
- `Montant`
- `TelephoneMobileMoney`
- `StatutTransaction` (`EN_ATTENTE`, `REUSSIE`, `ECHOUEE`)
- `ExpirationDate`

## Agrégat racine
**TransactionPaiement**
- Contient `LogPaiement[]`
- Référence une `Vente`

## Structure
TransactionPaiement
├── montant
├── telephone
├── statut
├── expiration_date
├── vente_id
└── LogPaiement[]

## Règles métier

| ID | Règle |
|----|-------|
| PAI1 | Toute tentative de paiement est logguée (réussie ou échouée) dans `LogPaiement` |
| PAI2 | Une transaction `EN_ATTENTE` expire après 10 min → `ECHOUEE` |
| PAI3 | Paiement réussi → déclenche `Vente.status = PAYEE` + création `Ticket` |
| PAI4 | Un même `transactionId` externe (Orange/MTN) ne peut pas être utilisé deux fois |
| PAI5 | Pendant `EN_ATTENTE`, la `Vente` est en "réservation" (quota consommé temporairement) |