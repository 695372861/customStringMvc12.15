public class Account {
    public void operation() {

//        String str=new Account().getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println("Operation+"+str);
        System.out.println("operation......asmtest");
    }

    public static void say(int number)
    {
        SecurityChecker.checkSecurity();
    }
}
