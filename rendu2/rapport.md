_Baptiste WETTERWALD - Hamza LOUADA - Groupe 2_

# **Rapport A31 - Version 2 (09/01/2022)**

## **Présentation des classes (listées dans l'ordre proposé par IntelliJ) :**

## **Package Controller :**

### **Classe GameController :**

La vue GameView, n’ayant pas le droit de modifier le modèle, appelle les méthodes de ce contrôleur qui s’en charge. Il permet également d’ajouter des observateurs (IObserver) à des objets observables (qui héritent de la classe Observable).

### **Classe ParametersController :**

Ce contrôleur permet la mise en place des paramètres du jeu (ajout des joueurs et des variantes utilisées), il est utilisé par la vue ParametersGUI.


## **Package Model :**

### **Enum Biome :**

Énumération de tous les environnements existants dans le jeu. Le biome CASTLE agit comme un joker, c’est-à-dire qu’il est compatible avec tous les autres biomes.

### **Classe Castle :**

Correspond au château du joueur. Il s’agit d’une sous-classe de Tile ayant un biome CASTLE, ce n’est donc pas un domino puisqu’il ne représente qu'une cellule dans la grille d'un Kingdom. Un Kingdom ne possède qu’un seul Castle.

### **Classe Domino :**

Un domino possède un identifiant (qui ne sera pas connu du joueur puisqu’il n’a d’intérêt que lorsque l'on détermine le nouvel ordre de jeu) et est composé de deux Tiles (les deux cases du domino).
La classe nous permet également de tourner le domino dans n’importe quel sens afin de bien pouvoir le placer dans un Kingdom.

### **Classe Game :**

Cette classe se charge du déroulement d’une partie de KingDomino. C’est elle qui crée les joueurs (bien que l’utilisateur les ajoute depuis la vue) et qui calcule le score des joueurs en tenant compte des variantes (classe GameConstraint) sélectionnées.

### **Classe abstraite GameConstraint :**

Superclasse de toutes les variantes (MiddleKingdom, Harmony).

### **Classe Harmony :**

Variante : elle est respectée si toutes les cellules d’un Kingdom possèdent une Tile (c’est-à-dire qu’il n’y a pas de trou dans la grille).

### **Interface IObserver :**

Interface que les observateurs implémentent (voir plus bas).

### **Classe Kingdom :**

Chaque joueur possède un royaume, représenté par un tableau 2 dimensions de 5*5 cellules. On peut, grâce à cette classe, connaître les emplacements libres du royaume et ainsi placer les différents dominos.
Elle possède également une méthode permettant de calculer le score de la grille selon les dominos placés, ainsi qu’une méthode permettant de déplacer ces mêmes dominos dans une direction donnée, rendant ainsi la grille dynamique.
Cette fonctionnalité n’était pas demandée dans le cahier des charges, mais il nous semblait intéressant de la proposer afin de laisser aux joueurs la possibilité de choisir de respecter (ou non) la variante Empire du Milieu même après le commencement de la partie.

### **Classe MiddleKingdom**

Variante : elle est respectée si le château se trouve au milieu du royaume (centre de la grille) à la fin de la partie.

### **Classe abstraite Observable**

C’est une classe abstraite, superclasse de tous les objets pouvant être observés (Kingdom, Wallet et Game).

### **Classe Player :**

Représente un joueur, possédant un pseudo et un royaume (Kingdom). Il possède également un attribut score afin de ne pas avoir à le recalculer trop souvent.

### **Classe Tile :**

Une Tile correspond à un demi-domino, ayant un Biome (environnement) et un nombre de couronnes fixes.

### **Classe Wallet :**

Correspond à une petite pioche de dominos. Sa taille dépend du nombre de joueurs. Cette pioche se rétrécit au fur et à mesure que les joueurs jouent leur tour dans le round en question. Elle est réinitialisée puis remplie avec de nouveaux dominos tirés aléatoirement à chaque nouveau round.

## **Package Utilities :**

### **Classe CSVReader :**

Cette classe nous permet de lire le fichier “.csv” qui nous a été fourni et ainsi d’instancier tous les dominos du jeu.

## **Package View :**

### **Class GameView :**

Gère l’affichage du plateau de jeu complet, c’est-à-dire : du Wallet, des Kingdoms, de la sélection/modification du domino avant placement dans l’un des Kingdoms et de l’écran de fin.

### **Class ParametersGUI :**

Fenêtre permettant à l’utilisateur de choisir le nombre de joueurs et leur pseudo ainsi que de sélectionner les différentes variantes (classe GameConstraint) de la partie (Harmony, MiddleKingdom).

## **Patrons de conception utilisés :**

#### Concernant la conception, nous avons utilisé les patrons de conception suivants :

- Pour la classe CSVReader, nous utilisons un “Singleton” car nous n’avons besoin que d’une seule instance de cette classe utilitaire. Il n’y a aucune raison d’en avoir plusieurs.


- Nous avons aussi utilisé un “Template” : en effet, les classes Harmony et MiddleKingdom héritent toutes les deux de la méthode setNewScore() présente dans GameConstraint (puisque son fonctionnement est le même pour les deux classes), mais elles redéfinissent chacune la méthode abstraite respects(), destinée à vérifier si la variante en question est respectée ou pas.


- Nous utilisons également le patron “Observer” : GameView observe Game, Wallet et Kingdom. Ceci permet de ne mettre à jour que certains éléments dans l’affichage réalisé par GameView, lorsque c’est nécessaire. Pour ce faire, les trois classes observées appellent la méthode notifyObservers() présente dans Observable, qui appellera elle-même une méthode update() dans GameView.

## **Notre exécutable :**
 
Notre exécutable est un fichier .JAR contenant à la fois nos classes compilées, le fichier CSV permettant d’instancier nos différents dominos, et les images nécessaires au bon affichage de la vue ParametersGUI. Il suffit de le lancer (avec la commande “java -jar a31-kingdomino-fork”, ou en double-cliquant sur l’exécutable si l’OS le permet) pour que l’application démarre.

## **Déroulement d’une partie :**
Avant que la partie ne commence, une interface est proposée au joueur : il y choisira le nombre de joueurs et les variantes (harmonie/empire du milieu) avec lesquelles il souhaite jouer.

Ensuite, le jeu démarre : les joueurs vont, chacun leur tour, choisir un domino dans la liste de dominos située à droite dans l’interface (ce que l’on a appelé “Wallet”). Une fois le domino sélectionné, ils pourront le tourner comme ils le souhaitent, puis le placer sur leur royaume (grille de 5*5) ou bien choisir de passer leur tour (s’ils ne peuvent pas poser de domino parmi ceux disponibles dans le wallet par exemple), empêchant ainsi les autres joueurs de choisir le domino sélectionné.
Le joueur peut également, avant de placer un domino ou de passer son tour, déplacer les éléments dans la grille à l’aide des boutons situés au-dessus de son royaume.
Cette action ne mettra pas fin à son tour de jeu.

L’ordre des joueurs pour le tour suivant dépend de la position du domino choisi dans le wallet.

## <span style="color:red">/!\\</span> 
**Lors de l’exécution, il se peut que votre écran n’affiche pas en entier les titres de la fenêtre de paramétrage (selon sa résolution). Si cela se produit, cliquez sur le bouton au bas de l’écran.**