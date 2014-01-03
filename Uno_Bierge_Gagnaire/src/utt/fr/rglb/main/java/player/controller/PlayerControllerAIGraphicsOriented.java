package utt.fr.rglb.main.java.player.controller;

import com.google.common.base.Preconditions;

import utt.fr.rglb.main.java.player.AI.CardPickerStrategy;
import utt.fr.rglb.main.java.view.AbstractView;


public class PlayerControllerAIGraphicsOriented extends PlayerControllerGraphicsOriented {
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")//FIXME
	private CardPickerStrategy cardPickerStrategy;
	
/* ========================================= CONSTRUCTOR ========================================= */
	
	public PlayerControllerAIGraphicsOriented(String name, AbstractView consoleView, CardPickerStrategy cardPickerStrategy) {
		super(name,consoleView);
		Preconditions.checkNotNull(cardPickerStrategy,"[ERROR] Impossible create AI player : provided strategy is null");
		this.cardPickerStrategy = cardPickerStrategy;
	}

}
