/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.*;
public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		for(int i=0;i<13;i++) {
			for(int j=0;j<nPlayers;j++) {
				if(i==0) {
					used[j]=0;
					used2[j]=0;
				}
				display.printMessage(playerNames[j]+"'s turn");
				display.waitForPlayerToClickRoll(j+1);
				for(int k=0;k<N_DICE;k++) {
					dice[k] = rgen.nextInt(1,6);
				}
				display.displayDice(dice);
				display.printMessage("select dice for reroll");
				display.waitForPlayerToSelectDice();
				for(int k=0;k<N_DICE;k++) {
					if(display.isDieSelected(k)) dice[k]=rgen.nextInt(1,6);
				}
				display.displayDice(dice);
				display.printMessage("select dice finally for reroll");
				display.waitForPlayerToSelectDice();
				for(int k=0;k<N_DICE;k++) {
					if(display.isDieSelected(k)) dice[k]=rgen.nextInt(1,6);
				}
				display.displayDice(dice);
				display.printMessage("select category");
				int category =display.waitForPlayerToSelectCategory();
				boolean p =checkCategory(dice, category,j);
				if(p) {
					display.updateScorecard(category,j+1,score(category));
					if(category<=6) upperscore[j]+= score(category);
					else lowerscore[j]+=score(category);
				}
				else display.updateScorecard(category,j+1,0);
				int c=0;
			if(used[j]==6) {
				display.updateScorecard(UPPER_SCORE,j+1,upperscore[j]);
				if(upperscore[j]>=63) {
					display.updateScorecard(UPPER_BONUS,j+1,35);
					c =1;
				}
				else display.updateScorecard(UPPER_BONUS,j+1,0);
				}
			if(used2[j]==7) {
				display.updateScorecard(LOWER_SCORE,j+1,lowerscore[j]);
			
			if((used2[j]+used[j])==13) display.updateScorecard(TOTAL,j+1,(upperscore[j]+lowerscore[j]+c*35));	
			}			
				
			}
		}
		
		}
	
	
	private boolean checkCategory(int[] dice,int category,int j) {
		
		if(category==ONES) {
			used[j]+=1;
			return true;
		}
		if(category==TWOS){
			used[j]+=1;
			return true;
		}
		if(category==THREES){
			used[j]+=1;
			return true;		
		}
		if(category==FOURS){
			used[j]+=1;
			return true;
		}
		if(category==FIVES) {
			used[j]+=1;
			return true;
		}
		if(category==SIXES) {
			used[j]+=1;
			return true;
		}
		if(category==THREE_OF_A_KIND) {
			used2[j]+=1;
			sort(dice);
			return checkforthree(dice);
		}
		if(category==FOUR_OF_A_KIND) {
			used2[j]+=1;
			sort(dice);
			return checkforfour(dice);
		}
		if(category==FULL_HOUSE) {
			used2[j]+=1;
			sort(dice);
			if(checkforthree(dice)) {
				if((dice[0]==dice[1])||(dice[3]==dice[4])) return true;
			}
			return false;
		}
		if(category==SMALL_STRAIGHT) {
			used2[j]+=1;
			sort(dice);
			return checkforonlytwo(dice);
			
		}
		if(category==LARGE_STRAIGHT) {
			used2[j]+=1;
			sort(dice);
		     return checkforstraight(dice);
			
		}
		if(category==YAHTZEE) {
			used2[j]+=1;
			sort(dice);
			if(dice[0]==dice[dice.length-1]) return true;
			return false;
		}
			
		
		if(category==CHANCE) {
			used2[j]+=1;
			return true;
		}
		return true;
	}
	private boolean checkforonlytwo(int[] dice) {
		
		int p =0;
		for(int i=0;i<5;i++) {
			int j=i+1;
			for(;j<5;j++){
				if(dice[i]==dice[j]) p++;
			}
		}
		if(dice[0]==dice[dice.length-1]) return true;
		if(checkforstraight(dice)) return true;
		return (p==1);
	}
	private boolean checkforstraight(int[] dice) {
	
		int p =0;
		for(int i=0;i<5;i++) {
			int j=i+1;
			for(;j<5;j++){
				if(dice[i]==dice[j]) p++;
			}
		}
		
		return (p==0);
	}
	private void sort(int[] dice) {
		for(int i=0;i<dice.length-1;i++) {
			int min =i;
			for(int j=i+1;j<dice.length;j++) {
				if(dice[j]<dice[min]) min =j;
			}
			int x =dice[i];
			dice[i] = dice[min];
			dice[min]=x;
				
		}
	}
	private boolean checkforthree(int[] dice) {
        boolean p =false;
		for(int i=0;i<dice.length-2;i++) {
			if((dice[i]==dice[i+1])&(dice[i+1]==dice[i+2])) p=true; 
		}
		return p;
	}
	private boolean checkforfour(int[] dice) {
        boolean p =false;
		for(int i=0;i<dice.length-3;i++) {
			if((dice[i]==dice[i+1])&(dice[i+1]==dice[i+2])&(dice[i+2]==dice[i+3])) p=true; 
		}
		return p;
	}
	private int score(int category) {
        		
		if(category==ONES) {
			return count(dice,category);
		}
		if(category==TWOS) {
			return 2*count(dice,category);
		}
		if(category==THREES) {
			return 3*count(dice,category);
		}
		if(category==FOURS) {
			return 4*count(dice,category);
		}
		if(category==FIVES) {
			return 5*count(dice,category);
		}
		if(category==SIXES) {
			return 6*count(dice,category);
		}
		if(category==THREE_OF_A_KIND) return sum(dice);		
		if(category==FOUR_OF_A_KIND) return sum(dice);
		if(category==FULL_HOUSE) return 25;
		if(category==SMALL_STRAIGHT) return 30;
		if(category==LARGE_STRAIGHT) return 40;	
		if(category==YAHTZEE) return 50;
		if(category==CHANCE) return sum(dice);
		return 0;
	}
	
	private int sum(int[] dice) {
		int sum =0;
		for(int i=0;i<dice.length;i++) {
			sum+=dice[i];
		}
		return sum;
	}
	private int count(int[] dice,int category) {
		int p=0;
		for(int i=0;i<N_DICE;i++) {
			if(dice[i]==category) p++;
		}
		return p;
	}
		
/* Private instance variables */
	private int nPlayers;
	private int[] used= new int[100];
	private int[] upperscore= new int[100];
	private int[] used2= new int[100];
	private int[] lowerscore= new int[100];
	private int[] dice= new int[N_DICE];
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
