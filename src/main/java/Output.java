import java.io.*;

public class Output {
    public static boolean isDigit(char x) {
        return x >= '0' && x <= '9';
    }

    public static void outputFromBinary(String inputFileName, char[][] maze) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFileName));
             BufferedReader pathReader = new BufferedReader(new FileReader("filesOut/path.txt"));
             DataOutputStream out = new DataOutputStream(new FileOutputStream("filesOut/wynik.bin"))) {

            int id = in.readInt();
            out.writeInt(id);

            int escape = in.read();
            out.write(escape);

            short columns = in.readShort();
            out.writeShort(columns);

            short lines = in.readShort();
            out.writeShort(lines);

            // Pomiń EntryX & EntryY
            in.readShort();
            in.readShort();

            columns = Short.reverseBytes(columns);
            lines = Short.reverseBytes(lines);

            // EntryX & EntryY
            for(int i=0; i<lines; i++){
                for(int j=0; j<columns; j++){
                    if(maze[i][j] == 'P'){
                        out.writeShort(Short.reverseBytes((short)(j + 1)));
                        out.writeShort(Short.reverseBytes((short)(i + 1)));
                    }
                }
            }

            // Pomiń ExitX & ExitY
            in.readShort();
            in.readShort();

            // ExitX & ExitY
            for(int i=0; i<lines; i++){
                for(int j=0; j<columns; j++){
                    if(maze[i][j] == 'K'){
                        out.writeShort(Short.reverseBytes((short)(j + 1)));
                        out.writeShort(Short.reverseBytes((short)(i + 1)));
                    }
                }
            }

            int reservedOne = in.readInt();
            out.writeInt(reservedOne);

            int reservedTwo = in.readInt();
            out.writeInt(reservedTwo);

            int reservedThree = in.readInt();
            out.writeInt(reservedThree);

            // Pomiń counter
            in.readInt();

            // Liczenie słów kodowych
            int counter = 0;
            int prev = 't';
            for(int i = 0; i<maze.length; i++){
                for(int j = 0; j<maze[0].length; j++){
                    if(prev != maze[i][j] && prev != 't'){
                        if((maze[i][j] == ' ' && prev == '.') || (maze[i][j] == '.' && prev == ' ')){
                            counter--;
                        }
                        counter++;
                    }
                    prev = maze[i][j];
                }
                counter++;
            }
            out.writeInt(Integer.reverseBytes(counter));

            in.readInt(); // Pomiń solutionOffset
            int solutionOffset = 123;
            out.writeInt(Integer.reverseBytes(solutionOffset));

            int separator = in.read();
            out.write(separator);

            int wall = in.read();
            out.write(wall);

            int path = in.read();
            out.write(path);

            // Zapisywanie słów kodowych
            prev = 't';
            int count = 0;
            int znak = 0;

            for(int i=0; i<lines; i++){
                for(int j=0; j<columns; j++){
                    if(maze[i][j] == 'X'){
                        znak = wall;
                    }else if(maze[i][j] == ' ' || maze[i][j] == 'P' || maze[i][j] == 'K' || maze[i][j] == '.'){
                        znak = path;
                    }

                    if(prev != znak && (i == 0 || j != 0)){
                        if(prev != 't'){
                            if(count < 0 ) count = 0;
                            out.write(separator);
                            out.write(prev);
                            out.write(count);
                        }
                        count = 0;
                    }else{
                        count++;
                        if(count==255){
                            out.write(separator);
                            out.write(znak);
                            out.write(count);
                            count = -1;
                        }
                    }
                    prev = znak;
                }
                if(count<0){
                    continue;
                }

                out.write(separator);
                out.write(znak);
                out.write(count);

                count=-1;
            }

            out.write(separator);
            out.write(znak);
            out.write(count);

            out.writeInt(id);

            // Liczenie kroków ścieżki
            int kroki = 0;
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    if (maze[i][j] == '.') {
                        kroki++;
                    }
                }
            }
            kroki -= 2;

            out.writeInt(Integer.reverseBytes(kroki));

            // Zapisywanie ścieżki
            prev = 't';
            int prevC = 0;
            int liczba = 0;
            int c;

            while ((c = pathReader.read()) != -1) {
                if (c != '\n') {
                    if (isDigit((char) c)) {
                        if (isDigit((char) prev)) {
                            liczba *= 10;
                            liczba += (c - '0');
                        } else {
                            liczba += (c - '0');
                        }
                    } else {
                        if (isDigit((char) prev)) {
                            if (liczba > 255) {
                                while (liczba > 0) {
                                    int pom = liczba > 255 ? 255 : liczba;
                                    out.write(prevC);
                                    out.write(pom);
                                    liczba -= pom;
                                }
                            } else {
                                out.write(prevC);
                                out.write(liczba);
                                liczba = 0;
                            }
                        }
                        prevC = c;
                    }
                    prev = c;
                }
            }
            out.write(prevC);
            out.write(liczba);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void outputFromText(String inputFileName, char[][] maze) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFileName));
             BufferedReader pathReader = new BufferedReader(new FileReader("filesOut/path.txt"));
             DataOutputStream out = new DataOutputStream(new FileOutputStream("filesOut/wynik.bin"))) {

            int id = 0x52524243;
            out.writeInt(Integer.reverseBytes(id));

            int escape = 27;
            out.write(escape);

            int columns = Input.countColumns(inputFileName);
            out.writeShort(Short.reverseBytes((short) columns));

            int lines = Input.countRows(inputFileName);
            out.writeShort(Short.reverseBytes((short) lines));

            // Znajdowanie wejścia
            short entryX = 0;
            short entryY = 0;

            for(int i=0; i<lines; i++){
                for(int j=0; j<columns; j++){
                    if(maze[i][j] == 'P'){
                        entryX = (short)(j + 1);
                        entryY = (short)(i + 1);
                    }
                }
            }

            out.writeShort(Short.reverseBytes(entryX));
            out.writeShort(Short.reverseBytes(entryY));

            // Znajdowanie wyjścia
            short exitX = 0;
            short exitY = 0;

            for(int i=0; i<lines; i++){
                for(int j=0; j<columns; j++){
                    if(maze[i][j] == 'K'){
                        exitX = (short)(j + 1);
                        exitY = (short)(i + 1);
                    }
                }
            }

            out.writeShort(Short.reverseBytes(exitX));
            out.writeShort(Short.reverseBytes(exitY));

            int reservedOne = 255;
            out.writeInt(Integer.reverseBytes(reservedOne));

            int reservedTwo = 255;
            out.writeInt(Integer.reverseBytes(reservedTwo));

            int reservedThree = 255;
            out.writeInt(Integer.reverseBytes(reservedThree));

            int prev = 't';
            int count = 1;

            // Liczenie słów kodowych
            for(int i = 0; i<maze.length; i++){
                for(int j = 0; j<maze[0].length; j++){
                    if(prev != maze[i][j] && prev != 't'){
                        if((maze[i][j] == ' ' && prev == '.') || (maze[i][j] == '.' && prev == ' ')){
                            count--;
                        }
                        count++;
                    }
                    prev = maze[i][j];
                }
                count++;
            }

            int counter = count;
            out.writeInt(Integer.reverseBytes(counter));

            int solutionOffset = 123;
            out.writeInt(Integer.reverseBytes(solutionOffset));

            int separator = 35;
            out.write(separator);

            int wall = 88;
            out.write(wall);

            int path = 32;
            out.write(path);

            // Zapisywanie słów kodowych
            prev = 't';
            count = 0;
            int znak = 0;

            for(int i=0; i<lines; i++){
                for(int j=0; j<columns; j++){
                    if(maze[i][j] == 'X'){
                        znak = wall;
                    }else if(maze[i][j] == ' ' || maze[i][j] == 'P' || maze[i][j] == 'K' || maze[i][j] == '.'){
                        znak = path;
                    }

                    if(prev != znak && (i == 0 || j != 0)){
                        if(prev != 't'){
                            if(count < 0 ) count = 0;
                            out.write(separator);
                            out.write(prev);
                            out.write(count);
                        }
                        count = 0;
                    }else{
                        count++;
                        if(count==255){
                            out.write(separator);
                            out.write(znak);
                            out.write(count);
                            count = -1;
                        }
                    }
                    prev = znak;
                }
                if(count<0){
                    continue;
                }

                out.write(separator);
                out.write(znak);
                out.write(count);

                count=-1;
            }

            out.write(separator);
            out.write(znak);
            out.write(count);

            out.writeInt(Integer.reverseBytes(id));

            // Liczenie kroków ścieżki
            int kroki = 0;
            for (int i = 0; i < lines; i++) {
                for (int j = 0; j < columns; j++) {
                    if (maze[i][j] == '.') {
                        kroki++;
                    }
                }
            }
            kroki -= 2;
            out.writeInt(Integer.reverseBytes(kroki));

            // Zapisywanie ścieżki
            int c;
            prev = 't';
            int prevC = 0;
            int liczba = 0;

            while ((c = pathReader.read()) != -1) {
                if (c != '\n') {
                    if (isDigit((char) c)) {
                        if (isDigit((char) prev)) {
                            liczba *= 10;
                            liczba += (c - '0');
                        } else {
                            liczba += (c - '0');
                        }
                    } else {
                        if (isDigit((char) prev)) {
                            if (liczba > 255) {
                                while (liczba > 0) {
                                    int pom = liczba > 255 ? 255 : liczba;
                                    out.write(prevC);
                                    out.write(pom);
                                    liczba -= pom;
                                }
                            } else {
                                out.write(prevC);
                                out.write(liczba);
                                liczba = 0;
                            }
                        }
                        prevC = c;
                    }
                    prev = c;
                }
            }
            out.write(prevC);
            out.write(liczba);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}