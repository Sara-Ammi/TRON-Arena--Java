import java.awt.*;
import java.util.Random;

import javax.swing.*;

public class TronJava extends JPanel {
    // ma grille 
    private int[][] grid;
    private int cellWidth;
    private int cellHeight;
    // les directions 
    private static char HAUT = 'U' ;
    private static char BAS = 'D';
    private static char GAUCHE = 'L' ;
    private static char DROITE = 'R' ;
    // obstacle et fraise
    private static  int OBSTACLE = -1;
    private static  int fraise1 = 3;

    // constructeur 
    public TronJava (int[][] grid, int cellWidth, int cellHeight) {
        this.grid = grid;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        setPreferredSize(new Dimension(grid[0].length * cellWidth, grid.length * cellHeight));
    }
    
    // mise à jour de mon affichage 
    public void updateGrid(int[][] newGrid) {
        this.grid = newGrid;
        repaint(); // Appeler repaint() pour redessiner le composant
    }

    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.gray);    
        // dessin des lignes horizontalles 
        for (int row = 0; row < grid.length; row++) {
            int y = row * cellHeight;
            g2d.drawLine(0, y, grid[0].length * cellWidth, y);
        }
    
        // dessin des lignes verticalles
        for (int col = 0; col < grid[0].length; col++) {
            int x = col * cellWidth;
            g2d.drawLine(x, 0, x, grid.length * cellHeight);
        }
    
        // coloriage des cases selon leur contenue 
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == 1) {
                    g2d.setColor(Color.BLUE);
                    g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                } else if (grid[row][col] == 2) {
                    g2d.setColor(Color.RED);
                    g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                } else if (grid[row][col] == OBSTACLE) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                }
                if (grid[row][col] == OBSTACLE) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                } else if ( grid[row][col] == fraise1) {
                    g2d.setColor(Color.yellow);
                    g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                } 
            }
        }
    
        g2d.dispose();
    }
    
    // récupération de la direction choisie par le joueur 
    public static char demanderDirection(int joueur) {
       String direction =  JOptionPane.showInputDialog(null ,"Joueur " + joueur + ", veuillez entrer une direction (U : haut, D : bas, L : gauche, R : droite) : ");
       if (direction.equals("U")){
        return HAUT ;
       } else if (direction.equals("D")){
        return BAS ;
       }else if (direction.equals("L")){
        return  GAUCHE;
       }else if (direction.equals("R")){
        return DROITE ;
       } else { 
        JOptionPane.showMessageDialog(null, "Direction invalide !");
        return demanderDirection(joueur);} 
        }

        // récupération du nombre d'obstacles associé au niveau 
        public static int niveau() {
            String niveau= JOptionPane.showInputDialog("choisissez le niveau de difficulté : facile, normal, difficile.");
            if (niveau.equals("facile")) {
                return 10;
            } else if (niveau.equals("normal")) {
                return 15;
            } else if (niveau.equals("difficile")) {
                return 20;
            } else {
                return niveau();
            }
        }

        public static void placerObstacles(int[][] grid, int nombre) {
            Random random = new Random();
            for (int i = 0; i < nombre; i++) {
                int row = random.nextInt(grid.length);
                int col = random.nextInt(grid[0].length);
        // placement aleatoire les obstacles
                if (grid[row][col] == 0) {
                    grid[row][col] = OBSTACLE;
                } else {
                    i--; // Réessayer si la cellule est déjà occupée
                }
            }
        }

       // placement aleatoire de la fraise 
        public static void fraises(int[][] grid ){
            Random random = new Random();
                int i = 0 ; 
                while (i < 1) {
                    int row = random.nextInt(grid.length);
                    int col = random.nextInt(grid[0].length);
                    if (grid[row][col] == 0) {
                        grid[row][col] = fraise1;
                        i++;
                    } 
                }
        }

       // effacement de la fraise 
        public static void effacefraise (int[][] grid) {
        for (int i = 0; i < grid.length ; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] == 3 ) {
                 grid[i][j] = 0 ; 
                }
            }
        }
        }


        // le "power-up" turbo qui donne au joueur un dexième coup non limité par les obstacles 
        public static void turbo(int[][] grid , int posXJ,int posYJ,int joueur , boolean reponse) {
            char directionJ1 ;
            if (reponse) {
                directionJ1 = demanderDirectionRobot(grid, joueur, posXJ, posYJ);
            } else {
                directionJ1 = demanderDirection(joueur);
            }
            
        if (directionJ1 == HAUT && posXJ > 0) {
            posXJ--;
        } else if (directionJ1 == GAUCHE && posYJ > 0 ) {
            posYJ--;
        } else if (directionJ1 == BAS && posXJ < grid.length-1 ) {
            posXJ++;
        } else if (directionJ1 == DROITE &&  posYJ < grid[0].length-1 ) {
            posYJ++;
        } else {
          turbo(grid,posXJ,posYJ,joueur, reponse); // si il atteint les limite de la grille, il doit changer de direction 
        }

        grid[posXJ][posYJ] = joueur ; 

        }
      
      // choisir son adversaire 
       public static boolean choisirSonAdv () {
        String adv = JOptionPane.showInputDialog(" voulais vous jouer avec un (choisissez un nombre) : 1- joueur / 2- robot ");
        if (adv.equals("1")) {
            return false ;
        } else if (adv.equals("2")) {
            return true ;
        } else {
            return choisirSonAdv();
        }
       }
          
       // le robot qui choisit la case vide pour faire son coup 
       public static char demanderDirectionRobot(int [][] grid , int joueur , int x , int y ) {
        if ( y-1 >= 0 && grid[x][y-1] == 0 || y-1 >= 0 && grid[x][y-1] == fraise1 ) {
            return GAUCHE;
        } else if ( x+1 < grid.length && grid[x+1][y] == 0 || x+1 < grid.length && grid[x+1][y] == fraise1 ) {
            return BAS ;
        }else if ( x-1 >= 0 && grid[x-1][y] == 0 || x-1 >= 0 && grid[x-1][y] == fraise1) {
            return HAUT;
        } else {
        return DROITE;
        }
       }


    public static void main(String[] args) {
        // Création de la grille de jeu
        int[][] grid = new int[12][22];
        JFrame frame = new JFrame("TRON");
       
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.getContentPane().add(new TronJava(grid, 30 , 30 ));
        frame.pack(); // adapte l'affichage à la taille de la grille 
        frame.setVisible(true); // pour que l'afichage soit visisble tout au long du jeu 
          

   
    // Initialisation des joueurs
    int joueur1 = 1;
    int joueur2 = 2;
    int posXJ1 = 2;
    int posYJ1 = 2;
    int posXJ2 = 9;
    int posYJ2 = 19;
    grid[2][3]= 3;
    grid[2][4]= -1;
    grid[posXJ1][posYJ1]= joueur1;
    grid[posXJ2][posYJ2]= joueur2;

    frame.getContentPane().getComponent(0).repaint();
 
    boolean reponse = choisirSonAdv();
    int nombre = niveau();
    placerObstacles(grid,nombre);

    frame.getContentPane().getComponent(0).repaint();
    int j = 0 ;

    // Boucle principale du jeu 

    while (true) {
        // Mouvement du joueur 1
        char directionJ1 = demanderDirection(joueur1);
        if (directionJ1 == HAUT && posXJ1 > 0) {
            posXJ1--;
        } else if (directionJ1 == BAS && posXJ1 < grid.length-1 ) {
            posXJ1++;
        } else if (directionJ1 == GAUCHE && posYJ1 > 0 ) {
            posYJ1--;
        } else if (directionJ1 == DROITE &&  posYJ1 < grid[0].length-1 ) {
            posYJ1++;
        } else {
            JOptionPane.showMessageDialog(null, "Collision ! Le joueur 2 gagne !");
            break;
        }
       
        // Vérification de collision 
        if (grid[posXJ1][posYJ1] != 0 ) {
            if(grid[posXJ1][posYJ1] == 3) {
            turbo(grid,posXJ1,posYJ1,joueur1,false);
            } else {
            JOptionPane.showMessageDialog(null, "Collision ! Le joueur 2 gagne !");
            break;
        }
        }
       
        // Ajout du tracé de la moto du joueur 1 sur la grille
        grid[posXJ1][posYJ1] = joueur1;
         
        // compteur pour la fraise 
        j++ ;
        if(j == 5 ) {
            effacefraise(grid);
            fraises(grid);
            j = 0 ; 
        }

        frame.getContentPane().getComponent(0).repaint();
         
        // Mouvement du joueur 2
        char directionJ2 ; 
        if (reponse) {
            directionJ2 = demanderDirectionRobot(grid ,joueur2 , posXJ2 , posYJ2);
        } else {
            directionJ2 = demanderDirection(joueur2);
        }
        
        if (directionJ2 == HAUT && posXJ2 > 0) {
            posXJ2--;
        } else if (directionJ2 == BAS && posXJ2 < grid.length-1) {
            posXJ2++;
        } else if (directionJ2 == GAUCHE && posYJ2 > 0) {
            posYJ2--;
        } else if (directionJ2 == DROITE && posYJ2 < grid[0].length -1) {
            posYJ2++;
        } else {
            JOptionPane.showMessageDialog(null, "Collision ! Le joueur 1 gagne !");
            break;
        }
       
        // Vérification de collision 
        if (grid[posXJ2][posYJ2] != 0) {
            if(grid[posXJ2][posYJ2] == 3) {
                turbo(grid,posXJ2,posYJ2,joueur2,reponse);
                } else {
                JOptionPane.showMessageDialog(null, "Collision ! Le joueur 1 gagne !");
                break;
            }
        }

        // compteur pour la fraise 
        j++ ;
        if(j == 5 ) {
            effacefraise(grid);
            fraises(grid);
            j = 0 ; 
        }
        // Ajout du tracé de la moto du joueur 1 sur la grille
        grid[posXJ2][posYJ2] = joueur2;

        frame.getContentPane().getComponent(0).repaint();
    }
   
      
    }
}
