package gui;

//Eclipse imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;






import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Arrays;






//user imports
import org.pushingpixels.*;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 
 * @author Eric Zhu 
 * 
 * Using the JSON Simple library and insubstantial look and feel
 *
 */

@SuppressWarnings("unused")
public class TToeUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int rows = 3;
	public int cols = 3;
	public char[][] grid = new char[rows][cols]; 
	public char[][] usedDiagonals = new char[rows][cols];
	public int roundNum = 0;
	public String instructions;
	public int pRow = 0;
	public int pCol = 0;
	//public int diagonalCount = 0;
	public static boolean won = false;
	public boolean instrutions = false;

	//Menu components
	public JMenuBar jmb = new JMenuBar();
	public JMenu file = new JMenu("File");
	public JMenuItem exit = new JMenuItem("Exit");
	public JMenuItem newGame = new JMenuItem("New");
	public JMenuItem nextGame = new JMenuItem("Next Round");
	public JMenuItem saveFile = new JMenuItem("Save");
	public JMenuItem openFile = new JMenuItem("Open");
	//TextBox
	public JTextPane TextField = new JTextPane();
	public static JTextArea ConsoleOutput = new JTextArea();


	//dimensions
	public Dimension minSize = new Dimension(500, 500);
	public Dimension maxSize = new Dimension(750, 750);

	//Buttons
	public JButton nextRound = new JButton("Next Round");

	//JSON Object
	public JSONObject jsonOBJ = new JSONObject();

	//JSON Parser
	public JSONParser jsonParser = new JSONParser();

	//filechooser
	public JFileChooser dialog = new JFileChooser(
			System.getProperty("home.dir"));

	//tabbed panes
	public JTabbedPane consolePanes = new JTabbedPane();


	//font slider label
	public JLabel fontLabel = new JLabel("Game Size");

	//font size slider
	public JSlider fontSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 35);

	//jpanel
	public JPanel topPanel = new JPanel();
	
	

	public TToeUI() throws FileNotFoundException{
		
		
		
		//Setup the UI 
		this.setLayout(new BorderLayout());
		this.setLocation(getHeight()/2, getWidth()/2);
		this.setSize(500, 500);
		this.setMinimumSize(minSize);
		this.setMaximumSize(maxSize);
		this.setVisible(true);
		this.setTitle("Tic Tac Toe - Round " + roundNum);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(TextField, BorderLayout.CENTER);
		this.add(nextRound,BorderLayout.EAST);
		this.add(consolePanes, BorderLayout.SOUTH);
		this.add(topPanel, BorderLayout.NORTH);

		topPanel.add(fontLabel, BorderLayout.BEFORE_FIRST_LINE);
		topPanel.add(fontSlider, BorderLayout.CENTER);

		//Add Textareas to consoles
		consolePanes.setSize(500, 300);
		consolePanes.addTab("System Console", null, ConsoleOutput, "Console Output");


		//define textarea properties
		TextField.setEditable(false);
		TextField.setFont(new Font("Sans", Font.PLAIN, 30));
		TextField.setAutoscrolls(true);
		TextField.setForeground(Color.WHITE);
		StyledDocument sDoc = TextField.getStyledDocument();
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attributeSet, Color.white);
		StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
		TextField.setCharacterAttributes(attributeSet, true);
		sDoc.setParagraphAttributes(0, sDoc.getLength(),attributeSet, false );


		//console area outputs
		ConsoleOutput.setBackground(Color.BLACK);
		ConsoleOutput.setForeground(Color.GREEN);
		ConsoleOutput.setSize(500, 200);
		ConsoleOutput.setEditable(false);
		ConsoleOutput.setText("Outputs to the desktop");
		//slider properties
		fontSlider.setMinorTickSpacing(5);
		fontSlider.setMajorTickSpacing(10);
		fontSlider.setPaintTicks(true);
		fontSlider.setSnapToTicks(true);
		fontSlider.setPaintTrack(true);
		fontSlider.setPaintLabels(true);

		//draw menu
		this.setJMenuBar(jmb);
		jmb.add(file);
		file.add(newGame);
		file.add(nextGame);
		file.addSeparator();
		file.add(openFile);
		file.add(saveFile);
		file.addSeparator();
		file.add(exit);


		this.pack();
		OverallLogic();
		
		fontSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				TextField.setFont(new Font("Times", Font.PLAIN, fontSlider.getValue()));
			}
		});

		//New Game Action 
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					nGame();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		//next round action
		nextRound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					OverallLogic();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		//Exit Action 
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		nextGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					nextGameMethod();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		nextGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveFile.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)

					saveFile(dialog.getSelectedFile().getAbsolutePath());



			}
		});

		openFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

					readInFile(dialog.getSelectedFile().getAbsolutePath());

				}
			}
		});
	}



	private void readInFile(String fileName) {
		try {
			Object obj = jsonParser.parse(new FileReader(fileName));
			JSONObject jsonObject = (JSONObject) obj;

			grid = (char[][]) jsonObject.get("Grid State");
			pRow = (int) jsonObject.get("pRow");
			pCol = (int) jsonObject.get("pCol");
			//diagonalCount = (int) jsonObject.get("Diagonal Count");
			roundNum = (int) jsonObject.get("Round Num");


		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@SuppressWarnings("unchecked")
	private void saveFile(String fileName) {
		// TODO Auto-generated method stub
		if (roundNum%2 == 0) {
			jsonOBJ.put("Player", "Player 1");
		}else{
			jsonOBJ.put("Player", "Player 2");
		}

		jsonOBJ.put("Round Num", roundNum);
		//jsonOBJ.put("Diagonal Count", diagonalCount);
		jsonOBJ.put("Grid State", grid);
		jsonOBJ.put("pRow", pRow);
		jsonOBJ.put("pCol", pCol);


		try {
			FileWriter file = new FileWriter(fileName + ".json");
			file.write(jsonOBJ.toJSONString());
			System.out.println("Successfully Copied JSON Object to File...");
			System.out.println("\nJSON Object: " + jsonOBJ);
			file.flush();
			file.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}

	//new game 
	public void nGame() throws FileNotFoundException{
		/*TextField.setText("");
		pRow = 0;
		pCol = 0;
		won = false;
		roundNum = 0;
		Arrays.fill(grid, null);
		Arrays.fill(usedDiagonals, null);
		OverallLogic();*/
		this.dispose();
		TToeUI t = new TToeUI();
	}

	public void nextGameMethod() throws FileNotFoundException{
		OverallLogic();
	}
	

	//check if anyone won
	public void WinLogic() throws FileNotFoundException{
		//diagonals
		while (won == false){
			checkrow:
				for(int i = 0; i < 3; ++ i){

					for(int j = 0; j < 3; ++ j){

						//DEBUG STUFF 
						/*
						System.out.println("DEBUG the value of i is : " + i );
						System.out.println("DEBUG the value of j is : " + j);
						System.out.println("DEBUG the value of pRow is" + pRow);
						System.out.println("DEBUG the value of pCol is" + pCol);
						 */
						if(grid[i][j] == 'x'){
							if(j == 2 && i == 0 || j == 1 && i == 1 || j == 0 && i == 2){
								usedDiagonals[i][j] = 'f';
								if(usedDiagonals[0][2] == 'f' && usedDiagonals[1][1] == 'f' && usedDiagonals[2][0] == 'f' ){
									won = true;
									System.out.println("Player one won! By diagonals.");
									JOptionPane.showMessageDialog(null,"Player one won by diagonals!");
									nGame();
									break checkrow;
								}
							}
						}
						
						if(grid[i][j] == 'o'){
							if(j == 2 && i == 0 || j == 1 && i == 1 || j == 0 && i == 2){
								usedDiagonals[i][j] = 'j';
								if(usedDiagonals[0][2] == 'j' && usedDiagonals[1][1] == 'j' && usedDiagonals[2][0] == 'j' ){
									won = true;
									System.out.println("Player one won! By diagonals.");
									JOptionPane.showMessageDialog(null,"Player two won by diagonals!");
									nGame();

									break checkrow;
								}
							}
							
						}

						if(j==i && grid[i][j] == 'x' ) {

							usedDiagonals[i][j] = 'f';

							if(usedDiagonals[0][0] == 'f' && usedDiagonals[1][1] == 'f' && usedDiagonals[2][2] == 'f' ){
								won = true;
								System.out.println("Player one won! By diagonals.");
								JOptionPane.showMessageDialog(null,"Player one won by diagonals!");
								nGame();		
								break checkrow;

							}
						}
						if(j==i && grid[i][j] == 'o' ) {

							usedDiagonals[i][j] = 'j';

							if(usedDiagonals[0][0] == 'j' && usedDiagonals[1][1] == 'j' && usedDiagonals[2][2] == 'j' ){
								won = true;
								System.out.println("Player two won! By diagonals.");
								JOptionPane.showMessageDialog(null,"Player two won by diagonals!");
								nGame();
								break checkrow;

							}
						}
					}
				}
		//cols
		cols:
			for(int i = 0; i < 3; i ++){

				if(grid[0][i] == 'x' && grid[1][i] == 'x' && grid[2][i] == 'x'){
					won = true;
					System.out.println("Player one won! By rows");
					JOptionPane.showMessageDialog(null,"Player one won by placing 3 in a cols!");
					nGame();
					break cols;
				}else if (grid[0][i] == 'o' && grid[1][i] == 'o' && grid[2][i] == 'o' ){
					won = true;
					System.out.println("Player two won!");
					JOptionPane.showMessageDialog(null,"Player two won by placing 3 in a cols!");
					nGame();
					break;

				}

				//cols
				for(int j = 0; j < 3; j++){

					if(grid[j][0] == 'x' && grid[j][1] == 'x' && grid[j][2] == 'x'){
						won = true;
						System.out.println("Player one won! By cols");
						JOptionPane.showMessageDialog(null,"Player one won by placing 3 in a rows!");

					}else if (grid[j][0] == 'o' && grid[j][1] == 'o' &&grid[j][2] == 'o' ){
						won = true;
						System.out.println("Player two won!");
						JOptionPane.showMessageDialog(null,"Player two won by placing 3 in a rows!");

					}
				}

			}
				break;
		}
	}


	public void DrawBoard(){
		TextField.setText(grid[0][0] + "|" + grid[0][1] + "|"+ grid[0][2] +"\n"+ "------" + "\n" + grid[1][0] + "|" + grid[1][1] + "|"+ grid[1][2] + "\n" + "------" + "\n" + grid[2][0] + "|" + grid[2][1] + "|"+ grid[2][2]);

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
		/**Console stuff
		System.out.println("Type in the row grid number:");
		pRow = s.nextInt();
		System.out.println("Type in the col grid number:");
		pCol = s.nextInt();
		 **/

		try{
			pRow = Integer.parseInt(JOptionPane.showInputDialog("Input Row Number:"));
			pRow = pRow -1;
			pCol = Integer.parseInt(JOptionPane.showInputDialog("Input Col Number:"));
			pCol = pCol -1;
		}catch(Exception e1){
			JOptionPane.showMessageDialog(null,"Please use a number");
		}

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
				try{
					JOptionPane.showMessageDialog(null,"Please chooose a different location");
					pRow = Integer.parseInt(JOptionPane.showInputDialog("Input Row Number:"));
					pRow = pRow -1;
					pCol = Integer.parseInt(JOptionPane.showInputDialog("Input Col Number:"));
					pCol = pCol -1;
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null,"Please use a number");
					pRow = Integer.parseInt(JOptionPane.showInputDialog("Input Row Number:"));
					pRow = pRow -1;
					pCol = Integer.parseInt(JOptionPane.showInputDialog("Input Col Number:"));
					pCol = pCol -1;
				}
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
				try{
					JOptionPane.showMessageDialog(null,"Please chooose a different location");
					pRow = Integer.parseInt(JOptionPane.showInputDialog("Input Row Number:"));
					pRow = pRow -1;
					pCol = Integer.parseInt(JOptionPane.showInputDialog("Input Col Number:"));
					pCol = pCol -1;
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null,"Please use a number");
				}
				if(roundNum%2 == 0 ){
					grid[pRow][pCol] = 'x';
					roundNum ++;
				}else{
					grid[pRow][pCol] = 'o';
					roundNum ++;
				}
			}else{
				try{
					//JOptionPane.showMessageDialog(null,"Please chooose a different location");
					pRow = Integer.parseInt(JOptionPane.showInputDialog("Input Row Number:"));
					pRow = pRow -1;
					pCol = Integer.parseInt(JOptionPane.showInputDialog("Input Col Number:"));
					pCol = pCol -1;
				}catch(Exception e1){
					JOptionPane.showMessageDialog(null,"Please use a number");
				}
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
	public static void consoleToText() throws FileNotFoundException{
		//console stuff
	
			ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
			
			PrintStream printStream = new PrintStream(new FileOutputStream("C:/users/student/desktop/tic tac toe game.txt"));
			System.setOut(printStream);
			//ConsoleOutput.setText(bOutputStream.toString());
			//System.out.println("hi");
		
		
	}
	
	
	//Does everything; makes code neater imo
	public void OverallLogic() throws FileNotFoundException{
		
		consoleToText();
		TToeUI.this.setTitle("Tic Tac Toe - Round " + roundNum);
		System.out.println("Round Number is:" + roundNum);
		PlacingLogic();
		DrawBoard();
		WinLogic();

	}



	public static void main(String[] args)
			throws UnsupportedLookAndFeelException, ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {


				try {
					UIManager
					.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				TToeUI ttoe = null;
				try {
					ttoe = new TToeUI();
					ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
					
					PrintStream printStream = new PrintStream(bOutputStream);
					System.setOut(printStream);
					TToeUI.ConsoleOutput.setText(bOutputStream.toString());
				
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SwingUtilities.updateComponentTreeUI(ttoe);

			}
		});

	}

}
