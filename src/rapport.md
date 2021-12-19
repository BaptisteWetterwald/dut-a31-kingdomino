_Baptiste WETTERWALD - Hamza LOUADA - Gr2_

##Rapport A31 - Version 1 (19/12/2021)

###Choix de conception :

**Modèle MVC :**

Nous suivons les règles du modèle MVC (Model, View, Controller).

**Classe Game :**

Cette classe se charge de la gestion du déroulement d’une partie de KingDomino.
Elle instancie les vues (observers

**Classe Player :**

Représente un joueur possédant un pseudo et un royaume (Kingdom). Elle nous permet de savoir si le joueur vient de jouer et quel domino il a joué.

**Classe Kingdom :**

Cette classe représente le royaume d’un joueur. Il est représenté sous forme d’un tableau 2 dimensions 5*5. On peut, grâce à cette classe, connaître les emplacements libres du royaume et ainsi placer les différents dominos.

**Classe Domino :**

Un domino possède un identifiant (qui ne sera pas connu du joueur puisqu’il n’a d’intérêt que lorsqu’on détermine le nouvel ordre de jeu) et est composé de deux Tiles (les deux cases du domino). La classe nous permet également de tourner le domino dans n’importe quel sens afin de bien pouvoir le placer.

**Classe Tile :**

Une Tile correspond à un demi-domino, ayant un Biome (environnement) et possédant un nombre de couronnes.

**Enum Biome:**

Énumération de tous les environnements existants dans le jeu. Chacun d’entre eux est associé à un code couleur. Le biome CASTLE agit comme un joker, c’est-à-dire qu’il est compatible avec tous les autres biomes. Chaque biome est associé à une couleur, utilisée pour colorier la Tile dans la grille du Kingdom.

**Classe Castle :**

Correspond au château du joueur. Il s’agit d’une sous-classe de Tile ayant un biome CASTLE.

**Classe Wallet :**

Correspond à une petite pioche de dominos. Sa taille dépend du nombre de joueurs. Cette pioche se rétrécit au fur et à mesure que les joueurs jouent leur tour dans le round en question. Elle est réinitialisée (avec des dominos tirés aléatoirement) à chaque nouveau round.

**Classe CSVReader:**

Cette classe nous permet de lire le fichier “.csv” qui nous a été fourni et ainsi d’instancier tous les dominos du jeu.

**Class Constants :**

Contient toutes les constantes que nos classes utilisent. Permet de changer les paramètres du jeu depuis une seule et même classe.

**Interface IObserver :**

Nous avons choisi d’utiliser le design pattern Observer afin de faire réagir les vues au comportement d’autres objets.

**Class KingdomObserver :**

Cette classe correspond à un observateur de la classe Kingdom. Elle s’actualise lorsque la grille du Kingdom associé est modifiée. Elle est instanciée une fois par joueur.

**Class WalletObserver :**

Cette classe correspond à un observateur de la pioche des dominos à choisir. Il n’en existe qu’une instance par partie.

**Class Observable :**

C’est une classe abstraite, superclasse de tous les objets pouvant être observés (Kingdom, Wallet).

**Class Board :**

Gère l’affichage du plateau de jeu complet : il contient le Wallet, les Kingdoms et la sélection/modification du domino avant placement dans l’un des Kingdoms.

###Notre exécutable :
Notre exécutable est un fichier .JAR contenant à la fois nos classes compilées et le fichier CSV permettant d’instancier nos différents dominos.
Il suffit de le lancer (java -jar a31-kingdomino-fork) pour que l’application démarre.

###Ce que nous avons déjà fait :
Avant que la partie ne commence, une interface est proposée au joueur : il choisira le nombre de joueurs et les variantes (harmonie/empire du milieu) avec lesquelles il souhaite jouer.
Ensuite, le jeu démarre : les joueurs vont, chacun leur tour, choisir un domino dans la liste de dominos à droite dans l’interface (c’est ce qu’on a appelé “Wallet”). Une fois le domino sélectionné, ils pourront le tourner comme ils le souhaitent, puis le placer sur leur royaume (grille de 5*5) ou bien choisir de passer leur tour (s’ils ne peuvent pas poser de domino parmi ceux disponibles dans le wallet), empêchant ainsi les autres joueurs de choisir le domino sélectionné. L’ordre des joueurs pour le tour suivant dépend du domino choisi.
Pour l’instant, le calcul du score s’effectue à la fin du tour de chaque joueur, mais ne prend pas en compte les variantes.

###Ce qu’il nous reste à coder :
- Des flèches pour déplacer les éléments dans la grille du joueur, lui permettant de choisir de respecter ou non la variante Empire du Milieu.
- Le calcul du score avec les variantes.
- L’affichage des scores à la fin.
- La vérification pour la variante Harmonie : toutes les cases utilisées ou non.
