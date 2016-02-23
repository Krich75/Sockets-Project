# Projet-Sockets

## Description :

Ce répertoire contient le code source des différents programmes répondant aux problématiques exposé sur le sujet :

### Serveur C :

	Le code du serveur C est situé dans server/serverC.
Ce serveur peut gèrer plusieurs connexions simultanément (non limité: point faible), et rejette les requêtes trop volumineuses.
Son port d'écoute est paramètrable.

### Serveurs java non sécurisé et SSL :

	Les fonctions de serveurs java non sécurisé, et SSL son assurées par le même programme.
En effet, nous avons choisi d'écrire une bibliothèque logiciel factorisant les codes sources nécessaires à
ces deux fonctionnalités. Ainsi, le programme "serveurJAVA" ne fait qu'utiliser cette bibliothèque, et le code
correspondant à la création d'un serveur se trouve dans "libServerJava".

	Ce programme peux gèrer un nombre défini (limité) de connexions simultanés, rejette les requêtes trop volumineuses,
ferme la connection si le client est trop lent à envoyer sa requête, attends la fin des connexions client avant de s'arrêter.
Ses deux ports d'écoute sont paramètrables.

	Il est à noter aussi que le fichiers "truststore" et "keystore" sont chargés directement depuis le fichier .jar
du serveur. Nous avons pour cela créé une seconde bibliothèque nommée "libSSLHelpersJava".
Nous savons que cette fonctionnalité n'est pas nécessaire dans ce genre de projet, mais nous souhaitions tester sa faisabilité
dans des applications plus utiles (exemple: déploiement d'un truststore dans un jar signé).
Pour finir, ce chargement depuis le jar permet de ne pas avoir à saisir le chemin des "trustore" ou "keystore" ainsi que
leur mots de passe en paramètre au lancement du programme.

### Client :

	Nous avons réalisé notre client en java. Il utilise également notre bibliothèque "libSSLHelpersJava".
Toutes les adresses et ports des serveurs auxquels il émmet des requêtes sont paramètrables.

### Serveur PHP:

	Il se résume en un seul fichier. Les méthodes GET et POST peuvent toutes deux répondres aux requêtes de commptage des caractères
et au calcul de la valeur d'une phrase. Cependant, le client utilise bien GET pour l'une, et POST pour l'autre (voir code client).



## Contenu de l'archive :
.
├── client			-> Contient le code source du client java.
├── lib				-> Contient le code source des bibliothèques que nous avons développé pour ce projet.
│	├── libServerJava
│	└── libSSLHelpersJava
└── server			-> Contient les codes source des différents serveurs.
	├── serverC
	├── serverJAVA		-> Assure la fonction de serveur non sécurisé et de server SSL (voir sources)
	└── serverPHP


	Chaque projet nécessitant une compilation possède sont propre makefile et un makefile global permet de compiler tous les projets.

	Notez que les projets client, libServerJava, libSSLHelpersJava et serverJAVA sont des projets eclipse et peuvent être importé
comme tel dans l'IDE, mais il sera nécessaire de reconfigurer les dépendances à :


	- libServerJava et libSSLHelpersJava pour le projet serverJAVA
	
	- libSSLHelpersJava pour le projet client


## Compiler les différents projets :

	$ make

## Lancement et paramètres des programmes :

	- client java :	$ java -jar client/build/client.jar [-clear hostname[:port]] [-http hostname[:port][/path]] [-ssl hostname[:port]]

	- server java :	$ java -jar server/serverJAVA/build/server.jar [-clear port] [-ssl port]

	- server c :	$ ./server/serverC/server [port]

### Il est possible que vous n'ayez la permission de lancer l'éxecutable c, dans ce cas lancez :

	$ chmod +x server/serverC/serverC


### Cas particulier pour le serveur PHP :

	Copiez le fichier index.php sur un server HTTP,
	Ou lancez :
	
		$ php -S 127.0.0.1:8888 -t server/serverPHP 


###  Valeurs par défaut (c=client, s=serveur):

	Chemin du server PHP 	=  index.php	c
	HostName 		=  127.0.0.1	cs
	Port serveur C 		=  7777		cs
	Port serveur Java 	=  7777		cs
	Port serveur Php 	=  8888		cs
	Port serveur Java SSL 	=  9999		cs


## Fonctionalités associées aux différents serveurs :

-- 1) Demander le nombre de voyelles d'une phrase. 	-> serveurC ou serveurJava
-- 2) Demander le nombre de consonne d'une phrase.	-> serveurC ou serveurJava
-- 3) Demander le nombre de lettre d'une phrase.	-> serveurPhp
-- 4) Demander la valeur d'une phrase d'une phrase.	-> serveurPhp
-- 5) Demander le nombre de voyelles d'une phrase (SSL).-> serveurJava
-- 6) Demander le nombre de consones d'une phrase (SSL).-> serveurJava
