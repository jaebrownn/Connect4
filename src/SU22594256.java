import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;
import javax.swing.text.AttributeSet.FontAttribute;
import javax.swing.text.StyleConstants.FontConstants;

import java.util.Scanner;
import java.io.File;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class SU22594256 {
	static int row = 6;
	static int col = 7;
	static boolean DEBUG = true;
	static int bomb = 0;
	static int tele = 1;
	static int colchang = 2;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		boolean gui = false;
		if (args[0].charAt(0) == 'G') {
			gui = true;
		} else {
			gui = false;
		}

		int input = 0;
		int pos = -1;
		int[][] grid = new int[row][col];
		boolean player1 = true;
		// boolean startingPlayer = true;
		int[] p1powers = { 2, 2, 2 };
		int[] p2powers = { 2, 2, 2 };
		int colIn = -1;
		boolean gameOver = false;
		boolean bombplayedlast = false;
		boolean startingPlayer = true;

		if (!gui) {
			StdOut.println("****************************************************************************");
			StdOut.println("*  _______  _______  __    _  __    _  _______  _______  _______  _   ___  *"
					+ "\n* |       ||       ||  |  | ||  |  | ||       ||       ||       || | |   | *"
					+ "\n* |       ||   _   ||   |_| ||   |_| ||    ___||       ||_     _|| |_|   | *"
					+ "\n* |       ||  | |  ||       ||       ||   |___ |       |  |   |  |       | *"
					+ "\n* |      _||  |_|  ||  _    ||  _    ||    ___||      _|  |   |  |___    | *"
					+ "\n* |     |_ |       || | |   || | |   ||   |___ |     |_   |   |      |   | *"
					+ "\n* |_______||_______||_|  |__||_|  |__||_______||_______|  |___|      |___| *");
			StdOut.println("*                                                                          *");
			StdOut.println("****************************************************************************");
			int[] curppowers = new int[3];
			while (true) {
				bombplayedlast = false;
				if (player1) {
					StdOut.println("Player 1's turn (You are Red):");
					curppowers = p1powers;
				} else {
					StdOut.println("Player 2's turn (You are Yellow):");
					curppowers = p2powers;
				}

				StdOut.println("Choose your move: \n 1. Play Normal \n 2. Play Bomb (" + curppowers[0]
						+ " left) \n 3. Play Teleportation (" + curppowers[1] + " left) \n 4. Play Colour Changer ("
						+ curppowers[2] + " left)\n 5. Display Gameboard \n 6. Load Test File \n 0. Exit");

				try {
					input = StdIn.readInt();
				} catch (Exception InputMismatchException) {
					input = -1;
				}

				switch (input) {
				case 0:
					Exit();
					break;

				case 1:
					colIn = validCol();

					Play(colIn, grid, player1);
					break;

				case 2:
					if (curppowers[bomb] > 0) {
						curppowers[bomb]--;
						colIn = validCol();
						bombplayedlast = true;

						Bomb(colIn, grid, player1);
					} else {
						StdOut.println("You have no bomb power discs left");
						player1 = !player1;
					}
					break;

				case 3:
					if (curppowers[tele] > 0) {
						curppowers[tele]--;
						colIn = validCol();

						Teleport(colIn, grid, player1);
					} else {
						StdOut.println("You have no teleporter power discs left");
						player1 = !player1;
					}
					break;
				case 4:
					if (curppowers[colchang] > 0) {
						curppowers[colchang]--;
						colIn = validCol();

						Colour_Changer(colIn, grid, player1);
					} else {
						StdOut.println("“You have no colour changer power discs left");
						player1 = !player1;
					}
					break;

				default:
					StdOut.println("Please choose a valid option");
					player1 = !player1;
					break;
				}

				Display(grid);

				gameOver = playerWin(grid, p1powers, p2powers, bombplayedlast);
				if (gameOver) {
					player1 = !startingPlayer;
					startingPlayer = !startingPlayer;
					replay(grid, p1powers, p2powers);
				} else {
					player1 = !player1;
				}
			}
		} else {

			StdDraw.enableDoubleBuffering();

			int guiX[][] = new int[row][col];
			int guiY[][] = new int[row][col];
			int posX = 0;
			int posY = 5;
			double radius = 2;
			for (int x = 1; x <= 7; x++) {
				posY = 5;
				for (int y = 1; y <= 6; y++) {
					guiX[posY][posX] = 4 * x - (5 / 2) + 6;
					guiY[posY][posX] = 4 * y - (5 / 2) + 4;
					StdOut.print(guiX[posY][posX] + " " + guiY[posY][posX] + "\t");
					posY--;
				}
				StdOut.println();
				posX++;
			}

			int[] curppowers = new int[3];
			input = -1;

			initialiseBoard(guiX, guiY);
			StdDraw.show();

			while (true) {
				StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
				StdDraw.filledRectangle(20, 30, 14, 2);
				StdDraw.show();
				bombplayedlast = false;
				StdDraw.mouseX();
				StdDraw.mouseY();

				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
				StdDraw.filledRectangle(42, 35, 7, 3);

				Font font = new Font(Font.SANS_SERIF, Font.BOLD, 20);
				StdDraw.setFont(font);

				if (player1) {
					curppowers = p1powers;
					StdDraw.setPenColor(StdDraw.RED);
					StdDraw.text(42, 35, "Player 1's turn:");
					StdDraw.text(42, 34, "you are red");

				} else {
					curppowers = p2powers;
					StdDraw.setPenColor(StdDraw.YELLOW);
					StdDraw.text(42, 35, "Player 2's turn:");
					StdDraw.text(42, 34, "you are yellow");
				}

				StdDraw.setPenColor(StdDraw.DARK_GRAY);
				StdDraw.filledRectangle(42, 29, 5.5, 3);
				StdDraw.filledRectangle(42, 21, 5.5, 3);
				StdDraw.filledRectangle(42, 13, 5.5, 3);
				StdDraw.filledRectangle(42, 5, 5.5, 3);

				Font font2 = new Font(Font.SANS_SERIF, Font.BOLD, 14);
				StdDraw.setFont(font2);
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.text(42, 29, "Normal play");
				StdDraw.text(42, 21, "Number of bombs");
				StdDraw.text(42, 20, "left: " + curppowers[0]);
				StdDraw.text(42, 13, "Number of teleporters");
				StdDraw.text(42, 12, "left: " + curppowers[1]);
				StdDraw.text(42, 5, "Number of colour changers");
				StdDraw.text(42, 4, "left: " + curppowers[2]);
				StdDraw.setFont(font);

				boolean pressed = false;
				boolean optionChosen = false;
				StdDraw.pause(200);

				while (optionChosen == false) {
					StdDraw.setPenColor(StdDraw.GREEN);
					if (StdDraw.isKeyPressed(KeyEvent.VK_N) || (StdDraw.mouseX() > 36.5 && StdDraw.mouseX() < 47.5
							&& StdDraw.mouseY() < 32 && StdDraw.mouseY() > 26) && StdDraw.isMousePressed()) {
						input = 1;
						StdDraw.rectangle(42, 29, 5.48, 2.98);
						optionChosen = true;
						break;
					} else if (StdDraw.isKeyPressed(KeyEvent.VK_B)
							|| (StdDraw.mouseX() > 36.5 && StdDraw.mouseX() < 47.5 && StdDraw.mouseY() < 24
									&& StdDraw.mouseY() > 18) && StdDraw.isMousePressed()) {
						input = 2;
						StdDraw.rectangle(42, 21, 5.48, 2.98);
						optionChosen = true;
						break;
					} else if (StdDraw.isKeyPressed(KeyEvent.VK_T)
							|| (StdDraw.mouseX() > 36.5 && StdDraw.mouseX() < 47.5 && StdDraw.mouseY() < 16
									&& StdDraw.mouseY() > 10) && StdDraw.isMousePressed()) {
						input = 3;
						StdDraw.rectangle(42, 13, 5.48, 2.98);
						optionChosen = true;
						break;
					} else if (StdDraw.isKeyPressed(KeyEvent.VK_C)
							|| (StdDraw.mouseX() > 36.5 && StdDraw.mouseX() < 47.5 && StdDraw.mouseY() < 8
									&& StdDraw.mouseY() > 2) && StdDraw.isMousePressed()) {
						input = 4;
						StdDraw.rectangle(42, 5, 5.48, 2.98);
						optionChosen = true;
						break;
					} else if (StdDraw.isKeyPressed(KeyEvent.VK_X)) {
						input = 0;
						optionChosen = true;
						Exit();
						break;
					} else {
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.setFont(font);
						StdDraw.text(20, 30, "Please Select an option.");
						StdDraw.show();
					}
				}
				// }

				StdDraw.setFont(font);
				StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
				// StdDraw.text(20, 30, "Please Select an option.");
				StdDraw.filledRectangle(20, 30, 14, 2);
				StdDraw.show();

				StdDraw.setPenColor(StdDraw.GREEN);
				StdDraw.text(20, 30, "Select the column you'd like to play");
				StdDraw.show();

				colIn = -1;
				StdDraw.pause(200);

				if (!StdDraw.isMousePressed()) {
					while (!StdDraw.isMousePressed() || colIn == -1) {
						if (StdDraw.isKeyPressed(KeyEvent.VK_0)) {
							colIn = 0;
							StdOut.println("AWEEEEEE");
							break;
						} else if (StdDraw.isKeyPressed(KeyEvent.VK_1)) {
							colIn = 1;
							break;
						} else if (StdDraw.isKeyPressed(KeyEvent.VK_2)) {
							colIn = 2;
							break;
						} else if (StdDraw.isKeyPressed(KeyEvent.VK_3)) {
							colIn = 3;
							break;
						} else if (StdDraw.isKeyPressed(KeyEvent.VK_4)) {
							colIn = 4;
							break;
						} else if (StdDraw.isKeyPressed(KeyEvent.VK_5)) {
							colIn = 5;
							break;
						} else if (StdDraw.isKeyPressed(KeyEvent.VK_6)) {
							colIn = 6;
							break;
						} else if ((StdDraw.mouseX() >= 33.5 || StdDraw.mouseX() < 5.5 || StdDraw.mouseY() >= 28
								|| StdDraw.mouseY() < 4)) {
							colIn = -1;

						} else {
							if ((StdDraw.mouseX() >= 5.5 && StdDraw.mouseX() <= 9.5)) {
								colIn = 0;
							} else if ((StdDraw.mouseX() < 13.5)) {
								colIn = 1;
							} else if ((StdDraw.mouseX() < 17.5)) {
								colIn = 2;
							} else if ((StdDraw.mouseX() < 21.5)) {
								colIn = 3;
							} else if ((StdDraw.mouseX() < 25.5)) {
								colIn = 4;
							} else if ((StdDraw.mouseX() < 29.5)) {
								colIn = 5;
							} else if ((StdDraw.mouseX() < 33.5)) {
								colIn = 6;
							}
						}
					}
				} else {
					input = -1;
					StdDraw.pause(100);
				}

				StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
				// StdDraw.text(20, 30, "Please Select an option.");
				StdDraw.filledRectangle(20, 30, 14, 2);
				StdDraw.show();

				// colIn=GUIColIn();

				StdDraw.setFont(font);
				StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
				StdDraw.filledRectangle(20, 30, 14, 2);
				StdDraw.show();

//				StdDraw.pause(100);
//				DisplayGui(grid, guiX, guiY);
//				StdDraw.show();

				switch (input) {
				case 0:
					Exit();
					break;

				case 1:
					StdAudio.play("140382__d-w__coins-01.wav");
					Play(colIn, grid, player1);
					break;

				case 2:
					if (curppowers[bomb] > 0) {
						// StdDraw.enableDoubleBuffering();
						// StdDraw.show();
						// StdDraw.picture(20, 20, "bomb-2025548_960_720.png");
						// StdDraw.pause(5);
						// StdDraw.clear();
						// createBoard(guiX, guiY);
						curppowers[bomb]--;
						bombplayedlast = true;
						StdAudio.play("244345__willlewis__musket-explosion.wav");
						Bomb(colIn, grid, player1);
					} else {
						StdDraw.setFont(font);
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.text(20, 30, "Out of bomb discs.");
						StdDraw.show();
						StdDraw.pause(1000);
						player1 = !player1;
					}
					break;

				case 3:
					if (curppowers[tele] > 0) {
						curppowers[tele]--;
						StdAudio.play("123222__cgeffex__bullet-whiz.wav");
						Teleport(colIn, grid, player1);
					} else {
						StdDraw.setFont(font);
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.text(20, 30, "Out of teleportation discs.");
						StdDraw.show();
						StdDraw.pause(1000);
						player1 = !player1;
					}
					break;
				case 4:
					if (curppowers[colchang] > 0) {
						curppowers[colchang]--;
						StdAudio.play("166158__adam-n__slurp.wav");
						Colour_Changer(colIn, grid, player1);
					} else {
						StdDraw.setFont(font);
						StdDraw.setPenColor(StdDraw.GREEN);
						StdDraw.text(20, 30, "Out of colour changer discs.");
						StdDraw.show();
						StdDraw.pause(1000);
						player1 = !player1;
					}
					break;

				default:
					StdOut.println("Please choose a valid option");
					player1 = !player1;
					break;
				}

				DisplayGui(grid, guiX, guiY);
				StdDraw.show();

				gameOver = playerWin(grid, p1powers, p2powers, bombplayedlast);
				if (gameOver) {
					Font font5 = new Font(Font.SANS_SERIF, Font.BOLD, 16);
					StdDraw.setFont(font5);
					StdDraw.setPenColor(StdDraw.DARK_GRAY);
					StdDraw.filledRectangle(20, 20, 6, 3);
					StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
					StdDraw.filledRectangle(20, 20, 5.5, 2.5);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.rectangle(17.5, 20, 2, 1);
					StdDraw.setPenColor(StdDraw.RED);
					StdDraw.filledRectangle(17.5, 20, 2, 1);
					StdDraw.setFont(font);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.setFont(font5);
					StdDraw.text(17.5, 20, "No");
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.rectangle(22.5, 20, 2, 1);
					StdDraw.setPenColor(StdDraw.GREEN);
					StdDraw.filledRectangle(22.5, 20, 2, 1);
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.setFont(font5);
					StdDraw.text(22.5, 20, "Yes");
					StdDraw.setFont(font5);
					StdDraw.text(20, 21.6, "Play Again:");
					Font font3 = new Font(Font.SANS_SERIF, Font.BOLD, 14);
					StdDraw.setFont(font3);
					StdDraw.text(20, 18.4, "Press 1 for yes or 2 for no");
					String playerWin = "PLAYER 2";
					if (player1 == true) {
						playerWin = "PLAYER 1";
					}
					StdDraw.setPenColor(StdDraw.GREEN);
					Font font4 = new Font(Font.SANS_SERIF, Font.BOLD, 50);
					StdDraw.setFont(font4);
					StdDraw.text(20, 25, playerWin + " WINS!");
					StdDraw.show();
					player1 = !startingPlayer;
					startingPlayer = !startingPlayer;
					replayGUI(grid, p1powers, p2powers, guiX, guiY);
//				initialiseBoard(guiX, guiY);
//				DisplayGui(grid, guiX, guiY);
				} else {
					player1 = !player1;
				}
			}
		}

	}

//	public static int GUIColIn()
//	{
//		int colIn=-1;
//		
//		if (!StdDraw.isMousePressed()) {
//			while (!StdDraw.isMousePressed() || colIn == -1) {
//				if (StdDraw.mouseX() >= 33.5 || StdDraw.mouseX() < 5.5 || StdDraw.mouseY() >= 28
//						|| StdDraw.mouseY() < 4) {
//					colIn = -1;
//				} else {
//					if ((StdDraw.mouseX() >= 5.5 && StdDraw.mouseX() <= 9.5) || StdDraw.isKeyPressed(KeyEvent.VK_0)) {
//						colIn = 0;
//					} else if ((StdDraw.mouseX() < 13.5) || StdDraw.isKeyPressed(KeyEvent.VK_0)) {
//						colIn = 1;
//					} else if ((StdDraw.mouseX() < 17.5) || StdDraw.isKeyPressed(KeyEvent.VK_0)) {
//						colIn = 2;
//					} else if ((StdDraw.mouseX() < 21.5) || StdDraw.isKeyPressed(KeyEvent.VK_0)) {
//						colIn = 3;
//					} else if ((StdDraw.mouseX() < 25.5) || StdDraw.isKeyPressed(KeyEvent.VK_0)) {
//						colIn = 4;
//					} else if ((StdDraw.mouseX() < 29.5) || StdDraw.isKeyPressed(KeyEvent.VK_0)) {
//						colIn = 5;
//					} else if ((StdDraw.mouseX() < 33.5) || StdDraw.isKeyPressed(KeyEvent.VK_0)) {
//						colIn = 6;
//					}
//				}
//			}
//		} else {
//			GUIColIn();
//		}
//		
//		return colIn;
//	}

	public static void initialiseBoard(int[][] guiX, int[][] guiY) {
		StdDraw.setCanvasSize(1000, 800);
		StdDraw.setXscale(0, 50);
		StdDraw.setYscale(0, 40);
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledRectangle(25, 20, 25, 20);
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(25, 20, 24.5, 19.5);
		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
		StdDraw.filledRectangle(20, 20, 15, 17);
		StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
		StdDraw.filledRectangle(20, 20, 14.5, 16.5);
		StdDraw.setPenColor(StdDraw.BLACK);
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 70);
		StdDraw.setFont(font);
		StdDraw.text(23, 33, "Connect 4");
		StdDraw.setPenColor(StdDraw.BOOK_RED);
		StdDraw.filledCircle(7.5, 33.5, 1.5);
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.filledCircle(7.5, 33.5, 1.3);
		StdDraw.setPenColor(StdDraw.ORANGE);
		StdDraw.filledCircle(11, 33.5, 1.5);
		StdDraw.setPenColor(StdDraw.YELLOW);
		StdDraw.filledCircle(11, 33.5, 1.3);

//		StdDraw.setPenColor(StdDraw.DARK_GRAY);
//		StdDraw.filledRectangle(42, 29, 5.5, 3);
//		StdDraw.filledRectangle(42, 21, 5.5, 3);
//		StdDraw.filledRectangle(42, 13, 5.5, 3);
//		StdDraw.filledRectangle(42, 5, 5.5, 3);

		double radius = 2;
		for (int x = 1; x <= 7; x++) {
			for (int y = 1; y <= 6; y++) {
				StdDraw.setPenColor(StdDraw.DARK_GRAY);
				StdDraw.filledCircle(4 * x - (5 / 2) + 6, 4 * y - (5 / 2) + 4, radius);
				StdOut.println((4 * x - (5 / 2) + 6) + " " + (y - (5 / 2) + 4));
				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
				StdDraw.filledCircle(4 * x - (5 / 2) + 6, 4 * y - (5 / 2) + 4, radius - 0.2);
			}
		}
	}

	public static int validCol() {
		StdOut.println("Choose a column to play in:");
		int colIn;
		try {
			colIn = StdIn.readInt();
		} catch (Exception InputMismatchException) {
			colIn = -1;
		}
		while (colIn < 0 || colIn > 6) {
			StdOut.println("Illegal move, please input legal move:");
			try {
				colIn = StdIn.readInt();
			} catch (Exception InputMismatchException) {
				colIn = -1;
			}
		}
		return colIn;
	}

	public static boolean playerWin(int[][] grid, int[] p1powers, int[] p2powers, boolean bombplayedlast) {
		boolean gameOver;
		if (Check_Win(grid, p1powers, p2powers, bombplayedlast) == 0) {
			gameOver = false;
		} else if (Check_Win(grid, p1powers, p2powers, bombplayedlast) == 1) {
			StdOut.println("Player 1 wins!");
			gameOver = true;
		} else if (Check_Win(grid, p1powers, p2powers, bombplayedlast) == 2) {
			StdOut.println("Player 2 wins!");
			gameOver = true;
		} else {
			StdOut.println("Draw!");
			gameOver = true;
		}
		return gameOver;
	}

	public static void replay(int[][] grid, int[] p1powers, int[] p2powers) {
		boolean replayOption = false;
		StdOut.println("Do you want to play again? \n 1. Yes \n 2. No");
		int end = StdIn.readInt();
		try {
			if (end == 1) {
				replayOption = true;
				grid = resetBoard(grid, p1powers, p2powers);
			} else if (end == 2) {
				Exit();
			} else {
				replayOption = false;
			}
		} catch (Exception InputMismatchException) {
			replayOption = false;
		}
		while (!replayOption) {
			StdOut.println("Choose either 1 for Yes or 2 for No:");
			end = StdIn.readInt();
			try {
				if (end == 1) {
					grid = resetBoard(grid, p1powers, p2powers);
					replayOption = true;
				} else if (end == 2) {
					Exit();
				} else {
					replayOption = false;
				}
			} catch (Exception InputMismatchException) {
				replayOption = false;
			}
		}
	}

	public static void replayGUI(int[][] grid, int[] p1powers, int[] p2powers, int[][] guiX, int[][] guiY) {
		int end = 3;
		while (end == 3) {
			if (StdDraw.mouseX() > 15.5 && StdDraw.mouseX() < 19.5 && StdDraw.mouseY() > 19 && StdDraw.mouseY() < 21 && StdDraw.isMousePressed()) {
				end = 2;
			} else if (StdDraw.mouseX() > 20.5 && StdDraw.mouseX() < 24.5 && StdDraw.mouseY() > 19
					&& StdDraw.mouseY() < 21 && StdDraw.isMousePressed()) {
				end = 1;
			} else if (StdDraw.isKeyPressed(KeyEvent.VK_1)) {
				end = 1;
			} else if (StdDraw.isKeyPressed(KeyEvent.VK_2)) {
				end = 2;
			}
		}

		if (end == 1) {
			grid = resetBoard(grid, p1powers, p2powers);
		} else if (end == 2) {
			Exit();
		}

		initialiseBoard(guiX, guiY);
		DisplayGui(grid, guiX, guiY);
	}

	public static int[][] resetBoard(int[][] grid, int[] p1powers, int[] p2powers) {
		for (int i = 0; i < 6; i++) {
			for (int k = 0; k < 7; k++) {
				grid[i][k] = 0;
			}
		}

		for (int i = 0; i < 3; i++) {
			p1powers[i] = 2;
			p2powers[i] = 2;
		}
		return grid;
	}

	public static void Display(int[][] grid) {
		StdOut.println("");
		for (int i = -1; i < 6; i++) {
			for (int j = -1; j < 7; j++) {
				if (i == -1) {
					if (j == -1) {
						StdOut.print("  ");
					} else {
						StdOut.print(j + " ");
					}
				} else {
					if (j == -1) {
						StdOut.print(i + " ");
					} else {
						if (grid[i][j] == 0) {
							StdOut.print("* ");
						} else if (grid[i][j] == 1) {
							StdOut.print("R ");
						} else {
							StdOut.print("Y ");
						}
					}
				}
			}
			StdOut.println("");
		}
		StdOut.println("");
	}

	public static void DisplayGui(int[][] grid, int[][] guiX, int[][] guiY) {
//		StdDraw.enableDoubleBuffering();
//		StdDraw.setCanvasSize(1000, 800);
//		StdDraw.setXscale(0, 50);
//		StdDraw.setYscale(0, 40);
//		StdDraw.setPenColor(StdDraw.GRAY);
//		StdDraw.filledRectangle(25, 20, 25, 20);
//		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
//		StdDraw.filledRectangle(25, 20, 24.5, 19.5);
//		StdDraw.setPenColor(StdDraw.BOOK_BLUE);
//		StdDraw.filledRectangle(20, 20, 15, 17);
//		StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
//		StdDraw.filledRectangle(20, 20, 14.5, 16.5);
//		StdDraw.setPenColor(StdDraw.BLACK);
//		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 70);
//		StdDraw.setFont(font);
//		StdDraw.text(23, 33, "Connect 4");
//		StdDraw.setPenColor(StdDraw.BOOK_RED);
//		StdDraw.filledCircle(7.5, 33.5, 1.5);
//		StdDraw.setPenColor(StdDraw.RED);
//		StdDraw.filledCircle(7.5, 33.5, 1.3);
//		StdDraw.setPenColor(StdDraw.ORANGE);
//		StdDraw.filledCircle(11, 33.5, 1.5);
//		StdDraw.setPenColor(StdDraw.YELLOW);
//		StdDraw.filledCircle(11, 33.5, 1.3);
//		
//		double radius = 2;
//		for (int x = 1; x <= 7; x++) {
//			for (int y = 1; y <= 6; y++) {
//				StdDraw.setPenColor(StdDraw.DARK_GRAY);
//				StdDraw.filledCircle(4 * x - (5 / 2) + 6, 4 * y - (5 / 2) + 4, radius);
//				StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
//				StdDraw.filledCircle(4 * x - (5 / 2) + 6, 4 * y - (5 / 2) + 4, radius - 0.2);
//			}
//		}

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (grid[i][j] == 0) {
					StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
					StdDraw.filledCircle(guiX[i][j], guiY[i][j], 1.8);
				} else if (grid[i][j] == 1) {
					StdDraw.setPenColor(StdDraw.BOOK_RED);
					StdDraw.filledCircle(guiX[i][j], guiY[i][j], 1.8);
					StdDraw.setPenColor(StdDraw.RED);
					StdDraw.filledCircle(guiX[i][j], guiY[i][j], 1.6);
				} else {
					StdDraw.setPenColor(StdDraw.ORANGE);
					StdDraw.filledCircle(guiX[i][j], guiY[i][j], 1.8);
					StdDraw.setPenColor(StdDraw.YELLOW);
					StdDraw.filledCircle(guiX[i][j], guiY[i][j], 1.6);
				}
			}
		}
	}

	public static void Exit() {
		System.exit(0);
	}

	public static int[][] Play(int i, int[][] grid, boolean player1) {
		if (grid[0][i] == 0) {
			int playerNo = 0;
			if (player1) {
				playerNo = 1;
			} else {
				playerNo = 2;
			}

			int rowNo = 5;
			boolean placed = false;
			while (rowNo >= 0 && placed == false) {
				if (grid[rowNo][i] == 0) {
					grid[rowNo][i] = playerNo;
					placed = true;
					break;
				}
				rowNo--;
			}
		} else {
			StdOut.println("Column is full.");
		}

		return grid;
	}

	public static int Check_Win(int[][] grid, int[] p1powers, int[] p2powers, boolean bombplayedlast) {
		int winner = 0;
		boolean twoWinners;
		int winner1 = 0;
		int winner2 = 0;
		int lasti = 0, lastj = 0;

		for (int i = 5; i >= 0; i--) {
			lasti = i;
			for (int j = 0; j <= 6; j++) {
				lastj = j + 1;
				if (grid[i][j] != 0) {
					if (winner == 0 && i > 2 && j < 4) {
						winner = CheckRightDiagonal(i, j, grid);
					}
					if (winner == 0 && i < 3 && j < 4) {
						winner = CheckLeftDiagonal(i, j, grid);
					}
					if (winner == 0 && j < 4) {
						winner = checkHorizontal(i, j, grid);
					}
					if (winner == 0 && i > 2) {
						winner = CheckVertical(i, j, grid);
					}
					if (winner != 0) {
						if (bombplayedlast) {
							if (winner1 == 0) {
								winner1 = winner;
								winner = 0;
							} else {
								if (winner == winner1) {
									winner = 0;
								} else {
									winner = 3;
								}
							}
						} else {
							break;
						}
					}
				}
			}
		}

		if (checkBoardFull(grid) == true) {
			winner = 3;
		}

		return winner;
	}

	public static int[][] Bomb(int i, int[][] grid, boolean player1) {

		if (grid[0][i] == 0) {
			int playerNo = 0;
			if (player1) {
				playerNo = 1;
			} else {
				playerNo = 2;
			}

			int rowNo = 5;
			boolean placed = false;
			while (rowNo >= 0 && placed == false) {
				if (grid[rowNo][i] == 0) {
					grid[rowNo][i] = playerNo;
					placed = true;
					break;
				}
				rowNo--;
			}

			for (int k = i - 1; k <= i + 1; k++) {
				for (int j = rowNo + 1; j >= rowNo - 1; j--) {
					try {
						grid[j][k] = 0;
					} catch (Exception ArrayIndexOutOfBoundsException) {

					}
				}
			}

			grid[rowNo][i] = playerNo;
			int[] dropCol;
			int[] afterDrop;
			for (int k = i - 1; k <= i + 1; k++) {
				dropCol = new int[6];
				int countDropCol = 0;
				afterDrop = new int[6];
				for (int j = 5; j >= 0; j--) {
					try {
						dropCol[countDropCol] = grid[j][k];
						countDropCol++;
					} catch (Exception ArrayIndexOutOfBoundsException) {

					}
//					dropCol[countDropCol] = grid[j][k];
//					countDropCol++;
				}

				afterDrop = bombDrop(dropCol);
				int countAfterDrop = 0;
				for (int j = 5; j >= 0; j--) {
					try {
						grid[j][k] = afterDrop[countAfterDrop];
						countAfterDrop++;
					} catch (Exception ArrayIndexOutOfBoundsException) {

					}
//					grid[j][k] = afterDrop[countAfterDrop];
//					countAfterDrop++;
				}
			}
		} else {
			StdOut.println("Column is full.");
		}
		return grid;
	}

	public static int[] bombDrop(int[] dropCol) {
		int[] newCol = new int[6];
		int countNewCol = 0;
		for (int j = 0; j <= 5; j++) {
			if (dropCol[j] != 0) {
				newCol[countNewCol] = dropCol[j];
				countNewCol++;
			}
		}
		return newCol;
	}

	public static int[][] Teleport(int i, int[][] grid, boolean player1) {
		int playerNo = 0;
		if (player1) {
			playerNo = 1;
		} else {
			playerNo = 2;
		}

		int rowNo = 0;
		if (grid[5][i] == 0) {

		} else {
			boolean foundRow = false;
			while (rowNo <= 5 && foundRow == false) {
				if (grid[rowNo][i] != 0) {
					foundRow = true;
					break;
				}
				rowNo++;
			}

			int valueToTeleport = grid[rowNo][i];
			grid[rowNo][i] = playerNo;
			int colToTransportTo = i + 3;
			if (colToTransportTo > 6) {
				colToTransportTo = colToTransportTo - 7;
			}
			boolean playerToTrans = true;
			if (valueToTeleport == 2) {
				playerToTrans = false;
			}
			if (grid[0][colToTransportTo] == 0) {
				Play(colToTransportTo, grid, playerToTrans);
			} else {
				StdOut.println("Column is full.");
			}
		}
		return grid;
	}

	public static int[][] Colour_Changer(int i, int[][] grid, boolean player1) {

		int playerNo = 0;
		if (player1) {
			playerNo = 1;
		} else {
			playerNo = 2;
		}

		int rowNo = 0;
		boolean foundRow = false;

		while (rowNo < 5 && foundRow == false) {
			if (grid[rowNo][i] != 0) {
				foundRow = true;
				break;
			}
			rowNo++;
		}

		if (rowNo == 5 && grid[rowNo][i] == 0) {
			grid[rowNo][i] = playerNo;
		} else if (grid[rowNo][i] == 1) {
			grid[rowNo][i] = 2;
		} else {
			grid[rowNo][i] = 1;
		}

		return grid;
	}

	public static int CheckRightDiagonal(int startingRow, int startingCol, int[][] grid) {
		int winner = 0;
		int current = grid[startingRow][startingCol];
		boolean same = true;
		int i = startingRow - 1;
		int j = startingCol + 1;

		while (same == true && i > startingRow - 4 && j < startingCol + 4) {
			if (grid[i][j] == current) {
				same = true;
			} else if (grid[i][j] != current) {
				same = false;
			}
			i--;
			j++;
		}

		if (same) {
			winner = grid[startingRow][startingCol];
		} else {
			winner = 0;
		}
		return winner;
	}

	public static int CheckLeftDiagonal(int startingRow, int startingCol, int[][] grid) {
		int winner = 0;
		int current = grid[startingRow][startingCol];
		boolean same = true;
		int i = startingRow + 1;
		int j = startingCol + 1;

		while (same == true && i < startingRow + 4 && j < startingCol + 4) {
			if (grid[i][j] == current) {
				same = true;
			} else if (grid[i][j] != current) {
				same = false;
			}
			i++;
			j++;
		}

		if (same) {
			winner = grid[startingRow][startingCol];
		} else {
			winner = 0;
		}
		return winner;
	}

	public static int CheckVertical(int startingRow, int startingCol, int[][] grid) {
		int winner = 0;
		int current = grid[startingRow][startingCol];
		boolean same = true;
		int i = startingRow - 1;
		int j = startingCol;

		while (same == true && i > startingRow - 4) {
			if (grid[i][j] == current) {
				same = true;
			} else if (grid[i][j] != current) {
				same = false;
			}
			i--;
		}

		if (same) {
			winner = grid[startingRow][startingCol];
		} else {
			winner = 0;
		}
		return winner;
	}

	public static int checkHorizontal(int startingRow, int startingCol, int[][] grid) {
		int winner = 0;
		int current = grid[startingRow][startingCol];
		boolean same = true;
		int i = startingRow;
		int j = startingCol + 1;

		while (same == true && j < startingCol + 4) {
			if (grid[i][j] == current) {
				same = true;
			} else if (grid[i][j] != current) {
				same = false;
			}
			j++;
		}

		if (same) {
			winner = grid[startingRow][startingCol];
		} else {
			winner = 0;
		}
		return winner;
	}

	public static boolean checkBoardFull(int[][] grid) {
		boolean full = true;
		for (int i = 5; i >= 0; i--) {
			for (int j = 6; j >= 0; j--) {
				if (grid[i][j] == 0) {
					full = false;
					break;
				}
			}
		}
		return full;
	}
}
