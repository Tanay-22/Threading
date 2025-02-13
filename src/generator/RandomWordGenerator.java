package generator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomWordGenerator
{

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 10;
    private static final long NUM_WORDS = 100000000L;
    private static final String OUTPUT_FILE = "random_words.txt";

    public static void main(String[] args)
    {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE))
        {
            Random random = new Random();
            for (int i = 0; i < NUM_WORDS; i++)
            {
                String word = generateRandomWord(random);
                writer.write(word + "\n");
            }
            System.out.println("Generated " + NUM_WORDS + " random words in '" + OUTPUT_FILE + "'");
        }
        catch (IOException e)
        {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    private static String generateRandomWord(Random random)
    {
        int length = random.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; i++)
        {
            char randomChar = CHARACTERS.charAt(random.nextInt(CHARACTERS.length()));
            word.append(randomChar);
        }
        return word.toString();
    }
}