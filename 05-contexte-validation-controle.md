# Contexte 1 : Organisation & Équipe

## Entités
- `Organisation`
- `Utilisateur`
- `Role`
- `Permission`

## Value Objects
- `NomOrganisation`
- `EmailUtilisateur`
- `RoleName` (ex: "ADMIN", "CONTROLEUR", "VENDEUR")

## Agrégat racine
**Organisation**
- Contient `Utilisateur[]`
- Contient `Role[]`

## Règles métier

| ID | Règle |
|----|-------|
| ORG1 | Une organisation a au moins un admin |
| ORG2 | Un utilisateur peut avoir plusieurs rôles dans la même organisation |
| ORG3 | Les permissions sont : `CREER_EVENT`, `GERER_SCHEDULE`, `VOIR_VENTES`, `VALIDER_TICKET`, `VENDRESUR_PLACE` |
| ORG4 | Seul un admin peut créer/modifier des rôles |

## Structure
Organisation (1)
│
└── Utilisateur (n)
│
└── Role (n)
│
└── Permission (n)