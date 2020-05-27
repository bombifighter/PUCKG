PUCKG - Puck Game
=================

A small logical PvP game with pucks, made as a university course project.

## How to play this game?

Given a 6x6 game table. Each of the two players have 2 pucks at the beginning of the game. There are 2 types of cell in the game:

* Empty cell: where players can put their pucks.

* Black cell: no player is able to place pucks there like the table had a "hole" on that cell. The table has exactly one black cell.

Players make actions alternately. Each player can make one action of these per round:

* Place a new puck next to an existing one, choosing one of its 8 adjacent cells.
* Move and existing puck with 2 cells if the desired cell to be put in is empty. Moving is available vertically, horizontally and diagonally, and by moving it is allowed to "jump over" existing pucks.

After each action made, all pucks of the other player in the 8 adjacent cells to the newly placed or replaced turns into the color of the player who made actions.

The game ends if the next player cannot make actions. The winner is the player with more pucks on the table. In the end the number of empty cells are added to the points of the player who made the last action.

## Requirements

Building the project requires JDK 11 or later and [Apache Maven](https://maven.apache.org/)
