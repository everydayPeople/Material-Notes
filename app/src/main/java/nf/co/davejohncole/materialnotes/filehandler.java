package nf.co.davejohncole.materialnotes;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Teacher on 9/17/2014.
 * This class is intended to make life simpler and handle all file related tasks
 */
public class filehandler {


    //Constructor
    public filehandler() {
        //check we can read and write to the system
        System.out.println("Can write to external files: " + isExternalStorageWritable());
        System.out.println("Can read from external files: " + isExternalStorageReadable());
        if (!isExternalStorageReadable()) { /** need alert here and kill the app */ }
        if (!isExternalStorageReadable()) { /** need alert here and kill the app */ }

    }

    //checks we can write to storage
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /* creates a File array to store filenames */
    public File[] listFilesLocal () {
        String path = Environment.getExternalStorageDirectory() + File.separator + "MaterialNotes";
        File f = new File(path);
        File[] file = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name)
            {
                return (name.endsWith(".txt")||name.endsWith(".TXT"));
            }
        });
        //sort files into date modified
        if (file != null) { sortByDateModified(file);}
        return file;
    }

    /* sort a File array by date modified */
    public File[] sortByDateModified(File[] file) {
        Arrays.sort(file, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                    return -1;
                } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });
        return file;
    }

    public String[] search(String sf) {
        sf=sf.trim();
        String path = Environment.getExternalStorageDirectory() + File.separator + "MaterialNotes";
        File f = new File(path);
        File file[] = f.listFiles();
        String result[] = new String[file.length];
        for (int i = 0; i < file.length; i++) {
            if (file[i].getName().toLowerCase().contains(sf.toLowerCase())) {
                result[i] = file[i].getName();
            }
        }
        List<String> list = new ArrayList<String>(Arrays.asList(result));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);

    }

    public String openFileLocal (String fn) {
        return "text";
    }

    /** called on first run of the app  */
    public Boolean mkDirLocal(String r) throws IOException {
        //creates file resource
        File file;
        String readMe = r;
        //create the directory first
        file = new File(Environment.getExternalStorageDirectory() + "/MaterialNotes");
        file.mkdir();
        //then make the Read Me file
        file = new File(Environment.getExternalStorageDirectory() + File.separator + "MaterialNotes" + File.separator + "Example Note.txt");
        file.createNewFile();

        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(readMe);
                myOutWriter.close();
                fOut.close();
                System.out.println("File Created");
                return true;
        } catch (IOException e) {
                System.out.println("Problem Creating File");
                return false;
        }


    }


    /** Creates a local file */
    public boolean createFileLocal(String filename, String fileContent) {

        /*
        had to remove the trim - for some reason it creates 2 files.
        filename = filename.trim();
        */
        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "MaterialNotes" + File.separator + filename + ".txt");
            file.createNewFile();
            //File myFile = new File("/sdcard/MaterialNotes/"+ filename);
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(fileContent);
                myOutWriter.close();
                fOut.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    /** opens a note, returns the content as a String */
    public StringBuilder getNoteContentLocal(String filename) throws IOException {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File("/sdcard/MaterialNotes/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            //System.out.println(text);
        } catch (Exception e) {
            System.out.println("...something went wrong with getNoteContentLocal");
            text.append("");
        }
        return text;
    }

    public boolean saveFileLocal (String filename, CharSequence fileContent) {
        File myFile = new File("/sdcard/MaterialNotes/"+ filename);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(fileContent);
            myOutWriter.close();
            fOut.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean delFileLocal (String filename) {
        File myFile = new File("/sdcard/MaterialNotes/" + filename);
        return myFile.delete();
    }





}
