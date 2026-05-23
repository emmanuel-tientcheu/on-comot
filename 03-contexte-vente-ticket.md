# Contexte 3 : Vente & Ticket

## Entités
- `Vente`
- `Ticket`

## Value Objects
- `VenteId`
- `TicketId`
- `Prix`
- `StatutVente` (`EN_ATTENTE`, `PAYEE`, `ECHOUEE`, `REMBOURSEE`)
- `StatutTicket` (`NON_UTILISE`, `UTILISE`)
- `QRCode` (signé cryptographiquement)

## Agrégat racine
**Vente** (cœur du système)
- Contient un `Ticket` (après paiement réussi)
- Référence `event_id` (obligatoire)
- Référence `type_ticket_id` (obligatoire)
- Référence `schedule_id` (nullable)

## Structure
Vente
├── event_id (obligatoire)
├── type_ticket_id (obligatoire)
├── schedule_id (nullable)
├── prix (copié du TypeTicket au moment de la vente)
├── statut (EN_ATTENTE / PAYEE / ECHOUEE)
└── ticket_id (nullable, généré après paiement)

Ticket
├── qr_code (signé)
├── statut (NON_UTILISE / UTILISE)
└── vente_id

## Règles métier

| ID | Règle |
|----|-------|
| VNT1 | Une vente a toujours `event_id` + `type_ticket_id` non nuls |
| VNT2 | Le `prix` de la vente est figé au moment de la création (copie du prix du TypeTicket) |
| VNT3 | Si `schedule_id` est NULL → vente **directe événement** (pas de contrainte horaire) |
| VNT4 | Si `schedule_id` est renseigné :
- Vente autorisée uniquement pendant la plage horaire du schedule
- On applique le `quota_journalier` du schedule si défini |
  | VNT5 | Le `quota_total_par_type` limite les ventes PAYEE pour ce type (tous schedules confondus + direct) |
  | VNT6 | Le `quota_total_event` (si défini) limite la somme de **tous les types** |
  | VNT7 | Une vente commence en `EN_ATTENTE` → après paiement réussi → `PAYEE` + génération Ticket |
  | VNT8 | Après échec ou timeout (10 min) → statut `ECHOUEE` |
  | VNT9 | Un `Ticket` a un `StatutTicket` (`NON_UTILISE` → `UTILISE` irréversible) |
  | VNT10 | Un ticket est lié à un `TypeTicket` (hérite son prix, mais le prix est déjà dans Vente) |