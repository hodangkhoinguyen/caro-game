import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;

public class Caro extends JFrame implements ActionListener 
{	
    private int row = 15, col = 15, height = 1000, width = 1000;
    private final int max = 5;
    private int total = 0;
    private JPanel gamePanel;
    private JLabel turnLabel;
    private int turnIndex = 0;
    private String first = "O", second = "X";
    private Stack<ArrayList<Integer>> stack = new Stack<ArrayList<Integer>>();
    private JButton[][] b = new JButton[row][col];
    private boolean check[][] = new boolean[row][col];

    public Caro(String title)
    {
        super(title);
        add(createMainPanel());
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(width,height);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public JPanel createMainPanel()
    {
        JPanel main = new JPanel();
        
        JPanel tp = new JPanel();
        tp.setLayout(new BorderLayout());
        
        JPanel top = new JPanel();
        JLabel title = new JLabel("Game Caro");
        title.setFont(new Font("Serif", Font.BOLD, 20));
        top.setBorder(new EmptyBorder(5,0,5,0));
        top.add(title);
        
        tp.add(top, BorderLayout.NORTH);
        tp.add(createButtonPanel(), BorderLayout.CENTER);
        main.setLayout(new BorderLayout());
        
        gamePanel = createGamePanel();
        main.add(tp, BorderLayout.NORTH);
        main.add(gamePanel, BorderLayout.CENTER);
        return main;
    }
    
    public JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        
        JPanel lp = new JPanel();
        turnLabel = new JLabel();
        setTurnLabel();
        lp.add(turnLabel);
        lp.setBorder(new EmptyBorder(0,5,0,0));
        
        JPanel bp = new JPanel();
        bp.setBorder(new EmptyBorder(0,0,0,5));
        bp.setLayout(new GridLayout(1,3,5,5));
        bp.add(createButton("New Game"));
        bp.add(createButton("Undo"));
        bp.add(createButton("Exit"));
        
        buttonPanel.add(lp, BorderLayout.WEST);
        buttonPanel.add(bp, BorderLayout.EAST);
        return buttonPanel;
    }
    
    public JButton createButton(String name)
    {
        JButton b = new JButton(name);
        b.addActionListener(this);
        
        return b;
    }
    
    public JPanel createGamePanel()
    {
        JPanel gp = new JPanel();
        gp.setBorder(new EmptyBorder(5,5,5,5));
        gp.setLayout(new GridLayout(row,col,1,1));
        
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                b[i][j] = new JButton();
                b[i][j].setBackground(Color.white);
                b[i][j].addActionListener(this);
                gp.add(b[i][j]);
            }
        }
        return gp;
    }
    
    public void setTurnLabel()
    {
        if (turnIndex == 0) 
            turnLabel.setText("The first player: O");
        else turnLabel.setText("The second player: X");
    }
    
    public void addPoint(int i, int j)
    {
        check[i][j] = true;
        total++;
        ArrayList<Integer> l = new ArrayList<>();
        l.add(i);
        l.add(j);
        
        stack.add(l);
        
        if (turnIndex == 0)
        {
            b[i][j].setText(first);
            b[i][j].setForeground(Color.red);
        }
        else
        {
            b[i][j].setText(second);
            b[i][j].setForeground(Color.blue);
        }
        
    }
    
    public boolean checkWin(int i, int j)
    {
        String s = b[i][j].getText();
        
        int r = i+1, c = j;
        int num = 1;
        
        //check win for the column
        while (r < i+max && r < row && b[r][j].getText() == s)
        {
            num++;
            r++;
        }
        r = i-1;
        while (r > i-max && r >= 0 && b[r][j].getText() == s)
        {
            num++;
            r--;
        }
        if (num == max) return true;
        
        //check win for the column
        num = 1;
        c = j+1;
        
        while (c < j+max && c < col && b[i][c].getText() == s)
        {
            num++;
            c++;
        }
        c = j-1;
        while (c > j-max && c >= 0 && b[i][c].getText() == s)
        {
            num++;
            c--;
        }
        
        if (num == max) return true;
        
        //check win for the first diagonal
        num = 1;
        r = i+1; c = j+1;
        
        while (r < i+max && r < row && c < j+max && c < col && b[r][c].getText() == s)
        {
            num++;
            r++; c++;
        }
        
        r = i-1; c = j-1;
        while (r > i-max && r >= 0 && c > j-max && c >= 0 && b[r][c].getText() == s)
        {
            num++;
            r--; c--;
        }
        
        if (num == max) return true;
        
        //check for the second diagonal
        num = 1;
        r = i+1; c = j-1;
        
        while (r < i+max && r < row && c > j-max && c >= 0 && b[r][c].getText() == s)
        {
            num++;
            r++; c--;
        }
        
        r = i-1; c = j+1;
        
        while (r > i-max && r >= 0 && c < j+max && c < col && b[r][c].getText() == s)
        {
            num++;
            r--; c++;
        }
        
        if (num == max) return true;
        
        return false;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                if (e.getSource() == b[i][j])
                {
                    if (!check[i][j])
                    {
                        addPoint(i,j);
                        if (checkWin(i,j))
                        {
                            String s = "second X";
                            if (turnIndex == 0) s = "first Y";
                            JOptionPane.showMessageDialog(null, "The " + s + " player win the game!!", "Congratulation", JOptionPane.INFORMATION_MESSAGE);
                            newGame();
                        }
                        turnIndex = 1-turnIndex;
                        setTurnLabel();
                        if (total == row*col)
                        {
                            JOptionPane.showMessageDialog(null, "Both of you win!!", "Tie", JOptionPane.INFORMATION_MESSAGE);
                            newGame();
                        }
                    }
                    else
                    {
                        
                    }
                }
            }
        }
        
        String s = e.getActionCommand();
        if (s == "New Game")
        {
            newGame();
        }
        else if (s == "Undo")
        {
            undo();
        }
        else if (s == "Exit")
        {
            int select = JOptionPane.showConfirmDialog(null, "Are you sure?", "Exit the game", JOptionPane.YES_NO_OPTION);
            if (select == 0) System.exit(0);
        }
        
    }
    
    public void newGame()
    {
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < col; j++)
            {
                b[i][j].setText("");
                check[i][j] = false;
                turnIndex = 0;
            }
        }
    }
    
    public void undo()
    {
        if (stack.size() == 0)
        {
            
        }
        else
        {
            ArrayList<Integer> l = stack.pop();
            
            int i = l.get(0), j = l.get(1);
            b[i][j].setText("");
            check[i][j] = false;
            
            turnIndex = 1-turnIndex;
            setTurnLabel();
            total--;
        }
    }
    
    public static void main(String[] args)
    {
        new Caro("Caro");
    }
}