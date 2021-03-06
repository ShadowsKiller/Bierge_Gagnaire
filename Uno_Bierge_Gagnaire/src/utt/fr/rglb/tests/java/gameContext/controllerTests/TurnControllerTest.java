package utt.fr.rglb.tests.java.gameContext.controllerTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import utt.fr.rglb.main.java.cards.model.basics.Card;
import utt.fr.rglb.main.java.cards.model.basics.Color;
import utt.fr.rglb.main.java.player.controller.PlayerControllerConsoleOriented;
import utt.fr.rglb.main.java.player.model.PlayersToCreate;
import utt.fr.rglb.main.java.turns.controller.TurnControllerConsoleOriented;
import utt.fr.rglb.main.java.view.AbstractView;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Classe de tests unitaires validant le comportement des méthodes de la classe TurnController
 * </br>Utilisation de simulacres pour la vue et le lecteur bufferisé --injection de dépendance permettant d'émuler une entrée au clavier (Mockito)
 * @see TurnControllerConsoleOriented
 */
public class TurnControllerTest {
	private AbstractView mockedView;
	private TurnControllerConsoleOriented turnControllerWithoutScramble;
	private TurnControllerConsoleOriented turnControllerWithScrambledPlayers;
	private BufferedReader mockedInputStream;
	private int whateverPath;
	
	@Before
	public void setup() {
		this.whateverPath = 0;
		this.mockedView = mock(AbstractView.class);
		this.turnControllerWithoutScramble = new TurnControllerConsoleOriented(this.mockedView);
		this.turnControllerWithScrambledPlayers = new TurnControllerConsoleOriented(this.mockedView);
		this.mockedInputStream = mock(BufferedReader.class);
	}
	
	/* ========================================= CONSTRUCTOR ========================================= */
	
	@Test(expected=NullPointerException.class)
	public void testFailToCreateTurnControllerDueToNullView() {
		this.turnControllerWithoutScramble = new TurnControllerConsoleOriented(null);
	}
	
	/* ========================================= PLAYER CREATION ========================================= */

	@Test
	public void testCreatePlayers() {	
		Collection<String> players = generatePlayerNames();
		this.turnControllerWithoutScramble.createPlayersWithoutScamblingFrom(players,this.mockedInputStream);
		PlayersToCreate playersToCreate = new PlayersToCreate();
		playersToCreate.addHumanPlayer("p1");
		playersToCreate.addHumanPlayer("p2");
		playersToCreate.addHumanPlayer("p3");
		this.turnControllerWithScrambledPlayers.createPlayersFrom(playersToCreate,this.mockedInputStream);
	}
	
	@Test(expected=NullPointerException.class)
	public void testFailToCreatePlayersDueToNullCollection() {
		this.turnControllerWithScrambledPlayers.createPlayersFrom(null,this.mockedInputStream);
	}
	
	@Test(expected=NullPointerException.class)
	public void testFailToCreatePlayersWithoutScramblingDueToNullCollection() {
		this.turnControllerWithoutScramble.createPlayersWithoutScamblingFrom(null,this.mockedInputStream);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFailToCreatePlayersWithoutScramblingDueToIncorrectPlayerNumber() {
		Collection<String> emptyNameCollection = new ArrayList<String>();
		this.turnControllerWithoutScramble.createPlayersWithoutScamblingFrom(emptyNameCollection,this.mockedInputStream);
	}
	
	@Test
	public void testGiveCardsToNextPlayer() {
		Card c1 = new Card(1,Color.BLUE,this.whateverPath);
		Card c2 = new Card(2,Color.BLUE,this.whateverPath);
		Card c3 = new Card(3,Color.BLUE,this.whateverPath);
		Collection<Card> cards = Arrays.asList(c1,c2,c3);
		Collection<String> playerNames =generatePlayerNames();
		this.turnControllerWithoutScramble.createPlayersWithoutScamblingFrom(playerNames,this.mockedInputStream);
		PlayerControllerConsoleOriented currentPlayer = this.turnControllerWithoutScramble.findNextPlayerWithoutChangingCurrentPlayer();
		this.turnControllerWithoutScramble.giveCardsToNextPlayer(cards);
		assertEquals(3,currentPlayer.getNumberOfCardsInHand());
	}
	
	/* ========================================= TURN ORDER ========================================= */
	
	@Test
	public void reverseCurrentOrder() {
		this.turnControllerWithoutScramble.reverseCurrentOrder();
		this.turnControllerWithScrambledPlayers.reverseCurrentOrder();
	}
	
	@Test
	public void testFindNextPlayerWithDefaultTurnOrder() {
		Collection<String> players = generatePlayerNames();
		this.turnControllerWithoutScramble.createPlayersWithoutScamblingFrom(players,this.mockedInputStream);
		
		PlayerControllerConsoleOriented currentPlayer = this.turnControllerWithoutScramble.findNextPlayer();
		assertEquals("player1",currentPlayer.getAlias());
		
		currentPlayer = this.turnControllerWithoutScramble.findNextPlayer();
		assertEquals("player2",currentPlayer.getAlias());

		currentPlayer = this.turnControllerWithoutScramble.findNextPlayer();
		assertEquals("player1",currentPlayer.getAlias());
	}

	
	@Test
	public void testFindNextPlayerWithReversedTurnOrder() {
		Collection<String> playerNames = generatePlayerNames();
		this.turnControllerWithoutScramble.createPlayersWithoutScamblingFrom(playerNames,this.mockedInputStream);
		this.turnControllerWithoutScramble.reverseCurrentOrder();
		
		PlayerControllerConsoleOriented currentPlayer = this.turnControllerWithoutScramble.findNextPlayer();
		assertEquals("player2",currentPlayer.getAlias());
		
		currentPlayer = this.turnControllerWithoutScramble.findNextPlayer();
		assertEquals("player1",currentPlayer.getAlias());

		currentPlayer = this.turnControllerWithoutScramble.findNextPlayer();
		assertEquals("player2",currentPlayer.getAlias());
	}

	private Collection<String> generatePlayerNames() {
		Collection<String> playerNames = new ArrayList<String>();
		playerNames.add("player1");
		playerNames.add("player2");
		return playerNames;
	}
}
