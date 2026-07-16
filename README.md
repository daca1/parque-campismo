# Campsite Management System — Java OOP Project 🏕️

Full object-oriented analysis, design, and implementation of a campsite management system ("Gestão de um Parque de Campismo"), built as an individual university coursework project. Covers both graded phases: the Phase 1 UML/requirements design and the Phase 2 Java console application built on top of it.

**Course:** Fundamentos de Programação Orientada a Objetos (Object-Oriented Programming Fundamentals)
**Program:** CTeSP TPSI, Escola Superior de Tecnologia de Setúbal — Instituto Politécnico de Setúbal
**Academic year:** 2025/2026

## What the system does

Manages a campsite front-office end to end: registering guests ("utentes") and their units (motorhome, caravan, or tent) into plots ("talhões"), tracking stays ("entradas") from check-in to check-out, and issuing invoices with seasonal pricing, tourist tax, and VAT applied. It's a menu-driven Java console application with full data persistence between sessions.

## Features

- **Guests (Utentes):** register with polymorphic ID documents (Citizen Card, Passport, Driving License, or a generic document auto-issued to children under 5), search, update contact details, delete (only if never checked in)
- **Units (Unidades):** motorhomes, caravans, and tents, each with type-specific attributes; size automatically determines the pricing tier (e.g., large vs. small motorhome)
- **Plots (Talhões):** organized into color-coded sectors, each with a capacity, optional WiFi/electricity, and single-stay occupancy enforcement
- **Stays & reservations (Entradas):** check guests and units into plots, future-dated entries automatically become reservations, guests/units can be added or removed mid-stay
- **Invoicing (Faturas):** seasonal pricing (high season Jun 15 – Sep 15 vs. low season), 25% VAT, €1/day tourist tax for foreign adult guests (VAT-exempt), invoices are immutable once closed and can only be voided, never edited
- **Persistence:** the entire park state is saved to a single file via Java serialization and reloaded automatically on the next run
- **Two ready-to-use test datasets** plus the option to start from an empty park

## Project structure

```
parque-campismo/
├── src/parquecampismo/
│   ├── Main.java                 # Entry point
│   ├── FrontOffice.java          # Console menu system
│   ├── ParqueCampismo.java       # Main orchestrator / master registries
│   ├── Entrada.java              # Stay/reservation
│   ├── Fatura.java               # Invoice
│   ├── Utente.java               # Guest
│   ├── Identificacao.java        # Abstract ID document + subclasses
│   ├── Unidade.java              # Abstract unit + Autocaravana/Caravana/Tenda
│   ├── Talhao.java, Setor.java   # Plots and sectors
│   ├── Precario.java, Servico.java, Epoca.java, TaxaTuristica.java  # Pricing
│   ├── GestorFicheiros.java      # Save/load via serialization
│   ├── ConsoleUtils.java         # Input helpers
│   └── ...                       # enums (TipoUnidade, EstadoEntrada, etc.)
└── docs/
    ├── manual_utilizador.md      # User manual (Phase 2)
    ├── relatorio_fase1.pdf/.docx # Phase 1 report
    ├── enunciado.pdf             # Official assignment brief
    ├── especificacao_sistema.docx
    ├── anexo1_metodologia.pptx
    └── template_relatorio.docx
```

## How to run

Requires Java 11+. With NetBeans: `File > Open Project`, select the repository's root folder (the one containing `build.xml` and `nbproject/`) — NetBeans will recognize it as an Ant-based Java project. Then just hit Run. From the command line:

```bash
cd src
javac parquecampismo/*.java
java parquecampismo.Main
```

On first run, choose one of two pre-configured test parks (or start empty), then use the menu to manage guests, units, stays, and invoices. See [`docs/manual_utilizador.md`](docs/manual_utilizador.md) for a full guided walkthrough of every menu.

## Assignment brief

This is an **individual** project (not a group assignment) for Fundamentos de Programação Orientada a Objetos. The official brief — full requirements, pricing tables, plot-occupancy rules, and the grading rubric — is in [`docs/enunciado.pdf`](docs/enunciado.pdf), with the base specification document it was built from in [`docs/especificacao_sistema.docx`](docs/especificacao_sistema.docx) and the noun/verb analysis methodology (CRC cards) in [`docs/anexo1_metodologia.pptx`](docs/anexo1_metodologia.pptx).

The project runs in three graded phases:

| Phase | Deliverable | Weight |
|---|---|---|
| 1 | Requirements analysis & UML design | 35% |
| 2 | Full Java implementation in NetBeans, prototype with all required features | 65% |
| — | Mandatory oral defense each phase | pass/fail on code ownership |

Submissions go through Moodle and are checked with MOSS for plagiarism; each phase also requires defending the code orally to prove it's genuinely the student's own work. **Both phases were already submitted, orally defended, and graded** — this repository is a portfolio-polished rebuild of the same design for public display, not a live or resubmittable assignment.

## Methodology

Requirements were analyzed using the noun/verb method: entities and actions were extracted directly from the assignment brief, cross-referenced into entity–action–description tables, then modeled into class candidates with attributes and methods. Use case diagrams were sketched for the main flows, and every ambiguity or inconsistency found in the brief was documented along with the design decision taken to resolve it. Phase 2 implements that design directly in Java, preserving every documented decision from Phase 1.

## Key design decisions

- **Identification is polymorphic:** `Identificacao` is an abstract superclass with `CartaoCidadao`, `Passaporte`, `CartaConducao`, and `DocumentoGenerico` (auto-assigned to children under 5 without their own ID) as subclasses.
- **Units share a common contract:** `Unidade` is an abstract superclass for `Autocaravana`, `Caravana`, and `Tenda`, each with different attributes (dimensions, plate, driver) but a common association to a plot. Motorhomes and caravans already include electricity/WiFi in their daily rate, so the system blocks adding those as extra paid services to them.
- **A stay ("Entrada") aggregates guests, units, and plots** and can only be removed once it has none of either. The responsible guest can only be removed once they're the last guest left, which is what actually makes an empty, closeable stay reachable.
- **Invoices are immutable once closed** — they can only be voided ("anulada"), never edited or deleted, matching real invoicing rules. "Printing" an invoice means writing it to a text file, per the brief's own wording.
- **Pricing is seasonal:** high season (Jun 15 – Sep 15) vs. low season, applied per unit type and guest type, plus a €1/day tourist tax for foreign adult guests, exempt from VAT.
- **Persistence via serialization:** the whole park graph (sectors, plots, guests, units, stays, invoices) is saved/loaded as a single object graph with `ObjectOutputStream`/`ObjectInputStream` rather than a hand-rolled file format or a database.

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

The complete Phase 1 write-up — entity/verb tables, full class candidate table with attributes and methods, all use case scenarios, and every documented assumption — is in [`docs/relatorio_fase1.pdf`](docs/relatorio_fase1.pdf) (Portuguese).

## Documents

- [`docs/manual_utilizador.md`](docs/manual_utilizador.md) — Phase 2 user manual, a guided tour of every menu in the console app
- [`docs/relatorio_fase1.pdf`](docs/relatorio_fase1.pdf) / [`.docx`](docs/relatorio_fase1.docx) — the Phase 1 deliverable (this student's analysis and design work)
- [`docs/enunciado.pdf`](docs/enunciado.pdf) — official assignment brief, pricing tables, occupancy rules, and grading rubric
- [`docs/especificacao_sistema.docx`](docs/especificacao_sistema.docx) — base system specification the noun/verb analysis was applied to
- [`docs/anexo1_metodologia.pptx`](docs/anexo1_metodologia.pptx) — noun/verb + CRC card methodology reference
- [`docs/template_relatorio.docx`](docs/template_relatorio.docx) — report template provided by the instructor

## Status

✅ Phase 1 — Requirements analysis & OOP design
✅ Phase 2 — Full Java implementation (console app, persistence, invoicing)

## Author

Diego Teran

## License

MIT — see [LICENSE](LICENSE).
