import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;

public class Input {
    public static int countColumns(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String Line = reader.readLine();
        reader.close();

        if (Line != null) {
            return Line.length();
        } else {
            return 0;
        }
    }

    public static int countRows(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        return lines;
    }

    // Konwersja labiryntu z pliku binarnego do tekstoego
    public static void binaryToText(String binaryFilePath) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(binaryFilePath));
             FileWriter out = new FileWriter("maze_decoded.txt")) {


            int id = Integer.reverseBytes(in.readInt());
            int escape = in.read();
            short columns = Short.reverseBytes(in.readShort());
            short lines = Short.reverseBytes(in.readShort());
            short entryX = Short.reverseBytes(in.readShort());
            short entryY = Short.reverseBytes(in.readShort());
            short exitX = Short.reverseBytes(in.readShort());
            short exitY = Short.reverseBytes(in.readShort());
            int reserved_one = Integer.reverseBytes(in.readInt());
            int reserved_two = Integer.reverseBytes(in.readInt());
            int reserved_three = Integer.reverseBytes(in.readInt());
            int counter = Integer.reverseBytes(in.readInt());
            int solution_offset = Integer.reverseBytes(in.readInt());
            int separator = in.read();
            int wall = in.read();
            int path = in.read();

//            System.out.println("id = " + id);
//            System.out.println("escape = " + escape);
//            System.out.println("columns = " + columns);
//            System.out.println("lines = " + lines);
//            System.out.println("entryX = " + entryX);
//            System.out.println("entryY = " + entryY);
//            System.out.println("exitX = " + exitX);
//            System.out.println("exitY = " + exitY);
//            System.out.println("reserved_one = " + reserved_one);
//            System.out.println("reserved_two = " + reserved_two);
//            System.out.println("reserved_three = " + reserved_three);
//            System.out.println("counter = " + counter);
//            System.out.println("solution_offset = " + solution_offset);
//            System.out.println("separator = " + separator);
//            System.out.println("wall = " + wall);
//            System.out.println("path = " + path);

            for(int i=0; i<lines; i++){
                if(i!=0) out.write('\n');
                for(int j=0; j<columns; j++){
                    char znak;
                    int temp;
                    int ile;

                    temp = in.read();
                    temp = in.read();

                    if(temp == wall){
                        znak = 'X';
                    }else if(temp == path){
                        znak = ' ';
                    }else{
                        znak = '?';
                    }

                    temp = in.read();
                    ile = temp + 1;
                    int count = 0;

                    j--;
                    while(count<ile){
                        j++;
                        if(j == columns){
                            j = 0;
                            i++;
                        }

                        if(i == entryY-1 && j == entryX-1){
                            out.write('P');
                        }else if(i == exitY-1 && j == exitX-1){
                            out.write('K');
                        }else{
                            out.write(znak);
                        }
                        count++;
                    }
                }

            }
        }

    }

    // Wczytywanie labiryntu z pliku tekstowego do wektora 2d
    public static char[][] readMaze(String filePath, int rows, int columns) throws IOException {
        char[][] maze = new char[rows][columns];
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            maze[i] = line.toCharArray();
            i++;
        }
        reader.close();
        return maze;
    }


}
