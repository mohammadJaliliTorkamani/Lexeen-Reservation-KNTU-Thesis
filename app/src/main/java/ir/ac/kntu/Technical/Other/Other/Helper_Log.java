package ir.ac.kntu.Technical.Other.Other;

/**
 * logger class
 */
public class Helper_Log {
    private static int errorNum = 1;

    /**
     * logs error into console
     *
     * @param throwable thrown error
     * @param classNAme containing class name
     */
    public static void errorLog(Throwable throwable, Class classNAme) {
        try {
            Helper.getInstance().error(errorNum, "Exception has occurred ! " + throwable.getMessage());
        } catch (Exception e) {
            Helper.getInstance().error(errorNum, "faile to load throwable message, " + e.getMessage());
        }
        try {
            Helper.getInstance().error(errorNum, "Above stacktrace (" + classNAme.getName() + ") is : ");
        } catch (Exception e) {
            Helper.getInstance().error(errorNum++, "failed to load classname, " + e.getStackTrace());
        }
        try {
            throwable.printStackTrace();
        } catch (Exception e) {
            Helper.getInstance().error(errorNum++, "faile to print stack trace, " + e.getMessage());
        }
    }

    /**
     * logs error into class (used for null occurrence)
     *
     * @param className containing class name
     */
    public static void errorLog(Class className) {
        Helper.getInstance().error(errorNum, "Null occurrence ! ");
        Helper.getInstance().error(errorNum++, "above classname : (" + className.getName() + ")");
    }
}
