import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SingleThread
{
    private static final Logger log = Logger.getLogger(SingleThread.class.getName());
    private static final String INPUT_FILE = "random_words.txt";

    public static void main(String[] args)
    {
        long lenSum = 0L;
        long count = 0L;
        Map<Integer, Integer> lengthMap = new HashMap<>();
        long start = System.currentTimeMillis();
        try(BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE)))
        {
            String word;
            while((word = reader.readLine()) != null )
            {
                int len = word.length();
                count++;
                lengthMap.put(len, lengthMap.getOrDefault(word, 0) + 1);
                lenSum += len;
            }
            log.info("Total Time Taken in Single Threaded = " +
                            (System.currentTimeMillis() - start) + "ms");

            log.info("Average Length of word: " + (1.0 * lenSum / count));

        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
