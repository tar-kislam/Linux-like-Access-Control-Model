import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

class securityProject {
    public static class FindFile
    {
        public void findFile(String name, File file)
        {
            File[] list = file.listFiles();
            if(list!=null)
                for (File fil : list)
                {
                    if (fil.isDirectory())
                    {
                        findFile(name,fil);
                    }
                    else if (name.equalsIgnoreCase(fil.getName()))
                    {
                        System.out.println(fil.getParentFile());
                    }
            }
        }
    }
    public static class User {

        private final String Name;
        private final String Password;

        public User(String name, String password) {
            Name = name;
            Password = password;
        }
    }
    private static String processDetails(ProcessHandle process) {
        return String.format("%8d %8s %10s %26s %-40s",
                process.pid(),
                text(process.parent().map(ProcessHandle::pid)),
                text(process.info().user()),
                text(process.info().startInstant()),
                text(process.info().commandLine()));
    }

    private static String text(Optional<?> optional) {

        return optional.map(Object::toString).orElse("-");

    }

    public static void main(String[] args) throws IOException {
        boolean access = false;
        String op,pass,user,makedir = "";
        boolean bool= false;
        String[] pathnames;
        Scanner scan = new Scanner(System.in);
        File f = null;


        List<User> users = new ArrayList<User>();
        users.add(new User("admin","1234"));

        while(true) {
            System.out.println("Enter username: ");
            user = scan.next();
            System.out.println("Enter password: ");
            pass = scan.next();
            for (User sample : users) {
                if ((sample.Name.equals(user)) && (sample.Password.equals(pass))) {
                    access = true;
                    System.out.println("Access Granted...");
                    break;
                }
            }
            op = scan.nextLine();
            while (access) {
                System.out.print("$");
                op = scan.nextLine();
                if (op.equals("mkdir")) {

                    makedir = scan.next();
                    f = new File("/home/tarik/" + makedir);

                    // create
                    bool = f.mkdir();

                    // print
                    System.out.print("Directory created? " + bool);

                }
                else if (op.equals("ls")) {
                    File folder = new File("/home/tarik");

                    // Populates the array with names of files and directories
                    pathnames = folder.list();

                    // For each pathname in the pathnames array
                    for (String pathname : pathnames) {
                        // Print the names of files and directories
                        System.out.println(pathname);
                    }
                }
                else if (op.equals("dir")) {
                    String[] a;

                    // Creates a new File instance by converting the given pathname string
                    // into an abstract pathname
                    File b = new File("/home/tarik");

                    // Populates the array with names of files and directories
                    a = b.list();

                    // For each pathname in the pathnames array
                    assert a != null;
                    for (String pathname : a) {
                        // Print the names of files and directories
                        System.out.println(pathname);
                    }
                }
                else if (op.equals("sudo")) {
                    System.out.println("Enter username: ");
                    user = scan.next();
                    System.out.println("Enter password: ");
                    pass = scan.next();
                    boolean chk = false;
                    for (User sample : users) {
                        if ((sample.Name.equals(user)) && (sample.Password.equals(pass))) {
                            chk=true;
                            System.out.println("Access Granted...");
                        }
                    }
                    if (!chk) {
                        System.out.println("Wrong password...");
                    }
                }
                else if (op.equals("cat")) {
                    String fileName1 = "";
                    String fileName2 = "";
                    System.out.println("First file name?");
                    fileName1 = scan.next();
                    System.out.println("Second file?");
                    fileName2 = scan.next();

                    // Read the file as string
                    String first = Files.readString(Path.of("/home/tarik" + fileName1));
                    String second = Files.readString(Path.of("/home/tarik" + fileName2));
                    String finale = first + second;
                    // Write the file
                    Files.writeString(Path.of("/home/tarik/cat.txt"), finale);
                    finale = Files.readString(Path.of("/home/tarik/cat.txt"));
                    System.out.println("The output is: " + finale);
                }
                else if (op.equals("rm")) {
                    String fileName = "";
                    System.out.println("File name?");
                    fileName = scan.next();
                    File toDelete = new File("/home/tarik" + fileName);
                    if (toDelete.delete()) {
                        System.out.println("Deleted the file: " + toDelete.getName());
                    } else {
                        System.out.println("Failed to delete the file.");
                    }
                }
                else if (op.equals("touch")) {
                    String fileName = "";
                    System.out.println("File name?");
                    fileName = scan.next();
                    File MyFile = new File("/home/tarik" + fileName);
                    if (MyFile.createNewFile()) {
                        System.out.println("Created the file: " + MyFile.getName());
                    } else {
                        System.out.println("Failed to create the file or already exists.");
                    }
                }
                else if (op.equals("adduser")) {
                    String userName = "";
                    String password = "";
                    System.out.println("Enter Username:");
                    userName = scan.next();
                    System.out.println("Enter Password:");
                    password = scan.next();
                    users.add(new User(userName, password));
                    System.out.println("User " + userName + "added successfully.");
                }
                else if (op.equals("find")) {
                    FindFile ff = new FindFile();
                    System.out.println("Enter the file to be searched.. ");
                    String name = scan.next();
                    System.out.println("Enter the directory where to search ");
                    String directory = scan.next();
                    ff.findFile(name, new File(directory));
                }
                else if(op.equals("umask")){
                    String umaskScan = "";
                    umaskScan = scan.next();
                    Path path = Paths.get("/home/tarik/"+umaskScan);
                    if (!Files.exists(path)) Files.createFile(path);
                    Set<PosixFilePermission> perms = Files.readAttributes(path, PosixFileAttributes.class).permissions();

                    System.out.format("Permissions before: %s%n",  PosixFilePermissions.toString(perms));

                    perms.add(PosixFilePermission.OWNER_WRITE);
                    perms.add(PosixFilePermission.OWNER_READ);
                    perms.add(PosixFilePermission.OWNER_EXECUTE);
                    perms.add(PosixFilePermission.GROUP_WRITE);
                    perms.add(PosixFilePermission.GROUP_READ);
                    perms.add(PosixFilePermission.GROUP_EXECUTE);
                    perms.add(PosixFilePermission.OTHERS_WRITE);
                    perms.add(PosixFilePermission.OTHERS_READ);
                    perms.add(PosixFilePermission.OTHERS_EXECUTE);
                    Files.setPosixFilePermissions(path, perms);

                    System.out.format("Permissions after:  %s%n",  PosixFilePermissions.toString(perms));
                }
                else if(op.equals("stat")){
                    String FileInfo = "";
                    System.out.println("Enter you file name : \n");
                    FileInfo = scan.next();
                    File file = new File("/home/tarik/"+FileInfo);

                    System.out.println("File Name : "+ file.getName());

                    System.out.println("File last modified : "+ file.lastModified());

                    System.out.println("File size : " + file.length() + " Bytes");

                    System.out.println("Path : "+file.getPath());

                    System.out.println("Abs Path : "+file.getAbsolutePath());

                    System.out.println("Parent : "+ file.getParent());

                    System.out.println(file.exists() ? "File exists":"File does not exist");

                    System.out.println(file.canWrite() ? "File is writable" : "File is not writable");

                    System.out.println(file.canRead() ? "File is readable" : "File is not readable");

                    System.out.println(file.isHidden() ?"File is hidden" : "File is not hidden");

                    System.out.println(file.isDirectory() ? "Is a directory" : "Is not a directory");

                    System.out.println(file.isFile() ? "Is a file" : "Is not a file");

                    System.out.println(file.isAbsolute() ? "File is absolute" : "File is not absolute" );
                }
                else if(op.equals("chmod")){
                    String chmodSetup = "";
                    System.out.println("Enter the file name please : \n");
                    chmodSetup = scan.next();
                    // creating a new file instance
                    File file = new File("/home/tarik/",chmodSetup);
                    System.out.println("Enter the +w +r +x  or  -w -r -x :");
                    op = scan.next();
                    int conditions=6;
                    // check if file exists
                    boolean exists = file.exists();

                    if(exists) {
                        // changing the file permissions
                        if (op.equals("+x"))
                            file.setExecutable(true);
                        if (op.equals("-x"))
                            file.setExecutable(false);
                        if (op.equals("+r"))
                            file.setReadable(true);
                        if (op.equals("-r"))
                            file.setReadable(false);
                        if (op.equals("+w"))
                            file.setWritable(true);
                        if (op.equals("-w"))
                            file.setWritable(false);

                        System.out.println("File permissions changed.");
                        // printing the permissions associated with the file currently
                        System.out.println("Executable: " + file.canExecute());
                        System.out.println("Readable: " + file.canRead());
                        System.out.println("Writable: " + file.canWrite());
                    }
                    else{
                        System.out.println("File not found.");
                    }
                }
                else if(op.equals("ps")){
                    ProcessHandle.allProcesses()
                            .forEach(process -> System.out.println(processDetails(process)));
                }
                else if (op.equals("exit")) {
                    System.exit(0);
                }
                else System.out.println("Command '" + op + "' not found");
                op = scan.nextLine();
            }
            System.out.println("Access Denied...");
        }
    }
}