/*
Author:    Michael Wilson
Course:    COP2220
Title  :   Project 3 - Rock-Paper-Scissors-Lizard-Spock
Due Date:  3/19/2017
*/

#define _CRT_SECURE_NO_WARNINGS
#define _CRT_NONSTDC_NO_DEPRECATE

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

#define SHELDON "Sheldon" 
#define LEONARD "Leonard"

#define SHELDON_WINS 0
#define LEONARD_WINS 1
#define TIE 2

#define ROCK 0
#define PAPER 1
#define SCISSORS 2
#define LIZARD 3
#define SPOCK 4

#define INVALID -1
#define ERROR_MSG "\nError: Invalid command line aguments"

/* Converts a shape name into its numerical value
shape - the string of the shape's name
*/
int convertShape(char* shape)
{
	int var = INVALID;
	if (strcmp(shape, "rock") == 0) var = ROCK;
	else if (strcmp(shape, "paper") == 0) var = PAPER;
	else if (strcmp(shape, "scissors") == 0) var = SCISSORS;
	else if (strcmp(shape, "lizard") == 0) var = LIZARD;
	else if (strcmp(shape, "spock") == 0) var = SPOCK;
	return var;
}

/* Gets the better of the two shapes which defeat the one given
shape - the shape given
*/
int getBetterShape(int shape)
{
	int result = INVALID;
	if (shape == ROCK) result = PAPER;
	else if (shape == PAPER) result = SCISSORS;
	else if (shape == SCISSORS) result = SPOCK;
	else if (shape == LIZARD) result = ROCK;
	else if (shape == SPOCK) result = LIZARD;
	return result;
}

/* Returns leonards next shape pick based on the current same state
sheldonsCurrentShape - the shape sheldon used in the last match
leonardsCurrentShape - the shape leonard used in the last match
gameOutcome - the outcome of the last match
*/
int determineLeonardsNextShape(int sheldonsCurrentShape, int leonardsCurrentShape, int gameOutcome)
{
	int pick = INVALID;
	if (gameOutcome == LEONARD_WINS) pick = leonardsCurrentShape;
	else if (gameOutcome == SHELDON_WINS)
	{
		pick = getBetterShape(sheldonsCurrentShape);
	}
	else pick = getBetterShape(leonardsCurrentShape);
	return pick;
}

/* Returns sheldons next shape pick based on the current same state
sheldonsCurrentShape - the shape sheldon used in the last match
leonardsCurrentShape - the shape leonard used in the last match
gameOutcome - the outcome of the last match
*/
int determineSheldonsNextShape(int sheldonsCurrentShape, int leonardsCurrentShape, int gameOutcome)
{
	int pick = INVALID;
	
	if (sheldonsCurrentShape == SPOCK)
	{
		if (gameOutcome == SHELDON_WINS) pick = ROCK;
		else if (gameOutcome == LEONARD_WINS) pick = PAPER;
		else pick = LIZARD;
	}
	else pick = SPOCK;

	return pick;
}

/* retruns the outcome of the match based on sheldon and leonards shapes
sheldonsShape - the shape sheldon used
leonardsShape - the shape leonard used
*/
int determineWinner(int sheldonsShape, int leonardsShape)
{
	int result = TIE;

	if (sheldonsShape == ROCK)
	{
		if (leonardsShape == ROCK) result = TIE;
		else if (leonardsShape == PAPER) result = LEONARD_WINS;
		else if (leonardsShape == SCISSORS)result = SHELDON_WINS;
		else if (leonardsShape == LIZARD)result = SHELDON_WINS;
		else if (leonardsShape == SPOCK)result = LEONARD_WINS;
	}
	else if (sheldonsShape == PAPER)
	{
		if (leonardsShape == ROCK) result = SHELDON_WINS;
		else if (leonardsShape == PAPER) result = TIE;
		else if (leonardsShape == SCISSORS) result = LEONARD_WINS;
		else if (leonardsShape == LIZARD) result = LEONARD_WINS;
		else if (leonardsShape == SPOCK) result = SHELDON_WINS;
	}
	else if (sheldonsShape == SCISSORS)
	{
		if (leonardsShape == ROCK) result = LEONARD_WINS;
		else if (leonardsShape == PAPER) result = SHELDON_WINS;
		else if (leonardsShape == SCISSORS) result = TIE;
		else if (leonardsShape == LIZARD) result = SHELDON_WINS;
		else if (leonardsShape == SPOCK) result = LEONARD_WINS;
	}
    else if (sheldonsShape == LIZARD)
	{
		if (leonardsShape == ROCK) result = LEONARD_WINS;
		else if (leonardsShape == PAPER) result = SHELDON_WINS;
		else if (leonardsShape == SCISSORS) result = LEONARD_WINS;
		else if (leonardsShape == LIZARD) result = TIE;
		else if (leonardsShape == SPOCK) result = SHELDON_WINS;
	}
	else if (sheldonsShape == SPOCK)
	{
		if (leonardsShape == ROCK) result = SHELDON_WINS;
		else if (leonardsShape == PAPER) result = LEONARD_WINS;
		else if (leonardsShape == SCISSORS) result = SHELDON_WINS;
		else if (leonardsShape == LIZARD) result = LEONARD_WINS;
		else if (leonardsShape == SPOCK) result = TIE;
	}

	return result;
}


/* Displays the initial shapes of both players and the outcome of the game after all rounds 
sheldonsInitialShape - the shape name sheldon picks initially
sheldonWins - the amount of times sheldon won a match
leonardsInitialShape - the shape name leonard picks initially
leonardWins - the amount of times leonard won a match
ties - the amount of times the match ended in a tie
*/
void displayResults(char* sheldonsInitialShape, int sheldonWins, char* leonardsInitialShape, int leonardWins, int ties)
{
	printf("%s's initial shape: %s\n%s's initial shape: %s\n\n", SHELDON, sheldonsInitialShape, LEONARD, leonardsInitialShape);

	if (sheldonWins == leonardWins)
	{
		printf("Tie Game!");
		printf("\n%s and %s each won %d game(s) and tied %d game(s)", SHELDON, LEONARD, sheldonWins, ties);
	}
	else
	{
		if (sheldonWins > leonardWins) printf("%s Wins!", SHELDON);
		else printf("%s Wins!", LEONARD);

		printf("\n%s won %d game(s), %s won %d game(s), and they tied %d game(s)", SHELDON, sheldonWins, LEONARD, leonardWins, ties);
	}
}

/* Updates the scores of sheldon, leonard, and ties based on the outcome provided
gameOutcome - the outcome of the last match
pSheldonsWinCount - a pointer to sheldons win count
pLeonardsWinCount - a pointer to leonards win count
pTieCount - a pointer to the tie count
*/
void updateScores(int gameOutcome, int *pSheldonsWinCount, int *pLeonardsWinCount, int *pTieCount)
{
	if (gameOutcome == SHELDON_WINS) (*pSheldonsWinCount)++;
	else if (gameOutcome == LEONARD_WINS) (*pLeonardsWinCount)++;
	else (*pTieCount)++;
}

/* Runs through all the rounds provided and calculates the overall outcome based on the initial shapes provided.
sheldonsInitialShape - the shape sheldon picks initially
leonardsInitialShape - the shape leonard picks initially
numberGame - the amount of rounds to be played
pSheldonsWinCount - a pointer to sheldons win count
pLeonardsWinCount - a pointer to leonards win count
pTieCount - a pointer to the tie count
*/
void playGame(int sheldonsInitialShape, int leonardsInitialShape, int numberGames, int *pSheldonsWinCount, int *pLeonardsWinCount, int *pTieCount)
{
	int sheldonsShape = sheldonsInitialShape;
	int leonardsShape = leonardsInitialShape;
	int winner = TIE;

	for (int i = 0; i < numberGames; i++)
	{
		winner = determineWinner(sheldonsShape, leonardsShape); // determine round winner
		updateScores(winner, pSheldonsWinCount, pLeonardsWinCount, pTieCount); // update scores
		leonardsShape = determineLeonardsNextShape(sheldonsShape, leonardsShape, winner); // pick leonards shape for next round
		sheldonsShape = determineSheldonsNextShape(sheldonsShape, leonardsShape, winner); // pick sheldons next for next round
	}
}


int main(int argc, char *argv[])
{
	printf("Michael Wilson\nRock-Paper-Scissors-Lizard-Spock\n\n"); // Display programmer name and program title

	if (argc == 4)
	{
		// declare program variables
		int sheldonsFirstShape = convertShape(strlwr(argv[1])),
			leonardsFirstShape = convertShape(strlwr(argv[2])),
			rounds = atoi(argv[3]), 
			sheldonWins = 0, leonardWins = 0, ties = 0;

	
		if (sheldonsFirstShape != INVALID && leonardsFirstShape != INVALID) // if sheldon and leondards shapes are valid continue with program
		{
			playGame(sheldonsFirstShape, leonardsFirstShape, rounds, &sheldonWins, &leonardWins, &ties);
			displayResults(strlwr(argv[1]), sheldonWins, strlwr(argv[2]), leonardWins, ties);
		}
		else printf(ERROR_MSG);
	}
	else printf(ERROR_MSG);

	printf("\n\n");
	system("pause");
	return 0;
}