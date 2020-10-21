package CDC_GUI;



import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import project1package.AllData;
import project1package.Person;




public class DatabaseManagerGUI extends JPanel{
	
	private static AllData myData;
	private int width = 1200, height = 800;
	Color cdcBlue = new Color(0, 65, 119);
	JTable personTable;
	int row;
	JPanel pInfo, buttonPanel;

	boolean editEnabled = false;
	boolean removeEnabled = false;
	boolean addEnabled = false;
	private JFrame controllingFrame; 
	private boolean isEdited = false;
	int OptionY;
	int OptionX;
	
	JTextField selID;
	JTextField selFn;
	JTextField selLn;
	JTextField selBd;
	JTextField selPh;
	JTextField selSt;
	JTextField selCn;
	
	public DatabaseManagerGUI() {
		 
		super(new GridLayout(2,1));
		setBackground(Color.white);
		
	
		
		//-----------------------------------------------------
				      //    Create Panels   //
	    //-----------------------------------------------------
		
		
		//create upper panel
		JPanel topPanel = new JPanel(new GridLayout(1,2));
		topPanel.setBackground(cdcBlue);
		topPanel.setBounds(0,0,width,height);
		topPanel.setLayout(null);
		add(topPanel);
		
		//create personSelecion panel in the upper panel (RIGHT)
		pInfo = new JPanel();
		pInfo.setBackground(Color.lightGray);
		pInfo.setBounds(width/2,0,width/2,height/2);
		pInfo.setLayout(null);
		topPanel.add(pInfo);
		
		//create buttonPanel in the upper panel (LEFT)
		buttonPanel = new JPanel();
		buttonPanel.setBackground(cdcBlue);
		buttonPanel.setBounds(0,0,width/2,height/2);
		buttonPanel.setLayout(null);
		topPanel.add(buttonPanel);
		
		//create Panel that holds the jTable
		JPanel DBPanel = new JPanel();
		DBPanel.setBackground(Color.green);
		DBPanel.setBounds(0,0,width,height);
		DBPanel.setLayout(null);
		add(DBPanel);
		
		
		
		//-----------------------------------------------------
				      //    Table Functions   //
	    //-----------------------------------------------------


		
		//create table without Person data
		String[] colTitles = { "ID","First Name", "Last Name", "Birthdate", "Phone Number", "Status", "Close Contacts"}; 
		DefaultTableModel model = new DefaultTableModel(colTitles,0) {
			 @Override
			    public boolean isCellEditable(int row, int column) {
			        return false;
			    }
		};
		
		personTable = new JTable(model); 
		personTable.setRowHeight(40);
		personTable.setEnabled(false);
		
		
		
		//Make ScrollPane from personTable
        JScrollPane sp = new JScrollPane(personTable); 
        sp.setSize(width, height/2); 
        sp.setVisible(true); 
        DBPanel.add(sp);
	
		//Adds data from the Text file to the table
		myData = new AllData("./contactData.txt");
		Iterator<String> iter = myData.getIterator();
		
		while(iter.hasNext()) {
			Object[] objs;
			Person person = myData.findPerson(iter.next());
			objs = new Object[] {person.getId(), person.getfName(), 
								person.getlName(), person.getBirthday(),
								person.getPhone(), person.getStatus(), 
								person.getContacts() };
			
			model.addRow(objs);
		}
		
		
		//RESETS TABLE when their is a change
		MouseAdapter saveChanges = new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				
				//removes all rows from current table
				int numrows =  model.getRowCount();
				for(int i = 0; i < numrows; i++) {
					model.removeRow(0);
				}
				
				//Adds data from the Text file to the table
				myData = new AllData("./contactData.txt");
				Iterator<String> iter = myData.getIterator();
				
				while(iter.hasNext()) {
					Object[] objs;
					Person person = myData.findPerson(iter.next());
					objs = new Object[] {person.getId(), person.getfName(), 
										person.getlName(), person.getBirthday(),
										person.getPhone(), person.getStatus(), 
										person.getContacts() };
					model.addRow(objs);
				}
				//clears Panel
				pInfo.removeAll();
				pInfo.repaint();
		}};
		
		
		//---------------------------------------------------------
		//---------------------------------------------------------
		//         Buttons that are inside ButtonOption          //
	    //---------------------------------------------------------
		//---------------------------------------------------------		
		
		
		
		//confirmButton - Used in AddPerson - confirms and verifies added person
		//---------------------------------------------------------------------
				JButton confirmButton = new JButton("Confirm");
				confirmButton.setBackground(Color.WHITE);
				confirmButton.setBounds(10,10,130,40);   //can use setLocation() and setSize() as well
				confirmButton.setForeground(Color.BLACK);
				confirmButton.setFont(new Font("Geneva", Font.PLAIN, 20));
				confirmButton.setVisible(false);
				confirmButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						
						//
						LocalDate bd = null;
						try {
							 bd = LocalDate.parse(selBd.getText());
						}  catch (Exception e) {
							 JOptionPane.showMessageDialog(pInfo,
					                   "Date Format incorrect "
					                   + "please use: yyyy-mm-dd ",
					                   "Error Message",
					                    JOptionPane.ERROR_MESSAGE);
							return;	 
						}
						
						
						isEdited = false;
						String cn = selCn.getText();
						String str[] = cn.split(",");
					
						//add edited person and their contacts
						Person p = new Person(selID.getText(), selFn.getText(),selLn.getText(), bd, selPh.getText(), selSt.getText());

						for(String id : str) {
							p.addContact(id);
						}
						
						//add person, write to file, and exit mode
						myData.add(p); 
						myData.writeFile();
						
						//clears Panel
						pInfo.removeAll();
		                pInfo.repaint();
						
						
					
					}
				});
				confirmButton.addMouseListener(saveChanges);
				pInfo.add(confirmButton);
				
				
		//Cancel Button - used in AddPerson and Edit Person - nullifies any changes made in the program and cleanly exits that mode
		//-------------------------------------------------------------------------------------------------------------------------
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setBackground(Color.WHITE);
				cancelButton.setBounds(400,10,130,40);   //can use setLocation() and setSize() as well
				cancelButton.setForeground(Color.BLACK);
				cancelButton.setFont(new Font("Geneva", Font.PLAIN, 20));
				cancelButton.setVisible(false);
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						isEdited = false;
						pInfo.removeAll();
		                pInfo.repaint();
					
					}
				});
				
				pInfo.add(cancelButton);
				
				
		// saveButton - used in Edit Person - Saves the edited changes for that persons
		//----------------------------------------------------------------------------
				JButton saveButton = new JButton("Save Changes");
				pInfo.add(saveButton);
				saveButton.setBackground(Color.WHITE);
				saveButton.setBounds(10,10,200,40);   //can use setLocation() and setSize() as well
				saveButton.setForeground(Color.BLACK);
				saveButton.setFont(new Font("Geneva", Font.PLAIN, 20));
				saveButton.setVisible(false);
				saveButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						
						isEdited = false;
						
						LocalDate bd = LocalDate.parse(selBd.getText());
						String cn = selCn.getText();
						
						cn = cn.replace("[", "");
						cn = cn.replace("]", "");
						String str[] = cn.split(",");
					
						//add edited person and their contacts
						Person p = new Person(selID.getText(), selFn.getText(),selLn.getText(), bd, selPh.getText(), selSt.getText());
						for(String id : str) {
							p.addContact(id);
						}
						myData.remove(selID.getText()); //remove old entry
						myData.add(p); //add edited entry
						myData.writeFile();
					}
				});
				saveButton.addMouseListener(saveChanges);
				
				
				
				
		//Delete Button - used in Remove Person - Confirms the deletion of the person selected
		//-----------------------------------------------------------------------------------
				JButton deleteButton = new JButton("Delete");
				deleteButton.setBackground(Color.WHITE);
				deleteButton.setBounds(10,10,130,40);   //can use setLocation() and setSize() as well
				deleteButton.setForeground(Color.BLACK);
				deleteButton.setFont(new Font("Geneva", Font.PLAIN, 20));
				deleteButton.setVisible(false);
				deleteButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						
						myData.remove(selID.getText()); //remove old entry
						myData.writeFile();
					}
				});
				deleteButton.addMouseListener(saveChanges);
				pInfo.add(deleteButton);
		
		
		
		
		
		//-----------------------------------------------------		
		//-----------------------------------------------------
				      //    Create Function Buttons   //
					  //     Edit | Remove | Add     //
	    //-----------------------------------------------------
		//-----------------------------------------------------		
		
		
		//Edit Button - Enables Edit Panel
		JButton editButton = new JButton("Edit Person");
		editButton.setBackground(Color.WHITE);
		editButton.setBounds(170,50,250,50);   //can use setLocation() and setSize() as well
		editButton.setForeground(Color.BLACK);
		editButton.setFont(new Font("Geneva", Font.PLAIN, 24));
		editButton.setVisible(true);
		editButton.setToolTipText("Select me before Clicking on the Table");
		editButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				 editEnabled = true;
				 removeEnabled = false;
				 addEnabled = false;
				 personTable.setEnabled(true);
				
			}
		});
		
		buttonPanel.add(editButton);
		
		
		//Pencil Icon
		JLabel pencil = new JLabel("");
		ImageIcon img1 = new ImageIcon (this.getClass().getResource("/CDC_GUI/e.png"));
		pencil.setIcon(img1);
		pencil.setBounds(430,40,img1.getIconWidth(), img1.getIconHeight());  //can use setLocation() and setSize() as well
		buttonPanel.add(pencil);
		
		
		
		
		
		//Remove Button - Enables Remove Panel
		JButton removeButton = new JButton("Remove Person");
		removeButton.setBackground(Color.WHITE);
		removeButton.setBounds(170,150,250,50);    //can use setLocation() and setSize() as well
		removeButton.setForeground(Color.BLACK);
		removeButton.setFont(new Font("Geneva", Font.PLAIN, 24));
		removeButton.setVisible(true);
		removeButton.setToolTipText("Select me before Clicking on the Table");
		removeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				 editEnabled = false;
				 removeEnabled = true;
				 addEnabled = false;
				 personTable.setEnabled(true);
			}
		});
		buttonPanel.add(removeButton);
		
		//x Icon
				JLabel x = new JLabel("");
				ImageIcon img3 = new ImageIcon (this.getClass().getResource("/CDC_GUI/xx.png"));
				x.setIcon(img3);
				x.setBounds(430,140,img3.getIconWidth(), img3.getIconHeight());  //can use setLocation() and setSize() as well
				buttonPanel.add(x);
				
				
				
		
		
		
		//Add Button - Enables Add Panel
		JButton addButton = new JButton("Add Person");
		addButton.setBackground(Color.WHITE);
		addButton.setBounds(170,250,250,50); //can use setLocation() and setSize() as well
		addButton.setForeground(Color.BLACK);
		addButton.setFont(new Font("Geneva", Font.PLAIN, 24));
		addButton.setVisible(true);
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				 editEnabled = false;
				 removeEnabled = false;
				 addEnabled = true;
				 int y = 70;
				 
				 pInfo.removeAll();
				
				 //Enables Confirm and Cancel
				 confirmButton.setVisible(true);
				 pInfo.add(confirmButton);
				 cancelButton.setVisible(true);
				 pInfo.add(cancelButton);
				 
				 
				  //Add Icon
					JLabel p1 = new JLabel("");
					ImageIcon img1 = new ImageIcon (this.getClass().getResource("/CDC_GUI/p.png"));
					p1.setIcon(img1);
					p1.setBounds(10,100,img1.getIconWidth(), img1.getIconHeight());  //can use setLocation() and setSize() as well
					pInfo.add(p1);
				
					//Add Icon
					JLabel p2 = new JLabel("");
					ImageIcon img2 = new ImageIcon (this.getClass().getResource("/CDC_GUI/p.png"));
					p2.setIcon(img2);
					p2.setBounds(10,250,img2.getIconWidth(), img2.getIconHeight());  //can use setLocation() and setSize() as well
					pInfo.add(p2);
				
				 //Listen for changes in info
				 KeyAdapter textChange = new KeyAdapter() {
					 @Override
					 public void keyPressed(KeyEvent e) {
			        	isEdited = true;
			        	
					 }};
			
				 
				 //Loads each Field with the Persons information and listens for any changes
				 selID = new JTextField();
				 selID.setBounds(300,y,250,30);
				 selID.addKeyListener(textChange);
				 pInfo.add(selID);
				 JTextField IdTB = new JTextField("ID :");
				 IdTB.setFont(new Font("Geneva", Font.PLAIN, 24));
				 IdTB.setBounds(100, y,200,30);
				 pInfo.add(IdTB);
				 y+=45;
				 
				 
				 selFn = new JTextField();
				 selFn.setBounds(300,y,250,30);
				 selFn.addKeyListener(textChange);
				 pInfo.add(selFn);
				 JTextField FnTB = new JTextField("First Name:");
				 FnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
			
				 FnTB.setBounds(100, y,200,30);
				 pInfo.add(FnTB);
				 y+=45;
				

				 selLn = new JTextField();
				 selLn.setBounds(300,y,250,30);
				 selLn.addKeyListener(textChange);
				 pInfo.add(selLn);
				 JTextField LnTB = new JTextField("Last Name: ");
				 LnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
				 LnTB.setBounds(100, y,200,30);
				 pInfo.add(LnTB);
				 y+=45;
				 
				 
				 selBd = new JTextField();
				 selBd.setBounds(300,y,250,30);
				 selBd.addKeyListener(textChange);
				 pInfo.add(selBd);
				 JTextField BdTB = new JTextField("Birthdate: ");
				 BdTB.setFont(new Font("Geneva", Font.PLAIN, 24));
				 BdTB.setBounds(100, y,200,30);
				 pInfo.add(BdTB);
				 y+=45;
				 
				
				 selPh = new JTextField();
				 selPh.setBounds(300,y,250,30);
				 selPh.addKeyListener(textChange);
				 pInfo.add(selPh);
				 JTextField PhTB = new JTextField("Phone Number: ");
				 PhTB.setFont(new Font("Geneva", Font.PLAIN, 24));
				 PhTB.setBounds(100, y,200,30);
				 pInfo.add(PhTB);
				 y+=45;
				 
				
				 selSt = new JTextField();
				 selSt.setBounds(300,y,250,30);
				 selSt.addKeyListener(textChange);
				 pInfo.add(selSt);
				 JTextField StTB = new JTextField("Status: ");
				 StTB.setFont(new Font("Geneva", Font.PLAIN, 24));
				 StTB.setBounds(100, y,200,30);
				 pInfo.add(StTB);
				 y+=45;
				 
				
				 selCn = new JTextField();
				 selCn.setBounds(300,y,250,30);
				 selCn.addKeyListener(textChange);
				 pInfo.add(selCn);
				 JTextField CnTB = new JTextField("Contacts: ");
				 CnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
				 CnTB.setBounds(100, y,200,30);
				 pInfo.add(CnTB);
				 y+=45;
				 
				 
				 repaint();
				
			}
		});
		buttonPanel.add(addButton);
		
		//plus Icon
		JLabel plus = new JLabel("");
		ImageIcon img2 = new ImageIcon (this.getClass().getResource("/CDC_GUI/p.png"));
		plus.setIcon(img2);
		plus.setBounds(430,240,img2.getIconWidth(), img2.getIconHeight());  //can use setLocation() and setSize() as well
		buttonPanel.add(plus);
		

			

		//////
		////// Cannot change the ID Currently - Problem
		//////
		
	
		
		
		
		//Called when the table is clicked on
		//The User must click on the Edit or Remove Option in order for a row to be selected
		personTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int y = 70;
			
	
			
				 //Checks if ButtonOption has been pressed
				 if (!personTable.isEnabled()) {
					 JOptionPane.showMessageDialog(pInfo,
			                   "Please prompt EDIT or REMOVE before selecting the table",
			                   "Error Message",
			                    JOptionPane.ERROR_MESSAGE);
					return;	 
				 }
				 
				 if(isEdited) { //checks and prompts to save if another button or table press has been registered
						JOptionPane.showMessageDialog(controllingFrame,
				                   "Unsaved progress made : Please Save!",
				                   "Error Message",
				                    JOptionPane.ERROR_MESSAGE);
						return;
					 }
				 pInfo.removeAll();
				 
		//-----------------------------------------------------		
		//-----------------------------------------------------
				      //    Edit function   //
	    //-----------------------------------------------------
		//-----------------------------------------------------		 
				 
				 if(editEnabled) { //checks if editButton has been selected
						 
					     //screen reset
						 repaint();
						 pInfo.removeAll();
						 
						    //Pencil Icon
							JLabel pencil1 = new JLabel("");
							ImageIcon img1 = new ImageIcon (this.getClass().getResource("/CDC_GUI/e.png"));
							pencil1.setIcon(img1);
							pencil1.setBounds(10,100,img1.getIconWidth(), img1.getIconHeight());  //can use setLocation() and setSize() as well
							pInfo.add(pencil1);
						
							//Pencil Icon
							JLabel pencil2 = new JLabel("");
							ImageIcon img2 = new ImageIcon (this.getClass().getResource("/CDC_GUI/e.png"));
							pencil2.setIcon(img1);
							pencil2.setBounds(10,250,img2.getIconWidth(), img2.getIconHeight());  //can use setLocation() and setSize() as well
							pInfo.add(pencil2);
							
							
						 //adds save and cancel button
						 saveButton.setVisible(true);
						 pInfo.add(saveButton);
						 cancelButton.setVisible(true);
						 pInfo.add(cancelButton);
						 row = personTable.rowAtPoint(e.getPoint());
						
						 //checks for any field changes
						 KeyAdapter textChange = new KeyAdapter() {
							 @Override
							 public void keyPressed(KeyEvent e) {
					        	isEdited = true;  
							 }};
						
						 //Loads each Field with the Persons information and listens for any changes	 
							 selID = new JTextField(personTable.getValueAt(row, 0).toString());
							 selID.setBounds(300,y,250,30);
							 selID.addKeyListener(textChange);
							 pInfo.add(selID);
							 JTextField IdTB = new JTextField("ID :");
							 IdTB.setFont(new Font("Geneva", Font.PLAIN, 24));
							 IdTB.setBounds(100, y,200,30);
							 pInfo.add(IdTB);
							 y+=45;
							 
							 
							 selFn = new JTextField(personTable.getValueAt(row, 1).toString());
							 selFn.setBounds(300,y,250,30);
							 selFn.addKeyListener(textChange);
							 pInfo.add(selFn);
							 JTextField FnTB = new JTextField("First Name:");
							 FnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
							 FnTB.setBounds(100, y,200,30);
							 pInfo.add(FnTB);
							 y+=45;
							

							 selLn = new JTextField(personTable.getValueAt(row, 2).toString());
							 selLn.setBounds(300,y,250,30);
							 selLn.addKeyListener(textChange);
							 pInfo.add(selLn);
							 JTextField LnTB = new JTextField("Last Name: ");
							 LnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
							 LnTB.setBounds(100, y,200,30);
							 pInfo.add(LnTB);
							 y+=45;
							 
							 
							 selBd = new JTextField(personTable.getValueAt(row, 3).toString());
							 selBd.setBounds(300,y,250,30);
							 selBd.addKeyListener(textChange);
							 pInfo.add(selBd);
							 JTextField BdTB = new JTextField("Birthdate: ");
							 BdTB.setFont(new Font("Geneva", Font.PLAIN, 24));
							 BdTB.setBounds(100, y,200,30);
							 pInfo.add(BdTB);
							 y+=45;
							 
							
							 selPh = new JTextField(personTable.getValueAt(row, 4).toString());
							 selPh.setBounds(300,y,250,30);
							 selPh.addKeyListener(textChange);
							 pInfo.add(selPh);
							 JTextField PhTB = new JTextField("Phone Number: ");
							 PhTB.setFont(new Font("Geneva", Font.PLAIN, 24));
							 PhTB.setBounds(100, y,200,30);
							 pInfo.add(PhTB);
							 y+=45;
							 
							
							 selSt = new JTextField(personTable.getValueAt(row, 5).toString());
							 selSt.setBounds(300,y,250,30);
							 selSt.addKeyListener(textChange);
							 pInfo.add(selSt);
							 JTextField StTB = new JTextField("Status: ");
							 StTB.setFont(new Font("Geneva", Font.PLAIN, 24));
							 StTB.setBounds(100, y,200,30);
							 pInfo.add(StTB);
							 y+=45;
							 
							
							 selCn = new JTextField(personTable.getValueAt(row, 6).toString());
							 selCn.setBounds(300,y,250,30);
							 selCn.addKeyListener(textChange);
							 pInfo.add(selCn);
							 JTextField CnTB = new JTextField("Contacts: ");
							 CnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
							 CnTB.setBounds(100, y,200,30);
							 pInfo.add(CnTB);
							 y+=45;
							 
							 
						
						 }
				 
				 
				 
	   //-----------------------------------------------------		
	   //-----------------------------------------------------
				      //    Remove function   //
	   //-----------------------------------------------------
	   //-----------------------------------------------------		 
				 
				 if(removeEnabled) {
					
					//screen reset
					 repaint();
					 pInfo.removeAll();
					 
					//adds delete button
					 deleteButton.setVisible(true);
					 pInfo.add(deleteButton);
					 
					 row = personTable.rowAtPoint(e.getPoint());
					
					 
					 
					//Delete Icon
						JLabel x1 = new JLabel("");
						ImageIcon img1 = new ImageIcon (this.getClass().getResource("/CDC_GUI/xx.png"));
						x1.setIcon(img1);
						x1.setBounds(10,100,img1.getIconWidth(), img1.getIconHeight());  //can use setLocation() and setSize() as well
						pInfo.add(x1);
					
						//Delete Icon
						JLabel x2 = new JLabel("");
						ImageIcon img2 = new ImageIcon (this.getClass().getResource("/CDC_GUI/xx.png"));
						x2.setIcon(img2);
						x2.setBounds(10,250,img2.getIconWidth(), img2.getIconHeight());  //can use setLocation() and setSize() as well
						pInfo.add(x2);
					 
					 
					 
					 //Loads each Field with the Persons information and listens for any changes	
					 selID = new JTextField(personTable.getValueAt(row, 0).toString());
					 selID.setBounds(300,y,250,30);
					 selID.setEditable(false);
					 pInfo.add(selID);
					 JTextField IdTB = new JTextField("ID :");
					 IdTB.setFont(new Font("Geneva", Font.PLAIN, 24));
					 IdTB.setBounds(100, y,200,30);
					 pInfo.add(IdTB);
					 y+=45;
					 
					 
					 selFn = new JTextField(personTable.getValueAt(row, 1).toString());
					 selFn.setBounds(300,y,250,30);
					 selFn.setEditable(false);
					 pInfo.add(selFn);
					 JTextField FnTB = new JTextField("First Name:");
					 FnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
					 FnTB.setBounds(100, y,200,30);
					 pInfo.add(FnTB);
					 y+=45;
					

					 selLn = new JTextField(personTable.getValueAt(row, 2).toString());
					 selLn.setBounds(300,y,250,30);
					 selLn.setEditable(false);
					 pInfo.add(selLn);
					 JTextField LnTB = new JTextField("Last Name: ");
					 LnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
					 LnTB.setBounds(100, y,200,30);
					 pInfo.add(LnTB);
					 y+=45;
					 
					 
					 selBd = new JTextField(personTable.getValueAt(row, 3).toString());
					 selBd.setBounds(300,y,250,30);
					 selBd.setEditable(false);
					 pInfo.add(selBd);
					 JTextField BdTB = new JTextField("Birthdate: ");
					 BdTB.setFont(new Font("Geneva", Font.PLAIN, 24));
					 BdTB.setBounds(100, y,200,30);
					 pInfo.add(BdTB);
					 y+=45;
					 
					
					 selPh = new JTextField(personTable.getValueAt(row, 4).toString());
					 selPh.setBounds(300,y,250,30);
					 selPh.setEditable(false);
					 pInfo.add(selPh);
					 JTextField PhTB = new JTextField("Phone Number: ");
					 PhTB.setFont(new Font("Geneva", Font.PLAIN, 24));
					 PhTB.setBounds(100, y,200,30);
					 pInfo.add(PhTB);
					 y+=45;
					 
					
					 selSt = new JTextField(personTable.getValueAt(row, 5).toString());
					 selSt.setBounds(300,y,250,30);
					 selSt.setEditable(false);
					 pInfo.add(selSt);
					 JTextField StTB = new JTextField("Status: ");
					 StTB.setFont(new Font("Geneva", Font.PLAIN, 24));
					 StTB.setBounds(100, y,200,30);
					 pInfo.add(StTB);
					 y+=45;
					 
					
					 selCn = new JTextField(personTable.getValueAt(row, 6).toString());
					 selCn.setBounds(300,y,250,30);
					 selCn.setEditable(false);
					 pInfo.add(selCn);
					 JTextField CnTB = new JTextField("Contacts: ");
					 CnTB.setFont(new Font("Geneva", Font.PLAIN, 24));
					 CnTB.setBounds(100, y,200,30);
					 pInfo.add(CnTB);
					 y+=45;
					 
					 
					 
				 }

			}
		});

		
	  //creates exit button for entire frame
		JButton exitButton = new JButton("Exit");
		exitButton.setBackground(Color.WHITE);
		exitButton.setBounds(10,10,90,30);   //can use setLocation() and setSize() as well
		exitButton.setForeground(Color.BLACK);
		exitButton.setFont(new Font("Geneva", Font.PLAIN, 20));
		exitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				System.exit(0);
			}
		});
		buttonPanel.add(exitButton);
		
		
	}

}
