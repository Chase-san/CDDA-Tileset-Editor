# CDDA-Tileset-Editor

A tileset editor for Cataclysm DDA.

Warning: The source is a complete mess. Completely hacked together.

## How to Use

1. `File > Options` and set the path to Cataclysm DDA.
2. `File > Open > Refresh List`
3. `File > Open` and select the tileset you want to edit from the list.
4. Make any edits.
5. Save your changes with `File > Save`.


## How to Build

### Linux
1. Run `sudo apt install openjdk-8-jdk`
2. Run `sudo apt install ant`
3. Navigate to base directory of CDDATSE.
4. Run `ant`

The built editor should be in the `dispatch` folder.


## Current goals

For version 1.0.
* Ability to add or remove preexisting sprites from tiles (fg and bg).
* Ability to add or remove and alter weights on sprites inside tiles that use them.
* Ability to add or remove new tile definitions.

For version 1.1.
* Ability to open tilesets not inside the cdda files.
* Ability to save tilesets as a new tileset (Save As).
* Editing sprite sheet definitions (width, height, offset)
* Multitile editing. 

For version 1.2.
* Create new tilesets.
* Add or remove sprite sheet definitions.
* Fallback sprite handling.


## Licensing

The code and the icon have different licenses, see ICONLICENSE.

In short, you can do just about anything you want with the code, but the icon is mine.
