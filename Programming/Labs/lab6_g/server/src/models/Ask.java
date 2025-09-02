package models;

import utility.Console;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.NoSuchElementException;

public class Ask {
    public static class AskBreakException extends Exception {}

    public static Worker askWorker(Console console) throws AskBreakException{
        try {
            console.print("name: ");
            String name;
            while (true) {
                name = console.readln().trim();
                if (name.equals("exit")) throw new AskBreakException();
                if (!name.isEmpty()) {
                    break;
                }
                console.printErr("name cannot be empty\n");
                console.print("name: ");
            }
            Coordinates coordinates = askCoordinates(console);
            console.print("salary: ");
            long salary;
            while (true) {
                try {
                    salary = Long.parseLong(console.readln().trim());
                    if (salary <= 0) {
                        throw new NumberFormatException();
                    }
                    break;
                } catch (NumberFormatException e) {
                    console.printErr("salary must be a positive number\n");
                    console.print("salary: ");
                }
            }
            console.print("start date (yyyy-mm-dd): ");
            Date startDate = askDate(console);
            console.print("end date (yyyy-mm-dd): ");
            ZonedDateTime endDate = askZonedDateTime(console);
            Position position = askPosition(console);
            Organization organization = askOrganization(console);
            return new Worker(1, name, coordinates, salary, startDate, endDate, position, organization);
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }

    public static Coordinates askCoordinates(Console console) throws AskBreakException {
        try {
            console.println("coordinates");
            console.print("x: ");
            Integer x;
            while (true) {
                try {
                    String s = console.readln().trim();
                    if (s.equals("exit")) throw new AskBreakException();
                    x = Integer.parseInt(s);
                    if (x <= 443) {
                        break;
                    }
                    console.printErr("x must be less than or equal to 443\n");
                    console.print("x: ");
                } catch (NumberFormatException e) {
                    console.printErr("x must be an integer\n");
                    console.print("x: ");
                }
            }
            console.print("y: ");
            float y;
            while (true) {
                try {
                    y = Float.parseFloat(console.readln().trim());
                    break;
                } catch (NumberFormatException e) {
                    console.printErr("y must be a float\n");
                    console.print("y: ");
                }
            }
            return new Coordinates(x, y);
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }

    public static Date askDate(Console console) throws AskBreakException {
        try {
            Date date;
            while (true) {
                try {
                    String s = console.readln().trim();
                    if (s.equals("exit")) throw new AskBreakException();
                    //date = Date.from(ZonedDateTime.parse(s).toInstant());
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
                    break;
                } catch (Exception e) {
                    console.printErr("invalid date\n");
                    console.print("start date (yyyy-mm-dd): ");
                }
            }
            return date;
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }

    public static ZonedDateTime askZonedDateTime(Console console) throws AskBreakException {
        try {
            ZonedDateTime zonedDateTime;
            while (true) {
                try {
                    String s = console.readln();
                    if (s.equals("")) return null;
                    if (s.equals("exit")) throw new AskBreakException();
                    zonedDateTime = ZonedDateTime.parse(s.trim());
                    break;
                } catch (Exception e) {
                    console.printErr("invalid date\n");
                    console.print("start date (yyyy-mm-dd): ");
                }
            }
            return zonedDateTime;
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }

    public static Position askPosition(Console console) throws AskBreakException {
        try {
            console.print("position (" + Position.list() + "): ");
            Position position;
            while (true) {
                try {
                    String s = console.readln().trim();
                    if (s.equals("exit")) throw new AskBreakException();
                    position = Position.valueOf(s.toUpperCase());
                    break;
                } catch (IllegalArgumentException e) {
                    console.printErr("invalid position\n");
                    console.print("position (" + Position.list() + "): ");
                }
            }
            return position;
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }
    
    public static Organization askOrganization(Console console) throws AskBreakException {
        try {
            console.println("Organization");
            console.print("annual turnover: ");
            Double annualTurnover;
            while (true) {
                try {
                    String s = console.readln().trim();
                    if (s.equals("exit")) throw new AskBreakException();
                    annualTurnover = Double.parseDouble(s);
                    if (annualTurnover > 0) {
                        break;
                    }
                    console.printErr("annual turnover must be greater than 0\n");
                    console.print("annual turnover: ");
                } catch (NumberFormatException e) {
                    console.printErr("annual turnover must be a float\n");
                    console.print("annual turnover: ");
                }
            }
            Adress postalAdress = askAdress(console);
            return new Organization(annualTurnover, postalAdress);
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }

    public static Adress askAdress(Console console) throws AskBreakException {
        try {
            console.print("zip code: ");
            String zipCode;
            while (true) {
                String s = console.readln().trim();
                if (s.equals("exit")) throw new AskBreakException();
                zipCode = s;
                if (!zipCode.isEmpty()) {
                    break;
                }
                console.printErr("zip code cannot be empty\n");
                console.print("zip code: ");
            }
            console.print("location: ");
            Location location = askLocation(console);
            return new Adress(zipCode, location);
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }

    public static Location askLocation(Console console) throws AskBreakException {
        try {
            console.print("x: ");
            Float x;
            while (true) {
                try {
                    String s = console.readln().trim();
                    if (s.equals("exit")) throw new AskBreakException();
                    x = Float.parseFloat(s);
                    break;
                } catch (NumberFormatException e) {
                    console.printErr("x must be a float\n");
                    console.print("x: ");
                }
            }
            console.print("y: ");
            Double y;
            while (true) {
                try {
                    y = Double.parseDouble(console.readln().trim());
                    break;
                } catch (NumberFormatException e) {
                    console.printErr("y must be a float\n");
                    console.print("y: ");
                }
            }
            console.print("z: ");
            Double z;
            while (true) {
                try {
                    z = Double.parseDouble(console.readln().trim());
                    break;
                } catch (NumberFormatException e) {
                    console.printErr("z must be a float\n");
                    console.print("z: ");
                }
            }
            return new Location(x, y, z);
        }
        catch (NoSuchElementException e) {
            console.printErr("exit\n");
            return null;
        }
    }
}
