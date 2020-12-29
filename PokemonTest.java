import java.lang.reflect.*;

/**
 * This class tests a class
 * 
 * @author Mr. Aronson
 */
public class PokemonTest
{
    private String className = "Pokemon";
    private String[] fieldNames = {"catchRate", "attackPower", "weight", "level", "MAX_CATCH_RATE"};
    private String[] fieldTypes = {"double", "int", "double", "int", "double"};
    private String[] fieldValues = {"", "", "10.0", "1", ".25"};
    private boolean[] fieldFinal = {false, false, false, false, true};
    private boolean[] fieldInit = {false, false, false, false, false};
    private Object[] cArgs = {10.0, 1};
    private String toStringValid = "The Pokemon is level 1 and weighs 10.0.";

    private  boolean failed = false;
    private  Object t1;
    private  Class<?> c;
    private Constructor constructor;

    public static void main(String args[]) {
        PokemonTest p = new PokemonTest();
        p.doTest();
    }

    private int getFieldIndex(String f) 
    {
        for (int i = 0; i < fieldNames.length; i++) {
            if (f.contains(fieldNames[i]))
                return i;
        }
        return -1;
    }

    private void checkField(Field field)
    {
        String temp = field.toString();
        int index = getFieldIndex(temp);
        if (index < 0) {
            failure("Not a valid instance variable: "+ temp);
            return;
        }
        if (!temp.contains(fieldTypes[index]))
            failure(fieldNames[index] + " instance variable is not a " + fieldTypes[index]);
        else if (!fieldFinal[index] && !temp.contains("private"))
            failure(fieldNames[index] + " instance variable is not private");
        else if (fieldFinal[index] && !temp.contains("final"))
            failure(fieldNames[index] + " instance variable should be final");
        else if (fieldValues[index].length() > 0) {

            try
            {
                field.setAccessible(true);
                if (fieldTypes[index].equals("double") && 
                Math.abs((double)field.getDouble(t1)) - Double.parseDouble(fieldValues[index]) < .001)
                    fieldInit[index] = true;
                else if (fieldTypes[index].equals("int") && 
                field.getInt(t1) == Integer.parseInt(fieldValues[index]))
                    fieldInit[index] = true;

                else
                    failure("not setting " + fieldNames[index] + " correctly");
            }
            catch (IllegalAccessException e)
            {
                failure("not setting " + fieldNames[index] + " correctly");
            }
        }
    }

    private  void doTest()
    {
        //**********  Class Test **************************************
        // Instantiate a new object
        System.out.println("Testing your " + className + " class: \n");

        if (!failed)    testConstructor();
        if (!failed)    testInstanceVariables();
        if (!failed)    testGetMethods();
        if (!failed)    testSetMethods();
        if (!failed)    testOtherMethods();
        if (!failed)    testToString();
        if (!failed)    testEquals();

        if(!failed)
        {
            System.out.println("Congratulations, your " + className + " class works correctly \n");
            System.out.println("****************************************************\n");
        }

        if(!failed)
            System.out.println("Yay! You have successfully completed the project!");
        else
            System.out.println("\nBummer.  Try again.");
    }

    private  void failure(String str)
    {
        System.out.println("*** Failed: " + str);
        failed = true;
    }

    private void testConstructor() {
        System.out.println("Testing constructor");
        try
        {
            c = Class.forName(className);
            constructor = c.getConstructor(new Class[] {double.class, int.class});
            t1 = constructor.newInstance(cArgs);
        }
        catch (NoClassDefFoundError e)
        {
            failure("Epic Failure: missing " + className + " class");
        }
        catch (ClassNotFoundException e)
        {
            failure("Epic Failure: missing " + className + " class");
        }
        catch (NoSuchMethodException e)
        {
            failure("missing needed constructor :" + e.toString());
        }
        catch (Exception e)
        {
            failure(e.toString());
        }

        if(!failed)
            System.out.println("Passed  constructor test\n");

    }

    private void testInstanceVariables() {
        System.out.println("Testing instance variables");
        if(!failed)
        {
            Field[] fields = c.getDeclaredFields();
            if(fields.length == 0)
                failure("Class has no instance variables");
            else
            {
                for(Field field : fields)
                {
                    if (failed)
                        continue;
                    checkField(field);
                }
            }
        }

        if (!failed) {
            for (int i = 0; i < fieldInit.length; i++ ) {
                if (fieldValues[i].length() > 0 && !fieldInit[i])
                    failure(fieldNames[i] + " instance variable not set to proper value");
            }
        }

        if(!failed)
            System.out.println("Passed  instance variable test\n");

    }

    private void testGetMethods() {
        System.out.println("Testing get methods");

        try {
            if (!failed) {
                Object obj = constructor.newInstance(cArgs);

                for (int i = 0; i < fieldNames.length; i++) {
                    if (!fieldFinal[i]) {
                        String str = fieldNames[i];
                        str = "get" + str.substring(0,1).toUpperCase() + str.substring(1);
                        Method m = c.getDeclaredMethod(str);
                        Object tempNum = m.invoke(obj);
                        if (fieldValues[i].length() > 0) {
                            if (fieldTypes[i].equals("double") ) {

                                if (Math.abs((Double)tempNum - Double.parseDouble(fieldValues[i])) > .001)
                                    failure(str + " not getting proper value " + fieldValues[i]);

                            } else if (fieldTypes[i].equals("int")) {

                                if ((Integer)tempNum != Integer.parseInt(fieldValues[i]))
                                    failure(str + " not getting proper value " + fieldValues[i]);
                            }
                        }
                    }
                }
                /*
                Method s = c.getDeclaredMethod("setWeight", new Class[] {double.class});
                args = new Object[] {12.0};
                s.invoke(obj, args);
                 */

            }
        } catch (NoSuchMethodException e) {
            failure(e.toString());
        } catch (InstantiationException e) {
            failure(e.toString());
        } catch (IllegalAccessException e) {
            failure(e.toString());
        } catch (InvocationTargetException e) {
            failure(e.toString());
        }

        if(!failed)
            System.out.println("Passed  get methods test\n");
    }

    private void testSetMethods() {
        System.out.println("Testing set methods");
        if(!failed)
            System.out.println("Passed  set methods test\n");

    }

    private void testOtherMethods() {
        System.out.println("Testing other methods");

        try {
            // test gainWeight
            Object obj = constructor.newInstance(cArgs);
            Method m = c.getDeclaredMethod("gainWeight");
            m.invoke(obj);
            m = c.getDeclaredMethod("getWeight");
            double tempNum = (Double)m.invoke(obj);
            if (Math.abs(tempNum - 10.5) > .0001)
                failure("gainWeight changed weight to " + tempNum + " instead of 10.5");

            // test workout
            obj = constructor.newInstance(cArgs);
            m = c.getDeclaredMethod("workout");
            m.invoke(obj);
            m = c.getDeclaredMethod("getWeight");
            tempNum = (Double)m.invoke(obj);
            if (Math.abs(tempNum - 9.5) > .0001)
                failure("workout changed weight to " + tempNum + " instead of 9.5");

            // test levelUp
            obj = constructor.newInstance(cArgs);
            Method ap = c.getDeclaredMethod("getAttackPower");
            int origAP = (Integer)ap.invoke(obj);
            m = c.getDeclaredMethod("levelUp");
            m.invoke(obj);
            int tempInt = (Integer)ap.invoke(obj);
            if (tempInt != (int)(origAP*1.05))
                failure("levelUp did not change attackPower properly");
            m = c.getDeclaredMethod("getLevel");
            tempInt = (Integer)m.invoke(obj);
            if (tempInt != 2)
                failure("levelUp did not change level properly");

            // test cry
            m = c.getDeclaredMethod("cry");
            String str = (String)m.invoke(obj);
            if (!str.equals("Pokemon!"))
                failure("cry does not return Pokemon!");

            // test catchRate
            Method cr = c.getDeclaredMethod("getCatchRate");
            double sum = 0.0;
            for (int i = 0; !failed && i < 1000; i++) {
                obj = constructor.newInstance(cArgs);
                tempNum = (Double)cr.invoke(obj);
                if (tempNum > 0.25 || tempNum <= 0)
                    failure("catchRate should always be between 0 and MAX_CATCH_RATE");
                    sum += tempNum;
            }

              if (Math.abs(sum/1000 - .25/2) > .1)
                failure("catchRate not in proper range between 0 and MAX_CATCH_RATE");

            // test attackPower
            sum = 0;
            for (int i = 0; !failed && i < 1000; i++) {
                obj = constructor.newInstance(cArgs);
                tempInt = (Integer)ap.invoke(obj);
                if (tempInt < 60 || tempInt > 70)
                    failure("attackPower should always be between 60 and 70");
                sum += tempInt;
            }

            if (Math.abs(sum/1000 - 65) > 2)
                failure("attackPower not in proper range between 60 and 70");
        } catch (Exception e) {
            failure (e.toString());
        }
        if(!failed)
            System.out.println("Passed  other methods test\n");

    }

    private void testToString() {
        if(!failed)
        {
            System.out.println("Testing toString method");
            String objectToString = t1.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(t1));
            if(t1.toString().equals(objectToString))
                failure("missing toString method");
        }

        if(!failed)
        {
            if(!t1.toString().equals(toStringValid))
                failure("toString is invalid: \n" + t1.toString() + "\nshould be:\n" + toStringValid );
        }

        if(!failed)
            System.out.println("Passed  toString method test\n");
    }

    private void testEquals() {
        if(!failed)
        {
            System.out.println("Testing equals method");
            try
            {
                Constructor constructor = c.getConstructor(new Class[] {double.class, int.class});
                Object temp1 = constructor.newInstance(cArgs);
                Object temp2 = constructor.newInstance(new Object[] {10.0, 2});
                Object temp3 = constructor.newInstance(new Object[] {20.0, 1});
                if(!t1.equals(temp1))
                    failure("\n" + t1.toString() + "\nshould equal\n" + temp1.toString() + "\n(maybe missing equals method?)");
                else if (t1.equals(temp2))
                    failure("\n" + t1.toString() + "\nshould not equal\n" + temp2.toString() );
                else if (!t1.equals(temp3))
                    failure("\n" + t1.toString() + "\nshould equal\n" + temp3.toString() );

            }
            catch (Exception e)
            {
                failure(e.toString());
            }
        }
        if (!failed) 
            System.out.println("Passed  equals method test\n");
    }

}
