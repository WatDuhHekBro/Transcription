import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Transcription implements KeyListener
{
	private JFrame window;
	private JPanel contents;
	private JTextField lang,input,output,error;
	private HashMap<String[],String> index;
	private String language;
	private int key;
	
	// 72*case + 6*modifier + diacritic = vi_char
	public static final int[] vi_chars = new int[] // 144 values, these are actual character values
	{
		97,   //0 = a
		225,  //1 = 'a
		224,  //2 = `a
		7843, //3 = ?a
		227,  //4 = ~a
		7841, //5 = .a
		
		226,  //6 = ^a
		7845, //7 = ^'a
		7847, //8 = ^`a
		7849, //9 = ^?a
		7851, //10 = ^~a
		7853, //11 = ^.a
		
		259,  //12 = [a
		7855, //13 = ['a
		7857, //14 = [`a
		7859, //15 = [?a
		7861, //16 = [~a
		7863, //17 = [.a
		
		101,  //18 = e
		233,  //19 = 'e
		232,  //20 = `e
		7867, //21 = ?e
		7869, //22 = ~e
		7865, //23 = .e
		
		234,  //24 = ^e
		7871, //25 = ^'e
		7873, //26 = ^`e
		7875, //27 = ^?e
		7877, //28 = ^~e
		7879, //29 = ^.e
		
		105,  //30 = i
		237,  //31 = 'i
		236,  //32 = `i
		7881, //33 = ?i
		297,  //34 = ~i
		7883, //35 = .i
		
		111,  //36 = o
		243,  //37 = 'o
		242,  //38 = `o
		7887, //39 = ?o
		245,  //40 = ~o
		7885, //41 = .o
		
		244,  //42 = ^o
		7889, //43 = ^'o
		7891, //44 = ^`o
		7893, //45 = ^?o
		7895, //46 = ^~o
		7897, //47 = ^.o
		
		417,  //48 = ,o
		7899, //49 = ,'o
		7901, //50 = ,`o
		7903, //51 = ,?o
		7905, //52 = ,~o
		7907, //53 = ,.o
		
		117,  //54 = u
		250,  //55 = 'u
		249,  //56 = `u
		7911, //57 = ?u
		361,  //58 = ~u
		7909, //59 = .u
		
		432,  //60 = ,u
		7913, //61 = ,'u
		7915, //62 = ,`u
		7917, //63 = ,?u
		7919, //64 = ,~u
		7921, //65 = ,.u
		
		121,  //66 = y
		253,  //67 = 'y
		7923, //68 = `y
		7927, //69 = ?y
		7929, //70 = ~y
		7925, //71 = .y
		
		65,   //72 = A
		193,  //73 = 'A
		192,  //74 = `A
		7842, //75 = ?A
		195,  //76 = ~A
		7840, //77 = .A
		
		194,  //78 = ^A
		7844, //79 = ^'A
		7846, //80 = ^`A
		7848, //81 = ^?A
		7850, //82 = ^~A
		7852, //83 = ^.A
		
		258,  //84 = [A
		7854, //85 = ['A
		7856, //86 = [`A
		7858, //87 = [?A
		7860, //88 = [~A
		7862, //89 = [.A
		
		69,   //90 = E
		201,  //91 = 'E
		200,  //92 = `E
		7866, //93 = ?E
		7868, //94 = ~E
		7864, //95 = .E
		
		202,  //96 = ^E
		7870, //97 = ^'E
		7872, //98 = ^`E
		7874, //99 = ^?E
		7876, //100 = ^~E
		7878, //101 = ^.E
		
		73,   //102 = I
		205,  //103 = 'I
		204,  //104 = `I
		7880, //105 = ?I
		296,  //106 = ~I
		7882, //107 = .I
		
		79,   //108 = O
		211,  //109 = 'O
		210,  //110 = `O
		7886, //111 = ?O
		213,  //112 = ~O
		7884, //113 = .O
		
		212,  //114 = ^O
		7888, //115 = ^'O
		7890, //116 = ^`O
		7892, //117 = ^?O
		7894, //118 = ^~O
		7896, //119 = ^.O
		
		416,  //120 = ,O
		7898, //121 = ,'O
		7900, //122 = ,`O
		7902, //123 = ,?O
		7904, //124 = ,~O
		7906, //125 = ,.O
		
		85,   //126 = U
		218,  //127 = 'U
		217,  //128 = `U
		7910, //129 = ?U
		360,  //130 = ~U
		7908, //131 = .U
		
		431,  //132 = ,U
		7912, //133 = ,'U
		7914, //134 = ,`U
		7916, //135 = ,?U
		7918, //136 = ,~U
		7920, //137 = ,.U
		
		89,   //138 = Y
		221,  //139 = 'Y
		7922, //140 = `Y
		7926, //141 = ?Y
		7928, //142 = ~Y
		7924  //143 = .Y
	};
	
	public Transcription()
	{
		window = new JFrame("Transcription: 15 April, 2019");
		contents = new JPanel();
		
		lang = new JTextField();
		lang.setColumns(40);
		lang.setHorizontalAlignment(JTextField.CENTER);
		lang.addKeyListener(this);
		
		input = new JTextField();
		input.setColumns(40);
		input.setHorizontalAlignment(JTextField.CENTER);
		input.addKeyListener(this);
		
		output = new JTextField();
		output.setColumns(40);
		output.setHorizontalAlignment(JTextField.CENTER);
		
		error = new JTextField();
		error.setColumns(40);
		error.setHorizontalAlignment(JTextField.CENTER);
		error.setEditable(false);
		
		contents.add(lang);
		contents.add(input);
		contents.add(output);
		contents.add(error);
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(400,100,500,150);
		window.getContentPane().add(contents);
		window.setVisible(true);
		
		init();
	}
	
	private void updateText()
	{
		try
		{
			language = null;
			
			for(String[] aliases : index.keySet())
				for(String alias : aliases)
					if(alias.equals(lang.getText()))
						language = index.get(aliases);
			
			if(language == null)
				language = "latin_extended.txt";
			
			if(lang.getText().equals("vietnamese") || lang.getText().equals("vi"))
			{
				output.setEditable(false);
				
				if(input.getText().length() >= 1 && key == KeyStroke.getKeyStroke('/',0).getKeyCode()) //punctuation
				{
					int last_index = input.getText().length()-1;
					String keep = input.getText().substring(0,last_index);
					char change = input.getText().charAt(last_index);
					
					switch(change)
					{
						case '/':
							change = '.';
							break;
						case '?':
							change = ',';
							break;
					}
					
					input.setText(keep+change);
				}
				else if(input.getText().length() >= 2)
				{
					int last_index = input.getText().length()-1;
					String keep = input.getText().substring(0,last_index-1);
					int change = (int)input.getText().charAt(last_index-1);
					boolean changeable = false;
					boolean changeable_d = false;
					boolean changed = false;
					
					for(int i = 0; i < vi_chars.length; i++)
					{
						if(vi_chars[i] == change)
						{
							change = i;
							changeable = true;
							break; // This is absolutely essential for things like capital A. A -> char int -> index int, Example: A -> 73 (index 'A') -> I (char 'I')
						}
					}
					
					if(change == 100 || change == 273) // 100 = d, 273 = -d
						changeable_d = true;
					
					if(changeable || changeable_d)
					{
						// 72*case + 6*modifier + diacritic = vi_char
						int alphabetCase = change/72; // 0-1 (1)
						int modifier = (change%72)/6; // 0-11 (12)
						int diacritic = (change%72)%6; // 0-5 (6)
						
						if(key == KeyStroke.getKeyStroke(';',0).getKeyCode()) //modifier
						{
							if(change == 100)
								change = 273;
							else if(change == 273)
								change = 100;
							else
							{
								switch(modifier)
								{
									case 0: modifier = 1; break;
									case 1: modifier = 2; break;
									case 2: modifier = 0; break;
									case 3: modifier = 4; break;
									case 4: modifier = 3; break;
									case 6: modifier = 7; break;
									case 7: modifier = 8; break;
									case 8: modifier = 6; break;
									case 9: modifier = 10; break;
									case 10: modifier = 9; break;
								}
							}
							
							changed = true;
						}
						else if(changeable && key == KeyEvent.VK_QUOTE) //diacritic: rising
						{
							if(diacritic == 1)
								diacritic = 0;
							else
								diacritic = 1;
							
							changed = true;
						}
						else if(changeable && key == KeyStroke.getKeyStroke('[',0).getKeyCode()) //diacritic: falling
						{
							if(diacritic == 2)
								diacritic = 0;
							else
								diacritic = 2;
							
							changed = true;
						}
						else if(changeable && key == KeyStroke.getKeyStroke(',',0).getKeyCode()) //diacritic: hook
						{
							if(diacritic == 3)
								diacritic = 0;
							else
								diacritic = 3;
							
							changed = true;
						}
						else if(changeable && key == KeyStroke.getKeyStroke(']',0).getKeyCode()) //diacritic: sharp rising
						{
							if(diacritic == 4)
								diacritic = 0;
							else
								diacritic = 4;
							
							changed = true;
						}
						else if(changeable && key == KeyStroke.getKeyStroke('.',0).getKeyCode()) //diacritic: sharp falling
						{
							if(diacritic == 5)
								diacritic = 0;
							else
								diacritic = 5;
							
							changed = true;
						}
						
						if(changeable)
							change = vi_chars[72*alphabetCase + 6*modifier + diacritic];
						if(changed)
							input.setText(keep+(char)change);
					}
				}
			}
			else
			{
				output.setEditable(true);
				// "/" = mac, "\\" = windows
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("config/"+language)),"UTF-8"));
				String text = input.getText();
				boolean start_mode = false;
				boolean end_mode = false;
				String line;
				
				while((line = reader.readLine()) != null)
				{
					if(line.length() >= 1 && start_mode && line.charAt(0) != '$')
					{
						String A = line.substring(0,line.indexOf(":"));
						String B = line.substring(line.indexOf(":")+1);
						int a = A.length();
						
						if(text.length() >= a && text.substring(0,a).equals(A))
							text = B + text.substring(a,text.length());
					}
					else if(line.length() >= 1 && end_mode && line.charAt(0) != '$')
					{
						String A = line.substring(0,line.indexOf(":"));
						String B = line.substring(line.indexOf(":")+1);
						int a = A.length();
						
						if(text.length() >= a && text.substring(text.length()-a).equals(A))
							text = text.substring(0,text.length()-a) + B;
					}
					else if(line.length() >= 1 && line.charAt(0) != '#' && line.charAt(0) != '$')
					{
						String A = line.substring(0,line.indexOf(":"));
						String B = line.substring(line.indexOf(":")+1);
						text = text.replace(A,B); // replaceAll is the regex ver of replace (replace still replaces everything)
					}
					else if(line.equals("$end"))
						end_mode = true;
					else if(line.equals("$start"))
						start_mode = true;
					else if(line.length() >= 1 && line.charAt(0) == '$')
					{
						end_mode = false;
						start_mode = false;
					}
				}
				
				// Universal Separator //
				text = text.replace("||","\n");
				text = text.replace("|","");
				text = text.replace("\n","|");
				
				output.setText(text);
				error.setText("");
			}
			
			/*if(language.contains("korean") && input.getText().length() >= 3 && key == KeyStroke.getKeyStroke(' ',0).getKeyCode())
			{
				// This means that you have to press [Space] to fuse characters. I know, clunky.
				String text = output.getText();
				int length = text.length();
				int initialChar = (int)text.charAt(length-4);
				int medialChar = (int)text.charAt(length-3);
				int finalChar = (int)text.charAt(length-2);
				
				switch(initialChar)
				{
					case 12593: initialChar = 0; break; //g
					case 12594: initialChar = 1; break; //gg
					case : initialChar = 2; break; //n
					case : initialChar = 3; break; //d
					case : initialChar = 4; break; //dd
					case : initialChar = 5; break; //l/r
					case : initialChar = 6; break; //m
					case : initialChar = 7; break; //b
					case : initialChar = 8; break; //bb
					case : initialChar = 9; break; //s
					case : initialChar = 10; break; //ss
					case : initialChar = 11; break; //ng
					case : initialChar = 12; break; //j
					case : initialChar = 13; break; //jj
					case : initialChar = 14; break; //ch
					case : initialChar = 15; break; //k
					case : initialChar = 16; break; //t
					case : initialChar = 17; break; //p
					case : initialChar = 18; break; //h
					default: initialChar = 0;
				}
				
				switch(medialChar)
				{
					case : medialChar = 0; break; //a
					case : medialChar = 1; break; //ae
					case : medialChar = 2; break; //ya
					case : medialChar = 3; break; //yae
					case : medialChar = 4; break; //eo
					case : medialChar = 5; break; //e
					case : medialChar = 6; break; //yeo
					case : medialChar = 7; break; //ye
					case : medialChar = 8; break; //o
					case : medialChar = 9; break; //wa
					case : medialChar = 10; break; //wae
					case : medialChar = 11; break; //oe
					case : medialChar = 12; break; //yo
					case : medialChar = 13; break; //u
					case : medialChar = 14; break; //wo
					case : medialChar = 15; break; //we
					case : medialChar = 16; break; //wi
					case : medialChar = 17; break; //yu
					case : medialChar = 18; break; //eu
					case : medialChar = 19; break; //ui
					case : medialChar = 20; break; //i
					default: medialChar = 0;
				}
				
				switch(finalChar)
				{
					case : finalChar = 0; break; //<none>
					case : finalChar = 1; break; //g
					case : finalChar = 2; break; //gg
					case : finalChar = 3; break; //gs
					case : finalChar = 4; break; //n
					case : finalChar = 5; break; //nj
					case : finalChar = 6; break; //nh
					case : finalChar = 7; break; //d
					case : finalChar = 8; break; //l/r
					case : finalChar = 9; break; //lg
					case : finalChar = 10; break; //lm
					case : finalChar = 11; break; //lb
					case : finalChar = 12; break; //ls
					case : finalChar = 13; break; //lt
					case : finalChar = 14; break; //lp
					case : finalChar = 15; break; //lh
					case : finalChar = 16; break; //m
					case : finalChar = 17; break; //b
					case : finalChar = 18; break; //bs
					case : finalChar = 19; break; //s
					case : finalChar = 20; break; //ss
					case : finalChar = 21; break; //ng
					case : finalChar = 22; break; //j
					case : finalChar = 23; break; //ch
					case : finalChar = 24; break; //k
					case : finalChar = 25; break; //t
					case : finalChar = 26; break; //p
					case : finalChar = 27; break; //h
					default: finalChar = 0;
				}
				
				int c = (588*initialChar + 28*medialChar + finalChar) + 44032;
				
				output.setText(text.substring(0,length-4) + (char)c);
			}*/
			
			/*if(language.contains("korean"))
			{
				// [(initial) × 588 + (medial) × 28 + (final)] + 44032
				
				char[] list = output.getText().replace(" ","").toCharArray();
				int pos = 1;
				int init = 0;
				int mid = 0;
				int fin = 0;
				
				for(int i = 0; i < list.length; i++)
				{
					int val = (int)list[i];
					
					if(pos == 1) //initial
					{
						switch(val)
						{
							case 12593://g
						}
					}
					else if(pos == 2) //medial
					{
						switch(val)
						{
							case 12593://g
								
						}
					}
					else if(pos == 3) //final
					{
						switch(val)
						{
							case 12593://g
								
						}
					}
					
					if(pos == 3 && init != -1 && mid != -1 && fin != -1)
					{
						int ch = (588*init) + (28*mid) + (fin) + 44032;
						
					}
					
					if(pos > 3)
					{
						pos = 1;
						init = -1;
						mid = -1;
						fin = -1;
					}
					else
						pos++;
				}
				
				
			}*/
		}
		catch(Exception e) {error.setText(Arrays.toString(e.getStackTrace())+" [ERROR] "+e.toString());}
	}
	
	public void keyReleased(KeyEvent e) // Run all of the text replacing when a key is released.
	{
		key = e.getKeyCode();
		updateText();
	}
	
	private void init()
	{
		index = new HashMap<String[],String>();
		
		try
		{
			Scanner in = new Scanner(new File("config/init"));
			
			while(in.hasNext())
			{
				String[] line = in.nextLine().split(":");
				String[] aliases = line[0].split(",");
				String file = line[1];
				index.put(aliases,file);
			}
			
			in.close();
		}
		catch(Exception e) {error.setText("[ERROR] "+e.toString()+"   AT   "+Arrays.toString(e.getStackTrace()));}
	}
	
	public void keyPressed(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {} // Exclusive to alphanumeric keys, redundant because of keyReleased().
	public static void main(String[] args) {new Transcription();} // Run a non-static version of the main commands.
}

/*
[Future Plans (if I ever come back to this)]
- Korean
- Options for "config/init" = "$option=val".
- Vietnamese: Change modifier order.
- Vietnamese: Change diacritic keys.
- Note: latin_extended still has legacy Vietnamese, no point in removing it.

Original Goal (12 April, 2019): Create a working .jar with a universal config file (you can add as many language options as you want) for the TranscriptionGUI, then quit working on it. Leave it behind.
*/