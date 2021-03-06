package utt.fr.rglb.main.java.game.model;

import java.io.BufferedReader;
import java.util.Collection;

import utt.fr.rglb.main.java.cards.controller.CardsControllerConsoleOriented;
import utt.fr.rglb.main.java.cards.model.CardsModelBean;
import utt.fr.rglb.main.java.cards.model.basics.Card;
import utt.fr.rglb.main.java.cards.model.basics.Color;
import utt.fr.rglb.main.java.console.model.InputReader;
import utt.fr.rglb.main.java.dao.ConfigFileDaoException;
import utt.fr.rglb.main.java.dao.ConfigurationReader;
import utt.fr.rglb.main.java.main.ServerException;
import utt.fr.rglb.main.java.player.controller.AbstractPlayerController;
import utt.fr.rglb.main.java.player.controller.PlayerControllerBean;
import utt.fr.rglb.main.java.player.controller.PlayerControllerConsoleOriented;
import utt.fr.rglb.main.java.player.model.PlayerTeam;
import utt.fr.rglb.main.java.player.model.PlayersToCreate;
import utt.fr.rglb.main.java.turns.controller.TurnControllerConsoleOriented;
import utt.fr.rglb.main.java.view.AbstractView;

/**
 * Classe dont le rôle est de gérer le jeu en faisant appel à des méthodes de haut niveau </br>
 * Version console
 */
public class GameModelConsoleOriented extends AbstractGameModel {
	private static final long serialVersionUID = 1L;
	protected TurnControllerConsoleOriented turnController;
	private CardsControllerConsoleOriented cardsController;
	private BufferedReader inputStream;
	private InputReader inputReader;
	protected GameRule gameRule;

	/* ========================================= CONSTRUCTOR ========================================= */
	
	/**
	 * Constructeur de GameModel
	 * @param view Vue permettant d'afficher des informations
	 */
	public GameModelConsoleOriented(AbstractView view, BufferedReader inputStream) {
		this.turnController = new TurnControllerConsoleOriented(view);
		this.cardsController = new CardsControllerConsoleOriented(view);
		this.inputReader = new InputReader(view);
		this.inputStream = inputStream;
	}

	/* ========================================= INITIALIZING ========================================= */
	
	/**
	 * Méthode permettant d'initialiser les paramètres (nombre de joueurs, nom de chacun des joueurs)
	 */
	public void initializeGameSettings() {
		boolean configurationFileUsageRequired = this.inputReader.askForConfigurationFileUsage(this.inputStream);
		PlayersToCreate playersAwaitingCreation = null;
		if(configurationFileUsageRequired) {
			try {
				ConfigurationReader configurationReader = new ConfigurationReader();
				playersAwaitingCreation = configurationReader.readConfigurationAt("dist/config.ini");
			} catch (ConfigFileDaoException e) {
				int playerNumber = this.inputReader.getValidPlayerNumberDueToInvalidConfigFile(this.inputStream,e.getMessage());
				playersAwaitingCreation = this.inputReader.getAllPlayerNames(playerNumber,this.inputStream);
			}
		} else {
			int playerNumber = this.inputReader.getValidPlayerNumber(this.inputStream);
			playersAwaitingCreation = this.inputReader.getAllPlayerNames(playerNumber,this.inputStream);
		}
		this.turnController.createPlayersFrom(playersAwaitingCreation,this.inputStream);
		this.gameRule = this.inputReader.askForGameMode(playersAwaitingCreation.size(),this.inputStream);
		if(this.gameRule.indicatesTeamPlayScoring()) {
			this.turnController.splitPlayersIntoTeams();
			this.turnController.displayTeams();
		}
	}

	@Override
	public void initializeCardsAndHands() {
		this.cardsController.resetCards();
		for(int i=0; i<this.turnController.getNumberOfPlayers(); i++) {
			Collection<Card> cardsDrawn = this.cardsController.drawCards(7);
			this.turnController.giveCardsToNextPlayer(cardsDrawn);
		}
	}
	
	@Override
	public void resetEverything() {
		this.cardsController.resetCards();
		this.turnController.resetTurn();
		this.gameRule.resetFlag();
	}
	
	@Override
	public void resetCards() {
		this.cardsController.resetCards();
	}
	
	/* ========================================= GAME LOGIC ========================================= */
	
	/**
	 * Méthode permettant de demarrer un nouveau tour
	 * @return PlayerControllerBean correspondant au joueur venant de jouer son tour
	 */
	public PlayerControllerBean playOneTurn() {
		CardsModelBean references = this.cardsController.getReferences();
		PlayerControllerConsoleOriented currentPlayer = this.turnController.findNextPlayer();
		if(currentPlayer.hasAtLeastOnePlayableCard(references)) {
			chooseCardAndPlayIt(references, currentPlayer);
		} else {
			Card cardDrawn = this.cardsController.drawOneCard();
			currentPlayer.pickUpOneCard(cardDrawn);
			if(references.isCompatibleWith(cardDrawn)) {
				chooseCardAndPlayIt(references, currentPlayer);
			} else {
				currentPlayer.unableToPlayThisTurn(references);
			}
		}
		triggerEffect(currentPlayer);
		return new PlayerControllerBean(currentPlayer);
	}
	
	@Override
	protected void chooseCardAndPlayIt(CardsModelBean gameModelbean, AbstractPlayerController currentPlayer) {
		Card cardChosen = currentPlayer.startTurn(inputReader,gameModelbean);
		GameFlag flag = this.cardsController.playCard(cardChosen);
		this.gameRule.setFlag(flag);
	}
	
	/* ========================================= EFFECTS ========================================= */
	
	/**
	 * Méthode permettant de tirer la 1ère carte et d'appliquer son effect (pour peu qu'elle en ait un)
	 */
	public void drawFirstCardAndApplyItsEffect() {
		GameFlag effect = this.cardsController.applyEffectFromAnotherFirstCard();
		triggerEffectFromFirstCard(effect);
	}
	
	/**
	 * Méthode permettant d'appliquer l'effet de la 1ère carte (ou de tirer une nouvelle carte, s'il s'agit d'un +4)
	 * @param effectFromFirstCard
	 */
	protected void triggerEffectFromFirstCard(GameFlag effectFromFirstCard) {
		if(effectFromFirstCard.equals(GameFlag.PLUS_FOUR)) {
			drawFirstCardAndApplyItsEffect();
		} else {
			PlayerControllerConsoleOriented nextPlayer = this.turnController.findNextPlayerWithoutChangingCurrentPlayer();
			triggerEffect(nextPlayer);
		}
		this.turnController.resetPlayerIndex();
	}
	
	/**
	 * Méthode permettant d'appliquer l'effet associé à la carte venant d'être jouée
	 * @param currentPlayer Joueur venant de jouer son tour
	 */
	protected void triggerEffect(AbstractPlayerController currentPlayer) {
		if(this.gameRule.indicatesTwoPlayersMode()) {
			triggerEffectWithOnlyTwoPlayers(currentPlayer);
		} else {
			triggerEffectWithMoreThanTwoPlayers(currentPlayer);
		}
	}
	
	/**
	 * Méthode permettant d'appliquer les effets différement s'il n'y a que 2 joueurs
	 * @param currentPlayer Joueur venant de jouer son tour
	 */
	protected void triggerEffectWithOnlyTwoPlayers(AbstractPlayerController currentPlayer) {
		if(this.gameRule.shouldReverseTurn()) {
			this.triggerCycleSilently();
		} else {
			triggerEffectWithMoreThanTwoPlayers(currentPlayer);
		}
	}
	
	/**
	 * Méthode générale permettant d'appliquer les effets différement s'il y a 3 joueurs ou plus
	 * @param currentPlayer Joueur venant de jouer son tour
	 */
	protected void triggerEffectWithMoreThanTwoPlayers(AbstractPlayerController currentPlayer) {
		if(this.gameRule.shouldReverseTurn()) {
			triggerReverseCurrentOrder();
		} else if(this.gameRule.shouldSkipNextPlayerTurn()) {
			triggerSkipNextPlayer();
		} else if(this.gameRule.shouldPickColor()) {
			triggerColorPicking(currentPlayer,false);
		} else if(this.gameRule.shouldGivePlus2CardPenalty()) {
			PlayerControllerConsoleOriented nextPlayer = this.turnController.findNextPlayerWithoutChangingCurrentPlayer();
			triggerPlusX(2,nextPlayer);
		} else if(this.gameRule.shouldGivePlus4CardPenalty()) {
			PlayerControllerConsoleOriented nextPlayer = this.turnController.findNextPlayerWithoutChangingCurrentPlayer();
			boolean wantsToCheck = triggerBluffing(nextPlayer);
			if(wantsToCheck) {
				triggerPlusX(2,nextPlayer,true);
			}
			triggerPlusX(4,nextPlayer);
			triggerColorPicking(currentPlayer,true);
		} else if(this.gameRule.shouldGivePlus4CardPenaltyWhileBluffing()) {
			PlayerControllerConsoleOriented nextPlayer = this.turnController.findNextPlayerWithoutChangingCurrentPlayer();
			boolean wantsToCheck = triggerBluffing(nextPlayer);
			if(wantsToCheck) {
				triggerPlusX(2,currentPlayer,false);
				triggerPlusX(4,currentPlayer);
			} else {
				triggerPlusX(4,nextPlayer);
			}
			triggerColorPicking(currentPlayer,true);
		}
	}
	
	/* ========================================= EFFECTS - BASIS ========================================= */

	@Override
	protected void triggerReverseCurrentOrder() {
		this.turnController.reverseCurrentOrder();
	}
	
	@Override
	protected void triggerCycleSilently() {
		this.turnController.cycleSilently();
	}

	/**
	 * Méthode permettant d'empêcher le joueur suivant de jouer
	 */
	protected void triggerSkipNextPlayer() {
		this.turnController.skipNextPlayer();
	}
	
	/**
	 * Méthode permettant au joueur de selectionner une couleur
	 * @param currentPlayer Joueur actuel
	 * @param isRelatedToPlus4 Booléen valant <code>TRUE</code> si le choix est lié au jeu d'un +4, <code>FALSE</code> sinon
	 */
	protected void triggerColorPicking(AbstractPlayerController currentPlayer, boolean isRelatedToPlus4) {
		Color chosenColor = currentPlayer.hasToChooseColor(isRelatedToPlus4,this.inputReader);
		this.cardsController.setGlobalColor(chosenColor);
	}
	
	/**
	 * Méthode permettant de forcer un joueur à piocher des cartes
	 * @param cardsToDraw Nombre de cartes à piocher
	 * @param targetedPlayer Joueur devant piocher des cartes
	 */
	protected void triggerPlusX(int cardsToDraw, AbstractPlayerController targetedPlayer) {
		Collection<Card> cards = this.cardsController.drawCards(cardsToDraw);
		targetedPlayer.isForcedToPickUpCards(cards);
	}
	
	/**
	 * Méthode permettant de forcer un joueur à piocher des cartes avec un affichage particulier en fonction du résultat d'un +4 (bluff, ou non)
	 * @param cardsToDraw Nombre de cartes à piocher
	 * @param targetedPlayer Joueur devant piocher des cartes
	 * @param wasLegit Booleén valant <code>TRUE</code> s'il n'y a PAS eu bluff, <code>FALSE</code> sinon
	 */
	protected void triggerPlusX(int cardsToDraw, AbstractPlayerController targetedPlayer, boolean wasLegit) {
		Collection<Card> cards = this.cardsController.drawCards(cardsToDraw);
		if(wasLegit) {
			targetedPlayer.isForcedToPickUpCardsLegitCase(cards);
		} else {
			targetedPlayer.isForcedToPickUpCardsBluffCase(cards);
		}
	}

	/**
	 * Méthode permettant de demander à un joueur s'il souhaite accuser le précédent joueur de bluff
	 * @return <code>TRUE</code> en cas de réponse positive, <code>FALSE</code> sinon
	 */
	protected boolean triggerBluffing(AbstractPlayerController nextPlayer) {
		return nextPlayer.askIfHeWantsToCheckIfItsLegit(this.inputReader);
	}

	/**
	 * Méthode permettant de donner une pénalité au joueur
	 * @param currentPlayer Joueur actuel
	 * @param cardCount Nombre de cartes à piocher
	 */
	public void giveCardPenaltyTo(AbstractPlayerController currentPlayer, int cardCount) {
		Collection<Card> cardPenalty = this.cardsController.drawCards(cardCount);
		currentPlayer.isForcedToPickUpCards(cardPenalty);
	}
	
	/**
	 * Méthode permettant de donner une pénalité à un joueur (surcharge)
	 * @param player Joueur actuel
	 * @param cardCount Nombre de cartes à piocher
	 */
	public void giveCardPenaltyTo(PlayerControllerBean player, int cardCount) {
		Collection<Card> cardPenalty = this.cardsController.drawCards(cardCount);
		player.isForcedToPickUpCards(cardPenalty);
	}

	/**
	 * Méthode permettant de récupérer le choix du joueur actuel sous forme d'index
	 * @return int correspondant à l'index de la réponse du joueur
	 */
	public int getValidChoiceAnswer() {
		return this.inputReader.getValidAnswerFromDualChoice(this.inputStream);
	}
	
	/* ========================================= SCORE ========================================= */

	@Override
	public boolean computeScores(PlayerControllerBean gameWinner) {
		boolean someoneOrOneTeamWon = false;
		if(this.gameRule.indicatesTeamPlayScoring()) {
			someoneOrOneTeamWon = this.turnController.computeTeamEndOfTurn(gameWinner);
			this.turnController.displayTeamTotalScore();
		} else {
			someoneOrOneTeamWon = this.turnController.computeIndividualEndOfTurn(gameWinner);
			this.turnController.displayIndividualTotalScore();
		}
		waitForScoreDisplay();
		return someoneOrOneTeamWon;
	}

	@Override
	public boolean computeIndividualScore(PlayerControllerBean gameWinner) {
		boolean playerWon = this.turnController.computeIndividualEndOfTurn(gameWinner);
		this.turnController.displayIndividualTotalScore();
		waitForScoreDisplay();
		return playerWon;
	}

	@Override
	public boolean computeTeamScore(PlayerControllerBean gameWinner) {
		boolean playerWon = this.turnController.computeTeamEndOfTurn(gameWinner);
		this.turnController.displayTeamTotalScore();
		waitForScoreDisplay();
		return playerWon;
	}

	@Override
	protected  void waitForScoreDisplay() {
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			throw new ServerException("[ERROR] Something went wrong while waiting during score display",e);
		}
	}
	
	@Override
	public boolean indicatesTeamPlayScoring() {
		return this.gameRule.indicatesTeamPlayScoring();
	}
	
	@Override
	public PlayerTeam findWinningTeam(PlayerControllerBean winningPlayer) {
		return this.turnController.findWinningTeam(winningPlayer);
	}
}