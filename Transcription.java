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
	private int key;
	private boolean consume;
	
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
		window = new JFrame("Transcription v1.1");
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
			if(lang.getText().equals("vietnamese") || lang.getText().equals("vi"))
			{
				output.setEditable(false);
				
				if(input.getText().length() >= 1 && key == KeyStroke.getKeyStroke('/',0).getKeyCode()) //punctuation
				{
					int last_index = input.getCaretPosition()-1;
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
				else if(input.getCaretPosition() >= 1)
				{
					int last_index = input.getCaretPosition()-1;
					String keep_pre = input.getText().substring(0,last_index);
					String keep_post = input.getText().substring(last_index+1);
					int change = (int)input.getText().charAt(last_index);
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
						{
							int tmp = input.getCaretPosition();
							input.setText(keep_pre + (char)change + keep_post);
							input.setCaretPosition(tmp);
						}
					}
				}
				
				error.setText("");
			}
			else if(lang.getText().equals("korean") || lang.getText().equals("ko"))
			{
				output.setEditable(true);
				input.setText(TranscribeViaReader(input.getText(),"korean.txt"));
				error.setText("");
				
				// Algorithm //
				
				if((input.getText().length() == 3 || input.getText().length() == 2) && (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE))
				{
					// This means that you have to press [Enter] or [Space] to fuse characters. I know, clunky.
					int initialChar = (int)input.getText().charAt(0);
					int medialChar = (int)input.getText().charAt(1);
					int finalChar;
					
					if(input.getText().length() == 3)
						finalChar = (int)input.getText().charAt(2);
					else
						finalChar = 0;
					
					switch(initialChar)
					{
						case 12593: initialChar = 0; break; //g
						case 12594: initialChar = 1; break; //gg
						case 12596: initialChar = 2; break; //n
						case 12599: initialChar = 3; break; //d
						case 12600: initialChar = 4; break; //dd
						case 12601: initialChar = 5; break; //l/r
						case 12609: initialChar = 6; break; //m
						case 12610: initialChar = 7; break; //b
						case 12611: initialChar = 8; break; //bb
						case 12613: initialChar = 9; break; //s
						case 12614: initialChar = 10; break; //ss
						case 12615: initialChar = 11; break; //ng
						case 12616: initialChar = 12; break; //j
						case 12617: initialChar = 13; break; //jj
						case 12618: initialChar = 14; break; //ch
						case 12619: initialChar = 15; break; //k
						case 12620: initialChar = 16; break; //t
						case 12621: initialChar = 17; break; //p
						case 12622: initialChar = 18; break; //h
						default: initialChar = 0;
					}
					
					switch(medialChar)
					{
						case 12623: medialChar = 0; break; //a
						case 12624: medialChar = 1; break; //ae
						case 12625: medialChar = 2; break; //ya
						case 12626: medialChar = 3; break; //yae
						case 12627: medialChar = 4; break; //eo
						case 12628: medialChar = 5; break; //e
						case 12629: medialChar = 6; break; //yeo
						case 12630: medialChar = 7; break; //ye
						case 12631: medialChar = 8; break; //o
						case 12632: medialChar = 9; break; //wa
						case 12633: medialChar = 10; break; //wae
						case 12634: medialChar = 11; break; //oe
						case 12635: medialChar = 12; break; //yo
						case 12636: medialChar = 13; break; //u
						case 12637: medialChar = 14; break; //weo
						case 12638: medialChar = 15; break; //we
						case 12639: medialChar = 16; break; //wi
						case 12640: medialChar = 17; break; //yu
						case 12641: medialChar = 18; break; //eu
						case 12642: medialChar = 19; break; //ui
						case 12643: medialChar = 20; break; //i
						default: medialChar = 0;
					}
					
					switch(finalChar)
					{
						case 12593: finalChar = 1; break; //g
						case 12594: finalChar = 2; break; //gg
						case 12595: finalChar = 3; break; //gs
						case 12596: finalChar = 4; break; //n
						case 12597: finalChar = 5; break; //nj
						case 12598: finalChar = 6; break; //nh
						case 12599: finalChar = 7; break; //d
						case 12601: finalChar = 8; break; //l/r
						case 12602: finalChar = 9; break; //lg
						case 12603: finalChar = 10; break; //lm
						case 12604: finalChar = 11; break; //lb
						case 12605: finalChar = 12; break; //ls
						case 12606: finalChar = 13; break; //lt
						case 12607: finalChar = 14; break; //lp
						case 12608: finalChar = 15; break; //lh
						case 12609: finalChar = 16; break; //m
						case 12610: finalChar = 17; break; //b
						case 12612: finalChar = 18; break; //bs
						case 12613: finalChar = 19; break; //s
						case 12614: finalChar = 20; break; //ss
						case 12615: finalChar = 21; break; //ng
						case 12616: finalChar = 22; break; //j
						case 12618: finalChar = 23; break; //ch
						case 12619: finalChar = 24; break; //k
						case 12620: finalChar = 25; break; //t
						case 12621: finalChar = 26; break; //p
						case 12622: finalChar = 27; break; //h
						default: finalChar = 0;
					}
					
					int c = (588*initialChar + 28*medialChar + finalChar) + 44032;
					
					input.setText("");
					output.setText(output.getText() + (char)c);
				}
				else if(key == KeyEvent.VK_BACK_QUOTE)
				{
					output.setText(TranscribeViaReader(output.getText(),"hanja.txt"));
				}
				
				error.setText("");
			}
			else
			{
				String language = null;
				
				for(String[] aliases : index.keySet())
					for(String alias : aliases)
						if(alias.equals(lang.getText()))
							language = index.get(aliases);
				
				if(language == null)
					language = "latin_extended.txt";
				
				output.setEditable(true);
				output.setText(TranscribeViaReader(input.getText(),language));
				error.setText("");
			}
		}
		catch(Exception e) {error.setText(Arrays.toString(e.getStackTrace())+" [ERROR] "+e.toString());}
	}
	
	private String TranscribeViaReader(String text, String language)
	{
		try
		{
			// "/" = mac, "\\" = windows
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("config/"+language)),"UTF-8"));
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
			
			return text;
		}
		catch(Exception e) {error.setText("[ERROR] "+e.toString()+"   AT   "+Arrays.toString(e.getStackTrace()));}
		
		return "";
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
	
	public void keyPressed(KeyEvent e)
	{
		try
		{
			int k = e.getKeyCode();
			
			if(lang.getText().equals("vietnamese") || lang.getText().equals("vi"))
			{
				if(k == KeyStroke.getKeyStroke(';',0).getKeyCode() || k == KeyEvent.VK_QUOTE || k == KeyStroke.getKeyStroke('[',0).getKeyCode() || k == KeyStroke.getKeyStroke(',',0).getKeyCode() || k == KeyStroke.getKeyStroke(']',0).getKeyCode() || k == KeyStroke.getKeyStroke('.',0).getKeyCode())
					consume = true;
			}
			// Backspace works on your output as well for Korean. //
			// This needs to be here instead of at keyReleased() because otherwise, it backspaces the output even with one character in the input string.
			// Also, do NOT use String key here. There will be a delay of one key press, even though keyPressed() is functionally better.
			else if(lang.getText().equals("korean") || lang.getText().equals("ko"))
			{
				if(input.getText().length() == 0 && output.getText().length() >= 1 && k == KeyEvent.VK_BACK_SPACE)
					output.setText(output.getText().substring(0,output.getText().length()-1));
				
				if(input.getText().length() == 0 && k == KeyEvent.VK_SPACE)
					output.setText(output.getText()+" ");
				
				if(k == KeyEvent.VK_SPACE || k == KeyEvent.VK_BACK_QUOTE)
					consume = true;
			}
		}
		catch(Exception ex) {error.setText("[ERROR] "+ex.toString()+"   AT   "+Arrays.toString(ex.getStackTrace()));}
	}
	
	public void keyReleased(KeyEvent e) // Run all of the text replacing when a key is released.
	{
		key = e.getKeyCode();
		updateText();
	}
	
	public void keyTyped(KeyEvent e)
	{
		//note: key event is zero, so do everything in keyPressed except e.consume()
		
		if(consume)
		{
			e.consume();
			consume = false;
		}
	}
	
	public static void main(String[] args) {new Transcription();} // Run a non-static version of the main commands.
}

/*
[Future Plans (if I ever come back to this)]
- Options for "config/init" = "$option=val".
- Korean: More customization options.
- Vietnamese: Change modifier order.
- Vietnamese: Change diacritic keys.
- Chinese: Replace the pinyin system with a radicals/strokes system.
- Note: latin_extended still has legacy Vietnamese, no point in removing it.

[Changelog]
15 April, 2019: First release. It's a reworked version of TranscribeGUI which spanned the course of two months (IE from February 2019).
23 April, 2019: Added Korean and implemented its algorithm.

Original Goal (12 April, 2019): Create a working .jar with a universal config file (you can add as many language options as you want) for the TranscriptionGUI, then quit working on it. Leave it behind.
*/