package main.java.console.view;

import java.util.Collection;

import com.google.common.base.Preconditions;

import main.java.cards.model.basics.Card;
import main.java.cards.model.basics.CardSpecial;
import main.java.cards.model.basics.Color;

/**
 * Classe d�finissant les m�thodes d'affichage
 */
public abstract class View {
	protected FancyConsoleDisplay consoleDisplay;
	
	/* ========================================= CONSTRUCTOR ========================================= */
	
	/**
	 * Constructeur de vue
	 */
	public View() {
		this.consoleDisplay = new FancyConsoleDisplay();
	}
	
	/* ========================================= TEXT DISPLAY ========================================= */
	
	/**
	 * M�thode permettant d'afficher du texte en gras et aller � la ligne
	 * @param text Texte � afficher
	 */
	public void displayBoldText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.displayBoldText(text);
	}
	
	/**
	 * M�thode permettant d'afficher du texte en gras sans aller � la ligne
	 * @param text Texte � afficher
	 */
	public void appendBoldText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.appendBoldText(text);
	}
	
	/**
	 * M�thode permettant d'afficher un texte d'erreur (rouge/gras)
	 * @param text Texte � afficher
	 */
	public void displayErrorText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.displayErrorText(text);
	}
	
	/**
	 * M�thode permettant d'afficher un texte de succ�s (vert/gras)
	 * @param text Texte � afficher
	 */
	public void displaySuccessText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.displaySuccessText(text);
	}
	
	public void displayJokerText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.displayJokerText(text);
	}
	
	/* ========================================= CLEARING ========================================= */
	
	/**
	 * M�thode permettant de nettoyer la console (suppression de toutes lignes affich�es)
	 */
	public void clearDisplay() {
		this.consoleDisplay.clearDisplay();
	}
	
	/**
	 * M�thode permettant d'aller � la ligne (ligne vide)
	 */
	public void insertBlankLine() {
		this.consoleDisplay.displayBlankLine();
	}

	/* ========================================= EMPHASIS ========================================= */
	
	/**
	 * M�thode permettant d'afficher un titre avec emphase
	 * @param title Titre � afficher
	 */
	public void displayTitle(String title) {
		Preconditions.checkNotNull(title,"[ERROR] Impossible to display text : provided one is null");
		int length = title.length();
		insertBlankLine();
		this.consoleDisplay.displaySeparationBar(length);
		this.consoleDisplay.displayBoldText(title);
		this.consoleDisplay.displaySeparationBar(length);
		insertBlankLine();
	}
	
	/**
	 * M�thode permettant d'utiliser du texte comme barre de s�paration (affichage en couleurs n�gatives)
	 * @param text Texte � afficher
	 */
	public void displaySeparationText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.displaySeparationText(text);
	}
	
	/* ========================================= CARD DISPLAY ========================================= */
	
	/**
	 * M�thode permettant d'afficher une collection de cartes compl�te et d'aller � la ligne
	 * @param cardsToDisplay Collection de cartes � afficher
	 */
	public void displayCardCollection(Collection<Card> cardsToDisplay) {
		Preconditions.checkNotNull(cardsToDisplay,"[ERROR] Impossible to display card collection : provided one is null");
		int index = 0;
		for(Card currentCard : cardsToDisplay) {
			displayOneCard(currentCard,index++);
		}
		insertBlankLine();
	}
	
	/**
	 * M�thode permettant d'afficher une unique carte et d'aller � la ligne
	 * @param cardToDisplay Carte � afficher
	 */
	public void displayCard(Card cardToDisplay) {
		Preconditions.checkNotNull(cardToDisplay,"[ERROR] Impossible to display card : provided one is null");
		displayOneCard(cardToDisplay);
		insertBlankLine();
	}
	
	/**
	 * M�thode priv�e permettant d'afficher une carte avec gestion de sa couleur et de son type (num�rot�e, sp�ciale)
	 * @param cardToDisplay Carte � afficher
	 */
	private void displayOneCard(Card cardToDisplay) {
		Preconditions.checkNotNull(cardToDisplay,"[ERROR] Impossible to display card : provided one is null");
		if(cardToDisplay.isSpecial()) {
			CardSpecial explictSpecialCardToDisplay = (CardSpecial)cardToDisplay;
			displaySpecialCard(explictSpecialCardToDisplay);
		} else {
			displayNumberedCard(cardToDisplay);
		}
	}
	
	/**
	 * M�thode priv�e permettant d'afficher une unique carte avec gestion de sa couleur et de son type (num�rot�e, sp�ciale) en accolant son index
	 * @param cardToDisplay Carte � afficher
	 * @param index Index de la carte 
	 */
	private void displayOneCard(Card cardToDisplay, int index) {
		Preconditions.checkNotNull(cardToDisplay,"[ERROR] Impossible to display card : provided one is null");
		Preconditions.checkArgument(index >= 0,"[ERROR] Impossible to display card : provided index is invalid");
		if(cardToDisplay.isSpecial()) {
			CardSpecial explictSpecialCardToDisplay = (CardSpecial)cardToDisplay;
			displaySpecialCard(explictSpecialCardToDisplay,index);
		} else {
			displayNumberedCard(cardToDisplay,index);
		}
	}
	
	/* ========================================= SPECIAL CARD DISPLAY ========================================= */

	/**
	 * M�thode priv�e permettant d'afficher une carte sp�ciale dans la console (avec gestion de la couleur et de son effet)
	 * @param cardToDisplay Carte � afficher
	 */
	private void displaySpecialCard(CardSpecial cardToDisplay) {
		Preconditions.checkNotNull(cardToDisplay,"[ERROR] Impossible to display card : provided one is null");
		Color color = cardToDisplay.getCouleur();
		String effect = cardToDisplay.getEffet();
		if(color.equals(Color.BLUE)) {
			this.consoleDisplay.appendBlueText("[" + effect + "] ");
		} else if(color.equals(Color.RED)) {
			this.consoleDisplay.appendRedText("[" + effect + "] ");
		} else if(color.equals(Color.GREEN)) {
			this.consoleDisplay.appendGreenText("[" + effect + "] ");
		} else if(color.equals(Color.YELLOW)) {
			this.consoleDisplay.appendYellowText("[" + effect + "] ");
		} else { 	//if(colorFromCard.equals(Couleur.JOKER)
			this.consoleDisplay.appendJokerText("[" + effect + "] ");
		}
	}
	
	/**
	 * M�thode priv�e permettant d'afficher une carte sp�ciale dans la console (avec gestion de la couleur et de son effet) en accolant son index
	 * @param cardToDisplay Carte � afficher
	 * @param index Index de la carte
	 */
	private void displaySpecialCard(CardSpecial cardToDisplay, int index) {
		Preconditions.checkNotNull(cardToDisplay,"[ERROR] Impossible to display card : provided one is null");
		Preconditions.checkArgument(index >= 0,"[ERROR] Impossible to display card : provided index is invalid");
		Color color = cardToDisplay.getCouleur();
		String effect = cardToDisplay.getEffet();
		if(color.equals(Color.BLUE)) {
			//this.consoleDisplay.appendBoldIndex();
			this.consoleDisplay.appendBlueText(index + ":[" + effect + "] ");
		} else if(color.equals(Color.RED)) {
			//this.consoleDisplay.appendBoldIndex(index);
			this.consoleDisplay.appendRedText(index + ":[" + effect + "] ");
		} else if(color.equals(Color.GREEN)) {
			//this.consoleDisplay.appendBoldIndex(index);
			this.consoleDisplay.appendGreenText(index + ":[" + effect + "] ");
		} else if(color.equals(Color.YELLOW)) {
			//this.consoleDisplay.appendBoldIndex(index);
			this.consoleDisplay.appendYellowText(index + ":[" + effect + "] ");
		} else { 	//if(colorFromCard.equals(Couleur.JOKER)
			//this.consoleDisplay.appendBoldIndex(index);
			this.consoleDisplay.appendJokerText(index + ":[" + effect + "] ");
		}
	}
	
	/* ========================================= NUMBERED CARD DISPLAY ========================================= */
	
	/**
	 * M�thode priv�e permettant d'afficher une carte num�rot�e (avec gestion de sa couleur) 
	 * @param cardToDisplay Carte � afficher
	 */
	private void displayNumberedCard(Card cardToDisplay) {
		Preconditions.checkNotNull(cardToDisplay,"[ERROR] Impossible to display card : provided one is null");
		Color color = cardToDisplay.getCouleur();
		int value = cardToDisplay.getValeur();
		if(color.equals(Color.BLUE)) {
			this.consoleDisplay.appendBlueText("[" + value + "] ");
		} else if(color.equals(Color.RED)) {
			this.consoleDisplay.appendRedText("[" + value + "] ");
		} else if(color.equals(Color.GREEN)) {
			this.consoleDisplay.appendGreenText("[" + value + "] ");
		} else {	//if(colorFromCard.equals(Couleur.JAUNE)
			this.consoleDisplay.appendYellowText("[" + value + "] ");
		}
	}
	
	/**
	 * M�thode priv�e permettant d'afficher une carte num�rot�e (avec gestion de sa couleur) 
	 * @param cardToDisplay Carte � afficher
	 * @param index Index de la carte
	 */
	private void displayNumberedCard(Card cardToDisplay, int index) {
		Preconditions.checkNotNull(cardToDisplay,"[ERROR] Impossible to display card : provided one is null");
		Preconditions.checkArgument(index >= 0,"[ERROR] Impossible to display card : provided index is invalid");
		Color color = cardToDisplay.getCouleur();
		int value = cardToDisplay.getValeur();
		if(color.equals(Color.BLUE)) {
			this.consoleDisplay.appendBlueText(index + ":[" + value + "] ");
		} else if(color.equals(Color.RED)) {
			this.consoleDisplay.appendRedText(index + ":[" + value + "] ");
		} else if(color.equals(Color.GREEN)) {
			this.consoleDisplay.appendGreenText(index + ":[" + value + "] ");
		} else {	//if(colorFromCard.equals(Couleur.JAUNE)
			this.consoleDisplay.appendYellowText(index + ":[" + value + "] ");
		}
	}
	
	/* ========================================= STANDARD TEXT ========================================= */
	
	public void appendBoldRedText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.appendRedText(text);
	}
	
	public void appendBoldBlueText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.appendBlueText(text);
	}
	
	public void appendBoldGreenText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.appendGreenText(text);
	}
	
	public void appendBoldYellowText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.appendYellowText(text);
	}

	public void appendBoldJokerText(String text) {
		Preconditions.checkNotNull(text,"[ERROR] Impossible to display text : provided one is null");
		this.consoleDisplay.appendJokerText(text);
	}
}
