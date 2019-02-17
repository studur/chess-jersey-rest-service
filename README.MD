 # Application de calcul de la cote échiquéenne

[![Build Status](https://travis-ci.org/studur/chess-jersey-rest-service.svg?branch=master)](https://travis-ci.org/studur/chess-jersey-rest-service)
[![Coverage Status](https://coveralls.io/repos/github/studur/chess-jersey-rest-service/badge.svg?branch=master)](https://coveralls.io/github/studur/chess-jersey-rest-service?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/2d59811af9f71f0b1528/maintainability)](https://codeclimate.com/github/studur/chess-jersey-rest-service/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/2d59811af9f71f0b1528/test_coverage)](https://codeclimate.com/github/studur/chess-jersey-rest-service/test_coverage)

## Introduction

Cette application calcule les cotes des joueurs d'échecs suite à un tournoi de parties cotées selon les règles de la Fédération Québécoise des Échecs.

## Règles des calcul

source http://fqechecs.qc.ca/story/le-calcul-de-la-cote

### L'ordre de cotation

Un joueur peut avoir une cote provisoire ou permanente, ou encore être sans cote, et les règles de cotation s’appliquant à chacune de ces catégories diffèrent. Pour chaque compétition, on cote les joueurs dans l’ordre suivant: sans cote; avec une cote provisoire; avec une cote permanente.

### Les joueurs sans cote

Un joueur est sans cote s’il n’a pas encore joué dans une compétition cotée. Sa première cote est déterminée par sa «cote-performance», calculée comme suit:

Cote = Cm + 400 * (V - D) / nombre de parties jouées

où Cm représente la cote moyenne des adversaires,
V le nombre de victoires et D le nombre de défaites.

Si un joueur sans cote affronte d’autres joueurs sans cote, on attribue provisoirement une cote de 1100 à ses adversaires non cotés. Si la cote-performance d’un joueur sans cote n’atteint pas 1200, on lui ajoute un rajustement de 50% de la différence. Exemple: 800 devient 1000, 900 devient 1050, etc.

### Les joueurs avec une cote provisoire

La cote d’un joueur est provisoire lorsqu’il a joué 24 parties ou moins avant la tenue de la compétition. Sa nouvelle cote (qui devient permanente après la compétition si le nombre total de parties jouées excède 24) est déterminée à partir de la formule utilisée pour les joueurs sans cote.

La nouvelle cote est la moyenne de l’ancienne cote et de la cote-performance pour la compétition, pondérée selon le nombre de parties représentées par chacune. Alors, si un joueur a une cote provisoire de 1462, basée sur 12 parties jouées, et réalise une cote performance de 1630 lors d'un tournoi de 4 rondes, sa nouvelle cote est calculée comme suit:

Cote = (12*1462)+(4x1630)/12+4

Il n’y a pas de rajustements pour les cotes provisoires qui n’atteignent pas 1200.

### Les joueurs avec une cote permanente

La cote d’un joueur devient permanente après le tournoi qui contient sa 25e partie cotée. Pour chaque tournoi subséquent, on détermine sa nouvelle cote à l’aide de la formule:

Cote = Ca + 32 x (R - Rp)

où Ca représente l’ancienne cote permanente, R la somme des résultats obtenus durant le tournoi et Rp la somme des «résultats probables» déterminés à partir de la différence de cote entre les deux joueurs qui s’affrontent (voir le tableau suivant).

Pour chaque partie, une victoire vaut 1 point, une partie nulle 0,5 point et une défaite 0 point. On additionne tous les résultats pour obtenir R, mais sans compter les points obtenus suite a des repos ou à des exemptions.

Des bonis en points de cote sont accordés après une compétition lorsque l’augmentation de cote d’un joueur dépasse la limite suivante:

24 points pour les tournois de 4 rondes et ensuite
26 points pour 5 rondes,
28 points pour 6 rondes, etc.

Le joueur reçoit alors un point de boni pour chaque point de cote gagné dépassant la limite.

D’autre part, lorsque la cote d’un joueur dépasse 2300, soit avant, soit durant une compétition, tous les changements ultérieurs de cote sont divisés par deux pour déterminer la nouvelle cote.

Enfin, l’augmentation de cote acquise lors d’un match ne peut excéder 50 points.