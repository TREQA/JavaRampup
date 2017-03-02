import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by George Codreanu on 2/28/2017.
 */
public class javaRamp {


    public static void main(String[] args) throws IOException {

        String inputPath, keyword;
        Scanner input = new Scanner(System.in);
        int pathValid = 0;
        System.out.println("Please enter the path of the folder:");

        do{
            inputPath = input.next();
            File folder = new File(inputPath);

            if(folder.exists()){
                System.out.println("Specified path is correct. Please enter the string that should be searched for:");
                keyword = input.next();
                findStringAndCompress(folder,keyword,inputPath);
                File keywordFolder = new File(inputPath + "//" + keyword);
                keywordFolder.mkdir();
                copyFile(folder,keywordFolder);
                deleteOriginalZips(inputPath);
                pathValid =1;
            }
            else{
                System.out.println("Specified path is incorrect.Please try again:");
            }
        }while(pathValid == 0);
    }

    public static void deleteOriginalZips(String path){

        File folder = new File(path);

    File[] listOfZips = folder.listFiles();
    for (int i=0; i<listOfZips.length; i++){

        File file = listOfZips[i];
        if(file.isFile() && file.getName().endsWith(".zip")){
           if( file.delete()){
               System.out.println(file.getName() + " is deleted");
           }
            else{
               System.out.println("File not deleted");
           }
        }
    }
    }

   public static void findStringAndCompress(File folder, String keyword, String inputpath) throws IOException {

        File[] listOfFiles = folder.listFiles();
        int keywordFound;

        for (int i=0; i <listOfFiles.length;i++){

            File file = listOfFiles[i];
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    keywordFound = searchFile(file,keyword);
                    if(keywordFound == 1) {
                        compressFile(file,inputpath);
                    }
                }
            }
    }

    public static void copyFile(File folder, File keywordFolder) throws IOException {
        File[] zipList = folder.listFiles();
        File b = new File(keywordFolder.getPath());

       for(int i = 0 ; i<zipList.length;i++){

           File zip = zipList[i];
                   if(zip.isFile()&& zip.getName().endsWith(".zip")){

                       FileInputStream in = new FileInputStream(zip.getPath());
                       FileOutputStream out = new FileOutputStream(b.getPath()+"\\" + zip.getName());
                       byte[] buffer = new byte[1024];
                       int lenght;
                       while((lenght=in.read(buffer))>0){
                           out.write(buffer,0,lenght);
                       }
                       in.close();
                       out.close();
                   }
           }
        }

    public static void compressFile(File file, String inputPath) throws IOException {

        String compressedFolder = file.getParentFile() + "\\" + file.getName();
        System.out.println(compressedFolder);
        FileInputStream in = new FileInputStream(file.getPath());
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(compressedFolder + ".zip"));
        out.putNextEntry(new ZipEntry(file.getName()));

        int bytes_read;
        byte[] buffer = new byte[1024];

        while ((bytes_read = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytes_read);
        }
        in.close();
        out.finish();
        out.close();

        System.out.println("The file was compressed successfully!");
    }

    public static int searchFile(File file,String keyword) throws FileNotFoundException {

        Scanner scan = new Scanner(file);
        String regex = "\\d{3}" +keyword + "[A-Za-z]{1}";
        while (scan.hasNextLine()){
            String line = scan.nextLine();
            Pattern pattern = Pattern.compile(regex);
            Matcher match = pattern.matcher(line);
            if(match.find())
            {
                return 1;
            }
        }
        return 0;
    }
}