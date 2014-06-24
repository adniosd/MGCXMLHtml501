1) Classes in com.vivogaming.livecasino.game_logic package:
	1. BlockingChipPlacing.java
		add show "Banker Pair" and "Player Pair" text function in showBetZone() function
	2. ChipsPlacing.java
		add hide "Banker Pair" and "Player Pair" text function in hideAndMoveBetZone() function


2) layout file in res/layout folder
	1. update former images, including player.png, banker.png, tie.png, player_pair.png and banker_pair.png.
	2. add banker_pair_text.png and player_pair_text.png
	3. former tray ImageViews use "src" attribute to appoint their images, I have change to use "background" attribute.
	4. add "Banker Pair" and "Player Pair" text view with the help of ImageView tag.