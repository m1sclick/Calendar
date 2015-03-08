import java.util.Calendar;

class Flexible {

    private Calendar easter;

    Flexible(int year) {
        int month;
        int c = ((year % 19) * 19 + 15) % 30;
        int d = ((2 * (year % 4)) + (4 * (year % 7)) + (6 * c) + 6) % 7;
        int day = 4 + c + d;
        if (day > 30) {
            day = (4 + c + d) - 30;
            month = 5;
        } else {
            month = 4;
        }
        easter = Calendar.getInstance();
        easter.set(Calendar.YEAR, year);
        easter.set(Calendar.MONTH, month - 1);
        easter.set(Calendar.DAY_OF_MONTH, day);
    }

    public int[] get(int code) {
        switch (code) {
            case -1: {
                return easter();
            }

            case -2: {
                return shrovetide();
            }

            case -3: {
                return quinquagesima();
            }

            case -4: {
                return palmSunday();
            }

            case -5: {
                return feast();
            }

            case -6: {
                return pentecost();
            }
            default:
                break;
        }
        return null;
    }

    // Пасха
    public int[] easter() {
        return result(easter);
    }

    // Масниця
    public int[] shrovetide() {
        Calendar shrovetide = (Calendar) easter.clone();
        shrovetide.add(Calendar.DAY_OF_YEAR, -7 * 8);
        shrovetide.add(Calendar.DAY_OF_YEAR, 1);
        return result(shrovetide);
    }

    // Прощена неділя
    public int[] quinquagesima() {
        Calendar quinquagesima = (Calendar) easter.clone();
        quinquagesima.add(Calendar.DAY_OF_YEAR, -7 * 8 + 7);
        return result(quinquagesima);
    }

    // Вербна неділя
    public int[] palmSunday() {
        Calendar palmSunday = (Calendar) easter.clone();
        palmSunday.add(Calendar.DAY_OF_YEAR, -7);
        return result(palmSunday);
    }

    // Петрів піст
    public int[] feast() {
        Calendar feast = (Calendar) easter.clone();
        feast.add(Calendar.DAY_OF_YEAR, 39);
        return result(feast);
    }

    // Трійця
    public int[] pentecost() {
        Calendar pentecost = (Calendar) easter.clone();
        pentecost.add(Calendar.DAY_OF_YEAR, 49);
        return result(pentecost);
    }

    private int[] result(Calendar c) {
        int[] arr = {c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)};
        return arr;
    }
}
