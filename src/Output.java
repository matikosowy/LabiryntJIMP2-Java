import java.io.*;

public class Output {
    public static boolean isDigit(char x) {
        return x >= '0' && x <= '9';
    }

    public static void outputFromBinary(String inputFileName, char[][] maze) {
        try (DataInputStream in = new DataInputStream(new FileInputStream(inputFileName));
             BufferedReader pathReader = new BufferedReader(new FileReader("src/path.txt"));
             DataOutputStream out = new DataOutputStream(new FileOutputStream("wynik.bin"))) {

            int id = Integer.reverseBytes(in.readInt());
            out.writeInt(Integer.reverseBytes(id));

            int escape = in.read();
            out.write(escape);

            short columns = Short.reverseBytes(in.readShort());
            out.writeShort(Short.reverseBytes(columns));

            short lines = Short.reverseBytes(in.readShort());
            out.writeShort(Short.reverseBytes(lines));

            short entryX = Short.reverseBytes(in.readShort());
            out.writeShort(Short.reverseBytes(entryX));

            short entryY = Short.reverseBytes(in.readShort());
            out.writeShort(Short.reverseBytes(entryY));

            short exitX = Short.reverseBytes(in.readShort());
            out.writeShort(Short.reverseBytes(exitX));

            short exitY = Short.reverseBytes(in.readShort());
            out.writeShort(Short.reverseBytes(exitY));

            int reservedOne = Integer.reverseBytes(in.readInt());
            out.writeInt(Integer.reverseBytes(reservedOne));

            int reservedTwo = Integer.reverseBytes(in.readInt());
            out.writeInt(Integer.reverseBytes(reservedTwo));

            int reservedThree = Integer.reverseBytes(in.readInt());
            out.writeInt(Integer.reverseBytes(reservedThree));

            int counter = Integer.reverseBytes(in.readInt());
            out.writeInt(Integer.reverseBytes(counter));

            int solutionOffset = 123;
            out.writeInt(Integer.reverseBytes(solutionOffset));

            int separator = in.read();
            out.write(separator);

            int wall = in.read();
            out.write(wall);

            int path = in.read();
            out.write(path);

            // Writing labyrinth
            int temp;
            int cel = counter * 3;
            int licz = 0;
            while (licz < cel) {
                temp = in.read();
                out.write(temp);
                licz++;
            }

            out.writeInt(Integer.reverseBytes(id));

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

            // Writing path
            int prev = 't';
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


    public static void outputFromText(String inputFileName) {
        BufferedReader in = null;
        DataOutputStream out = null;
        BufferedReader pathReader = null;
        try {
            in = new BufferedReader(new FileReader(inputFileName));
            out = new DataOutputStream(new FileOutputStream("wynik.bin"));
            pathReader = new BufferedReader(new FileReader("src/path.txt"));

            int id = 0x52524243;
            out.writeInt(Integer.reverseBytes(id));

            int escape = 27;
            out.write(escape);

            int columns = Input.countColumns(inputFileName);
            out.writeShort(Short.reverseBytes((short) columns));

            int lines = Input.countRows(inputFileName);
            out.writeShort(Short.reverseBytes((short) lines));

            short x = 1;
            short y = 1;
            int ch;
            while ((ch = in.read()) != -1) {
                if (ch == 'P') {
                    break;
                }
                if (ch == '\n') {
                    y++;
                    x = 1;
                } else {
                    x++;
                }
            }

            in.close();

            short entryX = x;
            short entryY = y;
            out.writeShort(Short.reverseBytes(entryX));
            out.writeShort(Short.reverseBytes(entryY));

            in = new BufferedReader(new FileReader(inputFileName));

            while ((ch = in.read()) != -1) {
                if (ch == 'K') {
                    break;
                }
                if (ch == '\n') {
                    y++;
                    x = 1;
                } else {
                    x++;
                }
            }

            short exitX = x;
            short exitY = y;
            out.writeShort(Short.reverseBytes((short) exitX));
            out.writeShort(Short.reverseBytes((short) exitY));

            int reservedOne = 255;
            out.writeInt(Integer.reverseBytes(reservedOne));

            int reservedTwo = 255;
            out.writeInt(Integer.reverseBytes(reservedTwo));

            int reservedThree = 255;
            out.writeInt(Integer.reverseBytes(reservedThree));

            int prev = 't';
            int count = 1;

            in.close();
            in = new BufferedReader(new FileReader(inputFileName));

            while ((ch = in.read()) != -1) {
                if (ch != '\n') {
                    if (prev != ch && prev != 't') {
                        count++;
                    }
                    prev = ch;
                } else {
                    count++;
                }
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

            in.close();
            in = new BufferedReader(new FileReader(inputFileName));

            // Writing encoded words
            prev = 't';
            int prev_ch = 't';
            count = 0;
            int znak = 0;

            while((ch = in.read()) != -1){

                if(ch == 'X'){
                    znak = wall;
                }else if(ch == ' ' || ch == 'P' || ch == 'K'){
                    znak = path;
                }

                if(ch != '\n'){
                    if(prev != znak && prev_ch != '\n'){
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
                } else{
                    if(count<0){
                        continue;
                    }

                    out.write(separator);
                    out.write(znak);
                    out.write(count);

                    count=-1;
                }

                prev = znak;
                prev_ch = ch;
            }
            out.write(separator);
            out.write(znak);
            out.write(count);


            out.writeInt(Integer.reverseBytes(id));
            int kroki = 100;
            out.writeInt(Integer.reverseBytes(kroki));

            // Writing path
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
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (pathReader != null) {
                try {
                    pathReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

