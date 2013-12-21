LO02 UNO Project
Version du projet : 2.0.0

=====================================
SECTION 01 - Auteurs
---------------------------- 

	* Laurent BIERGE
	* Romain GAGNAIRE

=====================================
SECTION 02 - Notes
-------------------------

Eclipse & Codes ANSI :
	* La version "console" de notre jeu UNO utilise des codes ANSI pour afficher du texte en couleur dans la console
	* Eclipse en prend pas en charge ce code, affichant le texte en noir quoi qu'il arrive, raison pour laquelle le lancement du programme depuis Eclipse est déconseillé
	* Pour avoir la gestion des codes ANSI dans la console d'éclipse, se référer à la SECTION 4

Méthode la plus simple :
	* Il est donc préférable de lancer le JAR présent dans le dossier DIST. Un executable (.exe) est fourni permettant de lancer automatique le programme sous Windows.
	* [1] Pour Mac & Linux, il est fort possible que vous ayez à créer un dossier DIST (dans le dossier DIST --sous-entendu dist/dist) et de copier le fichier de configuration config.ini dans ce dernier (--sous-entendu /dist/dist/config.ini) si jamais vous souhaitez utiliser la fonction de lancement rapide de partie (utilisant une lecture du fichier de configuration)

[1] Ceci pour une raison très simple : 
	* Le programme utilise le fichier de configuration situé dans le dossier DIST.
	* Lors de l'export de l'executable JAR, le dossier contenant l'executable étant le dossier DIST, il est nécessaire de recréer ce même schéma pour pouvoir charger le fichier de configuration
	* De cette manière il est possible d'executer le programme au sein d'Eclipse ET depuis le JAR executable

=====================================
SECTION 03 - Maven
--------------------------

Maven & Eclipse :
	* Ce projet utilise Maven pour gérer automatiquement les dépendances. Cependant, il n'est installé nativement QUE dans la version "Eclipse Java EE IDE for Web Developers" 
	* Pour être lancé depuis Eclipse, le projet peut necessiter l'installation de plugins au sein d'Eclipse en fonction de la version d'Eclipse installée.
	* A noter que l'utilisation de Maven impose le fait que que les classes compilées se trouveront dans le dossier target/classes/*
	
Version d'Eclipse :
	* Pour connaitre la version d'Eclipse, il suffit de cliquer sur "Help" -> "About Eclipse".
	* Pour installer un plugin dans Eclipse, il suffit de cliquer sur "Help" -> "Eclipse Marketplace" et taper dans find le nom associé.

Pour la version "Eclipse Java EE IDE for Web Developers" :
	* Aucun plugin à installer

Pour la version "Eclipse Standard/SDK" (et toutes les autres) :
	* plugin "Maven Integration for Eclipse (Juno and newer)" : http://goo.gl/vhmB4A
	* plugin "Maven Integration for Eclipse WTP (Juno)" : http://goo.gl/pf2Oos

Importation du projet sous Eclipse avec Maven :
	* Il suffit ensuite pour IMPORTER LE PROJET dans Eclipse de faire "File" -> "Import" -> "Existing Maven Project"

=====================================
SECTION 04 - Plugins Recommandés
----------------------------------------------

Pour quelles raisons
	* Si vous souhaitez tester la couverture du code par les tests unitaires : EclEmma
	* Si vous souhaitez avoir un affichage du texte en couleur dans la console d'Eclipse : ANSI Escape in Console

EclEmma :
	* "EclEmma" (http://goo.gl/kUMw96) est recommandé mais pas requis.
	* Méthode d'installation : "Help" -> "Eclipse Marketplace" et taper dans find le nom associé.

ANSI Escape in Console
	* "ANSI Escape in Console" (http://goo.gl/YfzVB4) est dispensable si le programme est lancé directement depuis le jar --méthode recommandée
	* Méthode d'installation : "Help" -> "Install new software" -> Entrer http://www.mihai-nita.net/eclipse dans le champs "Work With" -> Cocher "ANSI console" -> Next/Finish

=====================================
SECTION 05 - Statut de l'application
--------------------------------------------

pom.xml
	* GroupId : Uno_Bierge_Gagnaire
	* ArtifactId : Uno_Bierge_Gagnaire
	* Version : 2.0.0
	* Packaging : jar

Couverture actuelle du code (tests unitaires) : 61.3 %

=====================================
SECTION 05 - Outils et bibliothèques utilisées
-------------------------------------------------------

Outils utilisés :
	* Maven

Utilisation de bibliothèques (voir pom.xml) :
	* JUnit : tests unitaires
	* Mockito & PowerMock : fonctionnalités avancées pour les tests unitaires (amélioration des possibilités de tests unitaires permettant de simuler le comportement d'objets)
	* Guava : multiples outils (gestion des préconditions, regex simplifées, matchers, entre autres)
	* Gson : lecture et récupération d'informations à partir de fichiers formattés en JSON (pour fichier de configuration)
	* Jansi : affichage en couleur dans la console