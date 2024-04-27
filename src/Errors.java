public class Errors {

    // Sprawdzenie ilości początków i końców
    public static int entryexitError(char[][] maze, int rows, int columns){
        int countP = 0;
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(maze[i][j] == 'P'){
                    countP++;
                }
            }
        }
        if(countP > 1){
            return 1;
        }else{
            return 0;
        }
    }

    // Sprawdzenie występowania nieznanych znaków w pliku labiryntu
    public static char unrecognisedCharacter(char[][] maze, int rows, int columns){
        for(int i=0; i<rows; i++){
            for(int j=0; j<columns; j++){
                if(maze[i][j] != 'P' && maze[i][j] != 'K' && maze[i][j] != 'X' && maze[i][j] != ' ' && maze[i][j] != '.'){
                    return maze[i][j];
                }
            }
        }
        return ' ';
    }

    // Sprawdzenie zgodności ilości znaków w wierszach
    public static int invalidMaze(char[][] maze, int rows, int columns){
        int length = maze[0].length;
        for(int i=0; i<rows; i++){
            if(maze[i].length != length){
                return 1;
            }
        }
        return 0;
    }

}
