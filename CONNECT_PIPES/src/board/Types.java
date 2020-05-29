package board;

import java.io.File;

public class Types {  //these define types of tiles based on available icons
    
    public String[] type;
    
    public Types() {
        int n = 0;
        File[] files = new File("src/icons/").listFiles();
        type = new String[files.length];
        for (File f : files) {
            type[n] = GetName(f);
            n++;
        }
    }
    
    public int GetIndex(String s) {
        int i = 0;
        for (String y : type) {
            if (y.equals(s))
                return i;
            i++;
        }
        return -1;
    }
    
    public static String GetName(File f) {
        int i = 0;
        while (f.getName().charAt(i) != '.') {
            i++;
        }
        char[] tmp = new char[i];
        for (int j = 0;  j < i; j++) {
            tmp[j] = f.getName().charAt(j);
        }
        return new String(tmp);
    }
}