# Campsite Management System — Analysis & Design (Phase 1) 🏕️

Object-oriented analysis and design for a campsite management system ("Gestão de um Parque de Campismo"), built as a university coursework project. This repository covers **Phase 1**: requirements analysis, entity/class modeling, and UML design — no implementation code yet (that's Phase 2).

**Course:** Fundamentos de Programação Orientada a Objetos (Object-Oriented Programming Fundamentals)
**Program:** CTeSP TPSI, Escola Superior de Tecnologia de Setúbal — Instituto Politécnico de Setúbal
**Academic year:** 2025/2026

## What the system does

Manages a campsite front-office end to end: registering guests ("utentes") and their units (motorhome, caravan, or tent) into plots ("talhões"), tracking stays ("entradas") from check-in to check-out, and issuing invoices with tourist tax and VAT applied. The system was designed to be implemented in Java, console-based, in NetBeans.

## Methodology

Requirements were analyzed using the noun/verb method: entities and actions were extracted directly from the assignment brief, cross-referenced into entity–action–description tables, then modeled into class candidates with attributes and methods. Use case diagrams were sketched for the main flows, and every ambiguity or inconsistency found in the brief was documented along with the design decision taken to resolve it.

## Key design decisions

- **Identification is polymorphic:** `Identificacao` is an abstract superclass with `CartaoCidadao`, `Passaporte`, `CartaConducao`, and `DocumentoGenerico` (auto-assigned to children under 5 without their own ID) as subclasses.
- **Units share a common contract:** `Unidade` is an abstract superclass for `Autocaravana`, `Caravana`, and `Tenda`, each with different attributes (dimensions, plate, driver) but a common association to a plot.
- **A stay ("Entrada") aggregates guests, units, and plots** and can only be removed once it has none of either. Removing a guest/unit from an open stay closes it (issuing an invoice) and opens a new one for whoever remains.
- **Invoices are immutable once closed** — they can only be voided ("anulada"), never edited or deleted, matching real invoicing rules.
- **Pricing is seasonal:** high season (Jun 15 – Sep 15) vs. low season, applied per unit type and guest type, plus a €1/day tourist tax for foreign adult guests.

## UML class diagram

![Class diagram](diagrams/class-diagram.png)

## Use case diagrams

**Check-in — registering a stay with a unit**

![Use case: check-in](diagrams/use-case-registo-entrada.jpg)

**Check-out — closing a stay and issuing an invoice**

![Use case: checkout](diagrams/use-case-checkout.jpg)

**Guest management — create, search, update, remove**

![Use case: guest management](diagrams/use-case-gestao-utentes.jpg)

## Full report

The complete write-up — entity/verb tables, full class candidate table with attributes and methods, all use case scenarios, and every documented assumption — is in [`docs/relatorio_fase1.pdf`](docs/relatorio_fase1.pdf) (Portuguese).

## Status

✅ Phase 1 — Requirements analysis & OOP design (this repo)
⏳ Phase 2 — Java implementation (not started yet)

## Author

Diego Teran

## License

MIT — see [LICENSE](LICENSE).
