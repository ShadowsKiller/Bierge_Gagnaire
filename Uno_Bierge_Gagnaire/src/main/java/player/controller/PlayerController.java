package main.java.player.controller;

import java.util.Collection;

import com.google.common.base.Preconditions;

import main.java.cards.model.basics.Carte;
import main.java.console.model.InputReader;
import main.java.player.model.PlayerModel;

public class PlayerController {
	private PlayerModel player;
	
	public PlayerController(String name) {
		Preconditions.checkNotNull(name,"[ERROR] name cannot be null");
		this.player = new PlayerModel(name);
	}
	
	public void pickUpCards(Collection<Carte> c) {
		Preconditions.checkNotNull(c,"[ERROR] Card collection picked up cannot be null");
		Preconditions.checkArgument(c.size()>0, "[ERROR] Card collection picked cannot be empty");
		this.player.pickUpCards(c);
	}
	
	public Carte playCard(int index) {
		Preconditions.checkState(this.player.getNumberOfCardsInHand() > 0, "[ERROR] Impossible to play a card : player has none");
		Preconditions.checkArgument(index >= 0 && index < this.player.getNumberOfCardsInHand(),"[ERROR] Incorrect index : must be > 0 (tried = " + index + ", but max is = " + this.player.getNumberOfCardsInHand());
		return this.player.playCard(index);
	}
	
	public String getAlias() {
		return this.player.getAlias();
	}
	
	public int getScore() {
		return this.player.getScore();
	}
	
	public int getNumberOfCardsInHand() {
		return this.player.getNumberOfCardsInHand();
	}
	
	@Override
	public String toString() {
		return this.player.toString();
	}
	
	private Collection<Carte> getDisplayableCardCollection() {
		return this.player.generateDisplayableCardCollection();
	}
	
	public Carte startTurn(InputReader inputReader, Carte currentCard) {
		String alias = this.player.getAlias();
		Collection<Carte> cardCollection = this.getDisplayableCardCollection();
		
		int index = inputReader.getFirstValidIndexFromInput(alias,cardCollection,currentCard);
		Carte choosenCard = this.player.peekAtCard(index);
		while(!choosenCard.isCompatibleWith(currentCard)) {
			index = inputReader.getAnotherValidIndexFromInputDueToIncompatibleCard(alias,cardCollection,currentCard);
			choosenCard = this.player.peekAtCard(index);
		}
		return this.player.playCard(index);
	}
}
