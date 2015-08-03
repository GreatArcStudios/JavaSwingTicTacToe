import java.util.Scanner;

/*
 * 
 * This is really inefficient, so I'll probably fix it up if I have time. 
 */

public class TToe {

	public int rows = 3;
	public int cols = 3;
	public char[][] grid = new char[rows][cols]; 
	public int roundNum = 0;
	public String instructions;
	public int pRow = 0;
	public int pCol = 0;
	public int diagonalCount = 0;
	public boolean won = false;

	public Scanner s = new Scanner(System.in);

	public TToe(){

		/*

		//define how large board will be
		System.out.println("How many rows?");
		rows = s.nextInt();
		System.out.println("How many cols");
		cols = s.nextInt();
		 */

		//Draw Game 
		System.out.println("TIC TAC TOE - Round " + roundNum);

		/*for(int i = 0; i < rows - 1; i ++){

		}*/

		System.out.println(grid[0][0] + "|" + grid[0][1] + "|"+ grid[0][2]);
		System.out.println("------");
		System.out.println(grid[1][0] + "|" + grid[1][1] + "|"+ grid[1][2]);
		System.out.println("------");
		System.out.println(grid[2][0] + "|" + grid[2][1] + "|"+ grid[2][2]);

		//get game input

		System.out.println("Show instructions? Type Y/N");
		instructions = s.nextLine().toLowerCase();
		if ( instructions.equals("y") ){

			System.out.println("First, type in the row id, for example the first row number is 0 and the max is 2 ");
			System.out.println("Second, type in the col id, for example the first col number is 0 and the max is 2 ");
			System.out.println("Ready?");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("GO!");

			while(won == false){
				OverallLogic();
			}



		}else if (instructions.equals("n")){

			while(won == false){
				OverallLogic();
			}


		}
	}

	//check if anyone won
	public void WinLogic(){


		//diagonals

		while (won == false){
			for(int i = 0; i < 3; ++ i){

				for(int j = 0; j < 3; ++ j){

					//DEBUG STUFF 
					/*
					System.out.println("DEBUG the value of i is : " + i );
					System.out.println("DEBUG the value of j is : " + j);
					System.out.println("DEBUG the value of pRow is" + pRow);
					System.out.println("DEBUG the value of pCol is" + pCol);
					 */

					if(j==i && grid[i][j] == 'x') {

						System.out.println("Debug: player fill found!!!");
						if(i == pRow && j == pCol){

							diagonalCount++;
							System.out.println("Diagonal Count is " + diagonalCount);
						}
						if(diagonalCount == 3){
							won = true;
							System.out.println("Player one won! By diagonals.");
						}

					}
					
					/*
					if(grid[i][j] == 'x'){
						System.out.println("DEBUG the value of j is : " + j);
						System.out.println("DEBUG the value of i is : " + i);
						won = true;
						System.out.println("Player one won! By diagonals");
					}*/
				}

			}


			//rows
			for(int i = 0; i < 3; i ++){

				if(grid[0][i] == 'x' && grid[1][i] == 'x' && grid[2][i] == 'x'){
					won = true;
					System.out.println("Player one won! By rows");
				}else if (grid[0][i] == 'o' && grid[1][i] == 'o' && grid[2][i] == 'o' ){
					won = true;
					System.out.println("Player two won!");
				}


				for(int j = 0; j < 3; j++){

					if(grid[j][0] == 'x' && grid[j][1] == 'x' && grid[j][2] == 'x'){
						won = true;
						System.out.println("Player one won! By cols");
					}else if (grid[j][0] == 'o' && grid[j][1] == 'o' &&grid[j][2] == 'o' ){
						won = true;
						System.out.println("Player two won!");
					}
				}

			}
			break;
		}
	}


	public void DrawBoard(){
		//draw board
		System.out.println("TIC TAC TOE - Round " + roundNum);
		System.out.println(grid[0][0] + "|" + grid[0][1] + "|"+ grid[0][2]);
		System.out.println("------");
		System.out.println(grid[1][0] + "|" + grid[1][1] + "|"+ grid[1][2]);
		System.out.println("------");
		System.out.println(grid[2][0] + "|" + grid[2][1] + "|"+ grid[2][2]);
	}

	public void PlacingLogic(){
		//placing logic
		System.out.println("Type in the row grid number:");
		pRow = s.nextInt();
		System.out.println("Type in the col grid number:");
		pCol = s.nextInt();


		try{
			if(grid[pRow][pCol] != 'x' && grid[pRow][pCol] != 'o'){
				if(roundNum%2 == 0 ){
					grid[pRow][pCol] = 'x';
					roundNum ++;
				}else{
					grid[pRow][pCol] = 'o';
					roundNum ++;
				}

			}else if (grid[pRow][pCol] == 'x' || grid[pRow][pCol] == 'o'){
				System.out.println("choose a dif location");
				System.out.println("Type in the row grid number:");
				pRow = s.nextInt();
				System.out.println("Type in the col grid number:");
				pCol = s.nextInt();
				if(roundNum%2 == 0 ){
					grid[pRow][pCol] = 'x';
					roundNum ++;
				}else{
					grid[pRow][pCol] = 'o';
					roundNum ++;
				}

			}
		}catch(Exception e ){
			e.printStackTrace();
			System.out.println("Use a number within the bounds of the board");
			if (grid[pRow][pCol] == 'x' || grid[pRow][pCol] == 'o'){
				System.out.println("choose a dif location");
				System.out.println("Type in the row grid number:");
				pRow = s.nextInt();
				System.out.println("Type in the col grid number:");
				pCol = s.nextInt();
				if(roundNum%2 == 0 ){
					grid[pRow][pCol] = 'x';
					roundNum ++;
				}else{
					grid[pRow][pCol] = 'o';
					roundNum ++;
				}
			}else{
				System.out.println("Type in the row grid number:");
				pRow = s.nextInt();
				System.out.println("Type in the col grid number:");
				pCol = s.nextInt();
				if(roundNum%2 == 0 ){
					grid[pRow][pCol] = 'x';
					roundNum ++;
				}else{
					grid[pRow][pCol] = 'o';
					roundNum ++;
				}
			}
		}


	}

	//Does everything; makes code neater imo
	public void OverallLogic(){

		PlacingLogic();
		WinLogic();
		DrawBoard();

	}
	public static void main(String[] args){
		TToe tt =  new TToe();


	}


}
