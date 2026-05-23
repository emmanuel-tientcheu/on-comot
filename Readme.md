# 🎟️ On Comot - Plateforme de billetterie événementielle

## 📖 Présentation générale

**On Comot** est une plateforme de vente de tickets pour événements qui permet à tout organisateur de créer, gérer et vendre ses billets en toute simplicité.

Que vous organisiez un concert, un festival, un séminaire, un match sportif ou tout autre événement, On Comot vous offre les outils nécessaires pour vendre vos tickets en ligne ou sur place, avec un contrôle total sur vos quotas, vos plages de vente et votre équipe.

---

## 🎯 À qui s'adresse On Comot ?

### Pour les organisateurs d'événements
- **Association culturelle** : festival, concert, théâtre
- **Club sportif** : match, compétition, gala
- **Entreprise** : séminaire, after-work, conférence
- **Particulier** : anniversaire, mariage, fête privée

### Pour les acheteurs
- **Grand public** : acheter des tickets pour sortir entre amis ou en famille
- **Groupes** : réservation multiple pour une sortie de groupe

### Pour les contrôleurs
- **Bénévoles ou personnel** : validation des tickets à l'entrée (scan QR code)

---

## ✨ Fonctionnalités principales

### Pour l'organisateur

| Fonctionnalité | Description |
|----------------|-------------|
| **Créer une organisation** | Gérez votre structure, vos événements et vos équipes |
| **Gérer les rôles** | Ajoutez des utilisateurs avec des permissions spécifiques (contrôleur, vendeur, admin) |
| **Créer des événements** | Définissez le nom, la date, le lieu, le quota global |
| **Types de tickets multiples** | Proposez Early Bird, Normal, VIP, Étudiant avec prix et quotas distincts |
| **Vente programmée (Schedule)** | Vendez vos tickets uniquement certains jours avec des quotas journaliers |
| **Vente directe événement** | Vendez des tickets sans contrainte horaire (avant ou pendant l'événement) |
| **Paiement mobile** | Intégration Orange Money et MTN Mobile Money |
| **Suivi des ventes** | Visualisez les tickets vendus, restants et les transactions |
| **Journal des actions** | Tracez toutes les actions critiques (qui a fait quoi et quand) |

### Pour le contrôleur

| Fonctionnalité | Description |
|----------------|-------------|
| **Scanner un ticket** | Validation rapide via QR code |
| **Marquer comme utilisé** | Un ticket ne peut être utilisé qu'une seule fois |

### Pour l'acheteur

| Fonctionnalité | Description |
|----------------|-------------|
| **Achat simple et rapide** | Choisissez votre type de ticket, payez par mobile money |
| **Ticket sécurisé** | QR code unique et signé cryptographiquement |
| **Ticket en mobile** | Recevez votre ticket par SMS ou email |

---

## 🏗️ Architecture technique (DDD)

Le projet est construit sur une architecture **Domain-Driven Design** avec les contextes suivants :

| Contexte | Description |
|----------|-------------|
| **Organisation & Équipe** | Gestion des organisations, utilisateurs, rôles et permissions |
| **Event, Schedule & TypeTicket** | Création d'événements, types de tickets, plages de vente programmées |
| **Vente & Ticket** | Gestion des ventes, génération de tickets avec QR code signé |
| **Paiement & Transaction** | Intégration Orange/MTN, logs de tentatives, timeout 10 min |
| **Validation & Contrôle** | Scan et validation des tickets à l'entrée |
| **Journalisation** | Traçabilité de toutes les actions critiques |


---

## 📊 Flux utilisateur typique

```text
1. Organisateur crée une organisation
2. Organisateur crée un événement avec :
   - Types de tickets (Early Bird = 5000 FCFA, Normal = 7000 FCFA)
   - Quota total = 1000 tickets
   - Schedule (vente programmée le 01/12/2026 avec quota journalier = 200)
3. Acheteur choisit un ticket (Early Bird) et paie par Orange Money
4. Paiement réussi → Ticket généré avec QR code unique
5. Le jour de l'événement, contrôleur scanne le QR code
6. Ticket marqué comme "utilisé" → entrée validée