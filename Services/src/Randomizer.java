/**
 * Created by silvia on 25/02/18.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Randomizer {
    public static Date time = new Date();
    public static long seed = time.getTime();
    public static Random randomNum = new Random(seed);

    /* Method to generate a random number between 1 and 49 and using
     * current time as seed. */
    public Integer generateRandomNumber(ArrayList numbers) {
        Boolean flag = false;
        Integer number = 0, min = 1, max = 49;
        while (!flag) {
            number = randomNum.nextInt(max - min + 1) + min;
            if (!this.isRepeated(number, numbers)) {
                numbers.add(number);
                flag = true;
            }
        }
        return number;
    }

    /* Method to check if a random number it was already generated */
    public Boolean isRepeated(Integer number, ArrayList numbers) {
        Boolean repeated = false;
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) == number) {
                repeated = true;
            }
        }
        return repeated;
    }
}