# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v1.2.4] - 2025-11-26
### :sparkles: New Features
- [`db76d55`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/db76d55f7307346817a53eedae8a0f011c880ae6) - Changed Necrocloak to body slot from charm slot *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`69adc2e`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/69adc2e57bac6f0a20912cd55fbd0339eb35e467) - GuiLichTome crash *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`dad2c98`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/dad2c98f40f3b0179f9ffb4c14863a6bc522259d) - Fixed crystal pyramid layer 2 for neutral (magic) crystals. Previously layer 2 only gave its mana regeneration effect when a layer 3 was also present. *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.2.1] - 2024-06-23
### :sparkles: New Features
- [`35bff54`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/35bff5491037e4e8c2f97433e28a20fd68f5d126) - Added a crafting recipe for soul receptacle *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`d102ca8`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/d102ca8840978e64b624f28760ceb28a16a315fc) - Fixed typo in en_us.lang *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`ccbcde1`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/ccbcde180922da86c18eae730eb8fa8163053299) - Fixed the lunar cycle timer for lich spell gaining through the receptacle *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.2.0] - 2024-06-16
### :sparkles: New Features
- [`71f250f`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/71f250f820900ab128f6a306c3c349afb66eb30c) - Zombie morph has the ability to locate nearby players *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`1ae2bf2`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/1ae2bf2132740f2a5f91b0534b8410ef4d0704e3) - Lowered Bat Ring tier to Uncommon *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`b47fb22`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/b47fb225374912739c3c669574fdc7d8f0c25c01) - The Soul Phylactery now refills itself when a minion dies which was summoned using Soul Conjuration *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`27bf916`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/27bf916710c44922a941973e75a2fbc02da4c770) - Implemented the multiblock Soul Receptacle structure for liches. A 5-layer pyramid, made out of elemental crystal blocks, with the Soul Receptacle block in the middle (similar to vanilla beacons) for liches. Each layer has unique effects, based on the crystals. See *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`711a876`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/711a876eaa7c696380c7d406c99867cd72a648c5) - Added 7 new lich-only spells *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`450417b`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/450417bf3bdf33ab8985e2e5a677efc247600e9e) - Added a new curse (earth element) - Curse of Lycanthropy *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`c2bc84a`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/c2bc84aa57f9dc95b0a905daf46a0c8a591e1488) - Added 10 new artefacts *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`6bb75dc`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/6bb75dc71e0c4ff3bc71644b7dae8ba2b62ce227) - Wand Duration Modifier now affect self morph spells *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`05ed902`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/05ed9028b6ebbfbac51ee16a0fae35f23aca2d70) - Fixed issue with /setlich that causes players that were set to be human again still reverted to lich morp form on relogging *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.0.2] - 2023-02-03
### :sparkles: New Features
- [`1f3fa6a`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/1f3fa6a3979430e539219533755b8a66a637cb6c) - Added Chinese translation (by LILPR1NC3) *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`2689ab5`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/2689ab509ba0bcf81539e7749c417cc01d988eb1) - Liches can fill Soul phials from their hotbar *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`1cdb2ee`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/1cdb2ee36e4ef09a950efba48cc101046ee618c5) - Won't spawn lich particles when the invisibility potion effect is active. Fixes [#14](https://github.com/WinDanesz/RiseOfTheAnimagus/pull/14) *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`87f6e6d`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/87f6e6de79f12dae28f65522ed361f9987dc0bd6) - Liches can't drown in water anymore (as they are undead). Fixes [#16](https://github.com/WinDanesz/RiseOfTheAnimagus/pull/16) *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`0160cc4`](https://github.com/WinDanesz/RiseOfTheAnimagus/commit/0160cc4b9bdf003ed4a01e61001d7c00fb82964a) - Fixed some typos. Fixes [#1](https://github.com/WinDanesz/RiseOfTheAnimagus/pull/1) *(commit by [@WinDanesz](https://github.com/WinDanesz))*


## [v1.0.1] - 2022-10-16
### :sparkles: New Features
- [`80187b2`](https://github.com/WinDanesz/BackportedStuff/commit/80187b2727cfc10695219c2e66d3b2871274e971) - Added Polished Deepslate, Deepslate Brick and all of their block variants *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`a9c2c74`](https://github.com/WinDanesz/BackportedStuff/commit/a9c2c74ee3b3fe3ae8b22fb4fcb79cc5c4c783f5) - Added Deepslate Tile Wall, Deepslate Brick Wall *(commit by [@WinDanesz](https://github.com/WinDanesz))*

### :bug: Bug Fixes
- [`364e0f3`](https://github.com/WinDanesz/BackportedStuff/commit/364e0f32381c70a08aa9ee941e6eae0210896b63) - Added missing smelting recipes for deepslate ores. Fixes [#1](https://github.com/WinDanesz/BackportedStuff/pull/1) *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`30ad85e`](https://github.com/WinDanesz/BackportedStuff/commit/30ad85ebee460da297c8d4dd988592c6661aa7b8) - Fixed missing Deepslate Gold Ore translation key *(commit by [@WinDanesz](https://github.com/WinDanesz))*
- [`45bb9b0`](https://github.com/WinDanesz/BackportedStuff/commit/45bb9b0cbdb4d39bddfe804b04fd37425c7bf9a2) - Fixed deepslate facings. Fixes [#2](https://github.com/WinDanesz/BackportedStuff/pull/2) *(commit by [@WinDanesz](https://github.com/WinDanesz))*


[v1.0.1]: https://github.com/WinDanesz/BackportedStuff/compare/v1.0.0...v1.0.1
[v1.0.2]: https://github.com/WinDanesz/RiseOfTheAnimagus/compare/v1.0.1...v1.0.2
[v1.2.0]: https://github.com/WinDanesz/RiseOfTheAnimagus/compare/v1.1.0...v1.2.0
[v1.2.1]: https://github.com/WinDanesz/RiseOfTheAnimagus/compare/v1.2.0...v1.2.1
[v1.2.4]: https://github.com/WinDanesz/RiseOfTheAnimagus/compare/v1.2.1...v1.2.4
