package ir.ac.kntu.Technical.Other.Other;

public class Helper_Log {
    private static int errorNum = 1;

    public static void errorLog(Throwable throwable, Class classNAme) {
        try {
            Helper.error(errorNum, "Exception has occurred ! " + throwable.getMessage());
        } catch (Exception e) {
            Helper.error(errorNum, "faile to load throwable message, " + e.getMessage());
        }
        try {
            Helper.error(errorNum, "Above stacktrace (" + classNAme.getName() + ") is : ");
        } catch (Exception e) {
            Helper.error(errorNum++, "failed to load classname, " + e.getStackTrace());
        }
        try {
            throwable.printStackTrace();
        } catch (Exception e) {
            Helper.error(errorNum++, "faile to print stack trace, " + e.getMessage());
        }
    }

    public static void errorLog(Class className) {
        Helper.error(errorNum, "Null occurrence ! ");
        Helper.error(errorNum++, "above classname : (" + className.getName() + ")");
    }
}
